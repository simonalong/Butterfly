package com.simonalong.butterfly.worker.distribute.server;

import com.simonalong.butterfly.sequence.ButterflyConfig;
import com.simonalong.butterfly.sequence.WorkerLoader;
import com.simonalong.butterfly.sequence.spi.WorkerIdHandler;

/**
 * @author shizi
 * @since 2020/4/26 11:53 PM
 */
public class DistributeServerWorkerLoader implements WorkerLoader {

    @Override
    public Boolean configAvailable(ButterflyConfig butterflyConfig) {
        return null;
    }

    @Override
    public WorkerIdHandler loadIdHandler(String namespace, ButterflyConfig butterflyConfig) {
        return null;
    }
}
