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
     * @param maxMachine 待更新的最大机器值
     */
    void updateCurrentMaxMachineNum(int maxMachine);

    /**
     * 更新当前最大的机器个数和更新扩展动作的状态
     *
     * @param maxMachine 待更新的最大机器值和扩容动作的状态
     */
    void updateCurrentMaxMachineNumAndExpandStatus(int maxMachine, String expandStatus);

    /**
     * 更新扩展动作的状态
     *
     * @param expandStatus 扩容动作的状态
     */
    void updateExpandStatus(String expandStatus);
}
