package com.simonalong.butterfly.worker.distribute;

import com.simonalong.butterfly.distribute.api.ButterflyDistributeApi;
import com.simonalong.butterfly.sequence.exception.ButterflyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

/**
 * @author shizi
 * @since 2020/4/26 11:13 PM
 */
@Slf4j
public class ButterflySeqGeneratorFactory {

    private static volatile ButterflySeqGeneratorFactory INSTANCE = null;
    private static volatile ButterflyDistributeApi butterflyDistributeApi;
    private String zkAddress = null;

    private ButterflySeqGeneratorFactory(){}

    public static ButterflySeqGeneratorFactory getInstance() {
        if (null != INSTANCE) {
            return INSTANCE;
        }

        synchronized (ButterflySeqGeneratorFactory.class) {
            if (null == INSTANCE) {
                INSTANCE = new ButterflySeqGeneratorFactory();
            }
        }
        return INSTANCE;
    }

    public synchronized ButterflySeqGeneratorFactory init(String zkAddress) {
        this.zkAddress = zkAddress;
        // 首次调用
        getSequenceApi();
        return this;
    }

    public static boolean haveInitialized() {
        return butterflyDistributeApi != null;
    }

    public ButterflyDistributeApi getSequenceApi() {
        if (null != butterflyDistributeApi) {
            return butterflyDistributeApi;
        }
        assert null != zkAddress : "please set zookeeper's address";

        try {
            // 硬编码引用
            ReferenceConfig<ButterflyDistributeApi> rc = new ReferenceConfig<>();
            rc.setApplication(new ApplicationConfig("butterfly-consumer"));
            rc.setRegistry(new RegistryConfig(zkAddress, "zookeeper"));
            rc.setInterface(ButterflyDistributeApi.class);

            butterflyDistributeApi = rc.get();
            return butterflyDistributeApi;
        } catch (Throwable e) {
            log.error("getSequenceApi exception", e);
            throw new ButterflyException("getSequenceApi exception");
        }
    }
}
