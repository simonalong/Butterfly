package com.simonalong.butterfly.worker.distribute;

import com.simonalong.butterfly.distribute.api.ButterflyDistributeApi;
import com.simonalong.butterfly.distribute.model.BitSequenceDTO;
import com.simonalong.butterfly.distribute.model.Response;
import com.simonalong.butterfly.sequence.ButterflyConfig;
import com.simonalong.butterfly.sequence.allocator.BeanBitAllocator;
import com.simonalong.butterfly.worker.distribute.config.DistributeClientButterflyConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shizi
 * @since 2020/4/27 7:52 PM
 */
@Slf4j
public class DistributeClientBitAllocator implements BeanBitAllocator {

    private BufferManager bufferManager;
    /**
     * 是否接受对应的配置
     *
     * @param butterflyConfig 具体的配置
     * @return true：接受，false：不接受
     */
    @Override
    public boolean acceptConfig(ButterflyConfig butterflyConfig) {
        if (null == butterflyConfig) {
            return false;
        }
        return butterflyConfig instanceof DistributeClientButterflyConfig;
    }

    /**
     * 初始化的配置的处理
     *
     * @param namespace       命名空间
     * @param butterflyConfig 配置
     */
    @Override
    public void postConstruct(String namespace, ButterflyConfig butterflyConfig) {
        DistributeClientButterflyConfig config = (DistributeClientButterflyConfig) butterflyConfig;
        String zkHost = config.getZkHose();

        ButterflySeqGeneratorFactory factory = ButterflySeqGeneratorFactory.getInstance().init(zkHost);
        ButterflyDistributeApi api = factory.getSequenceApi();

        Response<BitSequenceDTO> response = api.getNext(namespace);
        if (!response.isSuccess()) {
            log.error("get seq fail，namespace={}, errCode={}, errMsg={}", namespace, response.getErrCode(), response.getErrMsg());
            throw new RuntimeException("get seq fail：" + response.getErrMsg());
        }
        this.bufferManager = new BufferManager(response.getData());
    }

    @Override
    public long getTimeValue() {
        return bufferManager.getTime();
    }

    @Override
    public long getSequenceValue() {
        return bufferManager.getSequence();
    }

    @Override
    public int getWorkIdValue() {
        return bufferManager.getWorkId();
    }
}
