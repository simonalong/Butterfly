package com.simonalong.butterfly.sequence.allocator;

import com.simonalong.butterfly.sequence.ButterflyConfig;
import com.simonalong.butterfly.sequence.WorkerLoader;
import com.simonalong.butterfly.sequence.spi.ServiceLoaderFactory;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;

/**
 * @author shizi
 * @since 2020/4/27 12:19 PM
 */
@UtilityClass
public class BitAllocatorFactory {

    static {
        ServiceLoaderFactory.init(WorkerLoader.class);
    }

    public BitAllocator getBitAllocator(String namespace, ButterflyConfig butterflyConfig) {
        Collection<BitAllocator> bitAllocatorCollection = ServiceLoaderFactory.getChildObject(BitAllocator.class);
        for (BitAllocator allocator : bitAllocatorCollection) {
            if (allocator instanceof DefaultBitAllocator) {
                if (allocator.acceptConfig(butterflyConfig)) {
                    return allocator;
                }
            }
        }
        return new DefaultBitAllocator(namespace, butterflyConfig);
    }
}
