package com.simonalong.butterfly.sequence;

import lombok.ToString;

/**
 * @author shizi
 * @since 2020/4/10 12:15 AM
 */
public class ButterflyGeneratorTest extends BaseTest{

    @Test
    public void test() {
        ZookeeperButterflyConfig config = new ZookeeperButterflyConfig();
        config.setHost();
        ButterflyIdGenerator idGenerator = ButterflyIdGenerator.getInstance(config);
        idGenerator.addNamespaces("test1", "test2");
        show(idGenerator.getUUid("test1"));
    }
}
