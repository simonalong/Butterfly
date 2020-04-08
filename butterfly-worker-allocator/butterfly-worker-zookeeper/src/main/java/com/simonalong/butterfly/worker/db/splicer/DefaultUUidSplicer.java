package com.simonalong.butterfly.worker.db.splicer;

import com.ggj.platform.cornerstone.snowflake.ZookeeperClient;
import com.ggj.platform.cornerstone.snowflake.allocator.DefaultBitAllocator;
import lombok.extern.slf4j.Slf4j;

import static com.ggj.platform.cornerstone.snowflake.SnowflakeConstant.DELAY_START_TIME;

/**
 * @author shizi
 * @since 2020/2/3 8:48 下午
 */
@Slf4j
public class DefaultUUidSplicer extends SimpleUUidSplicer
{
    public DefaultUUidSplicer(String bizNamespace, ZookeeperClient zkClient)
    {
        super();
        init(bizNamespace, zkClient);
    }

    synchronized void init(String namespace, ZookeeperClient zkClient)
    {
        this.setBitAllocator(new DefaultBitAllocator(namespace, zkClient));

        // 延迟启动固定时间10ms
        delayStart();
    }

    /**
     * 延迟一段时间启动
     */
    private void delayStart()
    {
        synchronized(this)
        {
            try
            {
                this.wait(DELAY_START_TIME);
            }
            catch (InterruptedException e)
            {
                log.warn("延迟启动失败");
                Thread.currentThread().interrupt();
            }
        }
    }
}
