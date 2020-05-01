package com.simonalong.butterfly.sequence.allocator;

/**
 * @author shizi
 * @since 2020/5/1 11:00 AM
 */
public interface ExpireBitAllocator extends BitAllocator {

    /**
     * 获取该节点上一次的过期时间
     *
     * @param namespace 命名空间
     * @return 上次过期时间
     */
    default Long getLastExpireTime(String namespace) {
        return null;
    }
}
