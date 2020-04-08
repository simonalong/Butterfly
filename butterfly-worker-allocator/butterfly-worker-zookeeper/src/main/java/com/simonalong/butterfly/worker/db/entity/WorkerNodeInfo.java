package com.simonalong.butterfly.worker.db.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * worker 节点中的配置
 *
 * @author 柿子
 * @since 2019/11/8 10:42 上午
 */
@Data
@Accessors(chain = true)
public class WorkerNodeInfo implements Serializable
{

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
