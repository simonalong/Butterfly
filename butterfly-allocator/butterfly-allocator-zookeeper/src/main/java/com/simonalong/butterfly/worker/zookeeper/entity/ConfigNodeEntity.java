package com.simonalong.butterfly.worker.zookeeper.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author shizi
 * @since 2020/4/25 11:14 AM
 */
@Data
@Accessors(chain = true)
public class ConfigNodeEntity implements Serializable {

    /**
     * 当前业务的最大机器个数，为2的次幂
     */
    private Integer currentMaxMachine;
    /**
     * 时间戳占用bit
     */
    private Integer timestampBits;
    /**
     * 机器占用的bit个数
     */
    private Integer workerBits;
    /**
     * 自增序列占用的bit个数
     */
    private Integer sequenceBits;
    /**
     * 扩容状态：start，end
     */
    private String expandStatus;
}
