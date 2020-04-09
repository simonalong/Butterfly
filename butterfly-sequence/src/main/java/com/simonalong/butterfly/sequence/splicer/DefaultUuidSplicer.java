package com.simonalong.butterfly.sequence.splicer;

import com.simonalong.butterfly.worker.api.UuidGenerator;
import com.simonalong.butterfly.worker.api.allocator.BitAllocator;
import com.simonalong.butterfly.worker.api.allocator.DefaultBitAllocator;
import com.simonalong.butterfly.worker.api.exception.ButterflyException;
import lombok.extern.slf4j.Slf4j;

import static com.simonalong.butterfly.worker.api.UuidConstant.DELAY_START_TIME;
import static com.simonalong.butterfly.worker.api.UuidConstant.SEQ_LEFT_SHIFT;
import static com.simonalong.butterfly.worker.api.UuidConstant.TIME_LEFT_SHIFT;

/**
 * @author shizi
 * @since 2020/2/3 8:48 下午
 */
@Slf4j
public class DefaultUuidSplicer implements UuidSplicer {

    protected BitAllocator bitAllocator;

    public DefaultUuidSplicer(String bizNamespace, UuidGenerator uuidGenerator) {
        super();
        init(bizNamespace, uuidGenerator);
    }

    private synchronized void init(String namespace, UuidGenerator uuidGenerator) {
        this.bitAllocator = new DefaultBitAllocator(namespace, uuidGenerator);

        // 延迟启动固定时间10ms
        delayStart();
    }

    @Override
    synchronized public Long splice() {
        if (null == bitAllocator) {
            throw new ButterflyException("bitAllocator not init");
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
