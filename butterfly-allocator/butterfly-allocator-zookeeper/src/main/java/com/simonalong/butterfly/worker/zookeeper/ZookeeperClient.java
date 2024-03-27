package com.simonalong.butterfly.worker.zookeeper;

import com.alibaba.fastjson.JSON;
import com.simonalong.butterfly.sequence.exception.ButterflyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.Watcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Phaser;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.simonalong.butterfly.sequence.UuidConstant.LOG_PRE;

/**
 * @author shizi
 * @since 2020/4/25 10:38 AM
 */
@Slf4j
public class ZookeeperClient {

    private static final String ZK_LOG_PRE = LOG_PRE + "[zookeeper]：";
    private static final int SESSION_TIMEOUT = 10000;

    private String connectString;
    private ZooKeeper zookeeper = null;
    /**
     * 用于服务启动的同步
     */
    private Phaser phaser = new Phaser(1);
    private static volatile ZookeeperClient zookeeperClient = null;
    /**
     * 观察者回调器
     */
    private CallbackWatcher watcher = new CallbackWatcher();
    /**
     * 连接断开或者会话超时回调
     */
    private Runnable disconnectCallback;
    private Runnable connectSuccessCallback;

    private ZookeeperClient() {
    }

    public static ZookeeperClient getInstance() {
        if (null == zookeeperClient) {
            synchronized (ZookeeperClient.class) {
                if (null == zookeeperClient) {
                    zookeeperClient = new ZookeeperClient();
                }
            }
        }
        return zookeeperClient;
    }

    /**
     * 创建zookeeper的客户端
     *
     * @param connectString 链接字符
     * @return zk客户端
     */
    public ZookeeperClient connect(String connectString) {
        try {
            this.close();
            phaser.register();
            this.connectString = connectString;
            zookeeper = new ZooKeeper(connectString, SESSION_TIMEOUT, watcher);
            log.info(ZK_LOG_PRE + "connect zookeeper: " + connectString);
            phaser.arriveAndAwaitAdvance();
        } catch (Throwable e) {
            log.error(ZK_LOG_PRE + "connect fail", e);
            throw new ButterflyException("connect fail");
        }
        return zookeeperClient;
    }

    public ZookeeperClient reconnect() {
        return connect(connectString);
    }

    /**
     * 注册在连接断开或者会话超时时候的回调
     *
     * @param disconnectCallback 断开的回调
     * @return zk客户端
     */
    public ZookeeperClient registerDisconnectCallback(Runnable disconnectCallback) {
        this.disconnectCallback = disconnectCallback;
        return this;
    }

    public ZookeeperClient registerConnectSuccessCallback(Runnable connectSuccessCallback) {
        this.connectSuccessCallback = connectSuccessCallback;
        return this;
    }

    /**
     * 添加永久节点
     *
     * @param nodePath  节点路径
     * @param data      节点数据
     * @return zk的客户端类
     */
    public Boolean addPersistentNode(String nodePath, String data) {
        return createNode(nodePath, data, CreateMode.PERSISTENT);
    }

    /**
     * 添加永久临时节点
     *
     * @param nodePath 父级节点路径
     * @param data 节点数据
     * @return 新生成的节点路径
     */
    public Boolean addPersistentSeqNode(String nodePath, String data) {
        return createNode(nodePath, data, CreateMode.PERSISTENT_SEQUENTIAL);
    }

    /**
     * 添加临时节点
     *
     * @param nodePath 节点路径
     * @param data 节点数据
     * @return 新生成的节点路径
     */
    public Boolean addEphemeralNode(String nodePath, String data) {
        return createNode(nodePath, data, CreateMode.EPHEMERAL);
    }

