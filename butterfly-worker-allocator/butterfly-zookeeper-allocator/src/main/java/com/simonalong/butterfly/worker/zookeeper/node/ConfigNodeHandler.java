package com.simonalong.butterfly.worker.zookeeper.node;

/**
 * 节点 config 的读取和更新管理器
 *
 * @author shizi
 * @since 2020/4/25 11:17 AM
 */
public interface ConfigNodeHandler {

    /**
     * 读取当前最大的机器个数
     * @return value
     */
    int getCurrentMaxMachineNum();

    /**
     * 更新当前最大的机器个数
     *
     * @param value 待更新的值
     */
    void updateCurrentMaxMachineNum(int value);
}
