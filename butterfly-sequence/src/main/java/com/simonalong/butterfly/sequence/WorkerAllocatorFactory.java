package com.simonalong.butterfly.sequence;

import com.simonalong.butterfly.sequence.exception.ButterflyException;
import com.simonalong.butterfly.sequence.util.ServiceLoaderFactory;
import com.simonalong.butterfly.worker.api.ButterflyConfig;
import com.simonalong.butterfly.worker.api.WorkerAllocator;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author shizi
 * @since 2020/4/9 12:36 AM
 */
public class WorkerAllocatorFactory {

    static {
        ServiceLoaderFactory.init(WorkerAllocator.class);
    }

    public static WorkerAllocator getWorkerAllocator(ButterflyConfig butterflyConfig) {
        Collection<WorkerAllocator> workerAllocatorCollection = ServiceLoaderFactory.getChildObject(WorkerAllocator.class);
        Iterator<WorkerAllocator> it = workerAllocatorCollection.iterator();
        if (it.hasNext()) {
            return it.next().createInstance(butterflyConfig);
        } else {
            throw new ButterflyException("not find workerId allocator, please add butterfly-worker-allocator-db or butterfly-worker-allocator-zookeeper");
        }
    }
}
