package com.simonalong.butterfly.sequence.allocator;

import com.simonalong.butterfly.sequence.ButterflyConfig;
import com.simonalong.butterfly.sequence.spi.ServiceLoaderFactory;
import lombok.experimental.UtilityClass;

import java.util.Collection;

/**
 * @author shizi
 * @since 2020/4/27 12:19 PM
 */
@UtilityClass
public class BeanBitAllocatorFactory {

    static {
        ServiceLoaderFactory.init(BeanBitAllocator.class);
    }

    public BitAllocator getBitAllocator(String namespace, ButterflyConfig butterflyConfig) {
        Collection<BeanBitAllocator> bitAllocatorCollection = ServiceLoaderFactory.getChildObject(BeanBitAllocator.class);
        if (null != bitAllocatorCollection) {
            for (BeanBitAllocator allocator : bitAllocatorCollection) {
                if (allocator instanceof DefaultBitAllocator) {
                    if (allocator.acceptConfig(butterflyConfig)) {
                        allocator.postConstruct(namespace, butterflyConfig);
                        return allocator;
                    }
                }
            }
        }
        return new DefaultBitAllocator(namespace, butterflyConfig);
    }
}
