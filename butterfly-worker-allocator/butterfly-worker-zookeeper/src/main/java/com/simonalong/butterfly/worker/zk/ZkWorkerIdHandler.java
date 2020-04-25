package com.simonalong.butterfly.worker.zk;

import com.simonalong.butterfly.sequence.spi.WorkerIdHandler;
import com.simonalong.butterfly.worker.zk.node.ConfigNodeHandler;
import com.simonalong.butterfly.worker.zk.node.DefaultWorkerNodeHandler;
import com.simonalong.butterfly.worker.zk.node.WorkerNodeHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shizi
 * @since 2020/4/25 10:05 AM
 */
@Slf4j
public class ZkWorkerIdHandler implements WorkerIdHandler {

    private WorkerNodeHandler workerNodeHandler;

    @Override
    public Long getLastExpireTime() {
        return workerNodeHandler.getLastExpireTime();
    }

    @Override
    public Integer getWorkerId() {
        return workerNodeHandler.getWorkerId();
    }

    public ZkWorkerIdHandler(String namespace, ZookeeperClient zookeeperClient, ConfigNodeHandler configNodeHandler) {
        this.workerNodeHandler = new DefaultWorkerNodeHandler(namespace, zookeeperClient, configNodeHandler);
    }
}
