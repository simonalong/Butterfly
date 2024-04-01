package com.simonalong.butterfly.worker.zookeeper.node;

import com.alibaba.fastjson.JSON;
import com.simonalong.butterfly.sequence.exception.ButterflyException;
import com.simonalong.butterfly.worker.zookeeper.ZookeeperClient;
import com.simonalong.butterfly.worker.zookeeper.entity.SessionNodeEntity;
import com.simonalong.butterfly.worker.zookeeper.entity.WorkerNodeEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

import static com.simonalong.butterfly.sequence.UuidConstant.MAX_WORKER_SIZE;
import static com.simonalong.butterfly.worker.zookeeper.ZkConstant.SESSION_NODE;
import static com.simonalong.butterfly.worker.zookeeper.ZkConstant.ZK_LOG_PRE;

/**
 * @author shizi
 * @since 2020/4/25 10:45 AM
 */
@Slf4j
public class DefaultWorkerIdAllocator implements WorkerIdAllocator {

    /**
     * 当前分配的索引（就是workerId）
     */
    private Integer workerId;
    private final String namespace;
    protected ZookeeperClient zkClient;
    private final WorkerNodeHandler workerNodeHandler;
    private final ConfigNodeHandler configNodeHandler;

    public DefaultWorkerIdAllocator(String namespace, ZookeeperClient zkClient, WorkerNodeHandler workerNodeHandler, ConfigNodeHandler configNodeHandler) {
        this.namespace = namespace;
        this.zkClient = zkClient;
        this.workerNodeHandler = workerNodeHandler;
        this.configNodeHandler = configNodeHandler;
        init();
    }

    @Override
    public Integer getWorkerId() {
        return workerId;
    }

    @Override
    public String getWorkerNodePath() {
        return ZkNodeHelper.getWorkerPath(namespace, workerId);
    }

    /**
     * 连接zk并初始化
     *
     * <ul>
     * <li>1.读取业务对应的配置数据</li>
     * <li>2.通过哈希找到对应的工作节点</li>
     * <li>3.节点如果被占用则继续查找</li>
     * <li>4.如果没找到，标识机器都满了，则考虑扩容</li>
     * </ul>
     */
    private void init() {
        zkClient.reconnect().registerDisconnectCallback(this::init);

        // 初始化索引
        workerId = getWorkId(workerNodeHandler.getUidKey().hashCode());

        // 初始化节点信息
        initNode();
    }

    private void initNode() {
        if (findNode(workerId)) {
            return;
        }
        // 没有找到可用worker节点，则扩充worker节点
        expandWorker();
    }

    /**
     * 发现一个可用节点
     *
     * @param index 当前分配的临时的workerId
     * @return true：查找成功（找到插入的数据），false：没有找到可用的worker节点，进行扩容
     */
    private Boolean findNode(final Integer index) {
        log.info(ZK_LOG_PRE + " find one node to create session, workerId = " + index);
        String workerNodePathTem = ZkNodeHelper.getWorkerPath(namespace, index);
        if (zkClient.nodeExist(workerNodePathTem)) {
            // 添加分布式锁
            Boolean result = zkClient.distributeTryLock(ZkNodeHelper.getSessionCreateLock(namespace, index), () -> {
                if (addSessionNode(workerNodePathTem)) {
                    setWorkerId(index);
                    return true;
                }
                return false;
            });

            if (result) {
               return true;
            }
        }

        // 如果转了一圈都没有找到，则考虑扩容
        Integer nextIndex = getWorkId(index + 1);
        if (nextIndex.equals(this.workerId)) {
            return false;
        }
        return findNode(nextIndex);
    }

    /**
     * 添加session节点
     *
     * <ul>
     * <li>1.worker节点中数据不存在（节点刚初始化完或者session异常没有删除），则创建session并添加worker节点中的时间和key</li>
     * <li>2.数据存在，数据如果过期，则创建session并更新worker节点中的时间和key</li>
     * <li>3.数据存在，数据没有过期，则判断是否是自己，如果是，则删除之前的session，并创建新session然后更新worker节点中的时间和key</li>
     * </ul>
     *
     * @param workerPath 业务占用的机器的节点
     * @return true：添加成功，false：添加失败
     */
    private Boolean addSessionNode(String workerPath) {
        // 节点首次读取或者session异常没有删除
        WorkerNodeEntity workerNodeEntity = zkClient.readDataJson(workerPath, WorkerNodeEntity.class);
        if (null == workerNodeEntity) {
            return createSession(workerPath);
        }

        Long lastTime = workerNodeEntity.getLastExpireTime();
        String uidKeyTem = workerNodeEntity.getUidKey();

        // 节点过期：创建session节点
        if (null != lastTime && lastTime < System.currentTimeMillis()) {
            return createSession(workerPath);
        } else {
            // 节点未过期：如果是自己，则可以使用；否则创建
            if (null != uidKeyTem && workerNodeEntity.getUidKey().equals(workerNodeHandler.getUidKey())) {
                return createSession(workerPath);
            }
        }

        return false;
    }

