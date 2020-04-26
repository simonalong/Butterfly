package com.simonalong.butterfly.worker.distribute.client;

import com.simonalong.butterfly.distribute.api.ButterflyDistributeApi;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

/**
 * @author shizi
 * @since 2020/4/26 11:13 PM
 */
public class ButterflySeqGeneratorFactory {

    private static volatile ButterflySeqGeneratorFactory INSTANCE = null;
    private static volatile ButterflyDistributeApi butterflyDistributeApi;
    private String zkAddress = null;

    public ButterflySeqGeneratorFactory sequenceApi(ButterflyDistributeApi butterflyDistributeApi) {
        ButterflySeqGeneratorFactory.butterflyDistributeApi = butterflyDistributeApi;
        return this;
    }

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

    /**
     * 初始化
     */
    public synchronized ButterflySeqGeneratorFactory init(String zkAddress) {
        this.zkAddress = zkAddress;
        // 首次调用
        getSequenceApi();
        return this;
    }

    public ButterflyDistributeApi getSequenceApi() {
        if (null != butterflyDistributeApi) {
            return butterflyDistributeApi;
        }
        assert null != zkAddress : "请先设置zk地址";

        // 硬编码引用
        ReferenceConfig<ButterflyDistributeApi> reference = new ReferenceConfig<>();
        reference.setApplication(new ApplicationConfig("butterfly-consumer"));
        reference.setRegistry(new RegistryConfig(zkAddress));
        reference.setInterface(ButterflyDistributeApi.class);

        reference.setCheck(false);
        reference.setAsync(false);
        reference.setTimeout(3000);
        reference.setCluster("gsf_failover");

        butterflyDistributeApi = reference.get();
        return butterflyDistributeApi;
    }
}
