package com.simonalong.butterfly.worker.zookeeper.node;

import lombok.experimental.UtilityClass;

import static com.simonalong.butterfly.worker.zookeeper.ZkConstant.*;

/**
 * @author shizi
 * @since 2020/4/25 11:07 AM
 */
@UtilityClass
public class ZkNodeHelper {

    public String getNamespacePath(String namespace) {
        return ROOT_PATH + "/" + namespace;
    }

    public String getConfigPath(String namespace) {
        return getNamespacePath(namespace) + CONFIG_NODE;
    }

    public String getWorkerPath(String namespace, Integer workerId) {
        return getNamespacePath(namespace) + "/worker_" + workerId;
    }

    public String getSession(String namespace, Integer workerId) {
        return getWorkerPath(namespace, workerId) + SESSION_NODE;
    }

    public String getBizExpandLock(String namespace) {
        return ROOT_PATH + "/" + namespace + BIZ_EXPAND_LOCK;
    }

    public String getSessionCreateLock(String namespace) {
        return ROOT_PATH + "/" + namespace + SESSION_CREATE_LOCK;
    }
}