    private Boolean createSession(String workerPath) {
        String sessionPath = workerPath + SESSION_NODE;
        if (zkClient.nodeExist(sessionPath)) {
            zkClient.deleteNode(sessionPath);
        }

        // 记录ip和进程id到session节点
        SessionNodeEntity sessionNodeEntity = new SessionNodeEntity();
        if (zkClient.addPersistentNode(sessionPath, JSON.toJSONString(sessionNodeEntity))) {
            // 记录下次过期时间和本次启动的唯一编号
            workerNodeHandler.refreshNodeInfo(workerPath);
            return true;
        }
        return false;
    }

    /**
     * 扩展机器的个数
     * <p>
     * 如果节点不存在，则创建worker节点并添加session节点，如果节点存在，看下是否能够添加到数据中（这个时候认为已经有其他进程也参与了扩容）
     */
    private void expandWorker() {
        // 当前最大的机器个数
        Integer maxMachineNum = configNodeHandler.getCurrentMaxMachineNum();
        log.info(ZK_LOG_PRE + " ready to expand, biz=" + namespace + ", maxMachineNum=" + maxMachineNum);

        // 禁止扩容
        if (maxMachineNum >= getMaxMachineNum()) {
            throw new ButterflyException("当前最大值" + maxMachineNum + "已到机器最大值");
        }

        // 如果有可用节点则返回
        if (findNode(workerId)) {
            return;
        }


        ZookeeperClient.ZookeeperNodeDataListener lockReleaseListener = new ZookeeperClient.ZookeeperNodeDataListener();

        // 添加监听器：锁节点的监听器，用于在扩容完成时候，其他集群节点能够接收到通知
        zkClient.addNodeDataChangeListener(ZkNodeHelper.getBizExpandLock(namespace), lockReleaseListener);

        Boolean result = zkClient.distributeTryLock(ZkNodeHelper.getBizExpandLock(namespace), () -> {
            // 如果有可用节点则返回
            if (findNode(workerId)) {
                return true;
            }

            // 扩容
            if (innerExpand(maxMachineNum)) {
                log.info(ZK_LOG_PRE + " expand success, ready to find one node to create session");
                // 扩容成功，重新分配节点
                findNode(maxMachineNum);

                // todo 更新session节点数据中的锁内容为"release"

                return true;
            }
            return false;
        });

        if (!result) {
            // 添加监听器，等待B
            lockReleaseListener.listen((nodeContent)->{
                // todo 待完成
                if (nodeContent.equals("success")) {
                    if (findNode(workerId)) {
                        // todo
//                        return true;
                    }
                    // 没有找到可用worker节点，则扩充worker节点
                    expandWorker();
                    //
                    log.error("find error. expand node is error！！！！！");
//                    return false;
                }
            });
        }
    }

    private Boolean innerExpand(Integer maxMachineNum) {
        try {
            for (int index = maxMachineNum; index < maxMachineNum * 2; index++) {
                // 添加永久节点
                if (!zkClient.addPersistentNode(ZkNodeHelper.getWorkerPath(namespace, index))){
                    log.warn(ZK_LOG_PRE + "add persistent node error");
                }
            }

            log.debug(ZK_LOG_PRE + " maxMachineNum * 2 have created finished");

            configNodeHandler.updateCurrentMaxMachineNum(maxMachineNum * 2);
            return true;
        } catch (Throwable e) {
            log.debug(ZK_LOG_PRE + " inner expand error，", e);
            return false;
        }
    }

    private void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    /**
     * 获取节点的哈希值
     * <p>
     * 根据预发和线上环境可能冲突的问题，采用workId划分，预发分配一定个数，然后线上按照最大个数往后算
     */
    private Integer getWorkId(Integer index) {
        int maxNum = configNodeHandler.getCurrentMaxMachineNum();
        assert maxNum != 0 : "当前机器个数不为0";
        assert maxNum <= MAX_WORKER_SIZE : "当前机器最大个数设置有误";
        return index & (maxNum - 1);
    }

    /**
     * 获取当前业务的最大机器个数
     * <p>
     * 注意：
     * 这个个数是bit内部约束的最大个数
     */
    private Integer getMaxMachineNum() {
        return Math.toIntExact(MAX_WORKER_SIZE);
    }
}
