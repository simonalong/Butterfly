package com.simonalong.buffterfly.sample;

import com.simonalong.butterfly.worker.db.DbButterflyConfig;
import com.simonalong.butterfly.worker.db.entity.UuidGeneratorDO;
import com.simonalong.neo.Neo;
import lombok.SneakyThrows;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

import static com.simonalong.butterfly.worker.db.DbConstant.UUID_TABLE;

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
        //{symbol=0, sequence=1, workerId=0, abstractTime=2020-05-03 10:08:56.075, time=6170936075, uuid=25882781863124992}
        //{symbol=0, sequence=2, workerId=0, abstractTime=2020-05-03 10:08:56.075, time=6170936075, uuid=25882781863133184}
        //{symbol=0, sequence=1, workerId=0, abstractTime=2020-05-03 10:08:56.969, time=6170936969, uuid=25882785612832768}
        //{symbol=0, sequence=2, workerId=0, abstractTime=2020-05-03 10:08:56.969, time=6170936969, uuid=25882785612840960}
        //{symbol=0, sequence=3, workerId=0, abstractTime=2020-05-03 10:08:56.969, time=6170936969, uuid=25882785612849152}
        //{symbol=0, sequence=4, workerId=0, abstractTime=2020-05-03 10:08:56.969, time=6170936969, uuid=25882785612857344}
        //{symbol=0, sequence=5, workerId=0, abstractTime=2020-05-03 10:08:56.969, time=6170936969, uuid=25882785612865536}
        //{symbol=0, sequence=6, workerId=0, abstractTime=2020-05-03 10:08:56.969, time=6170936969, uuid=25882785612873728}
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
