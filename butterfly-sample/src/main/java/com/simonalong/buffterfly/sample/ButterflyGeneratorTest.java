package com.simonalong.buffterfly.sample;

import com.simonalong.butterfly.sequence.ButterflyIdGenerator;
import com.simonalong.butterfly.worker.db.DbButterflyConfig;
import org.junit.Test;

/**
 * @author shizi
 * @since 2020/4/10 12:15 AM
 */
public class ButterflyGeneratorTest {

//    @Test
//    public void testZk() {
//        ZkButterflyConfig config = new ZkButterflyConfig();
//        config.setHost();
//        ButterflyIdGenerator idGenerator = ButterflyIdGenerator.getInstance(config);
//        idGenerator.addNamespaces("test1", "test2");
//        System.out.println(idGenerator.getUUid("test1"));
//    }

    @Test
    public void testDb() {
        DbButterflyConfig config = new DbButterflyConfig();
        config.setUrl("");
        config.setUserName("");
        config.setPassword("");
        ButterflyIdGenerator idGenerator = ButterflyIdGenerator.getInstance(config);
        idGenerator.addNamespaces("test1", "test2");
        System.out.println(idGenerator.getUUid("test1"));
    }

//    @Test
//    public void testDistribute() {
//        DistributeButterflyConfig config = new DistributeButterflyConfig();
//        config.setHost();
//        ButterflyIdGenerator idGenerator = ButterflyIdGenerator.getInstance(config);
//        idGenerator.addNamespaces("test1", "test2");
//        System.out.println(idGenerator.getUUid("test1"));
//    }
}
