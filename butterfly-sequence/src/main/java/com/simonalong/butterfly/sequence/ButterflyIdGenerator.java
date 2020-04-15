package com.simonalong.butterfly.sequence;

import com.simonalong.butterfly.worker.api.exception.ButterflyException;
import com.simonalong.butterfly.sequence.splicer.DefaultUuidSplicer;
import com.simonalong.butterfly.sequence.splicer.UuidSplicer;
import com.simonalong.butterfly.worker.api.ButterflyConfig;
import com.simonalong.butterfly.worker.api.UuidAllocatorFactory;
import com.simonalong.butterfly.worker.api.UuidGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shizi
 * @since 2020/4/8 11:55 PM
 */
public final class ButterflyIdGenerator {

    private UuidGenerator uuidGenerator;
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
                    instance.uuidGenerator = UuidAllocatorFactory.create(butterflyConfig);
                }
            }
        }
        return instance;
    }


    /**
     * 添加命名空间
     *
     * @param namespaces 命名空间
     */
    public void addNamespaces(String... namespaces) {
        Arrays.stream(namespaces).forEach(n -> uUidBuilderMap.putIfAbsent(n, new DefaultUuidSplicer(n, uuidGenerator)));
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

    private UuidSplicer getUUidSplicer(String namespace) {
        if (!uUidBuilderMap.containsKey(namespace)) {
            throw new ButterflyException("命名空间" + namespace + "不存在，请先添加命名空间");
        }
        return uUidBuilderMap.get(namespace);
    }
}
