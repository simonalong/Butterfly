package com.simonalong.butterfly.worker.zookeeper;

import com.simonalong.butterfly.sequence.ButterflyConfig;
import com.simonalong.butterfly.sequence.WorkerLoader;
import com.simonalong.butterfly.sequence.exception.ButterflyException;
import com.simonalong.butterfly.sequence.spi.WorkerIdHandler;
import com.simonalong.butterfly.worker.zookeeper.node.DefaultConfigNodeHandler;

/**
 * @author shizi
 * @since 2020/4/25 10:03 AM
 */
public class ZkWorkerLoader implements WorkerLoader {

    @Override
    public Boolean isDefault() {
        return true;
    }

    @Override
    public Boolean configAvailable(ButterflyConfig butterflyConfig) {
        if (null == butterflyConfig) {
            return false;
        }
        return butterflyConfig instanceof ZkButterflyConfig;
    }

    @Override
    public WorkerIdHandler loadIdHandler(String namespace, ButterflyConfig butterflyConfig) {
        ZkButterflyConfig zkConfig = (ZkButterflyConfig) butterflyConfig;
        String host = zkConfig.getHost();

        ZookeeperClient zkClient = ZookeeperClient.getInstance();
        zkClient.connect(host);
        return new ZkWorkerIdHandler(namespace, zkClient, new DefaultConfigNodeHandler(namespace, zkClient));
    }
}
