package com.simonalong.butterfly.worker.db;

import com.simonalong.butterfly.sequence.ButterflyConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shizi
 * @since 2020/4/25 12:53 AM
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DbButterflyConfig extends ButterflyConfig {

    private String url;
    private String userName;
    private String password;
    /**
     * 是否自动创建相关表；true：当不存在相关表时，自动创建，false：任何情况都不自动创建
     */
    private Boolean autoCreateTable = false;
}
