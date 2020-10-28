package com.simonalong.butterfly.distribute.server.util;

import lombok.experimental.UtilityClass;

/**
 * @author shizi
 * @since 2020/6/2 4:24 PM
 */
@UtilityClass
public class ExceptionUtil {

    /**
     * 获取异常堆栈中的匹配的异常
     *
     * @param throwable 异常类
     * @param tClass    异常类型的类
     * @param <T>       异常类型
     * @return 具体的某个异常类
     */
    @SuppressWarnings("unchecked")
    public <T extends Throwable> T getCause(Throwable throwable, Class<T> tClass) {
        while (null != throwable) {
            if (tClass.isAssignableFrom(throwable.getClass())) {
                return (T) throwable;
            }
            throwable = throwable.getCause();
        }
        return null;
    }

    /**
     * 获取异常堆栈中异常
     *
     * @param throwable 异常类
     * @param <T>       异常类型
     * @return 具体的某个异常类
     */
    @SuppressWarnings("unchecked")
    public <T extends Throwable> T unwrapException(Throwable throwable) {
        Throwable e = throwable;
        while (null != throwable) {
            e = throwable;
            if (null == throwable.getClass()) {
                return (T) e;
            }
            throwable = throwable.getCause();
        }
        return (T) e;
    }
}
