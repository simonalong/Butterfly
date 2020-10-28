package com.simonalong.butterfly.distribute.config;

import com.simonalong.butterfly.sequence.ButterflyIdGenerator;
import com.simonalong.butterfly.worker.db.DbButterflyConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizi
 * @since 2020/10/28 8:04 下午
 */
@Configuration
public class ButterflySequenceServerConfig {

    @Value("${butterfly.url}")
    private String url;
    @Value("${butterfly.username}")
    private String username;
    @Value("${butterfly.password}")
    private String password;

    @Bean
    public ButterflyIdGenerator butterflyIdGenerator() {
        DbButterflyConfig config = new DbButterflyConfig();
        config.setUrl(url);
        config.setUserName(username);
        config.setPassword(password);
        return ButterflyIdGenerator.getInstance(config);
    }
}
