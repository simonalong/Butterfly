package com.simonalong.butterfly.worker.db;

import java.util.concurrent.TimeUnit;

import static com.simonalong.butterfly.sequence.UuidConstant.LOG_PRE;

/**
 * @author shizi
 * @since 2020/4/25 12:59 AM
 */
public interface DbConstant {

    String DB_LOG_PRE = LOG_PRE + "[db]";
    /**
     * 全局id生成器的表名
     */
    String UUID_TABLE = "neo_uuid_generator";
    /**
     * log前缀
     */
    String UUID_LOG_PRE = "[neo_snowflake] ";
    /**
     * zk中保留对应节点数据的时间长度
     */
    long KEEP_EXPIRE_TIME = TimeUnit.HOURS.toMillis(24);
    /**
     * 2020-02-22 00:00:00.000 对应的long型时间，作为回拨时间的最低进行计算
     */
    long START_TIME = 1582300800000L;

    /**
     * 机器id增加占用的bit数
     */
    int WORKER_BITS = 13;
    long WORKER_MAX_SIZE = 1 << WORKER_BITS;
    /**
     * 自增域占用的bit数
     */
    int SEQ_BITS = 9;
    long SEQ_MARK = ~(-1L << SEQ_BITS);
    long SEQ_MAX_SIZE = 1 << SEQ_BITS;
    long SEQ_LEFT_SHIFT = WORKER_BITS;
    /**
     * 时间占用的bit数
     */
    int TIME_BITS = 41;
    long TIME_LEFT_SHIFT = SEQ_BITS + SEQ_LEFT_SHIFT;

    /**
     * 符号位
     */
    long SYMBOL_LEFT_SHIFT = TIME_BITS + TIME_LEFT_SHIFT;

    /**
     * 时间过慢后域当前时间的门限，当前暂时设置为20个小时
     */
    long DELAY_THREAD_HOLD = TimeUnit.HOURS.toMillis(20);
    /**
     * 时间回拨的容忍度，2秒
     */
    long TIME_BACK = 2000L;
    /**
     * 延迟启动时间，10ms
     */
    long DELAY_START_TIME = 10L;
    /**
     * 心跳间隔时间单位秒
     */
    long HEART_INTERVAL_TIME = 5L;
}
