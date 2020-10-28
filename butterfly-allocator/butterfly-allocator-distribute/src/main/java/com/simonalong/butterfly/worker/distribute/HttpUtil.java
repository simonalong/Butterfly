package com.simonalong.butterfly.worker.distribute;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouzhenyong
 * @since 2019/2/28 上午10:38
 */
@Slf4j
@UtilityClass
public class HttpUtil {

    private static OkHttpClient client;

    static {
        client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).retryOnConnectionFailure(true).readTimeout(60, TimeUnit.SECONDS).build();
    }

    public Object get(String url, Map<String, String> headers) {
        return send("GET", url, headers, new HashMap<>());
    }

    public String getStr(String url, Map<String, String> headers) {
        return JSON.toJSONString(send("GET", url, headers, new HashMap<>()));
    }

    public Object post(String url, Map<String, String> headers, Map<String, Object> body) {
        return send("POST", url, headers, body);
    }

    public String postStr(String url, Map<String, String> headers, Map<String, Object> body) {
        return JSON.toJSONString(send("POST", url, headers, body));
    }

    public Object put(String url, Map<String, String> headers, Map<String, Object> body) {
        return send("PUT", url, headers, body);
    }

    public String putStr(String url, Map<String, String> headers, Map<String, Object> body) {
        return JSON.toJSONString(send("PUT", url, headers, body));
    }

    public Object patch(String url, Map<String, String> headers, Map<String, Object> body) {
        return send("PATCH", url, headers, body);
    }

    public String patchStr(String url, Map<String, String> headers, Map<String, Object> body) {
        return JSON.toJSONString(send("PATCH", url, headers, body));
    }

    public Object delete(String url, Map<String, String> headers, Map<String, Object> body) {
        return send("DELETE", url, headers, body);
    }

    public String deleteStr(String url, Map<String, String> headers, Map<String, Object> body) {
        return JSON.toJSONString(send("DELETE", url, headers, body));
    }

    public Object options(String url, Map<String, String> headers, Map<String, Object> body) {
        return send("OPTIONS", url, headers, body);
    }

    public String optionsStr(String url, Map<String, String> headers, Map<String, Object> body) {
        return JSON.toJSONString(send("OPTIONS", url, headers, body));
    }

    public Object send(String httpMethod, String url, Map<String, String> headers, Map<String, Object> bodyMap) {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(bodyMap));
        Request request;
        if (httpMethod.toLowerCase().equals("get")) {
            request = new Request.Builder().url(url).get().headers(Headers.of(headers)).build();
        } else {
            request = new Request.Builder().url(url).method(httpMethod, body).headers(Headers.of(headers)).build();
        }

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("url = " + url + "异常：" + response.message());
            }
            if (null == response.body()) {
                throw new RuntimeException("数据实体为空，url = " + url);
            }
            return JSONObject.parse(response.body().bytes());
        } catch (IOException e) {
            log.error("请求异常，url=" + url, e);
            throw new RuntimeException("请求异常");
        }
    }
}
