package com.simonalong.butterfly.worker.zk;

import java.util.concurrent.TimeUnit;

import static com.simonalong.butterfly.sequence.UuidConstant.LOG_PRE;

/**
 * @author shizi
 * @since 2020/4/25 10:06 AM
 */
public interface ZkConstant {

    String ZK_LOG_PRE = LOG_PRE + "[zk]";
    /**
     * 应用二方包名字
     */
    String GGJ_APP = "ggj_snowflake";
    /**
     * 格格家中间件的zk顶层路径
     */
    String GGJ_PATH = "/ggj/platform";
    /**
     * 根路径
     */
    String ROOT_PATH = GGJ_PATH + "/sequence/snowflake";
    /**
     * 机器节点的左前缀
     */
    String WORKER_NODE = "/worker";
    /**
     * 每个业务中的配置节点路径
     */
    String CONFIG_NODE = "/config";
    /**
     * 业务机器不足时候的扩容锁
     */
    String BIZ_EXPAND_LOCK = "/expand_lock";
    /**
     * 临时节点的路径
     */
    String SESSION_NODE = "/session";
    /**
     * session创建时候的锁
     */
    String SESSION_CREATE_LOCK = "/session_lock";
    /**
     * 分布式模式下的业务节点
     */
    String DISTRIBUTE_SERVER = "snowflake-server";
    /**
     * 雪花算法：本机模式
     */
    String MODE_LOCAL = "local";
    /**
     * 雪花算法：分布式模式
     */
    String MODE_DISTRIBUTE = "distribute";

    /**
     * zk中保留对应节点数据的时间长度
     */
    long KEEP_EXPIRE_TIME = TimeUnit.HOURS.toMillis(24);
    /**
     * 2019-11-9 0.0.0.000 对应的long型时间，作为回拨时间的最低进行计算
     */
    long START_TIME = 1573228800000L;

    /**
     * 机器id增加占用的bit数
     */
    int WORKER_BITS = 11;
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
     * 预留占用的bit数
     */
    int RSV_BITS = 2;
    long RSV_LEFT_SHIFT = TIME_BITS + TIME_LEFT_SHIFT;

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
