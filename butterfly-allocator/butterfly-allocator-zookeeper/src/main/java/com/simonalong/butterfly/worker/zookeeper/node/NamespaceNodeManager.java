package com.simonalong.butterfly.worker.zookeeper.node;

import com.alibaba.fastjson.JSON;
import com.simonalong.butterfly.sequence.exception.ButterflyException;
import com.simonalong.butterfly.worker.zookeeper.ZookeeperClient;
import com.simonalong.butterfly.worker.zookeeper.entity.ConfigNodeEntity;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.simonalong.butterfly.sequence.UuidConstant.*;
import static com.simonalong.butterfly.worker.zookeeper.ZkConstant.ROOT_PATH;
import static com.simonalong.butterfly.worker.zookeeper.ZkConstant.ZK_LOG_PRE;

/**
 * @author shizi
 * @since 2020/4/25 11:03 AM
 */
@Slf4j
public class NamespaceNodeManager {

    private static volatile NamespaceNodeManager INSTANCE = null;
    /**
     * 默认的最大机器个数
     */
    private static final Integer DEFAULT_MAX_MACHINE_NUM = 16;
    /**
     * config节点操作map
     */
    private Map<String, ConfigNodeHandler> configNodeHandlerMap = new HashMap<>(12);
    /**
     * worker节点操作map
     */
    private Map<String, WorkerNodeHandler> workerNodeHandlerMap = new HashMap<>(12);
    @Setter
    private ZookeeperClient zkClient;

    private NamespaceNodeManager() {
    }

    public static NamespaceNodeManager getInstance() {
        if (null == INSTANCE) {
            synchronized (NamespaceNodeManager.class) {
                if (null == INSTANCE) {
                    INSTANCE = new NamespaceNodeManager();
                }
            }
        }
        return INSTANCE;
    }

    public void add(String namespace) {
        if (configNodeHandlerMap.containsKey(namespace)) {
            return;
        }
        if (null == zkClient) {
            throw new ButterflyException("zkClient不可为空");
        }

        tryAddNamespace(namespace);

        ConfigNodeHandler configNodeHandler = new DefaultConfigNodeHandler(namespace, zkClient);
        configNodeHandlerMap.putIfAbsent(namespace, configNodeHandler);
        workerNodeHandlerMap.putIfAbsent(namespace, new DefaultWorkerNodeHandler(namespace, zkClient, configNodeHandler));
    }

    public int getWorkerId(String namespace) {
        return getWorkerNodeHandler(namespace).getWorkerId();
    }

    public Long getExpireTime(String namespace) {
        return getWorkerNodeHandler(namespace).getLastExpireTime();
    }

    public ConfigNodeHandler getConfigNodeHandler(String namespace) {
        check(namespace);
        return configNodeHandlerMap.get(namespace);
    }

    public WorkerNodeHandler getWorkerNodeHandler(String namespace) {
        check(namespace);
        return workerNodeHandlerMap.get(namespace);
    }

    /**
     * 核查命名空间
     */
    private void check(String namespace) {
        if (!configNodeHandlerMap.containsKey(namespace)) {
            throw new ButterflyException("命名空间" + namespace + "不存在");
        }
    }

    /**
     * 尝试创建命名空间
     *
     * @param namespace 命名空间
     */
    private void tryAddNamespace(String namespace) {
        if (null == namespace || "".equals(namespace)) {
            throw new ButterflyException("namespace is null");
        }

        if (namespace.equals(DISTRIBUTE_SERVER)) {
            throw new ButterflyException("node [" + namespace + "] is distribute'node, forbidden to add");
        }

        try {
            if (!zkClient.nodeExist(ZkNodeHelper.getNamespacePath(namespace))) {
                // 创建根节点
                zkClient.addPersistentNodeWithRecurse(ROOT_PATH);
                createNamespaceNode(namespace);
            }
        } catch (Throwable e) {
            log.error(ZK_LOG_PRE + "fail to add node[{}] to zookeeper", namespace);
            throw new ButterflyException("fail to add node[" + namespace + "] to zookeeper");
        }
    }

    /**
     * 创建命名空间节点 业务节点、config节点和对应的worker节点
     */
    private void createNamespaceNode(String namespace) {
        String namespacePath = ZkNodeHelper.getNamespacePath(namespace);
        if (!zkClient.nodeExist(namespacePath)) {
            // 创建业务节点
            zkClient.addPersistentNode(namespacePath);

            ConfigNodeEntity configEntity = new ConfigNodeEntity();
            configEntity.setCurrentMaxMachine(DEFAULT_MAX_MACHINE_NUM);
            configEntity.setTimestampBits(TIME_BITS);
            configEntity.setSequenceBits(SEQ_HIGH_BITS + SEQ_LOW_BITS);
            configEntity.setWorkerBits(WORKER_BITS);

            // 创建 config 节点
            zkClient.addPersistentNode(namespacePath + "/config", JSON.toJSONString(configEntity));

            // 创建 worker 节点
            for (int index = 0; index < DEFAULT_MAX_MACHINE_NUM; index++) {
                zkClient.addPersistentNode(namespacePath + "/worker_" + index);
            }
        }
    }
}
