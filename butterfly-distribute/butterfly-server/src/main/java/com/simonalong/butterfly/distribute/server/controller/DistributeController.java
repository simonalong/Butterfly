package com.simonalong.butterfly.distribute.server.controller;

import com.simonalong.butterfly.distribute.model.BitSequenceDTO;
import com.simonalong.butterfly.distribute.model.Response;
import com.simonalong.butterfly.distribute.server.service.DistributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shizi
 * @since 2020/10/28 5:22 下午
 */
@RequestMapping("api/butterfly/v1/worker")
@RestController
public class DistributeController {

    @Autowired
    private DistributeService distributeService;

    @GetMapping("getNext/{namespace}")
    public BitSequenceDTO getNext(@PathVariable("namespace") String namespace) {
        return distributeService.getNext(namespace);
    }
}
