package com.simonalong.butterfly.worker.distribute.client;

import com.simonalong.butterfly.sequence.ButterflyConfig;
import com.simonalong.butterfly.sequence.WorkerLoader;
import com.simonalong.butterfly.sequence.spi.WorkerIdHandler;
import com.simonalong.butterfly.worker.distribute.client.config.DistributeClientButterflyConfig;

/**
 * @author shizi
 * @since 2020/4/26 10:59 PM
 */
public class DistributeClientWorkerLoader implements WorkerLoader {

    @Override
    public Boolean configAvailable(ButterflyConfig butterflyConfig) {
        if (null == butterflyConfig) {
            return false;
        }
        return butterflyConfig instanceof DistributeClientButterflyConfig;
    }

    @Override
    public WorkerIdHandler loadIdHandler(String namespace, ButterflyConfig butterflyConfig) {
        // todo
        return null;
    }
}
