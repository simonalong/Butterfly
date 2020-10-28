package com.simonalong.butterfly.worker.distribute.config;

import com.simonalong.butterfly.sequence.ButterflyConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shizi
 * @since 2020/10/28 5:43 下午
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DistributeRestfulButterflyConfig extends ButterflyConfig {

    /**
     * 域名和端口配置，比如：host:port
     */
    private String hostAndPort;
}
