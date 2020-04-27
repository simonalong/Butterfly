package com.simonalong.butterfly.sequence.allocator;

import com.simonalong.butterfly.sequence.ButterflyConfig;
import com.simonalong.butterfly.sequence.WorkerLoader;
import com.simonalong.butterfly.sequence.spi.ServiceLoaderFactory;

import java.util.Collection;
import java.util.List;

/**
 * @author shizi
 * @since 2020/4/27 12:19 PM
 */
public class BitAllocatorFactory {

    static {
        ServiceLoaderFactory.init(WorkerLoader.class);
    }

    public BitAllocator getBitAllocator (String namespace, ButterflyConfig butterflyConfig){
        Collection<BitAllocator> bitAllocatorList = ServiceLoaderFactory.getChildObject(BitAllocator.class);
        // todo
    }
}
