package com.simonalong.butterfly.sequence;

import com.simonalong.butterfly.sequence.allocator.BitAllocator;
import com.simonalong.butterfly.sequence.allocator.BeanBitAllocatorFactory;
import com.simonalong.butterfly.sequence.exception.ButterflyException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static com.simonalong.butterfly.sequence.UuidConstant.DELAY_START_TIME;
import static com.simonalong.butterfly.sequence.UuidConstant.LOG_PRE;
import static com.simonalong.butterfly.sequence.UuidConstant.SEQ_HIGH_BITS;
import static com.simonalong.butterfly.sequence.UuidConstant.WORKER_BITS;
import static com.simonalong.butterfly.sequence.UuidConstant.SEQ_LOW_BITS;
import static com.simonalong.butterfly.sequence.UuidConstant.SEQ_HIGH_MARK;
import static com.simonalong.butterfly.sequence.UuidConstant.WORKER_MARK;
import static com.simonalong.butterfly.sequence.UuidConstant.SEQ_LOW_MARK;


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

        return (time << ((SEQ_HIGH_BITS + WORKER_BITS + SEQ_LOW_BITS)) | (((seq << WORKER_BITS) & SEQ_HIGH_MARK)) | ((workerId << SEQ_LOW_BITS) & WORKER_MARK) | (seq & SEQ_LOW_MARK));
    }
}
