package com.simonalong.butterfly.sequence.spi;

import com.simonalong.butterfly.sequence.ButterflyConfig;
import com.simonalong.butterfly.sequence.exception.ButterflyException;
import com.simonalong.butterfly.sequence.WorkerLoader;

import java.util.Collection;

/**
 * @author shizi
 * @since 2020/4/9 12:36 AM
 */
public final class WorkerIdHandlerFactory {

    static {
        ServiceLoaderFactory.init(WorkerLoader.class);
    }

    public static WorkerIdHandler getWorkerIdHandler(String namespace, ButterflyConfig butterflyConfig) {
        Collection<WorkerLoader> workerLoaderCollection = ServiceLoaderFactory.getChildObject(WorkerLoader.class);
        for (WorkerLoader allocator : workerLoaderCollection) {
            if (allocator.acceptConfig(butterflyConfig)) {
                return allocator.loadIdHandler(namespace, butterflyConfig);
            }
        }
//        throw new ButterflyException("not find workerId allocator, please add butterfly-worker-allocator-db or butterfly-worker-allocator-distribute");
        return null;
    }
}
