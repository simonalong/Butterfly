package com.simonalong.butterfly.worker.db.allocator;

import com.ggj.platform.cornerstone.snowflake.NamespaceNodeManager;
import com.ggj.platform.cornerstone.snowflake.TimeAdjuster;
import com.ggj.platform.cornerstone.snowflake.ZookeeperClient;
import com.ggj.platform.cornerstone.snowflake.entity.PaddedLong;
import com.ggj.platform.cornerstone.snowflake.exception.SnowflakeException;

import static com.ggj.platform.cornerstone.snowflake.SnowflakeConstant.*;
import static com.ggj.platform.cornerstone.snowflake.SnowflakeConstant.SEQ_MARK;

/**
 * @author shizi
 * @since 2020/2/3 10:15 下午
 */
public class DefaultBitAllocator implements BitAllocator
{
    private NamespaceNodeManager namespaceNodeManager = NamespaceNodeManager.getInstance();
    private String namespace;
    protected PaddedLong currentTime;
    protected PaddedLong sequence;

    public DefaultBitAllocator(String namespace, ZookeeperClient zkClient)
    {
        super();
        this.namespace = namespace;
        namespaceNodeManager.setZkClient(zkClient);
        namespaceNodeManager.add(namespace);
        currentTime = new PaddedLong(System.currentTimeMillis());
        sequence = new PaddedLong(0);
    }

    /**
     * 获取序列中的保留值
     */
    @Override
    public int getRsvValue()
    {
        return namespaceNodeManager.getRsv(namespace);
    }

    /**
     * 获取序列中的时间值
     */
    @Override
    public long getTimeValue()
    {
        return TimeAdjuster.getRelativeTime(currentTime.get());
    }

    /**
     * 获取序列中的workId对应的值
     */
    @Override
    public int getWorkIdValue()
    {
        return namespaceNodeManager.getWorkerId(namespace);
    }

    /**
     * 获取序列中的自增序列对应的值
     */
    @Override
    public long getSequenceValue()
    {
        if ((sequence.incrementAndGet() & SEQ_MARK) == 0)
        {
            currentTime.incrementAndGet();
            // 调整时间，防止时间过快或者过慢
            TimeAdjuster.adjustTime(currentTime);
            currentTimeIsValid();
            sequence.set(0);
            return 0;
        }
        return sequence.get();
    }

    /**
     * 判断当前系统是否还是可用的
     * <p>
     * 如果当前时间和上次的过期时间之间相差达到一定的阈值，则让当前应用系统不可用
     */
    protected void currentTimeIsValid()
    {
        Long expireTime = namespaceNodeManager.getExpireTime(namespace);
        if (null == expireTime)
        {
            return;
        }
        long now = System.currentTimeMillis();
        if (now >= expireTime)
        {
            throw new SnowflakeException("zk连接失效，超过最大过期时间");
        }
    }
}
