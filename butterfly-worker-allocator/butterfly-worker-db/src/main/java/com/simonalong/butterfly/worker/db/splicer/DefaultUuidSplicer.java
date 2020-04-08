package com.simonalong.butterfly.worker.db.splicer;

import com.simonalong.neo.Neo;
import com.simonalong.neo.exception.UuidException;
import com.simonalong.neo.uid.allocator.BitAllocator;
import com.simonalong.neo.uid.allocator.DefaultBitAllocator;
import lombok.extern.slf4j.Slf4j;

import static com.simonalong.neo.NeoConstant.LOG_PRE;
import static com.simonalong.neo.uid.UuidConstant.*;

/**
 * @author shizi
 * @since 2020/2/3 8:48 下午
 */
@Slf4j
public class DefaultUuidSplicer implements UuidSplicer {

    protected BitAllocator bitAllocator;

    public DefaultUuidSplicer(String bizNamespace, Neo neo) {
        super();
        init(bizNamespace, neo);
    }

    private synchronized void init(String namespace, Neo neo) {
        this.bitAllocator = new DefaultBitAllocator(namespace, neo);

        // 延迟启动固定时间10ms
        delayStart();
    }

    @Override
    synchronized public Long splice() {
        if (null == bitAllocator) {
            throw new UuidException("bitAllocator not init");
        }
        int workerId = bitAllocator.getWorkIdValue();
        long seq = bitAllocator.getSequenceValue();
        long time = bitAllocator.getTimeValue();

        return (time << TIME_LEFT_SHIFT) | (seq << SEQ_LEFT_SHIFT) | workerId;
    }

    /**
     * 延迟一段时间启动
     */
    private void delayStart() {
        synchronized (this) {
            try {
                this.wait(DELAY_START_TIME);
            } catch (InterruptedException e) {
                log.warn(LOG_PRE + "延迟启动失败");
                Thread.currentThread().interrupt();
            }
        }
    }
}
