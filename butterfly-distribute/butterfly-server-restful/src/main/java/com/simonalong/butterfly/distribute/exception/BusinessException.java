package com.simonalong.butterfly.distribute.exception;

import lombok.Getter;

/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {

    @Getter
    private String errCode;
    private String errMsg;

    public BusinessException() {
        super();
    }

    public BusinessException(Throwable e) {
        super(e);
    }

    public BusinessException(String message) {
        super(message);
        this.errMsg = message;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errMsg = message;
    }

    public BusinessException(String errCode, String message) {
        super(message);
        this.errCode = errCode;
        this.errMsg = message;
    }

    public BusinessException(String errCode, String message, Throwable cause) {
        super(message, cause);
        this.errCode = errCode;
        this.errMsg = message;
    }
}
