package com.simonalong.butterfly.worker.distribute.config;

import com.simonalong.butterfly.sequence.ButterflyConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shizi
 * @since 2020/4/26 11:32 PM
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DistributeDubboButterflyConfig extends ButterflyConfig {

    /**
     * zookeeper的域名和端口配置，比如：host:port
     */
    private String zkHostAndPort;
}
