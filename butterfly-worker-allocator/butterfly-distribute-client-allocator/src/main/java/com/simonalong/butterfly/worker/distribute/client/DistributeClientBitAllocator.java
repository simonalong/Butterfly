package com.simonalong.butterfly.worker.distribute.client;

import com.simonalong.butterfly.sequence.allocator.BitAllocator;

/**
 * @author shizi
 * @since 2020/4/27 7:52 PM
 */
public class DistributeClientBitAllocator implements BitAllocator {

    @Override
    public long getTimeValue() {
        return 0;
    }

    @Override
    public long getSequenceValue() {
        return 0;
    }

    @Override
    public int getWorkIdValue() {
        return 0;
    }
}
