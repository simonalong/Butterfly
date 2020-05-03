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
}