    /**
     * 添加临时节点
     *
     * @param nodePath 节点路径
     * @param data 节点数据
     * @return 新生成的节点路径
     */
    public Boolean addEphemeralSeqNode(String nodePath, String data) {
        return createNode(nodePath, data, CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    /**
     * 添加永久节点
     *
     * @param nodePath 节点路径
     * @return zk的客户端类
     */
    public Boolean addPersistentNode(String nodePath) {
        return createNode(nodePath, "", CreateMode.PERSISTENT);
    }

    /**
     * 添加永久临时节点
     *
     * @param nodePath 父级节点路径
     * @return 新生成的节点路径
     */
    public Boolean addPersistentSeqNode(String nodePath) {
        return createNode(nodePath, "", CreateMode.PERSISTENT_SEQUENTIAL);
    }

    /**
     * 添加临时节点
     *
     * @param nodePath 节点路径
     * @return 新生成的节点路径
     */
    public Boolean addEphemeralNode(String nodePath) {
        return createNode(nodePath, "", CreateMode.EPHEMERAL);
    }

    /**
     * 添加临时节点
     *
     * @param nodePath 节点路径
     * @return 新生成的节点路径
     */
    public Boolean addEphemeralSeqNode(String nodePath) {
        return createNode(nodePath, "", CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    /**
     * 循环创建永久节点
     *
     * @param nodePath 节点路径
     * @return 结果
     */
    public Boolean addPersistentNodeWithRecurse(String nodePath) {
        List<String> nodeList = Stream.of(nodePath.split("/")).filter(d -> !strIsEmpty(d)).collect(Collectors.toList());

        String path = "";
        for (String node : nodeList) {
            path += "/" + node;
            if (!nodeExist(path)) {
                addPersistentNode(path);
            }
        }
        return true;
    }

    /**
     * 删除节点
     *
     * @param nodePath 节点路径
     */
    public void deleteNode(String nodePath) {
        try {
            if (nodeExist(nodePath)) {
                this.zookeeper.delete(nodePath, -1);
                log.info(ZK_LOG_PRE + "node({}) delete success", nodePath);
            } else {
                log.warn(ZK_LOG_PRE + "node({}) not exist, no need to delete", nodePath);
            }
        } catch (InterruptedException | KeeperException e) {
            log.error(ZK_LOG_PRE + "delete node (" + nodePath + ") fail", e);
            throw new RuntimeException("delete node (" + nodePath + ") fail");
        }
    }

    /**
     * 循环删除节点以及子节点
     *
     * @param nodePath 路径
     */
    public void deleteNodeCycle(String nodePath) {
        List<String> pathList = getChildrenPathList(nodePath);
        if (!pathList.isEmpty()) {
            pathList.forEach(this::deleteNodeCycle);
        }
        deleteNode(nodePath);
    }

    /**
     * 修改zk中的数据
     *
     * @param nodePath 节点的全路径
     * @param data     对应的数据
     */
    public void writeNodeData(String nodePath, String data) {
        if (null != nodePath) {
            try {
                this.zookeeper.setData(nodePath, data.getBytes(), -1);
            } catch (KeeperException | InterruptedException e) {
                log.warn(ZK_LOG_PRE + "write data({}) to node({}) fail", data, nodePath);
                throw new RuntimeException("write data(" + data + ") to node(" + nodePath + ") fail");
            }
        }
    }

    public class CallbackWatcher implements Watcher {

        @Override
        public void process(WatchedEvent event) {
            log.info(ZK_LOG_PRE + "---------------------start-------------------");
            Event.KeeperState state = event.getState();
            Event.EventType type = event.getType();

            log.info(ZK_LOG_PRE + "receive Watcher notify");
            log.info(ZK_LOG_PRE + "connect status:\t" + state.toString());
            log.info(ZK_LOG_PRE + "event type:\t" + type.toString());
            if (Event.KeeperState.SyncConnected == state) {
                // 成功连接上ZK服务器
                if (Event.EventType.None == type) {
                    log.info(ZK_LOG_PRE + "connect zookeeper server success");
                    phaser.arriveAndDeregister();
                    if (null != connectSuccessCallback) {
                        connectSuccessCallback.run();
                    }
                }
            } else if (Event.KeeperState.Disconnected == state) {
                log.error(ZK_LOG_PRE + "disconnect to zookeeper server", new ButterflyException("disconnect to zookeeper server"));
                if (null != disconnectCallback) {
                    disconnectCallback.run();
                }
            } else if (Event.KeeperState.Expired == state) {
                log.error(ZK_LOG_PRE + "session expire，ready to reconnect");
                if (null != disconnectCallback) {
                    disconnectCallback.run();
                }
            }
            log.info(ZK_LOG_PRE + "---------------------end-------------------");
        }
    }

    /**
     * 关闭ZK连接
     */
    public void close() {
        if (null != this.zookeeper) {
            try {
                this.zookeeper.close();
            } catch (Exception e) {
                log.error(ZK_LOG_PRE + "close connect fail");
            }
        }
    }

    /**
     * 获取对应路径的子节点名称列表
     *
     * @param path 路径
     * @return 节点路径
     */
    public List<String> getChildrenPathList(String path) {
        try {
            return zookeeper.getChildren(path, false).stream().map(r -> path + "/" + r).collect(Collectors.toList());
        } catch (KeeperException | InterruptedException e) {
            log.error(ZK_LOG_PRE + "read path(" + path + ")'child path fail");
        }
        return Collections.emptyList();
    }

    /**
     * 获取对应路径的子节点名称列表
     *
     * @param path 路径
     * @return 节点路径
     */
    public List<String> getChildrenNameList(String path) {
        try {
            return new ArrayList<>(zookeeper.getChildren(path, false));
        } catch (KeeperException | InterruptedException e) {
            log.error(ZK_LOG_PRE + "read path(" + path + ")'child path name fail");
        }
        return Collections.emptyList();
    }

    /**
     * 读取指定节点数据内容
     *
     * @param path 路径
     * @return 节点数据
     */
    public String readData(String path) {
        try {
            if (null != zookeeper.exists(path, false)) {
                return new String(this.zookeeper.getData(path, false, null));
            }
            return null;
        } catch (KeeperException e) {
            log.error(ZK_LOG_PRE + "read data fail，path: " + path);
            return null;
        } catch (InterruptedException e) {
            log.error(ZK_LOG_PRE + "read data fail，path: " + path);
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public <T> T readDataJson(String path, Class<T> tClass) {
        String json = readData(path);
        if (null != json) {
            return JSON.parseObject(json, tClass);
        }
        return null;
    }

    /**
     * 判断节点是否存在
     *
     * @param nodePath 节点的全路径
     * @return 节点是否存在
     */
    public Boolean nodeExist(String nodePath) {
        try {
            return null != this.zookeeper.exists(nodePath, false);
        } catch (KeeperException | InterruptedException e) {
            log.error(ZK_LOG_PRE + "judge node exist fail, path=" + nodePath, e);
        }
        return false;
    }

    /**
     * 分布式锁，尝试加锁
     * <ul>
     *     <li>说明：修改为添加永久节点；临时节点存在时间配置不准确问题，进而导致正好业务没执行完就失效了，这里不排队，不做通用分布式锁，因此这里改用永久节点，在业务的正常运行中任何异常情况可以执行完删除；如果是业务异常退出情况，则手动删除也不错</li>
     * </ul>
     *
     * @param lockPath 锁路径
     * @param callable 加锁成功后的处理
     *
     * @return true：成功；false：失败
     */
    public Boolean distributeTryLock(String lockPath, Callable<Boolean> callable) {
        // 添加永久节点
        if (!addPersistentNode(lockPath)) {
            return false;
        }
        try {
            return callable.call();
        } catch (Throwable e) {
            return false;
        } finally {
            if (nodeExist(lockPath)) {
                deleteNode(lockPath);
            }
        }
    }

    public void distributeTryLock(String lockPath, Runnable runnable) {
        if (!addPersistentNode(lockPath)) {
            return;
        }
        try {
            runnable.run();
        } finally {
            if (nodeExist(lockPath)) {
                deleteNode(lockPath);
            }
        }
    }

    /**
     * 给这个节点中创建临时节点
     */
    private Boolean createNode(String node, String data, CreateMode createMode) {
        try {
            if (null == this.zookeeper.exists(node, false)) {
                String realPath = zookeeper.create(node, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
                if (null != this.zookeeper.exists(node, false)) {
                    log.info(ZK_LOG_PRE + "node create success, Path: " + realPath);
                    return true;
                } else {
                    log.info(ZK_LOG_PRE + "node(" + node + ") has existed");
                    return false;
                }
            } else {
                log.info(ZK_LOG_PRE + "node(" + node + ") has existed");
                return false;
            }
        } catch (KeeperException.NodeExistsException e) {
            log.warn(ZK_LOG_PRE + "node(" + node + ") has existed", e);
        } catch (InterruptedException e) {
            log.error(ZK_LOG_PRE + "node(" + node + ") create fail", e);
            Thread.currentThread().interrupt();
        } catch (Throwable e) {
            log.error(ZK_LOG_PRE + "node(" + node + ") create fail", e);
        }
        return false;
    }

    private Boolean strIsEmpty(String str) {
        return null == str || "".equals(str);
    }
}
