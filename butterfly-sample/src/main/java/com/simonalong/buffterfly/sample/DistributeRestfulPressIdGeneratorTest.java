package com.simonalong.buffterfly.sample;

import com.simonalong.butterfly.worker.distribute.config.DistributeRestfulButterflyConfig;
import lombok.SneakyThrows;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author shizi
 * @since 2020/10/28 6:18 下午
 */
public class DistributeRestfulPressIdGeneratorTest extends BaseTest {

    @BeforeClass
    public static void beforeClass() {
        config = new DistributeRestfulButterflyConfig();
        ((DistributeRestfulButterflyConfig)config).setHostAndPort("localhost:8080");
    }

    /**
     * 基本测试
     */
    @Test
    public void baseRunTest() {
        baseRun();

        //{symbol=0, sequence=0, workerId=2, abstractTime=2020-05-03 14:48:17.407, time=6187697407, uuid=25953083984969730}
        //{symbol=0, sequence=1, workerId=2, abstractTime=2020-05-03 14:48:17.407, time=6187697407, uuid=25953083984977922}
        //{symbol=0, sequence=2, workerId=2, abstractTime=2020-05-03 14:48:17.407, time=6187697407, uuid=25953083984986114}
        //{symbol=0, sequence=3, workerId=2, abstractTime=2020-05-03 14:48:17.407, time=6187697407, uuid=25953083984994306}
        //{symbol=0, sequence=4, workerId=2, abstractTime=2020-05-03 14:48:17.407, time=6187697407, uuid=25953083985002498}
        //{symbol=0, sequence=5, workerId=2, abstractTime=2020-05-03 14:48:17.407, time=6187697407, uuid=25953083985010690}
        //{symbol=0, sequence=6, workerId=2, abstractTime=2020-05-03 14:48:17.407, time=6187697407, uuid=25953083985018882}
        //{symbol=0, sequence=7, workerId=2, abstractTime=2020-05-03 14:48:17.407, time=6187697407, uuid=25953083985027074}
    }

    /**
     * 持续的低qps的压测，则这个QPS还是比较低，完全满足业务需求
     */
    @Test
    @SneakyThrows
    public void testQps1() {
        lowPressRun();

        //biz=biz0, qps = 5.2631578947368425单位（w/s）
        //biz=biz0, qps = 7.6923076923076925单位（w/s）
        //biz=biz0, qps = 7.6923076923076925单位（w/s）
        //biz=biz0, qps = 8.333333333333334单位（w/s）
        //biz=biz0, qps = 7.142857142857143单位（w/s）
        //biz=biz0, qps = 7.142857142857143单位（w/s）
        //biz=biz0, qps = 8.333333333333334单位（w/s）
        //biz=biz0, qps = 10.0单位（w/s）
        //biz=biz0, qps = 9.090909090909092单位（w/s）
        //biz=biz0, qps = 8.333333333333334单位（w/s）
    }

    /**
     * 低qps一段时间后到高QPS，可以支撑更高
     */
    @Test
    @SneakyThrows
    public void testQps2() {
        lowToHighPressRun();

        //biz=biz0, qps = 4.3478260869565215单位（w/s）
        //biz=biz0, qps = 28.80952380952381单位（w/s）
        //biz=biz0, qps = 58.57142857142857单位（w/s）
        //biz=biz0, qps = 83.52657004830918单位（w/s）
        //biz=biz0, qps = 100.86746987951807单位（w/s）
        //biz=biz0, qps = 146.41666666666666单位（w/s）
        //biz=biz0, qps = 183.89811738648947单位（w/s）
        //biz=biz0, qps = 193.90227576974564单位（w/s）
        //biz=biz0, qps = 239.0826612903226单位（w/s）
        //biz=biz0, qps = 222.49321676213447单位（w/s）
    }

    /**
     * 持续的高QPS，则只能达到最高的理论值（51.2w/s）
     */
    @Test
    @SneakyThrows
    public void testQps3() {
        highPressRun();

        //biz=biz0, qps = 132.17023526301878单位（w/s）
        //biz=biz0, qps = 79.14523149980214单位（w/s）
        //biz=biz0, qps = 45.77077993409008单位（w/s）
        //biz=biz0, qps = 46.010858562620776单位（w/s）
        //biz=biz0, qps = 46.140358971992804单位（w/s）
        //biz=biz0, qps = 46.112699437425064单位（w/s）
        //biz=biz0, qps = 46.20858555519616单位（w/s）
        //biz=biz0, qps = 45.76030750926646单位（w/s）
        //biz=biz0, qps = 45.54148829583751单位（w/s）
        //biz=biz0, qps = 45.57261996992207单位（w/s）
    }

    /**
     * 多业务的压测：低qps一段时间后到高QPS，可以支撑更高
     */
    @Test
    @SneakyThrows
    public void testQps4() {
        lowToHighMultiBizPressRun();

        //biz=biz0, qps = 5.0单位（w/s）
        //biz=biz0, qps = 44.81481481481482单位（w/s）
        //biz=biz0, qps = 56.27450980392157单位（w/s）
        //biz=biz0, qps = 80.79439252336448单位（w/s）
        //biz=biz0, qps = 114.68493150684931单位（w/s）
        //biz=biz0, qps = 170.91439688715954单位（w/s）
        //biz=biz0, qps = 194.906103286385单位（w/s）
        //biz=biz0, qps = 233.4327155519742单位（w/s）
        //biz=biz0, qps = 232.06457925636008单位（w/s）
        //biz=biz0, qps = 218.4107724178751单位（w/s）

        //biz=biz1, qps = 14.285714285714286单位（w/s）
        //biz=biz1, qps = 55.0单位（w/s）
        //biz=biz1, qps = 88.3076923076923单位（w/s）
        //biz=biz1, qps = 114.50331125827815单位（w/s）
        //biz=biz1, qps = 175.14644351464435单位（w/s）
        //biz=biz1, qps = 196.09375单位（w/s）
        //biz=biz1, qps = 214.8253557567917单位（w/s）
        //biz=biz1, qps = 239.6112489660877单位（w/s）
        //biz=biz1, qps = 226.84839789574366单位（w/s）
        //biz=biz1, qps = 212.989898989899单位（w/s）
    }

}
