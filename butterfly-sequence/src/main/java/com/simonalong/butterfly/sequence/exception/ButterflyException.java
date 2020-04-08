package com.simonalong.butterfly.sequence.exception;

/**
 * @author shizi
 * @since 2020/4/9 12:11 AM
 */
public class ButterflyException extends RuntimeException {

    public ButterflyException() {
        super();
    }

    public ButterflyException(String msg) {
        super(msg);
    }

    public ButterflyException(Throwable throwable) {
        super(throwable);
    }

    public ButterflyException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
