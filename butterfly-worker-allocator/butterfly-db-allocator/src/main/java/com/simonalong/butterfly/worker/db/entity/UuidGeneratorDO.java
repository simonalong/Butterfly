package com.simonalong.butterfly.worker.db.entity;

import com.simonalong.neo.annotation.Column;
import com.simonalong.neo.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author shizi
 * @since 2020/4/26 10:28 PM
 */
@Data
@Table("neo_uuid_generator")
@Accessors(chain = true)
public class UuidGeneratorDO {


    /**
     * 主键id
     */
    @Column("id")
    private Long id;

    /**
     * 命名空间
     */
    @Column("namespace")
    private String namespace;

    /**
     * 工作id
     */
    @Column("work_id")
    private Integer workId;

    /**
     * 下次失效时间
     */
    @Column("last_expire_time")
    private long lastExpireTime;

    /**
     * 本次启动唯一id
     */
    @Column("uid")
    private String uid;

    /**
     * 进程id
     */
    @Column("process_id")
    private String processId;

    /**
     * ip
     */
    @Column("ip")
    private String ip;
}

