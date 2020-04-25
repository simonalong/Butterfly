package com.simonalong.butterfly.worker.zk;

import com.alibaba.fastjson.JSON;
import com.simonalong.butterfly.sequence.exception.ButterflyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

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
    private ZooKeeper zk = null;
    /**
     * 用于服务启动的同步
     */
    private Phaser phaser = new Phaser(1);
    private static volatile ZookeeperClient zookeeperClient = null;
    /**
     * 观察者回调器
     */
    private ZookeeperClient.CallbackWatcher watcher = new ZookeeperClient.CallbackWatcher();
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
     */
    public ZookeeperClient connect(String connectString) {
        try {
            this.close();
            phaser.register();
            this.connectString = connectString;
            zk = new ZooKeeper(connectString, SESSION_TIMEOUT, watcher);
            log.info("连接zookeeper: " + connectString);
            phaser.arriveAndAwaitAdvance();
        } catch (Throwable e) {
            log.error(ZK_LOG_PRE + "连接创建失败，发生 IOException", e);
            throw new ButterflyException("连接创建失败");
        }
        return zookeeperClient;
    }

    public ZookeeperClient reconnect() {
        return connect(connectString);
    }

    /**
     * 注册在连接断开或者会话超时时候的回调
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
     * @param nodePath 节点路径
     * @return zk的客户端类
     */
    public Boolean addPersistentNode(String nodePath, String data) {
        return createNode(nodePath, data, CreateMode.PERSISTENT);
    }

    /**
     * 添加永久临时节点
     *
     * @param nodePath 父级节点路径
     * @return 新生成的节点路径
     */
    public Boolean addPersistentSeqNode(String nodePath, String data) {
        return createNode(nodePath, data, CreateMode.PERSISTENT_SEQUENTIAL);
    }

    /**
     * 添加临时节点
     *
     * @param nodePath 节点路径
     * @return 新生成的节点路径
     */
    public Boolean addEphemeralNode(String nodePath, String data) {
        return createNode(nodePath, data, CreateMode.EPHEMERAL);
    }

    /**
     * 添加临时节点
     *
     * @param nodePath 节点路径
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
                this.zk.delete(nodePath, -1);
                log.info("节点{}删除成功", nodePath);
            } else {
                log.warn("节点{}不存在, 无需删除", nodePath);
            }
        } catch (InterruptedException | KeeperException e) {
            log.error("删除节点" + nodePath + "失败", e);
            throw new RuntimeException("删除节点" + nodePath + "失败");
        }
    }

    /**
     * 循环删除节点以及子节点
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
                this.zk.setData(nodePath, data.getBytes(), -1);
            } catch (KeeperException | InterruptedException e) {
                log.warn("写入节点{}数据{}错误", nodePath, data);
                throw new RuntimeException("写入节点" + nodePath + "数据" + data + "错误", e);
            }
        }
    }

    public class CallbackWatcher implements Watcher {

        @Override
        public void process(WatchedEvent event) {
            log.info(ZK_LOG_PRE + "---------------------start-------------------");
            Event.KeeperState state = event.getState();
            Event.EventType type = event.getType();
            String path = event.getPath();

            log.info(ZK_LOG_PRE + "收到Watcher通知");
            log.info(ZK_LOG_PRE + "连接状态:\t" + state.toString());
            log.info(ZK_LOG_PRE + "事件类型:\t" + type.toString());
            if (Event.KeeperState.SyncConnected == state) {
                // 成功连接上ZK服务器
                if (Event.EventType.None == type) {
                    log.info(ZK_LOG_PRE + "成功连接上ZK服务器");
                    phaser.arriveAndDeregister();
                    if (null != connectSuccessCallback) {
                        connectSuccessCallback.run();
                    }
                }
            } else if (Event.KeeperState.Disconnected == state) {
                log.error(ZK_LOG_PRE + "与ZK服务器断开连接", new ButterflyException("与ZK服务器断开连接"));
                if (null != disconnectCallback) {
                    disconnectCallback.run();
                }
            } else if (Event.KeeperState.Expired == state) {
                log.error(ZK_LOG_PRE + "会话失效，重新建立连接");
                if (null != disconnectCallback) {
                    disconnectCallback.run();
                }
            }
            log.debug(ZK_LOG_PRE + "---------------------end-------------------");
        }
    }

    /**
     * 关闭ZK连接
     */
    public void close() throws InterruptedException {
        if (null != this.zk) {
            try {
                this.zk.close();
            } catch (Exception e) {
                log.error("关闭连接失败");
            }
        }
    }

    /**
     * 获取对应路径的子节点名称列表
     */
    public List<String> getChildrenPathList(String path) {
        try {
            return zk.getChildren(path, false).stream().map(r -> path + "/" + r).collect(Collectors.toList());
        } catch (KeeperException | InterruptedException e) {
            log.error("读取路径" + path + "的子节点路径失败");
        }
        return Collections.emptyList();
    }

    /**
     * 获取对应路径的子节点名称列表
     */
    public List<String> getChildrenNameList(String path) {
        try {
            return new ArrayList<>(zk.getChildren(path, false));
        } catch (KeeperException | InterruptedException e) {
            log.error("读取路径" + path + "的子节点名字失败");
        }
        return Collections.emptyList();
    }

    /**
     * 读取指定节点数据内容
     *
     * @param path 路径
     */
    public String readData(String path) {
        try {
            if (null != zk.exists(path, false)) {
                return new String(this.zk.getData(path, false, null));
            }
            return null;
        } catch (KeeperException e) {
            log.error(ZK_LOG_PRE + "读取数据失败，发生KeeperException，path: " + path);
            return null;
        } catch (InterruptedException e) {
            log.error(ZK_LOG_PRE + "读取数据失败，发生 InterruptedException，path: " + path);
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
     */
    public Boolean nodeExist(String nodePath) {
        try {
            return null != this.zk.exists(nodePath, false);
        } catch (KeeperException | InterruptedException e) {
            log.error("判断节点是否存在异常", e);
        }
        return false;
    }

    /**
     * 分布式锁
     */
    public <T> T distributeLock(String lockPath, Callable<T> callable) {
        try {
            // 添加分布式锁
            if (!addEphemeralNode(lockPath)) {
                throw new RuntimeException("加锁失败");
            }

            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException("执行异常", e);
        } finally {
            if (nodeExist(lockPath)) {
                deleteNode(lockPath);
            }
        }
    }

    /**
     * 分布式锁
     */
    public void distributeLock(String lockPath, Runnable callable) {
        try {
            // 添加分布式锁
            if (!addEphemeralNode(lockPath)) {
                throw new RuntimeException("加锁失败");
            }

            callable.run();
        } catch (Exception e) {
            throw new RuntimeException("执行异常", e);
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
            if (null == this.zk.exists(node, false)) {
                String realPath = zk.create(node, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
                log.info(ZK_LOG_PRE + "节点创建成功, Path: " + realPath);
                return true;
            } else {
                log.info(ZK_LOG_PRE + "节点" + node + "已经存在");
                return false;
            }
        } catch (KeeperException e) {
            log.error(ZK_LOG_PRE + "节点" + node + "创建失败，发生KeeperException");
        } catch (InterruptedException e) {
            log.error(ZK_LOG_PRE + "节点" + node + "创建失败，发生 InterruptedException");
            Thread.currentThread().interrupt();
        }
        return false;
    }

    private Boolean strIsEmpty(String str){
        return null == str || "".equals(str);
    }
}
