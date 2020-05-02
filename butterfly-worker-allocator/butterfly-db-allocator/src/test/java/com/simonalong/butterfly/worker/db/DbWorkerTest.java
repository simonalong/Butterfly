package com.simonalong.butterfly.worker.db;

import com.simonalong.butterfly.sequence.ButterflyIdGenerator;
import org.junit.Test;

/**
 * db的workerId 中的最初是从0开始
 *
 * @author shizi
 * @since 2020/5/3 12:32 AM
 */
public class DbWorkerTest {

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
}
