package com.simonalong.butterfly.worker.db;

import static com.simonalong.butterfly.sequence.UuidConstant.LOG_PRE;

/**
 * @author shizi
 * @since 2020/4/25 12:59 AM
 */
public interface DbConstant {

    /**
     * 日志前缀
     */
    String DB_LOG_PRE = LOG_PRE + "[db]";
    /**
     * 全局id生成器的表名
     */
    String UUID_TABLE = "butterfly_uuid_generator";
}
