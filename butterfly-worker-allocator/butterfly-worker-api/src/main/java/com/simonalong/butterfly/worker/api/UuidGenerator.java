package com.simonalong.butterfly.worker.api;

/**
 * 分布式全局id生成器
 *
 * @author zhouzhenyong
 * @since 2019/5/1 下午10:22
 */
public interface UuidGenerator {

    /**
     * 初始化
     *
     * @param butterflyConfig 配置
     */
    void init(ButterflyConfig butterflyConfig);
}
