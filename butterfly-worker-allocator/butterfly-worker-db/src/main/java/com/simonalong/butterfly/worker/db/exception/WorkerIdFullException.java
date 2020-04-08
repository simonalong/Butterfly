package com.simonalong.butterfly.worker.db.exception;

import com.simonalong.neo.exception.NeoException;

/**
 * @author shizi
 * @since 2020/3/21 下午4:57
 */
public class WorkerIdFullException extends NeoException {

    public WorkerIdFullException(String msg) {
        super(msg);
    }
}
