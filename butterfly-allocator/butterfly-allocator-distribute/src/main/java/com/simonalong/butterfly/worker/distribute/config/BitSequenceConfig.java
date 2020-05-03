package com.simonalong.butterfly.worker.distribute.config;

import com.simonalong.butterfly.distribute.model.BitSequenceDTO;
import lombok.Data;

/**
 * @author shizi
 * @since 2020/4/26 11:11 PM
 */
@Data
public class BitSequenceConfig {

    private Long time;
    private Integer sequence;
    private Integer workId;

    public void update(BitSequenceDTO bitSequenceDTO) {
        this.time = bitSequenceDTO.getTime();
        this.workId = bitSequenceDTO.getWorkId();
    }
}
