package com.simonalong.butterfly.distribute.model;

import lombok.Data;

/**
 * @author shizi
 * @since 2020/4/26 11:05 PM
 */
@Data
public class Response<T> {

    private String errCode;
    private String errMsg;
    private T data;

    public static <V> Response<V> success(V data) {
        Response<V> response = new Response<>();
        response.setData(data);
        return response;
    }

    public static <V> Response<V> fail(String errCode, String errMsg) {
        Response<V> response = new Response<>();
        response.setErrCode(errCode);
        response.setErrMsg(errMsg);
        return response;
    }

    public Boolean isSuccess() {
        return null == errCode;
    }
}
