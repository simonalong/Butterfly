package com.simonalong.buffterfly.sample;

import com.simonalong.butterfly.sequence.ButterflyIdGenerator;
import com.simonalong.butterfly.worker.distribute.config.DistributeButterflyConfig;
import lombok.SneakyThrows;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author shizi
 * @since 2020/5/3 2:11 AM
 */
public class ZkPressIdGeneratorTest extends BaseTest {

    private static DistributeButterflyConfig config = new DistributeButterflyConfig();

    @BeforeClass
    public static void beforeClass() {
        config.setZkHose("localhost:2181");
    }

    /**
     * 基本测试
     */
    @Test
    public void generateTest1() {
        ButterflyIdGenerator generator = ButterflyIdGenerator.getInstance(config);
        generator.addNamespaces("test1", "test2");

        //{symbol=0, sequence=0, workerId=11, abstractTime=2020-05-03 01:34:07.590, time=6140047590, uuid=25753226166927371}
        //{symbol=0, sequence=1, workerId=11, abstractTime=2020-05-03 01:34:07.590, time=6140047590, uuid=25753226166935563}
        //{symbol=0, sequence=2, workerId=11, abstractTime=2020-05-03 01:34:07.590, time=6140047590, uuid=25753226166943755}
        //{symbol=0, sequence=3, workerId=11, abstractTime=2020-05-03 01:34:07.590, time=6140047590, uuid=25753226166951947}
        //{symbol=0, sequence=4, workerId=11, abstractTime=2020-05-03 01:34:07.590, time=6140047590, uuid=25753226166960139}
        //{symbol=0, sequence=5, workerId=11, abstractTime=2020-05-03 01:34:07.590, time=6140047590, uuid=25753226166968331}
        //{symbol=0, sequence=6, workerId=11, abstractTime=2020-05-03 01:34:07.590, time=6140047590, uuid=25753226166976523}
        //{symbol=0, sequence=7, workerId=11, abstractTime=2020-05-03 01:34:07.590, time=6140047590, uuid=25753226166984715}
        show(ButterflyIdGenerator.parseUid(generator.getUUid("test1")));
        show(ButterflyIdGenerator.parseUid(generator.getUUid("test1")));
        show(ButterflyIdGenerator.parseUid(generator.getUUid("test2")));
        show(ButterflyIdGenerator.parseUid(generator.getUUid("test2")));
        show(ButterflyIdGenerator.parseUid(generator.getUUid("test2")));
        show(ButterflyIdGenerator.parseUid(generator.getUUid("test2")));
        show(ButterflyIdGenerator.parseUid(generator.getUUid("test2")));
        show(ButterflyIdGenerator.parseUid(generator.getUUid("test2")));
    }

    /**
     * 持续的低qps的压测，则这个QPS还是比较低，完全满足业务需求
     */
    @Test
    @SneakyThrows
    public void testQps1() {
        // todo 跑一下
        ButterflyIdGenerator generator = ButterflyIdGenerator.getInstance(config);
        generator.addNamespaces("biz0");
        int count = 10;
        int callNum = 10;
        int concurrentNum = 100;

        for (int i = 0; i < count; i++) {
            generateFun(generator, "biz0", callNum, concurrentNum);
            Thread.sleep(1000);
        }

        //biz=biz0, qps = 4.0单位（w/s）
        //biz=biz0, qps = 9.090909090909092单位（w/s）
        //biz=biz0, qps = 7.142857142857143单位（w/s）
        //biz=biz0, qps = 11.11111111111111单位（w/s）
        //biz=biz0, qps = 7.6923076923076925单位（w/s）
        //biz=biz0, qps = 5.0单位（w/s）
        //biz=biz0, qps = 6.666666666666667单位（w/s）
        //biz=biz0, qps = 11.11111111111111单位（w/s）
        //biz=biz0, qps = 8.333333333333334单位（w/s）
        //biz=biz0, qps = 7.6923076923076925单位（w/s）
    }

