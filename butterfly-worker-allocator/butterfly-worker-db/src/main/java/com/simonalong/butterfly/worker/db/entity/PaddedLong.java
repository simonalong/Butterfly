package com.simonalong.butterfly.worker.db.entity;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author shizi
 * @since 2020/2/4 11:24 上午
 */
@SuppressWarnings("unused")
public class PaddedLong extends AtomicLong {

    /**
     * 添加防止伪共享
     */
    private volatile long p1, p2, p3, p4, p5, p6 = 7L;

    public PaddedLong(long value) {
        super(value);
    }
}
