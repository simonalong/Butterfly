package com.simonalong.butterfly.worker.zookeeper;

import com.simonalong.butterfly.sequence.spi.WorkerIdHandler;
import com.simonalong.butterfly.worker.zookeeper.node.NamespaceNodeManager;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shizi
 * @since 2020/4/25 10:05 AM
 */
@Slf4j
public class ZkWorkerIdHandler implements WorkerIdHandler {

    private NamespaceNodeManager namespaceNodeManager = NamespaceNodeManager.getInstance();
    private String namespace;

    @Override
    public Long getLastExpireTime() {
        return namespaceNodeManager.getExpireTime(namespace);
    }

    @Override
    public Integer getWorkerId() {
        return namespaceNodeManager.getWorkerId(namespace);
    }

    public ZkWorkerIdHandler(String namespace, ZookeeperClient zookeeperClient) {
        namespaceNodeManager.setZkClient(zookeeperClient);
        namespaceNodeManager.add(namespace);
        this.namespace = namespace;
    }
}
