package com.simonalong.butterfly.worker.zookeeper.node;

import com.simonalong.butterfly.sequence.exception.ButterflyException;
import com.simonalong.butterfly.worker.zk.ZookeeperClient;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shizi
 * @since 2020/4/25 11:03 AM
 */
@Slf4j
public class NamespaceNodeManager {

    private static volatile NamespaceNodeManager INSTANCE = null;
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
        // 包含则不再添加
        if (configNodeHandlerMap.containsKey(namespace)) {
            return;
        }
        if (null == zkClient) {
            throw new ButterflyException("zkClient不可为空");
        }

        ConfigNodeHandler configNodeHandler = new DefaultConfigNodeHandler(namespace, zkClient);
        configNodeHandlerMap.putIfAbsent(namespace, configNodeHandler);
        workerNodeHandlerMap.putIfAbsent(namespace, new DefaultWorkerNodeHandler(namespace, zkClient, configNodeHandler));
    }

    public int getWorkerId(String namespace) {
        return getWorkerNodeHandler(namespace).getWorkerId();
    }

    /**
     * 获取保留字段的值，默认为0
     */
    public int getRsv(String namespace) {
        return getConfigNodeHandler(namespace).getRsv();
    }

    /**
     * 获取过期时间
     */
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
}
