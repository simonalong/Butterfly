package com.simonalong.butterfly.worker.db.allocator;

/**
 * 序列中的bit对应的值的分配器
 * <p>
 * 1bit符号 + 2bit预留 + 41bit时间 + 9bit序列 + 11bit的workId
 *
 * @author shizi
 * @since 2020/2/3 8:07 下午
 */
public interface BitAllocator
{
    /**
     * 获取序列中的保留值
     */
    int getRsvValue();

    /**
     * 获取序列中的时间值
     */
    long getTimeValue();

    /**
     * 获取序列中的自增序列对应的值
     */
    long getSequenceValue();

    /**
     * 获取序列中的workId对应的值
     */
    int getWorkIdValue();
}
