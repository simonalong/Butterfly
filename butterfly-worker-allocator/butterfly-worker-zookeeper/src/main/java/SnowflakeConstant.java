import lombok.experimental.UtilityClass;

import java.util.concurrent.TimeUnit;

/**
 * @author shizi
 * @since 2020/2/3 11:54 上午
 */
@UtilityClass
public class SnowflakeConstant
{
    public static final String LOG_PRE = "[ggj_snowflake]:";
    /**
     * 应用二方包名字
     */
    public static final String GGJ_APP = "ggj_snowflake";
    /**
     * 格格家中间件的zk顶层路径
     */
    public static final String GGJ_PATH = "/ggj/platform";
    /**
     * 根路径
     */
    public static final String ROOT_PATH = GGJ_PATH + "/sequence/snowflake";
    /**
     * 机器节点的左前缀
     */
    public static final String WORKER_NODE = "/worker";
    /**
     * 每个业务中的配置节点路径
     */
    public static final String CONFIG_NODE = "/config";
    /**
     * 业务机器不足时候的扩容锁
     */
    public static final String BIZ_EXPAND_LOCK = "/expand_lock";
    /**
     * 临时节点的路径
     */
    public static final String SESSION_NODE = "/session";
    /**
     * session创建时候的锁
     */
    public static final String SESSION_CREATE_LOCK = "/session_lock";
    /**
     * 分布式模式下的业务节点
     */
    public static final String DISTRIBUTE_SERVER = "snowflake-server";
    /**
     * 雪花算法：本机模式
     */
    public static final String MODE_LOCAL = "local";
    /**
     * 雪花算法：分布式模式
     */
    public static final String MODE_DISTRIBUTE = "distribute";

    /**
     * zk中保留对应节点数据的时间长度
     */
    public static final long KEEP_EXPIRE_TIME = TimeUnit.HOURS.toMillis(24);
    /**
     * 2019-11-9 0.0.0.000 对应的long型时间，作为回拨时间的最低进行计算
     */
    public static final long START_TIME = 1573228800000L;

    /**
     * 机器id增加占用的bit数
     */
    public static final int WORKER_BITS = 11;
    public static final long WORKER_MAX_SIZE = 1 << WORKER_BITS;
    /**
     * 自增域占用的bit数
     */
    public static final int SEQ_BITS = 9;
    public static final long SEQ_MARK = ~(-1L << SEQ_BITS);
    public static final long SEQ_MAX_SIZE = 1 << SEQ_BITS;
    public static final long SEQ_LEFT_SHIFT = WORKER_BITS;
    /**
     * 时间占用的bit数
     */
    public static final int TIME_BITS = 41;
    public static final long TIME_LEFT_SHIFT = SEQ_BITS + SEQ_LEFT_SHIFT;
    /**
     * 预留占用的bit数
     */
    public static final int RSV_BITS = 2;
    public static final long RSV_LEFT_SHIFT = TIME_BITS + TIME_LEFT_SHIFT;

    /**
     * 时间过慢后域当前时间的门限，当前暂时设置为20个小时
     */
    public static final long DELAY_THREAD_HOLD = TimeUnit.HOURS.toMillis(20);
    /**
     * 时间回拨的容忍度，2秒
     */
    public static final long TIME_BACK = 2000L;
    /**
     * 延迟启动时间，10ms
     */
    public static final long DELAY_START_TIME = 10L;
    /**
     * 心跳间隔时间单位秒
     */
    public static final long HEART_INTERVAL_TIME = 5L;

    public String getNamespacePath(String namespace)
    {
        return ROOT_PATH + "/" + namespace;
    }

    public String getConfigPath(String namespace)
    {
        return getNamespacePath(namespace) + CONFIG_NODE;
    }

    public String getWorkerPath(String namespace, Integer workerId)
    {
        return getNamespacePath(namespace) + "/worker_" + workerId;
    }

    public String getSession(String namespace, Integer workerId)
    {
        return getWorkerPath(namespace, workerId) + SESSION_NODE;
    }

    public String getBizExpandLock(String namespace)
    {
        return ROOT_PATH + "/" + namespace + BIZ_EXPAND_LOCK;
    }

    public String getSessionCreateLock(String namespace)
    {
        return ROOT_PATH + "/" + namespace + SESSION_CREATE_LOCK;
    }
}
