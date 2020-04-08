package com.simonalong.butterfly.worker.db.splicer;

import com.ggj.platform.cornerstone.snowflake.allocator.BitAllocator;

/**
 * uuid连接器
 *
 * @author shizi
 * @since 2020/2/3 10:20 下午
 */
public interface UUidSplicer
{
    /**
     * 获取bit构造器
     */
    BitAllocator getBitAllocator();
    /**
     * 连接
     */
    Long splice();
}
