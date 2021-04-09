package com.simonalong.butterfly.sequence;

import java.util.concurrent.TimeUnit;

/**
 * @author shizi
 * @since 2020/2/3 11:54 上午
 */
public interface UuidConstant {

    /**
     * log前缀
     */
    String LOG_PRE = "[butterfly]";

    /**
     * 自增域的低位bit数
     */
    int SEQ_LOW_BITS = 1;
    /**
     * 机器域占用的bit数
     */
    int WORKER_BITS = 13;
    /**
     * 自增域的高位bit数
     */
    int SEQ_HIGH_BITS = 8;
    /**
     * 时间域占用的bit数
     */
    int TIME_BITS = 41;
    /**
     * 符号域占用的bit数
     */
    int SYMBOL_BITS = 1;
    /**
     * 自增域占用的bit数
     */
    int SEQ_BITS = SEQ_HIGH_BITS + SEQ_LOW_BITS;

    /**
     * 自增域的低位的掩码
     */
    long SEQ_LOW_MARK = ~(-1L << SEQ_LOW_BITS);
    /**
     * 机器id的掩码
     */
    long WORKER_MARK = (~(-1L << WORKER_BITS)) << SEQ_LOW_BITS;
    /**
     * 自增域的高位的掩码
     */
    long SEQ_HIGH_MARK = ~(-1L << (SEQ_HIGH_BITS)) << (WORKER_BITS + SEQ_LOW_BITS);
    /**
     * 时间域的高位的掩码
     */
    long TIME_MARK = ~(-1L << (TIME_BITS)) << (SEQ_HIGH_BITS + WORKER_BITS + SEQ_LOW_BITS);
    /**
     * 符号域的高位的掩码
     */
    long SYMBOL_MARK = ~(-1L << (SYMBOL_BITS)) << (TIME_BITS + SEQ_HIGH_BITS + WORKER_BITS + SEQ_LOW_BITS);

    /**
     * 自增域的虚拟掩码
     */
    long SEQ_MARK = ~(-1L << (SEQ_LOW_BITS + SEQ_HIGH_BITS));

    /**
     * worker节点的最大值
     */
    long MAX_WORKER_SIZE = 1 << WORKER_BITS;
    /**
     * 自增域最大值
     */
    long SEQ_MAX_SIZE = 1 << SEQ_BITS;

    /**
     * 时间过慢后域当前时间的门限，当前暂时设置为20个小时
     */
    long DELAY_THREAD_HOLD = TimeUnit.HOURS.toMillis(20);
    /**
     * 节点保留的时间
     */
    long KEEP_NODE_EXIST_TIME = TimeUnit.HOURS.toMillis(24);
    /**
     * 时间回拨的容忍度，2秒
     */
    long TIME_BACK = 2000L;
    /**
     * 延迟启动时间，10ms
     */
    long DELAY_START_TIME = 10L;
    /**
     * 分布式模式下的业务节点
     */
    String DISTRIBUTE_SERVER = "butterfly-server";
    /**
     * 心跳间隔时间单位秒
     */
    long HEART_TIME = 5L;
}
