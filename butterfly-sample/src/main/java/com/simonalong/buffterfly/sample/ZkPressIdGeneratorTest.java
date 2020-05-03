package com.simonalong.buffterfly.sample;

import com.simonalong.butterfly.worker.zookeeper.ZkButterflyConfig;
import lombok.SneakyThrows;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author shizi
 * @since 2020/5/3 2:11 AM
 */
public class ZkPressIdGeneratorTest extends BaseTest {


    @BeforeClass
    public static void beforeClass() {
        config = new ZkButterflyConfig();
        ((ZkButterflyConfig)config).setHost("localhost:2181");
    }

    /**
     * 基本测试
     */
    @Test
    public void baseRunTest() {
        baseRun();
        //{symbol=0, sequence=1, workerId=14, abstractTime=2020-05-03 10:12:56.022, time=6171176022, uuid=25883788273786894}
        //{symbol=0, sequence=2, workerId=14, abstractTime=2020-05-03 10:12:56.022, time=6171176022, uuid=25883788273795086}
        //{symbol=0, sequence=1, workerId=5, abstractTime=2020-05-03 10:12:56.435, time=6171176435, uuid=25883790006034437}
        //{symbol=0, sequence=2, workerId=5, abstractTime=2020-05-03 10:12:56.435, time=6171176435, uuid=25883790006042629}
        //{symbol=0, sequence=3, workerId=5, abstractTime=2020-05-03 10:12:56.435, time=6171176435, uuid=25883790006050821}
        //{symbol=0, sequence=4, workerId=5, abstractTime=2020-05-03 10:12:56.435, time=6171176435, uuid=25883790006059013}
        //{symbol=0, sequence=5, workerId=5, abstractTime=2020-05-03 10:12:56.435, time=6171176435, uuid=25883790006067205}
        //{symbol=0, sequence=6, workerId=5, abstractTime=2020-05-03 10:12:56.435, time=6171176435, uuid=25883790006075397}
    }

    /**
     * 持续的低qps的压测，则这个QPS还是比较低，完全满足业务需求
     */
    @Test
    @SneakyThrows
    public void testQps1() {
        lowPressRun();

        //biz=biz0, qps = 2.9411764705882355单位（w/s）
        //biz=biz0, qps = 6.666666666666667单位（w/s）
        //biz=biz0, qps = 7.6923076923076925单位（w/s）
        //biz=biz0, qps = 5.0单位（w/s）
        //biz=biz0, qps = 6.666666666666667单位（w/s）
        //biz=biz0, qps = 8.333333333333334单位（w/s）
        //biz=biz0, qps = 7.6923076923076925单位（w/s）
        //biz=biz0, qps = 10.0单位（w/s）
        //biz=biz0, qps = 10.0单位（w/s）
        //biz=biz0, qps = 10.0单位（w/s）
    }

    /**
     * 低qps一段时间后到高QPS，可以支撑更高，但是一旦持续的高并发，则后面会慢慢降下来
     */
    @Test
    @SneakyThrows
    public void testQps2() {
        lowToHighPressRun();

        //biz=biz0, qps = 3.4482758620689653单位（w/s）
        //biz=biz0, qps = 17.536231884057973单位（w/s）
        //biz=biz0, qps = 205.0单位（w/s）
        //biz=biz0, qps = 402.09302325581393单位（w/s）
        //biz=biz0, qps = 436.0416666666667单位（w/s）
        //biz=biz0, qps = 369.11764705882354单位（w/s）
        //biz=biz0, qps = 366.5783664459161单位（w/s）
        //biz=biz0, qps = 81.69486745628878单位（w/s）
        //biz=biz0, qps = 57.426150121065376单位（w/s）
        //biz=biz0, qps = 55.03840704004773单位（w/s）
    }

    /**
     * 持续的高QPS，则只能达到最高的理论值（51.2w/s）
     */
    @Test
    @SneakyThrows
    public void testQps3() {
        highPressRun();

        //biz=biz0, qps = 52.25752508361204单位（w/s）
        //biz=biz0, qps = 53.96945328943818单位（w/s）
        //biz=biz0, qps = 53.972366148531954单位（w/s）
        //biz=biz0, qps = 53.972366148531954单位（w/s）
        //biz=biz0, qps = 53.98110661268556单位（w/s）
        //biz=biz0, qps = 53.96945328943818单位（w/s）
        //biz=biz0, qps = 53.97527932207049单位（w/s）
        //biz=biz0, qps = 53.963628514381305单位（w/s）
        //biz=biz0, qps = 53.972366148531954单位（w/s）
        //biz=biz0, qps = 53.963628514381305单位（w/s）
    }

    /**
     * 多业务的压测：低qps一段时间后到高QPS，可以支撑更高
     */
    @Test
    @SneakyThrows
    public void testQps4() {
        lowToHighMultiBizPressRun();

        //biz=biz0, qps = 3.8461538461538463单位（w/s）
        //biz=biz0, qps = 22.830188679245282单位（w/s）
        //biz=biz0, qps = 287.0单位（w/s）
        //biz=biz0, qps = 384.22222222222223单位（w/s）
        //biz=biz0, qps = 377.1171171171171单位（w/s）
        //biz=biz0, qps = 387.0044052863436单位（w/s）
        //biz=biz0, qps = 433.57702349869453单位（w/s）
        //biz=biz0, qps = 104.77034358047017单位（w/s）
        //biz=biz0, qps = 62.9432059447983单位（w/s）
        //biz=biz0, qps = 60.72157314464374单位（w/s）

        //biz=biz1, qps = 7.142857142857143单位（w/s）
        //biz=biz1, qps = 75.625单位（w/s）
        //biz=biz1, qps = 318.8888888888889单位（w/s）
        //biz=biz1, qps = 432.25单位（w/s）
        //biz=biz1, qps = 402.5单位（w/s）
        //biz=biz1, qps = 388.716814159292单位（w/s）
        //biz=biz1, qps = 446.39784946236557单位（w/s）
        //biz=biz1, qps = 398.4731774415406单位（w/s）
        //biz=biz1, qps = 377.05882352941177单位（w/s）
        //biz=biz1, qps = 368.8205897051474单位（w/s）
    }
}
