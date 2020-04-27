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

    public static void checkConfig(ButterflyConfig butterflyConfig) {
        Collection<WorkerLoader> workerLoaderCollection = ServiceLoaderFactory.getChildObject(WorkerLoader.class);
        if (haveNonDefault(workerLoaderCollection)) {
            for (WorkerLoader allocator : workerLoaderCollection) {
                if (!allocator.isDefault()) {
                    if (!allocator.configAvailable(butterflyConfig)) {
                        throw new ButterflyException("the config " + butterflyConfig + " is illegal to this worker");
                    }
                }
            }
        } else {
            for (WorkerLoader allocator : workerLoaderCollection) {
                if (allocator.isDefault()) {
                    if (!allocator.configAvailable(butterflyConfig)) {
                        throw new ButterflyException("the config " + butterflyConfig + " is illegal to this worker");
                    }
                }
            }
        }
    }

    public static WorkerIdHandler getWorkerIdHandler(String namespace, ButterflyConfig butterflyConfig) {
        Collection<WorkerLoader> workerLoaderCollection = ServiceLoaderFactory.getChildObject(WorkerLoader.class);
        if (haveNonDefault(workerLoaderCollection)) {
            for (WorkerLoader allocator : workerLoaderCollection) {
                if (!allocator.isDefault()) {
                    return allocator.loadIdHandler(namespace, butterflyConfig);
                }
            }
        } else {
            for (WorkerLoader allocator : workerLoaderCollection) {
                if (allocator.isDefault()) {
                    return allocator.loadIdHandler(namespace, butterflyConfig);
                }
            }
        }
        throw new ButterflyException("not find workerId allocator, please add butterfly-worker-allocator-db or butterfly-worker-allocator-distribute");
    }

    /**
     * 查看是否有非默认的
     * <p> 如果有非默认的，则采用非默认的，否则采用默认的
     *
     * @return true：有非默认的（即用户自己制定的），false：用户没有指定，则采用默认的
     */
    private static Boolean haveNonDefault(Collection<WorkerLoader> workerLoaderCollection) {
        return workerLoaderCollection.stream().anyMatch(e -> !e.isDefault());
    }
}
