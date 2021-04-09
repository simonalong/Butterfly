package com.simonalong.buffterfly.sample;

import com.simonalong.butterfly.worker.db.DbButterflyConfig;
import lombok.SneakyThrows;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author shizi
 * @since 2020/5/3 2:01 AM
 */
public class DbPressIdGeneratorTest extends BaseTest {

    @BeforeClass
    public static void beforeClass() {
        config = new DbButterflyConfig();
        ((DbButterflyConfig)config).setUrl("jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false&&allowPublicKeyRetrieval=true");
        ((DbButterflyConfig)config).setUserName("neo_test");
        ((DbButterflyConfig)config).setPassword("neo@Test123");
    }

    /**
     * 基本测试
     */
    @Test
    public void baseRunTest() {
        baseRun();
        //命名空间：test1：{symbol=0, sequence=1, workerId=0, abstractTime=2021-04-09 17:22:52.658, time=29697772658, uuid=124561486650540033}
        //命名空间：test1：{symbol=0, sequence=2, workerId=0, abstractTime=2021-04-09 17:22:52.658, time=29697772658, uuid=124561486650556416}

        //命名空间：test2：{symbol=0, sequence=1, workerId=0, abstractTime=2021-04-09 17:22:53.127, time=29697773127, uuid=124561488617668609}
        //命名空间：test2：{symbol=0, sequence=2, workerId=0, abstractTime=2021-04-09 17:22:53.127, time=29697773127, uuid=124561488617684992}
        //命名空间：test2：{symbol=0, sequence=3, workerId=0, abstractTime=2021-04-09 17:22:53.127, time=29697773127, uuid=124561488617684993}
        //命名空间：test2：{symbol=0, sequence=4, workerId=0, abstractTime=2021-04-09 17:22:53.127, time=29697773127, uuid=124561488617701376}
        //命名空间：test2：{symbol=0, sequence=5, workerId=0, abstractTime=2021-04-09 17:22:53.127, time=29697773127, uuid=124561488617701377}

        //命名空间：test3：{symbol=0, sequence=1, workerId=0, abstractTime=2021-04-09 17:22:53.157, time=29697773157, uuid=124561488743497729}

        //命名空间：test4：{symbol=0, sequence=1, workerId=0, abstractTime=2021-04-09 17:22:53.186, time=29697773186, uuid=124561488865132545}
    }

    /**
     * 持续的低qps的压测，则这个QPS还是比较低，完全满足业务需求
     */
    @Test
    @SneakyThrows
    public void testQps1() {
        lowPressRun();

        //biz=biz0, qps = 5.555555555555555单位（w/s）
        //biz=biz0, qps = 4.761904761904762单位（w/s）
        //biz=biz0, qps = 6.25单位（w/s）
        //biz=biz0, qps = 7.142857142857143单位（w/s）
        //biz=biz0, qps = 6.666666666666667单位（w/s）
        //biz=biz0, qps = 5.555555555555555单位（w/s）
        //biz=biz0, qps = 6.25单位（w/s）
        //biz=biz0, qps = 7.142857142857143单位（w/s）
        //biz=biz0, qps = 7.142857142857143单位（w/s）
        //biz=biz0, qps = 8.333333333333334单位（w/s）
    }

    /**
     * 低qps一段时间后到高QPS，可以支撑更高，但是一旦持续的高并发，则后面会慢慢降下来
     */
    @Test
    @SneakyThrows
    public void testQps2() {
        lowToHighPressRun();

        //biz=biz0, qps = 14.285714285714286单位（w/s）
        //biz=biz0, qps = 71.17647058823529单位（w/s）
        //biz=biz0, qps = 287.0单位（w/s）
        //biz=biz0, qps = 540.3125单位（w/s）
        //biz=biz0, qps = 951.3636363636364单位（w/s）
        //biz=biz0, qps = 915.1041666666666单位（w/s）
        //biz=biz0, qps = 1230.0740740740741单位（w/s）
        //biz=biz0, qps = 71.75873173148378单位（w/s）
        //biz=biz0, qps = 57.405300738230665单位（w/s）
        //biz=biz0, qps = 55.034302759134974单位（w/s）
    }

    /**
     * 持续的高QPS，则只能达到最高的理论值（51.2w/s）
     */
    @Test
    @SneakyThrows
    public void testQps3() {
        highPressRun();

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
        lowToHighMultiBizPressRun();

        //biz=biz0, qps = 12.5单位（w/s）
        //biz=biz0, qps = 86.42857142857143单位（w/s）
        //biz=biz0, qps = 358.75单位（w/s）
        //biz=biz0, qps = 596.2068965517242单位（w/s）
        //biz=biz0, qps = 837.2单位（w/s）
        //biz=biz0, qps = 1009.7701149425287单位（w/s）
        //biz=biz0, qps = 1031.4285714285713单位（w/s）
        //biz=biz0, qps = 77.8736559139785单位（w/s）
        //biz=biz0, qps = 60.86744514307712单位（w/s）
        //biz=biz0, qps = 57.96497015394282单位（w/s）

        //biz=biz1, qps = 7.6923076923076925单位（w/s）
        //biz=biz1, qps = 134.44444444444446单位（w/s）
        //biz=biz1, qps = 717.5单位（w/s）
        //biz=biz1, qps = 910.0单位（w/s）
        //biz=biz1, qps = 1196.0单位（w/s）
        //biz=biz1, qps = 1311.1940298507463单位（w/s）
        //biz=biz1, qps = 1194.6762589928057单位（w/s）
        //biz=biz1, qps = 616.3617021276596单位（w/s）
        //biz=biz1, qps = 695.5131964809384单位（w/s）
        //biz=biz1, qps = 1244.5362563237775单位（w/s）
    }
}
