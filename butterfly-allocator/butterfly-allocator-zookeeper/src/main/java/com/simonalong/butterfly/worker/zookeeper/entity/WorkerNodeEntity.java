package com.simonalong.butterfly.worker.zookeeper.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author shizi
 * @since 2020/4/25 11:16 AM
 */
@Data
@Accessors(chain = true)
public class WorkerNodeEntity implements Serializable {

    /**
     * 当前业务的key
     */
    private String uidKey;
    /**
     * 当前工作节点的下次失效时间
     */
    private Long lastExpireTime;
    /**
     * ip信息
     */
    private String ip;
    /**
     * 进程id
     */
    private String processId;
}
