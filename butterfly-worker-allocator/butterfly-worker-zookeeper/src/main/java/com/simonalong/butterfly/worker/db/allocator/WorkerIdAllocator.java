package com.simonalong.butterfly.worker.db.allocator;

/**
 * workerId 的分配器
 *
 * @author shizi
 * @since 2020/2/6 3:39 下午
 */
public interface WorkerIdAllocator
{
    /**
     * 获取workerId
     */
    Integer getWorkerId();

    /**
     * 获取节点 worker_x 的路径信息
     */
    String getWorkerNodePath();
}
