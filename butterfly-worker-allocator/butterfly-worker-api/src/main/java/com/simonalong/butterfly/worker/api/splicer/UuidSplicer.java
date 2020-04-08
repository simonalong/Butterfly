package com.simonalong.butterfly.worker.api.splicer;

/**
 * uuid连接器
 *
 * @author shizi
 * @since 2020/2/3 10:20 下午
 */
public interface UuidSplicer {

    /**
     * 连接
     * @return 拼接字段得到uuid
     */
    Long splice();
}
