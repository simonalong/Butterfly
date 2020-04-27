package com.simonalong.butterfly.distribute.server.api.impl;

import com.simonalong.butterfly.distribute.api.ButterflyDistributeApi;
import com.simonalong.butterfly.distribute.model.BitSequenceDTO;
import com.simonalong.butterfly.distribute.model.Response;
import com.simonalong.butterfly.sequence.ButterflyIdGenerator;
import com.simonalong.butterfly.sequence.allocator.BitAllocator;
import org.apache.dubbo.common.utils.ConcurrentHashSet;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Set;

/**
 * @author shizi
 * @since 2020/4/28 12:11 AM
 */
@Service
public class ButterflyDistributeApiImpl implements ButterflyDistributeApi {

    @Autowired
    private ButterflyIdGenerator butterflyIdGenerator;
    private Set<String> namespaceSet = new ConcurrentHashSet<>();

    @Override
    public Response<BitSequenceDTO> getNext(String namespace) {
        BitAllocator bitAllocator;
        if(!namespaceSet.contains(namespace)) {
            butterflyIdGenerator.addNamespaces(namespace);
        }
        bitAllocator = butterflyIdGenerator.getBitAllocator(namespace);

        BitSequenceDTO sequenceDTO = new BitSequenceDTO();
        sequenceDTO.setNamespace(namespace);
        sequenceDTO.setNamespaceExist();
        bitAllocator.getSequenceValue();
        return null;
    }
}
