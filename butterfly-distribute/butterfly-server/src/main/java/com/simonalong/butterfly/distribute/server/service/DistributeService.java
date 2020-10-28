package com.simonalong.butterfly.distribute.server.service;

import com.simonalong.butterfly.distribute.model.BitSequenceDTO;
import com.simonalong.butterfly.distribute.model.Response;
import com.simonalong.butterfly.sequence.ButterflyIdGenerator;
import com.simonalong.butterfly.sequence.PaddedLong;
import com.simonalong.butterfly.sequence.TimeAdjuster;
import com.simonalong.butterfly.sequence.allocator.DefaultBitAllocator;
import com.simonalong.butterfly.sequence.exception.ButterflyException;
import org.apache.dubbo.common.utils.ConcurrentHashSet;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author shizi
 * @since 2020/10/28 5:26 下午
 */
@Service
public class DistributeService implements InitializingBean {

    @Autowired
    private ButterflyIdGenerator butterflyIdGenerator;
    private Set<String> namespaceSet = new ConcurrentHashSet<>();
    private PaddedLong currentTime;

    public BitSequenceDTO getNext(String namespace) {
        DefaultBitAllocator bitAllocator;
        if (!namespaceSet.contains(namespace)) {
            butterflyIdGenerator.addNamespaces(namespace);
            namespaceSet.add(namespace);
        }
        bitAllocator = (DefaultBitAllocator) butterflyIdGenerator.getBitAllocator(namespace);

        BitSequenceDTO sequenceDTO = new BitSequenceDTO();
        sequenceDTO.setNamespace(namespace);
        sequenceDTO.setTime(getTimeValue(namespace, bitAllocator));
        sequenceDTO.setWorkId(bitAllocator.getWorkIdValue());
        return sequenceDTO;
    }

    /**
     * 获取序列中的时间值
     */
    private long getTimeValue(String namespace, DefaultBitAllocator bitAllocator) {
        long time = TimeAdjuster.getRelativeTime(currentTime.getAndIncrement());

        TimeAdjuster.adjustTime(currentTime);
        currentTimeIsValid(namespace, bitAllocator);
        return time;
    }

    /**
     * 判断当前系统是否还是可用的
     * <p>
     * 如果当前时间和上次的过期时间之间相差达到一定的阈值，则让当前应用系统不可用
     */
    private void currentTimeIsValid(String namespace, DefaultBitAllocator bitAllocator) {
        Long expireTime = bitAllocator.getLastExpireTime(namespace);
        if (null == expireTime) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now >= expireTime) {
            throw new ButterflyException("zk连接失效，超过最大过期时间");
        }
    }

    @Override
    public void afterPropertiesSet() {
        currentTime = new PaddedLong(System.currentTimeMillis());
    }
}
