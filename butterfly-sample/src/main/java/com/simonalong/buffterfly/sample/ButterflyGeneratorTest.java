package com.simonalong.buffterfly.sample;

import com.simonalong.butterfly.sequence.ButterflyIdGenerator;
import lombok.ToString;

/**
 * @author shizi
 * @since 2020/4/10 12:15 AM
 */
public class ButterflyGeneratorTest {

    @Test
    public void test() {
        ZookeeperButterflyConfig config = new ZookeeperButterflyConfig();
        config.setHost();
        ButterflyIdGenerator idGenerator = ButterflyIdGenerator.getInstance(config);
        idGenerator.addNamespaces("test1", "test2");
        System.out.println(idGenerator.getUUid("test1"));
    }
}
