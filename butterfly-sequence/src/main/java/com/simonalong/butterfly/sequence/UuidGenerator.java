package com.simonalong.butterfly.sequence;

import com.simonalong.butterfly.sequence.exception.ButterflyException;
import com.simonalong.butterfly.sequence.splicer.DefaultUuidSplicer;
import com.simonalong.butterfly.sequence.splicer.UuidSplicer;
import com.simonalong.butterfly.sequence.util.ServiceLoaderFactory;
import com.simonalong.butterfly.worker.api.ButterflyConfig;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shizi
 * @since 2020/4/8 11:55 PM
 */
public final class UuidGenerator {

    private static volatile UuidGenerator instance;
    /**
     * key为对应业务命名空间，value为uuid的序列构造器
     */
    private Map<String, UuidSplicer> uUidBuilderMap = new HashMap<>();

    static {
        // todo
        ServiceLoaderFactory.init();
    }

    public void setConfig(ButterflyConfig butterflyConfig) {

    }

    /**
     * 全局id生成器的构造函数
     *
     * @param neo 数据库对象
     * @return 全局id生成器对象
     */
    public static UuidGenerator getInstance(ButterflyConfig butterflyConfig) {
        if (null == instance) {
            synchronized (UuidGenerator.class) {
                if (null == instance) {
                    instance = new UuidGenerator();
                    instance.neo = neo;
                    instance.init();
                }
            }
        }
        return instance;
    }

    private void init() {
        if (null == neo) {
            throw new ButterflyException("数据库对象为空");
        }
        if (!neo.tableExist(UUID_TABLE)) {
            throw new ButterflyException("数据库uuid表不存在，请创建表 neo_uuid_generator");
        }
    }

    /**
     * 添加命名空间
     *
     * @param namespaces 命名空间
     */
    public void addNamespaces(String... namespaces) {
        Arrays.stream(namespaces).forEach(n -> {
            uUidBuilderMap.putIfAbsent(n, new DefaultUuidSplicer(n, neo));
        });
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
