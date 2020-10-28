package com.simonalong.butterfly.distribute.server.api.impl;

import com.simonalong.butterfly.distribute.api.ButterflyDistributeApi;
import com.simonalong.butterfly.distribute.model.BitSequenceDTO;
import com.simonalong.butterfly.distribute.model.Response;
import com.simonalong.butterfly.distribute.server.service.DistributeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author shizi
 * @since 2020/4/28 12:11 AM
 */
@Slf4j
@Service
public class ButterflyDistributeApiImpl implements ButterflyDistributeApi {

    @Autowired
    private DistributeService distributeService;

    @Override
    public Response<BitSequenceDTO> getNext(String namespace) {
        return Response.success(distributeService.getNext(namespace));
    }
}
