package com.simonalong.butterfly.worker.zookeeper;

import com.simonalong.butterfly.sequence.ButterflyConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shizi
 * @since 2020/4/25 10:05 AM
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ZkButterflyConfig extends ButterflyConfig {

    private String host;
}
