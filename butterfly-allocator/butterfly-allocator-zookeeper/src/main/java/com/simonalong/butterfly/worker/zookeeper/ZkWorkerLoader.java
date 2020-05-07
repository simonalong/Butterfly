package com.simonalong.butterfly.worker.zookeeper;

import com.simonalong.butterfly.sequence.ButterflyConfig;
import com.simonalong.butterfly.sequence.WorkerLoader;
import com.simonalong.butterfly.sequence.spi.WorkerIdHandler;

/**
 * @author shizi
 * @since 2020/4/25 10:03 AM
 */
public class ZkWorkerLoader implements WorkerLoader {

    private ZookeeperClient zkClient;

    @Override
    public boolean acceptConfig(ButterflyConfig butterflyConfig) {
        if (null == butterflyConfig) {
            return false;
        }
        if (!(butterflyConfig instanceof ZkButterflyConfig)) {
            return false;
        }

        ZkButterflyConfig zkConfig = (ZkButterflyConfig) butterflyConfig;
        String host = zkConfig.getHost();
        zkClient = ZookeeperClient.getInstance();
        zkClient.connect(host);
        return true;
    }

    @Override
    public WorkerIdHandler loadIdHandler(String namespace, ButterflyConfig butterflyConfig) {
        return new ZkWorkerIdHandler(namespace, zkClient);
    }
}
