package com.simonalong.butterfly.worker.api;import com.simonalong.neo.Neo;
import com.simonalong.neo.exception.UuidException;
import com.simonalong.neo.uid.splicer.DefaultUuidSplicer;
import com.simonalong.neo.uid.splicer.UuidSplicer;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.simonalong.neo.uid.UuidConstant.UUID_TABLE;

/**
 * 分布式全局id生成器
 *
 * @author zhouzhenyong
 * @since 2019/5/1 下午10:22
 */
@Slf4j
public final class UuidGenerator {

    private Neo neo;
    private static volatile UuidGenerator instance;
    /**
     * key为对应业务命名空间，value为uuid的序列构造器
     */
    private Map<String, UuidSplicer> uUidBuilderMap = new HashMap<>();

    private UuidGenerator() {}

    /**
     * 全局id生成器的构造函数
     *
     * @param neo 数据库对象
     * @return 全局id生成器对象
     */
    public static UuidGenerator getInstance(Neo neo) {
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
            throw new UuidException("数据库对象为空");
        }
        if (!neo.tableExist(UUID_TABLE)) {
            throw new UuidException("数据库uuid表不存在，请创建表 neo_uuid_generator");
        }
    }

    /**
     * 添加命名空间
     *
     * @param namespaces 命名空间
     */
    public void addNamespaces(String... namespaces) {
        Arrays.stream(namespaces).forEach(n -> addUUidSplicer(n, new DefaultUuidSplicer(n, neo)));
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
            throw new UuidException("命名空间" + namespace + "不存在，请先添加命名空间");
        }
        return uUidBuilderMap.get(namespace);
    }

    /**
     * 添加对应业务命名空间的uuid构造器
     *
     * @param namespace          业务命名空间
     * @param defaultUuidSplicer uuid构造器
     */
    private void addUUidSplicer(String namespace, UuidSplicer defaultUuidSplicer) {
        uUidBuilderMap.putIfAbsent(namespace, defaultUuidSplicer);
    }
}
