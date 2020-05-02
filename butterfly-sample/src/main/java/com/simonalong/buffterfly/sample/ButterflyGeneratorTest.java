package com.simonalong.buffterfly.sample;

import com.simonalong.butterfly.sequence.ButterflyIdGenerator;
import com.simonalong.butterfly.worker.db.DbButterflyConfig;
import com.simonalong.butterfly.worker.distribute.config.DistributeButterflyConfig;
import com.simonalong.butterfly.worker.zookeeper.ZkButterflyConfig;
import org.junit.Test;

/**
 * @author shizi
 * @since 2020/4/10 12:15 AM
 */
public class ButterflyGeneratorTest {

    @Test
    public void testZk() {
        ZkButterflyConfig config = new ZkButterflyConfig();
        config.setHost("localhost:2181");
        ButterflyIdGenerator idGenerator = ButterflyIdGenerator.getInstance(config);
        idGenerator.addNamespaces("test1", "test2");

        // {symbol=0, sequence=1, workerId=11, abstractTime=2020-05-03 00:26:26.817, time=6135986817, uuid=25736194050498571}
        System.out.println(ButterflyIdGenerator.parseUid(idGenerator.getUUid("test1")));
        // {symbol=0, sequence=2, workerId=11, abstractTime=2020-05-03 00:26:26.817, time=6135986817, uuid=25736194050506763}
        System.out.println(ButterflyIdGenerator.parseUid(idGenerator.getUUid("test1")));
    }

    @Test
    public void testDb() {
        DbButterflyConfig config = new DbButterflyConfig();
        config.setUrl("jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false&&allowPublicKeyRetrieval=true");
        config.setUserName("neo_test");
        config.setPassword("neo@Test123");

        ButterflyIdGenerator idGenerator = ButterflyIdGenerator.getInstance(config);
        idGenerator.addNamespaces("test1", "test2");
        // {symbol=0, sequence=1, workerId=0, abstractTime=2020-05-03 00:33:19.140, time=6136399140, uuid=25737923458506752}
        System.out.println(ButterflyIdGenerator.parseUid(idGenerator.getUUid("test1")));
        // {symbol=0, sequence=2, workerId=0, abstractTime=2020-05-03 00:33:19.140, time=6136399140, uuid=25737923458514944}
        System.out.println(ButterflyIdGenerator.parseUid(idGenerator.getUUid("test1")));
    }

    @Test
    public void testDistribute() {
        DistributeButterflyConfig config = new DistributeButterflyConfig();
        config.setZkHose("localhost:2181");
        ButterflyIdGenerator idGenerator = ButterflyIdGenerator.getInstance(config);
        idGenerator.addNamespaces("test1", "test2");

        // {symbol=0, sequence=0, workerId=11, abstractTime=2020-05-03 01:34:07.586, time=6140047586, uuid=25753226150150155}
        System.out.println(ButterflyIdGenerator.parseUid(idGenerator.getUUid("test1")));
        // {symbol=0, sequence=1, workerId=11, abstractTime=2020-05-03 01:34:07.586, time=6140047586, uuid=25753226150158347}
        System.out.println(ButterflyIdGenerator.parseUid(idGenerator.getUUid("test1")));
    }
}
