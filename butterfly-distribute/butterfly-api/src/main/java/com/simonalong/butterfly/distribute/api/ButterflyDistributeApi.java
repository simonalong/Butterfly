package com.simonalong.butterfly.distribute.api;

import com.simonalong.butterfly.distribute.model.BitSequenceDTO;
import com.simonalong.butterfly.distribute.model.Response;

import java.util.List;

/**
 * @author shizi
 * @since 2020/4/26 11:04 PM
 */
public interface ButterflyDistributeApi {

    /**
     * 首次连接server，获取对应命名空间的所有初始序列中的值
     * @param namespaceList 命名空间级别
     * @return bit序列中各个部分的值的集合
     */
    Response<List<BitSequenceDTO>> getNext(List<String> namespaceList);

    /**
     * 获取下一个buffer对应的配置
     * @param namespace 业务命名空间
     * @return bit序列中各个部分的值
     */
    Response<BitSequenceDTO> getNext(String namespace);
}
