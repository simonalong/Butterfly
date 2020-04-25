package com.simonalong.butterfly.worker.zk.entity;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author shizi
 * @since 2020/4/25 11:15 AM
 */
public class PaddedLong extends AtomicLong {

    /**
     * 添加防止伪共享
     */
    private volatile long p1, p2, p3, p4, p5, p6 = 7L;

    public PaddedLong(long value) {
        super(value);
    }
}
