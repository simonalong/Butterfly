package com.simonalong.butterfly.worker.api;

import com.simonalong.butterfly.sequence.util.ServiceLoaderFactory;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author shizi
 * @since 2020/4/9 11:51 PM
 */
public class UuidAllocatorFactory {

    private static volatile UuidGenerator uuidGenerator;

    static {
        ServiceLoaderFactory.init(UuidGenerator.class);
    }

    public static UuidGenerator create(ButterflyConfig butterflyConfig) {
        if (null == uuidGenerator) {
            synchronized (UuidAllocatorFactory.class) {
                if (null == uuidGenerator) {
                    Collection<UuidGenerator> uuidGenerators = ServiceLoaderFactory.getChildObject(UuidGenerator.class);
                    Iterator<UuidGenerator> it = uuidGenerators.iterator();
                    if (it.hasNext()) {
                        uuidGenerator = it.next();
                    }
                }
            }
        } else {
            uuidGenerator.init(butterflyConfig);
        }
        return uuidGenerator;
    }
}
