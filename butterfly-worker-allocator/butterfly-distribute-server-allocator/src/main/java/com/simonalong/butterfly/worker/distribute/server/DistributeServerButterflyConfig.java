package com.simonalong.butterfly.worker.distribute.server;

import com.simonalong.butterfly.sequence.ButterflyConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shizi
 * @since 2020/4/26 11:52 PM
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DistributeServerButterflyConfig extends ButterflyConfig {

    private String zkHost;
}