    /**
     * 低qps一段时间后到高QPS，可以支撑更高，但是一旦持续的高并发，则后面会慢慢降下来
     */
    @Test
    @SneakyThrows
    public void testQps2() {
        // todo 跑一下
        ButterflyIdGenerator generator = ButterflyIdGenerator.getInstance(config);
        generator.addNamespaces("biz0");
        int count = 10;
        int callNum = 10;
        int concurrentNum = 100;

        for (int i = 0; i < count; i++) {
            generateFun(generator, "biz0", callNum + i * i * 100, concurrentNum + i * 10 * i);
            Thread.sleep(1000);
        }

        //biz=biz0, qps = 5.882352941176471单位（w/s）
        //biz=biz0, qps = 80.66666666666667单位（w/s）
        //biz=biz0, qps = 410.0单位（w/s）
        //biz=biz0, qps = 576.3333333333334单位（w/s）
        //biz=biz0, qps = 789.811320754717单位（w/s）
        //biz=biz0, qps = 1021.5116279069767单位（w/s）
        //biz=biz0, qps = 1177.7304964539007单位（w/s）
        //biz=biz0, qps = 71.68770106409305单位（w/s）
        //biz=biz0, qps = 57.44005812545411单位（w/s）
        //biz=biz0, qps = 55.02609603340292单位（w/s）
    }

    /**
     * 持续的高QPS，则只能达到最高的理论值（51.2w/s）
     */
    @Test
    @SneakyThrows
    public void testQps3() {
        // todo 跑一下
        ButterflyIdGenerator generator = ButterflyIdGenerator.getInstance(config);
        generator.addNamespaces("biz0");
        int count = 10;
        int callNum = 1000;
        int concurrentNum = 10000;

        for (int i = 0; i < count; i++) {
            generateFun(generator, "biz0", callNum, concurrentNum);
            Thread.sleep(1000);
        }

        //biz=biz0, qps = 51.956149010235364单位（w/s）
        //biz=biz0, qps = 53.98402072986396单位（w/s）
        //biz=biz0, qps = 53.97527932207049单位（w/s）
        //biz=biz0, qps = 53.972366148531954单位（w/s）
        //biz=biz0, qps = 53.97527932207049单位（w/s）
        //biz=biz0, qps = 53.978192810104716单位（w/s）
        //biz=biz0, qps = 53.97527932207049单位（w/s）
        //biz=biz0, qps = 53.97527932207049单位（w/s）
        //biz=biz0, qps = 53.972366148531954单位（w/s）
        //biz=biz0, qps = 53.98402072986396单位（w/s）
    }

    /**
     * 多业务的压测：低qps一段时间后到高QPS，可以支撑更高
     */
    @Test
    @SneakyThrows
    public void testQps4() {
        // todo 跑一下
        ButterflyIdGenerator generator = ButterflyIdGenerator.getInstance(config);
        generator.addNamespaces("biz0", "biz1");
        int count = 10;
        int callNum = 10;
        int concurrentNum = 100;

        for (int i = 0; i < count; i++) {
            generateFun(generator, "biz0", callNum + i * i * 100, concurrentNum + i * 10 * i);
            generateFun(generator, "biz1", callNum + i * i * 100, concurrentNum + i * 10 * i);
            Thread.sleep(1000);
        }

        //biz=biz0, qps = 1.492537313432836单位（w/s）
        //biz=biz0, qps = 20.862068965517242单位（w/s）
        //biz=biz0, qps = 119.58333333333333单位（w/s）
        //biz=biz0, qps = 402.09302325581393单位（w/s）
        //biz=biz0, qps = 581.3888888888889单位（w/s）
        //biz=biz0, qps = 660.5263157894736单位（w/s）
        //biz=biz0, qps = 751.4027149321267单位（w/s）
        //biz=biz0, qps = 85.83407407407407单位（w/s）
        //biz=biz0, qps = 60.890885750962774单位（w/s）
        //biz=biz0, qps = 57.79700837966951单位（w/s）

        //biz=biz1, qps = 5.882352941176471单位（w/s）
        //biz=biz1, qps = 21.228070175438596单位（w/s）
        //biz=biz1, qps = 260.90909090909093单位（w/s）
        //biz=biz1, qps = 367.8723404255319单位（w/s）
        //biz=biz1, qps = 709.4915254237288单位（w/s）
        //biz=biz1, qps = 574.1830065359477单位（w/s）
        //biz=biz1, qps = 646.147859922179单位（w/s）
        //biz=biz1, qps = 616.3617021276596单位（w/s）
        //biz=biz1, qps = 738.8473520249221单位（w/s）
        //biz=biz1, qps = 775.2205882352941单位（w/s）
    }

}
