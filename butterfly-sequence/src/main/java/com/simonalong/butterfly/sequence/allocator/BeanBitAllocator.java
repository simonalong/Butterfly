package com.simonalong.butterfly.sequence.allocator;

import com.simonalong.butterfly.sequence.ButterflyConfig;

/**
 * @author shizi
 * @since 2020/4/27 11:49 PM
 */
public interface BeanBitAllocator extends BitAllocator{

    /**
     * 是否接受对应的配置
     *
     * @param butterflyConfig 具体的配置
     * @return true：接受，false：不接受
     */
    default boolean acceptConfig(ButterflyConfig butterflyConfig) {
        return true;
    }

    /**
     * 初始化的配置的处理
     *
     * @param namespace 命名空间
     * @param butterflyConfig 配置
     */
    default void postConstruct(String namespace, ButterflyConfig butterflyConfig) {}
}
