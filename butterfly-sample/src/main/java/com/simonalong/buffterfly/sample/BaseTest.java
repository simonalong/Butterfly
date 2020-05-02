package com.simonalong.buffterfly.sample;

import com.simonalong.butterfly.sequence.ButterflyIdGenerator;
import com.simonalong.butterfly.worker.db.DbButterflyConfig;
import com.simonalong.butterfly.worker.distribute.config.DistributeButterflyConfig;
import com.simonalong.butterfly.worker.zookeeper.ZkButterflyConfig;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shizi
 * @since 2020/4/10 12:15 AM
 */
public class BaseTest {

    @SuppressWarnings("all")
    public ExecutorService executorService = Executors.newFixedThreadPool(1000, new ThreadFactory() {
        private AtomicInteger count = new AtomicInteger(0);

        @Override
        @SuppressWarnings("all")
        public Thread newThread(Runnable r) {
            return new Thread(r, "test_" + count.getAndIncrement());
        }
    });

//    @Test
//    public void testZk() {
//        ZkButterflyConfig config = new ZkButterflyConfig();
//        config.setHost("localhost:2181");
//        ButterflyIdGenerator idGenerator = ButterflyIdGenerator.getInstance(config);
//        idGenerator.addNamespaces("test1", "test2");
//
//        // {symbol=0, sequence=1, workerId=11, abstractTime=2020-05-03 00:26:26.817, time=6135986817, uuid=25736194050498571}
//        show(ButterflyIdGenerator.parseUid(idGenerator.getUUid("test1")));
//        // {symbol=0, sequence=2, workerId=11, abstractTime=2020-05-03 00:26:26.817, time=6135986817, uuid=25736194050506763}
//        show(ButterflyIdGenerator.parseUid(idGenerator.getUUid("test1")));
//    }
//
//    @Test
//    public void testDb() {
//        DbButterflyConfig config = new DbButterflyConfig();
//        config.setUrl("jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false&&allowPublicKeyRetrieval=true");
//        config.setUserName("neo_test");
//        config.setPassword("neo@Test123");
//
//        ButterflyIdGenerator idGenerator = ButterflyIdGenerator.getInstance(config);
//        idGenerator.addNamespaces("test1", "test2");
//        // {symbol=0, sequence=1, workerId=0, abstractTime=2020-05-03 00:33:19.140, time=6136399140, uuid=25737923458506752}
//        show(ButterflyIdGenerator.parseUid(idGenerator.getUUid("test1")));
//        // {symbol=0, sequence=2, workerId=0, abstractTime=2020-05-03 00:33:19.140, time=6136399140, uuid=25737923458514944}
//        show(ButterflyIdGenerator.parseUid(idGenerator.getUUid("test1")));
//    }
//
//    @Test
//    public void testDistribute() {
//        DistributeButterflyConfig config = new DistributeButterflyConfig();
//        config.setZkHose("localhost:2181");
//        ButterflyIdGenerator idGenerator = ButterflyIdGenerator.getInstance(config);
//        idGenerator.addNamespaces("test1", "test2");
//
//        // {symbol=0, sequence=0, workerId=11, abstractTime=2020-05-03 01:34:07.586, time=6140047586, uuid=25753226150150155}
//        show(ButterflyIdGenerator.parseUid(idGenerator.getUUid("test1")));
//        // {symbol=0, sequence=1, workerId=11, abstractTime=2020-05-03 01:34:07.586, time=6140047586, uuid=25753226150158347}
//        show(ButterflyIdGenerator.parseUid(idGenerator.getUUid("test1")));
//    }

    public void show(Object obj) {
        if (null == obj) {
            show("obj is null");
        } else {
            System.out.println(obj.toString());
        }
    }

    /**
     * 每次调用的统计，统计其中的QPS
     *
     * @param generator     id生成器
     * @param biz           业务名
     * @param callNum       调用多少次
     * @param concurrentNum 每次的并发量
     */
    @SneakyThrows
    public void generateFun(ButterflyIdGenerator generator, String biz, Integer callNum, Integer concurrentNum) {
        long start = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(callNum * concurrentNum);
        for (int index = 0; index < concurrentNum; index++) {
            executorService.execute(() -> {
                for (int j = 0; j < callNum; j++) {
                    generator.getUUid(biz);
                    latch.countDown();
                }
            });
        }

        latch.await();
        long duration = System.currentTimeMillis() - start;
        show("biz=" + biz + ", qps = " + (callNum * concurrentNum) / (duration * 10.0) + "单位（w/s）");
    }
}
