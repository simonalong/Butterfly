package com.simonalong.butterfly.worker.distribute;

import com.simonalong.butterfly.sequence.ButterflyConfig;
import com.simonalong.butterfly.sequence.allocator.BeanBitAllocator;
import com.simonalong.butterfly.worker.distribute.config.DistributeDubboButterflyConfig;
import com.simonalong.butterfly.worker.distribute.config.DistributeRestfulButterflyConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shizi
 * @since 2020/4/27 7:52 PM
 */
@Slf4j
public class DistributeBitAllocator implements BeanBitAllocator {

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
        return butterflyConfig instanceof DistributeDubboButterflyConfig || butterflyConfig instanceof DistributeRestfulButterflyConfig;
    }

    /**
     * 初始化的配置的处理
     *
     * @param namespace       命名空间
     * @param butterflyConfig 配置
     */
    @Override
    public void postConstruct(String namespace, ButterflyConfig butterflyConfig) {
        // 初始化客户端
        SequenceClient.getInstance().init(butterflyConfig);

        // 首次回调
        this.bufferManager = new BufferManager(SequenceClient.getInstance().getNext(namespace));
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
