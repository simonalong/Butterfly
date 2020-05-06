package com.simonalong.butterfly.worker.zookeeper;

import static com.simonalong.butterfly.sequence.UuidConstant.LOG_PRE;

/**
 * @author shizi
 * @since 2020/4/25 10:06 AM
 */
public interface ZkConstant {

    /**
     * 日志前缀
     */
    String ZK_LOG_PRE = LOG_PRE + "[zk]";
    /**
     * 根路径
     */
    String ROOT_PATH = "/butterfly/sequence";
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
}
