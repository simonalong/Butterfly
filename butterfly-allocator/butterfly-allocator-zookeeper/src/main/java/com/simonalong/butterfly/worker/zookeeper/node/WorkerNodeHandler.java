package com.simonalong.butterfly.worker.zookeeper.node;

/**
 * 节点 worker_xx 读取和更新管理器
 *
 * @author shizi
 * @since 2020/4/25 11:19 AM
 */
public interface WorkerNodeHandler {

    /**
     * 当前业务的key
     * @return uidKey
     */
    String getUidKey();

    /**
     * 当前工作节点的下次失效时间
     * @return 时间
     */
    Long getLastExpireTime();

    /**
     * ip信息
     * @return ip
     */
    String getIp();

    /**
     * 进程id
     * @return pid
     */
    String getProcessId();

    /**
     * 获取节点的下表索引
     * @return 节点名下标
     */
    Integer getWorkerId();

    /**
     * 刷新节点信息
     */
    void refreshNodeInfo();

    /**
     * 刷新worker节点信息
     * @param workerNodePath 工作节点路径
     */
    void refreshNodeInfo(String workerNodePath);
}
