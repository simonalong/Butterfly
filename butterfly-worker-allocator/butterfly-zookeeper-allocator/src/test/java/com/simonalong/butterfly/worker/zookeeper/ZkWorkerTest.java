package com.simonalong.butterfly.worker.zookeeper;

import com.simonalong.butterfly.sequence.ButterflyIdGenerator;
import org.junit.Test;

/**
 * @author shizi
 * @since 2020/5/3 12:03 AM
 */
public class ZkWorkerTest {

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
}
