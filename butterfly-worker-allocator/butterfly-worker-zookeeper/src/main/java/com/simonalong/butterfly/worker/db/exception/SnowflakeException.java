package com.simonalong.butterfly.worker.db.exception;

/**
 * @author 柿子
 * @since 2019/11/7 10:25 下午
 */
public class SnowflakeException extends RuntimeException
{

    public SnowflakeException()
    {
        super();
    }

    public SnowflakeException(String msg)
    {
        super(msg);
    }

    public SnowflakeException(String msg, Throwable throwable)
    {
        super(msg, throwable);
    }
}
