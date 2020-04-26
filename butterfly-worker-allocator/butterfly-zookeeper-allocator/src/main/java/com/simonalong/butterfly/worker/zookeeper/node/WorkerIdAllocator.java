package com.simonalong.butterfly.worker.zookeeper.node;

/**
 * @author shizi
 * @since 2020/4/25 10:45 AM
 */
public interface WorkerIdAllocator {

    /**
     * 获取workerId
     */
    Integer getWorkerId();

    /**
     * 获取节点 worker_x 的路径信息
     */
    String getWorkerNodePath();
}
