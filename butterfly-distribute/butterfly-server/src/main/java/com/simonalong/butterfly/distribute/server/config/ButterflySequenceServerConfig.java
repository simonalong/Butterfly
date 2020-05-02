package com.simonalong.butterfly.distribute.server.config;

import com.simonalong.butterfly.sequence.ButterflyIdGenerator;
import com.simonalong.butterfly.worker.zookeeper.ZkButterflyConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizi
 * @since 2020/4/28 12:21 AM
 */
@Configuration
public class ButterflySequenceServerConfig {

    @Value("${dubbo.registry.address}")
    private String zookeeperHost;

    @Bean
    public ButterflyIdGenerator butterflyIdGenerator() {
        ZkButterflyConfig config = new ZkButterflyConfig();
        config.setHost(zookeeperHost);
        return ButterflyIdGenerator.getInstance(config);
    }
}
