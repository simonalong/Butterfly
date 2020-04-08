package com.simonalong.butterfly.worker.db.splicer;

import com.ggj.platform.cornerstone.snowflake.allocator.BitAllocator;
import com.ggj.platform.cornerstone.snowflake.exception.SnowflakeException;
import lombok.Getter;
import lombok.Setter;

import static com.ggj.platform.cornerstone.snowflake.SnowflakeConstant.*;

/**
 * @author shizi
 * @since 2020/2/3 10:21 下午
 */
public class SimpleUUidSplicer implements UUidSplicer
{
    @Setter
    @Getter
    private BitAllocator bitAllocator;

    public SimpleUUidSplicer(){}
    public SimpleUUidSplicer(BitAllocator bitAllocator)
    {
        this.bitAllocator = bitAllocator;
    }

    synchronized public Long splice()
    {
        if (null == bitAllocator)
        {
            throw new SnowflakeException("bitAllocator not init");
        }
        long rsv = bitAllocator.getRsvValue();
        long seq = bitAllocator.getSequenceValue();
        long time = bitAllocator.getTimeValue();
        int workerId = bitAllocator.getWorkIdValue();

        return (rsv << RSV_LEFT_SHIFT) | (time << TIME_LEFT_SHIFT) | (seq << SEQ_LEFT_SHIFT) | workerId;
    }
}
