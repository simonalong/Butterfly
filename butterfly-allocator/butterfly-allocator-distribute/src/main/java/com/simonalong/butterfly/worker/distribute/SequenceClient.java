package com.simonalong.butterfly.worker.distribute;

import com.alibaba.fastjson.JSONObject;
import com.simonalong.butterfly.distribute.api.ButterflyDistributeApi;
import com.simonalong.butterfly.distribute.model.BitSequenceDTO;
import com.simonalong.butterfly.distribute.model.Response;
import com.simonalong.butterfly.sequence.ButterflyConfig;
import com.simonalong.butterfly.sequence.exception.ButterflyException;
import com.simonalong.butterfly.worker.distribute.config.DistributeDubboButterflyConfig;
import com.simonalong.butterfly.worker.distribute.config.DistributeRestfulButterflyConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

import static com.simonalong.butterfly.worker.distribute.DistributeConstant.DTB_LOG_PRE;

/**
 * @author shizi
 * @since 2020/10/28 5:45 下午
 */
@Slf4j
public class SequenceClient {

    private static volatile SequenceClient INSTANCE = null;
    private String serverHostAndPort = null;

    private SequenceClient() {}

    public static SequenceClient getInstance() {
        if (null != INSTANCE) {
            return INSTANCE;
        }

        synchronized (SequenceClient.class) {
            if (null == INSTANCE) {
                INSTANCE = new SequenceClient();
            }
        }
        return INSTANCE;
    }

    public void init(ButterflyConfig butterflyConfig) {
        if(null == butterflyConfig) {
            return;
        }

        if(butterflyConfig instanceof DistributeDubboButterflyConfig) {
            // 初始化dubbo的处理方式
            ButterflySeqGeneratorFactory.getInstance().init(((DistributeDubboButterflyConfig) butterflyConfig).getZkHostAndPort());
        } else if(butterflyConfig instanceof DistributeRestfulButterflyConfig) {
            serverHostAndPort = ((DistributeRestfulButterflyConfig) butterflyConfig).getHostAndPort();
        }
    }

    public BitSequenceDTO getNext(String namespace) {
        // 优先判断dubbo，如果dubbo没有则判断restful，如果都没有，则报异常
        if (ButterflySeqGeneratorFactory.haveInitialized()) {
            ButterflyDistributeApi api = ButterflySeqGeneratorFactory.getInstance().getSequenceApi();

            Response<BitSequenceDTO> response = api.getNext(namespace);
            if (!response.isSuccess()) {
                log.error(DTB_LOG_PRE + "get seq fail，namespace={}, errCode={}, errMsg={}", namespace, response.getErrCode(), response.getErrMsg());
                throw new RuntimeException("get seq fail：" + response.getErrMsg());
            }
            return response.getData();
        } else if (null != serverHostAndPort) {
            return HttpHelper.getOfStandard(BitSequenceDTO.class, "http://" + serverHostAndPort + "/api/butterfly/v1/worker/getNext/" + namespace);
        } else {
            throw new ButterflyException("No available server configuration found");
        }
    }
}
