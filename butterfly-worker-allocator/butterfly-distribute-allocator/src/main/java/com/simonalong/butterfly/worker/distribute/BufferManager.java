package com.simonalong.butterfly.worker.distribute;

import com.simonalong.butterfly.distribute.model.BitSequenceDTO;
import com.simonalong.butterfly.distribute.model.Response;
import com.simonalong.butterfly.sequence.PaddedLong;
import com.simonalong.butterfly.sequence.exception.ButterflyException;
import com.simonalong.butterfly.worker.distribute.config.BitSequenceConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

import static com.simonalong.butterfly.sequence.UuidConstant.SEQ_MAX_SIZE;

/**
 * @author shizi
 * @since 2020/4/26 11:00 PM
 */
@Slf4j
public class BufferManager {

    /**
     * 业务命名空间
     */
    private String namespace;
    private BitSequenceConfig buffer1 = new BitSequenceConfig();
    private BitSequenceConfig buffer2 = new BitSequenceConfig();
    private BitSequenceConfig currentBuffer;
    private PaddedLong currentSequence;
    /**
     * 刷新的状态
     */
    private volatile RefreshEnum refreshState = RefreshEnum.NON;
    /**
     * 这里暂时设置刷新比率为0.4
     */
    private float refreshRatio = 0.4f;
    private int refreshValue = (int) (SEQ_MAX_SIZE * refreshRatio);

    public BufferManager(BitSequenceDTO bitSequenceDTO) {
        if (null == bitSequenceDTO) {
            return;
        }
        this.namespace = bitSequenceDTO.getNamespace();
        buffer1.update(bitSequenceDTO);
        this.currentBuffer = buffer1;
        this.currentSequence = new PaddedLong(0);
    }

    public long getTime() {
        return currentBuffer.getTime();
    }

    /**
     * 双buffer方式获取新的序列值
     *
     * @return 新的序列
     */
    public long getSequence() {
        long sequence;
        while (true) {
            sequence = currentSequence.get();

            // 二级buffer刷新处理
            if (reachRefreshBuffer(sequence)) {
                if (refreshState == RefreshEnum.READY_ASYNC) {
                    continue;
                }

                synchronized (this) {
                    if (refreshState == RefreshEnum.READY_ASYNC) {
                        continue;
                    }
                    refreshState = RefreshEnum.READY_ASYNC;
                }

                // 刷新二级buffer
                refreshBuffer();
            }

            // 到达最后
            if (sequence >= SEQ_MAX_SIZE) {
                if (refreshState != RefreshEnum.READY_RPC && refreshState != RefreshEnum.FINISH) {
                    throw new ButterflyException("server get new buffer fail");
                }
                switchBuffer();
            }
            return currentSequence.getAndIncrement();
        }
    }

    public int getWorkId() {
        return currentBuffer.getWorkId();
    }

    /**
     * 到达刷新二级buffer的点
     */
    private boolean reachRefreshBuffer(long sequence) {
        return (sequence >= refreshValue && refreshState != RefreshEnum.READY_RPC && refreshState != RefreshEnum.FINISH);
    }

    private void refreshBuffer() {
        CompletableFuture.runAsync(() -> {
            try {
                synchronized (this) {
                    refreshState = RefreshEnum.READY_RPC;
                    updateBuffer(ButterflySeqGeneratorFactory.getInstance().getSequenceApi().getNext(namespace));
                    refreshState = RefreshEnum.FINISH;
                }
            } catch (Throwable e) {
                log.error("rpc 调用异常", e);
            }
        });
    }

    /**
     * 到达最后则进行buffer切换
     */
    private synchronized void switchBuffer() {
        if (refreshState == RefreshEnum.READY_RPC) {
            throw new ButterflyException("server get new buffer fail");
        }
        // 表示切换完成
        if (refreshState == RefreshEnum.NON) {
            return;
        }

        if (currentBuffer == buffer1) {
            currentBuffer = buffer2;
        } else if (currentBuffer == buffer2) {
            currentBuffer = buffer1;
        }

        currentSequence.set(0);
        refreshState = RefreshEnum.NON;
    }

    private void updateBuffer(Response<BitSequenceDTO> sequenceDTO) {
        if (!sequenceDTO.isSuccess()) {
            log.error("server return error：rsp={}", sequenceDTO);
            throw new ButterflyException("server return error");
        }

        BitSequenceDTO bitSequence = sequenceDTO.getData();
        if (currentBuffer == buffer1) {
            buffer2.update(bitSequence);
        } else if (currentBuffer == buffer2) {
            buffer1.update(bitSequence);
        }
    }

    /**
     * 二级buffer的刷新状态
     */
    enum RefreshEnum {
        /**
         * 无状态
         */
        NON,
        /**
         * 准备创建异步
         */
        READY_ASYNC,
        /**
         * 准备rpc获取远端数据
         */
        READY_RPC,
        /**
         * 完成
         */
        FINISH
    }
}
