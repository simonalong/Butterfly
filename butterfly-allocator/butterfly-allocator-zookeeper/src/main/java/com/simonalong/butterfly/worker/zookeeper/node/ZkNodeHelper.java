package com.simonalong.butterfly.worker.zookeeper.node;

import lombok.experimental.UtilityClass;

import static com.simonalong.butterfly.worker.zookeeper.ZkConstant.ROOT_PATH;
import static com.simonalong.butterfly.worker.zookeeper.ZkConstant.CONFIG_NODE;
import static com.simonalong.butterfly.worker.zookeeper.ZkConstant.WORKER_NODE;
import static com.simonalong.butterfly.worker.zookeeper.ZkConstant.SESSION_NODE;
import static com.simonalong.butterfly.worker.zookeeper.ZkConstant.BIZ_EXPAND_LOCK;
import static com.simonalong.butterfly.worker.zookeeper.ZkConstant.SESSION_CREATE_LOCK;


/**
 * @author shizi
 * @since 2020/4/25 11:07 AM
 */
@UtilityClass
public class ZkNodeHelper {

    // 路径：/butterfly/sequence/{namespace}
    public String getNamespacePath(String namespace) {
        return ROOT_PATH + "/" + namespace;
    }

    // 路径：/butterfly/sequence/{namespace}/config
    public String getConfigPath(String namespace) {
        return getNamespacePath(namespace) + CONFIG_NODE;
    }

    // 路径：/butterfly/sequence/{namespace}/worker_{workerId}
    public String getWorkerPath(String namespace, Integer workerId) {
        return getNamespacePath(namespace) + WORKER_NODE + workerId;
    }

    // 路径：/butterfly/sequence/{namespace}/worker_{workerId}/session
    public String getSession(String namespace, Integer workerId) {
        return getWorkerPath(namespace, workerId) + SESSION_NODE;
    }

    // 路径：/butterfly/sequence/{namespace}/expand_lock
    public String getBizExpandLock(String namespace) {
        return ROOT_PATH + "/" + namespace + BIZ_EXPAND_LOCK;
    }

    // 路径：/butterfly/sequence/{namespace}/worker_{workerId}/session_lock
    public String getSessionCreateLock(String namespace, Integer workerId) {
        return getWorkerPath(namespace, workerId) + SESSION_CREATE_LOCK;
    }
}
