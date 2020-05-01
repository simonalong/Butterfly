package com.simonalong.butterfly.sequence;

import com.simonalong.butterfly.sequence.allocator.BitAllocator;
import com.simonalong.butterfly.sequence.exception.ButterflyException;
import com.simonalong.butterfly.sequence.spi.WorkerIdHandlerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shizi
 * @since 2020/4/8 11:55 PM
 */
public final class ButterflyIdGenerator {

    private ButterflyConfig butterflyConfig;
    private static volatile ButterflyIdGenerator instance;
    /**
     * key为对应业务命名空间，value为uuid的序列构造器
     */
    private Map<String, UuidSplicer> uUidBuilderMap = new HashMap<>();

    /**
     * 全局id生成器的构造函数
     *
     * @return 全局id生成器对象
     */
    public static ButterflyIdGenerator getInstance(ButterflyConfig butterflyConfig) {
        if (null == instance) {
            synchronized (ButterflyIdGenerator.class) {
                if (null == instance) {
                    instance = new ButterflyIdGenerator();
                    instance.butterflyConfig = butterflyConfig;
                }
            }
        }
        return instance;
    }

    /**
     * 声明命名空间
     * <p>
     *  如果命名空间不存在，则新建，如果存在，则采用已经存在的命名空间
     *
     * @param namespaces 命名空间
     */
    public void declareNamespace(String... namespaces) {
        Arrays.stream(namespaces).forEach(n -> uUidBuilderMap.putIfAbsent(n, new UuidSplicer(n, butterflyConfig)));
    }

    /**
     * 获取对应命名空间的全局id
     *
     * @param namespace 业务的命名空间
     * @return 全局id生成器
     */
    public long getUUid(String namespace) {
        return getUUidSplicer(namespace).splice();
    }

    public BitAllocator getBitAllocator(String namespace) {
        return getUUidSplicer(namespace).getBitAllocator();
    }

    private UuidSplicer getUUidSplicer(String namespace) {
        if (!uUidBuilderMap.containsKey(namespace)) {
            throw new ButterflyException("命名空间" + namespace + "不存在，请先添加命名空间");
        }
        return uUidBuilderMap.get(namespace);
    }
}
