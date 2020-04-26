package com.simonalong.butterfly.worker.distribute.client.config;

import com.simonalong.butterfly.sequence.ButterflyConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shizi
 * @since 2020/4/26 11:32 PM
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DistributeClientButterflyConfig extends ButterflyConfig {

    private String zkHose;
}
