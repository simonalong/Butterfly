package com.simonalong.butterfly.sequence.allocator;

import com.simonalong.butterfly.sequence.ButterflyConfig;
import com.simonalong.butterfly.sequence.TimeAdjuster;
import com.simonalong.butterfly.sequence.spi.WorkerIdHandlerFactory;
import com.simonalong.butterfly.sequence.PaddedLong;
import com.simonalong.butterfly.sequence.exception.ButterflyException;
import com.simonalong.butterfly.sequence.spi.WorkerIdHandler;

import java.util.HashMap;
import java.util.Map;

import static com.simonalong.butterfly.sequence.UuidConstant.SEQ_MARK;

/**
 * @author shizi
 * @since 2020/2/3 10:15 下午
 */
// todo 这个默认的bit分配器，这里也要进行修改，因为这个东西在distribute中，client和server端采用的都是不同的
public class DefaultBitAllocator implements BitAllocator {

    private String namespace;
    private PaddedLong currentTime;
    private PaddedLong sequence;
    /**
     * worker节点操作map
     */
    private Map<String, WorkerIdHandler> workerIdHandlerMap = new HashMap<>(12);

    public DefaultBitAllocator(String namespace, ButterflyConfig butterflyConfig) {
        super();
        this.namespace = namespace;
        currentTime = new PaddedLong(System.currentTimeMillis());
        sequence = new PaddedLong(0);
        workerIdHandlerMap.putIfAbsent(namespace, WorkerIdHandlerFactory.getWorkerIdHandler(namespace, butterflyConfig));
    }

    /**
     * 获取序列中的时间值
     */
    @Override
    public long getTimeValue() {
        return TimeAdjuster.getRelativeTime(currentTime.get());
    }

    /**
     * 获取序列中的自增序列对应的值
     */
    @Override
    public long getSequenceValue() {
        if ((sequence.incrementAndGet() & SEQ_MARK) == 0) {
            currentTime.incrementAndGet();
            // 调整时间，防止时间过快或者过慢
            TimeAdjuster.adjustTime(currentTime);
            currentTimeIsValid();
            sequence.set(0);
            return 0;
        }
        return sequence.get();
    }

    /**
     * 获取序列中的workId对应的值
     */
    @Override
    public int getWorkIdValue() {
        return getWorkerIdHandler(namespace).getWorkerId();
    }

    /**
     * 获取过期时间
     */
    private Long getLastExpireTime(String namespace) {
        return getWorkerIdHandler(namespace).getLastExpireTime();
    }

    private WorkerIdHandler getWorkerIdHandler(String namespace) {
        check(namespace);
        return workerIdHandlerMap.get(namespace);
    }

    /**
     * 判断当前系统是否还是可用的
     * <p>
     * 如果当前时间和上次的过期时间之间相差达到一定的阈值，则让当前应用系统不可用
     */
    private void currentTimeIsValid() {
        Long lastExpireTime = getLastExpireTime(namespace);
        if (null == lastExpireTime) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now >= lastExpireTime) {
            throw new ButterflyException("数据库链接崩溃，超过最大过期时间");
        }
    }

    /**
     * 核查命名空间
     */
    private void check(String namespace) {
        if (!workerIdHandlerMap.containsKey(namespace)) {
            throw new ButterflyException("命名空间" + namespace + "不存在");
        }
    }
}
