package com.simonalong.butterfly.sequence;

import com.simonalong.butterfly.sequence.allocator.BitAllocator;
import com.simonalong.butterfly.sequence.allocator.BeanBitAllocatorFactory;
import com.simonalong.butterfly.sequence.exception.ButterflyException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static com.simonalong.butterfly.sequence.UuidConstant.*;

/**
 * @author shizi
 * @since 2020/4/25 12:38 AM
 */
@Slf4j
final class UuidSplicer {

    @Getter
    private BitAllocator bitAllocator;

    UuidSplicer(String bizNamespace, ButterflyConfig butterflyConfig) {
        synchronized (this) {
            this.bitAllocator = BeanBitAllocatorFactory.getBitAllocator(bizNamespace, butterflyConfig);

            // 延迟启动固定时间10ms
            try {
                this.wait(DELAY_START_TIME);
            } catch (InterruptedException e) {
                log.warn(LOG_PRE + "delay start fail");
                Thread.currentThread().interrupt();
            }
        }
    }

    synchronized Long splice() {
        if (null == bitAllocator) {
            throw new ButterflyException("bitAllocator not init");
        }
        int workerId = bitAllocator.getWorkIdValue();
        long seq = bitAllocator.getSequenceValue();
        long time = bitAllocator.getTimeValue();

        return (time << TIME_LEFT_SHIFT) | (seq << SEQ_LEFT_SHIFT) | workerId;
    }
}
