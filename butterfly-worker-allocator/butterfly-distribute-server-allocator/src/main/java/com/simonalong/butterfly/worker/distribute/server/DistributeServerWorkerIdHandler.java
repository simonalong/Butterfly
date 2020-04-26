package com.simonalong.butterfly.worker.distribute.server;

import com.simonalong.butterfly.sequence.spi.WorkerIdHandler;

/**
 * @author shizi
 * @since 2020/4/26 11:53 PM
 */
public class DistributeServerWorkerIdHandler implements WorkerIdHandler {

    @Override
    public Long getLastExpireTime() {
        return null;
    }

    @Override
    public Integer getWorkerId() {
        return null;
    }
}
