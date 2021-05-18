package com.simonalong.butterfly.worker.distribute;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.simonalong.neo.util.ObjectUtil;
import lombok.Data;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author shizi
 * @since 2020/11/21 11:28 AM
 */
@Slf4j
@UtilityClass
public class HttpHelper {

    private final String LOG_PRE = "[http-helper] ";
    private final String GET = "GET";
    private final String HEAD = "HEAD";
    private final String POST = "POST";
    private final String PUT = "PUT";
    private final String PATCH = "PATCH";
    private final String DELETE = "DELETE";
    // okhttp暂时不支持
    private final String OPTION = "OPTION";
    // okhttp暂时不支持
    private final String TRACE = "TRACE";

    private final OkHttpClient client;

    static {
        Integer TIME_OUT = 30 * 1000;
        client = new OkHttpClient.Builder().connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS).readTimeout(TIME_OUT, TimeUnit.MILLISECONDS).build();
        Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.INFO);
    }

    // ------------------ get ------------------
    public <T> T get(Class<T> responseClass, String url) {
        return doRequestForObject(responseClass, GET, url, Headers.of(), Parameters.of(), true, null);
    }

    public <T> List<T> getList(Class<T> responseClass, String url) {
        return doRequestForList(responseClass, GET, url, Headers.of(), Parameters.of(), true, null);
    }


    public <T> T getOfStandard(Class<T> responseClass, String url) {
        return doRequestForObjectOfStandard(responseClass, GET, url, Headers.of(), Parameters.of(), true, null);
    }

    public <T> List<T> getListOfStandard(Class<T> responseClass, String url) {
        return doRequestForListOfStandard(responseClass, GET, url, Headers.of(), Parameters.of(), true, null);
    }


    public <T> T get(Class<T> responseClass, String url, Parameters parameters) {
        return doRequestForObject(responseClass, GET, url, Headers.of(), parameters, true, null);
    }

    public <T> List<T> getList(Class<T> responseClass, String url, Parameters parameters) {
        return doRequestForList(responseClass, GET, url, Headers.of(), parameters, true, null);
    }


    public <T> T getOfStandard(Class<T> responseClass, String url, Parameters parameters) {
        return doRequestForObjectOfStandard(responseClass, GET, url, Headers.of(), parameters, true, null);
    }

    public <T> List<T> getListOfStandard(Class<T> responseClass, String url, Parameters parameters) {
        return doRequestForListOfStandard(responseClass, GET, url, Headers.of(), parameters, true, null);
    }


    public <T> T get(Class<T> responseClass, String url, Headers headers) {
        return doRequestForObject(responseClass, GET, url, headers, Parameters.of(), true, null);
    }

    public <T> List<T> getList(Class<T> responseClass, String url, Headers headers) {
        return doRequestForList(responseClass, GET, url, headers, Parameters.of(), true, null);
    }


    public <T> T getOfStandard(Class<T> responseClass, String url, Headers headers) {
        return doRequestForObjectOfStandard(responseClass, GET, url, headers, Parameters.of(), true, null);
    }

    public <T> List<T> getListOfStandard(Class<T> responseClass, String url, Headers headers) {
        return doRequestForListOfStandard(responseClass, GET, url, headers, Parameters.of(), true, null);
    }


    public <T> T get(Class<T> responseClass, String url, Headers headers, Parameters parameters) {
        return doRequestForObject(responseClass, GET, url, headers, parameters, true, null);
    }

    public <T> List<T> getList(Class<T> responseClass, String url, Headers headers, Parameters parameters) {
        return doRequestForList(responseClass, GET, url, headers, parameters, true, null);
    }


    public <T> T getOfStandard(Class<T> responseClass, String url, Headers headers, Parameters parameters) {
        return doRequestForObjectOfStandard(responseClass, GET, url, headers, parameters, true, null);
    }

    public <T> List<T> getListOfStandard(Class<T> responseClass, String url, Headers headers, Parameters parameters) {
        return doRequestForListOfStandard(responseClass, GET, url, headers, parameters, true, null);
    }



    public void get(String url) {
        doRequestForObject(null, GET, url, Headers.of(), Parameters.of(), true, null);
    }

    public void getList(String url) {
        doRequestForList(null, GET, url, Headers.of(), Parameters.of(), true, null);
    }


    public void getOfStandard(String url) {
        doRequestForObjectOfStandard(null, GET, url, Headers.of(), Parameters.of(), true, null);
    }

    public void getListOfStandard(String url) {
        doRequestForListOfStandard(null, GET, url, Headers.of(), Parameters.of(), true, null);
    }


    public void get(String url, Parameters parameters) {
        doRequestForObject(null, GET, url, Headers.of(), parameters, true, null);
    }

    public void getList(String url, Parameters parameters) {
        doRequestForList(null, GET, url, Headers.of(), parameters, true, null);
    }


    public void getOfStandard(String url, Parameters parameters) {
        doRequestForObjectOfStandard(null, GET, url, Headers.of(), parameters, true, null);
    }

    public void getListOfStandard(String url, Parameters parameters) {
        doRequestForListOfStandard(null, GET, url, Headers.of(), parameters, true, null);
    }


    public void get(String url, Headers headers) {
        doRequestForObject(null, GET, url, headers, Parameters.of(), true, null);
    }

    public void getList(String url, Headers headers) {
        doRequestForList(null, GET, url, headers, Parameters.of(), true, null);
    }


    public void getOfStandard(String url, Headers headers) {
        doRequestForObjectOfStandard(null, GET, url, headers, Parameters.of(), true, null);
    }

    public void getListOfStandard(String url, Headers headers) {
        doRequestForListOfStandard(null, GET, url, headers, Parameters.of(), true, null);
    }


    public void get(String url, Headers headers, Parameters parameters) {
        doRequestForObject(null, GET, url, headers, parameters, true, null);
    }

    public void getList(String url, Headers headers, Parameters parameters) {
        doRequestForList(null, GET, url, headers, parameters, true, null);
    }


    public void getOfStandard(String url, Headers headers, Parameters parameters) {
        doRequestForObjectOfStandard(null, GET, url, headers, parameters, true, null);
    }

    public void getListOfStandard(String url, Headers headers, Parameters parameters) {
        doRequestForListOfStandard(null, GET, url, headers, parameters, true, null);
    }

    // ------------------ head ------------------
    public void head(String url) {
        doRequestForObject(null, HEAD, url, Headers.of(), Parameters.of(), true, null);
    }

    public void headList(String url) {
        doRequestForList(null, HEAD, url, Headers.of(), Parameters.of(), true, null);
    }


    public void headOfStandard(String url) {
        doRequestForObjectOfStandard(null, HEAD, url, Headers.of(), Parameters.of(), true, null);
    }

    public void headListOfStandard(String url) {
        doRequestForListOfStandard(null, HEAD, url, Headers.of(), Parameters.of(), true, null);
    }


    public void head(String url, Parameters parameters) {
        doRequestForObject(null, HEAD, url, Headers.of(), parameters, true, null);
    }

    public void headList(String url, Parameters parameters) {
        doRequestForList(null, HEAD, url, Headers.of(), parameters, true, null);
    }


    public void headOfStandard(String url, Parameters parameters) {
        doRequestForObjectOfStandard(null, HEAD, url, Headers.of(), parameters, true, null);
    }

    public void headListOfStandard(String url, Parameters parameters) {
        doRequestForListOfStandard(null, HEAD, url, Headers.of(), parameters, true, null);
    }


    public void head(String url, Headers headers) {
        doRequestForObject(null, HEAD, url, headers, Parameters.of(), true, null);
    }

    public void headList(String url, Headers headers) {
        doRequestForList(null, HEAD, url, headers, Parameters.of(), true, null);
    }


    public void headOfStandard(String url, Headers headers) {
        doRequestForObjectOfStandard(null, HEAD, url, headers, Parameters.of(), true, null);
    }

    public void headListOfStandard(String url, Headers headers) {
        doRequestForListOfStandard(null, HEAD, url, headers, Parameters.of(), true, null);
    }


    public void head(String url, Headers headers, Parameters parameters) {
        doRequestForObject(null, HEAD, url, headers, parameters, true, null);
    }

    public void headList(String url, Headers headers, Parameters parameters) {
        doRequestForList(null, HEAD, url, headers, parameters, true, null);
    }


    public void headOfStandard(String url, Headers headers, Parameters parameters) {
        doRequestForObjectOfStandard(null, HEAD, url, headers, parameters, true, null);
    }

    public void headListOfStandard(String url, Headers headers, Parameters parameters) {
        doRequestForListOfStandard(null, HEAD, url, headers, parameters, true, null);
    }

    // ------------------ post ------------------
    public void post(String url, Object body) {
        doRequestForObject(null, POST, url, Headers.of(), Parameters.of(), false, body);
    }

    public void postOfStandard(String url, Object body) {
        doRequestForObjectOfStandard(null, POST, url, Headers.of(), Parameters.of(), false, body);
    }

    public void post(String url, Parameters parameters, Object body) {
        doRequestForObject(null, POST, url, Headers.of(), parameters, false, body);
    }

    public void postOfStandard(String url, Parameters parameters, Object body) {
        doRequestForObjectOfStandard(null, POST, url, Headers.of(), parameters, false, body);
    }

    public void post(String url, Headers headers, Object body) {
        doRequestForObject(null, POST, url, headers, Parameters.of(), false, body);
    }

    public void postOfStandard(String url, Headers headers, Object body) {
        doRequestForObjectOfStandard(null, POST, url, headers, Parameters.of(), false, body);
    }

    public void post(String url, Headers headers, Parameters parameters, Object body) {
        doRequestForObject(null, POST, url, headers, parameters, false, body);
    }

    public void postOfStandard(String url, Headers headers, Parameters parameters, Object body) {
        doRequestForObjectOfStandard(null, POST, url, headers, parameters, false, body);
    }


    public <T> T post(Class<T> responseClass, String url, Object body) {
        return doRequestForObject(responseClass, POST, url, Headers.of(), Parameters.of(), false, body);
    }

    public <T> List<T> postList(Class<T> responseClass, String url, Object body) {
        return doRequestForList(responseClass, POST, url, Headers.of(), Parameters.of(), false, body);
    }


    public <T> T postOfStandard(Class<T> responseClass, String url, Object body) {
        return doRequestForObjectOfStandard(responseClass, POST, url, Headers.of(), Parameters.of(), false, body);
    }

    public <T> List<T> postListOfStandard(Class<T> responseClass, String url, Object body) {
        return doRequestForListOfStandard(responseClass, POST, url, Headers.of(), Parameters.of(), false, body);
    }


    public <T> T post(Class<T> responseClass, String url, Parameters parameters, Object body) {
        return doRequestForObject(responseClass, POST, url, Headers.of(), parameters, false, body);
    }

    public <T> List<T> postList(Class<T> responseClass, String url, Parameters parameters, Object body) {
        return doRequestForList(responseClass, POST, url, Headers.of(), parameters, false, body);
    }


    public <T> T postOfStandard(Class<T> responseClass, String url, Parameters parameters, Object body) {
        return doRequestForObjectOfStandard(responseClass, POST, url, Headers.of(), parameters, false, body);
    }

    public <T> List<T> postListOfStandard(Class<T> responseClass, String url, Parameters parameters, Object body) {
        return doRequestForListOfStandard(responseClass, POST, url, Headers.of(), parameters, false, body);
    }


    public <T> T post(Class<T> responseClass, String url, Headers headers, Object body) {
        return doRequestForObject(responseClass, POST, url, headers, Parameters.of(), false, body);
    }

    public <T> List<T> postList(Class<T> responseClass, String url, Headers headers, Object body) {
        return doRequestForList(responseClass, POST, url, headers, Parameters.of(), false, body);
    }


    public <T> T postOfStandard(Class<T> responseClass, String url, Headers headers, Object body) {
        return doRequestForObjectOfStandard(responseClass, POST, url, headers, Parameters.of(), false, body);
    }

    public <T> List<T> postListOfStandard(Class<T> responseClass, String url, Headers headers, Object body) {
        return doRequestForListOfStandard(responseClass, POST, url, headers, Parameters.of(), false, body);
    }


    public <T> T post(Class<T> responseClass, String url, Headers headers, Parameters parameters, Object body) {
        return doRequestForObject(responseClass, POST, url, headers, parameters, false, body);
    }

    public <T> List<T> postList(Class<T> responseClass, String url, Headers headers, Parameters parameters, Object body) {
        return doRequestForList(responseClass, POST, url, headers, parameters, false, body);
    }


    public <T> T postOfStandard(Class<T> responseClass, String url, Headers headers, Parameters parameters, Object body) {
        return doRequestForObjectOfStandard(responseClass, POST, url, headers, parameters, false, body);
    }

    public <T> List<T> postListOfStandard(Class<T> responseClass, String url, Headers headers, Parameters parameters, Object body) {
        return doRequestForListOfStandard(responseClass, POST, url, headers, parameters, false, body);
    }

    // ------------------ put ------------------
    public void put(String url) {
        doRequestForObject(null, PUT, url, Headers.of(), Parameters.of(), false, null);
    }

    public void putOfStandard(String url) {
        doRequestForObjectOfStandard(null, PUT, url, Headers.of(), Parameters.of(), false, null);
    }

    public void put(String url, Headers headers, Parameters parameters) {
        doRequestForObject(null, PUT, url, headers, parameters, false, null);
    }

    public void putOfStandard(String url, Headers headers, Parameters parameters) {
        doRequestForObjectOfStandard(null, PUT, url, headers, parameters, false, null);
    }


    public <T> T put(Class<T> responseClass, String url) {
        return doRequestForObject(responseClass, PUT, url, Headers.of(), Parameters.of(), false, null);
    }

    public <T> List<T> putList(Class<T> responseClass, String url) {
        return doRequestForList(responseClass, PUT, url, Headers.of(), Parameters.of(), false, null);
    }


    public <T> T putOfStandard(Class<T> responseClass, String url) {
        return doRequestForObjectOfStandard(responseClass, PUT, url, Headers.of(), Parameters.of(), false, null);
    }

    public <T> List<T> putListOfStandard(Class<T> responseClass, String url) {
        return doRequestForListOfStandard(responseClass, PUT, url, Headers.of(), Parameters.of(), false, null);
    }


    public <T> T put(Class<T> responseClass, String url, Headers headers, Parameters parameters) {
        return doRequestForObject(responseClass, PUT, url, headers, parameters, false, null);
    }

    public <T> List<T> putList(Class<T> responseClass, String url, Headers headers, Parameters parameters) {
        return doRequestForList(responseClass, PUT, url, headers, parameters, false, null);
    }


    public <T> T putOfStandard(Class<T> responseClass, String url, Headers headers, Parameters parameters) {
        return doRequestForObjectOfStandard(responseClass, PUT, url, headers, parameters, false, null);
    }

    public <T> List<T> putListOfStandard(Class<T> responseClass, String url, Headers headers, Parameters parameters) {
        return doRequestForListOfStandard(responseClass, PUT, url, headers, parameters, false, null);
    }


    public void put(String url, Object body) {
        doRequestForObject(null, PUT, url, Headers.of(), Parameters.of(), false, body);
    }

    public void putOfStandard(String url, Object body) {
        doRequestForObjectOfStandard(null, PUT, url, Headers.of(), Parameters.of(), false, body);
    }

    public void put(String url, Parameters parameters, Object body) {
        doRequestForObject(null, PUT, url, Headers.of(), parameters, false, body);
    }

    public void putOfStandard(String url, Parameters parameters, Object body) {
        doRequestForObjectOfStandard(null, PUT, url, Headers.of(), parameters, false, body);
    }

    public void put(String url, Headers headers, Object body) {
        doRequestForObject(null, PUT, url, headers, Parameters.of(), false, body);
    }

    public void putOfStandard(String url, Headers headers, Object body) {
        doRequestForObjectOfStandard(null, PUT, url, headers, Parameters.of(), false, body);
    }

    public void put(String url, Headers headers, Parameters parameters, Object body) {
        doRequestForObject(null, PUT, url, headers, parameters, false, body);
    }

    public void putOfStandard(String url, Headers headers, Parameters parameters, Object body) {
        doRequestForObjectOfStandard(null, PUT, url, headers, parameters, false, body);
    }


    public <T> T put(Class<T> responseClass, String url, Object body) {
        return doRequestForObject(responseClass, PUT, url, Headers.of(), Parameters.of(), false, body);
    }

    public <T> List<T> putList(Class<T> responseClass, String url, Object body) {
        return doRequestForList(responseClass, PUT, url, Headers.of(), Parameters.of(), false, body);
    }


    public <T> T putOfStandard(Class<T> responseClass, String url, Object body) {
        return doRequestForObjectOfStandard(responseClass, PUT, url, Headers.of(), Parameters.of(), false, body);
    }

    public <T> List<T> putListOfStandard(Class<T> responseClass, String url, Object body) {
        return doRequestForListOfStandard(responseClass, PUT, url, Headers.of(), Parameters.of(), false, body);
    }


    public <T> T put(Class<T> responseClass, String url, Parameters parameters, Object body) {
        return doRequestForObject(responseClass, PUT, url, Headers.of(), parameters, false, body);
    }

    public <T> List<T> putList(Class<T> responseClass, String url, Parameters parameters, Object body) {
        return doRequestForList(responseClass, PUT, url, Headers.of(), parameters, false, body);
    }


    public <T> T putOfStandard(Class<T> responseClass, String url, Parameters parameters, Object body) {
        return doRequestForObjectOfStandard(responseClass, PUT, url, Headers.of(), parameters, false, body);
    }

    public <T> List<T> putListOfStandard(Class<T> responseClass, String url, Parameters parameters, Object body) {
        return doRequestForListOfStandard(responseClass, PUT, url, Headers.of(), parameters, false, body);
    }


    public <T> T put(Class<T> responseClass, String url, Headers headers, Object body) {
        return doRequestForObject(responseClass, PUT, url, headers, Parameters.of(), false, body);
    }

    public <T> List<T> putList(Class<T> responseClass, String url, Headers headers, Object body) {
        return doRequestForList(responseClass, PUT, url, headers, Parameters.of(), false, body);
    }


    public <T> T putOfStandard(Class<T> responseClass, String url, Headers headers, Object body) {
        return doRequestForObjectOfStandard(responseClass, PUT, url, headers, Parameters.of(), false, body);
    }

    public <T> List<T> putListOfStandard(Class<T> responseClass, String url, Headers headers, Object body) {
        return doRequestForListOfStandard(responseClass, PUT, url, headers, Parameters.of(), false, body);
    }


    public <T> T put(Class<T> responseClass, String url, Headers headers, Parameters parameters, Object body) {
        return doRequestForObject(responseClass, PUT, url, headers, parameters, false, body);
    }

    public <T> List<T> putList(Class<T> responseClass, String url, Headers headers, Parameters parameters, Object body) {
        return doRequestForList(responseClass, PUT, url, headers, parameters, false, body);
    }


    public <T> T putOfStandard(Class<T> responseClass, String url, Headers headers, Parameters parameters, Object body) {
        return doRequestForObjectOfStandard(responseClass, PUT, url, headers, parameters, false, body);
    }

    public <T> List<T> putListOfStandard(Class<T> responseClass, String url, Headers headers, Parameters parameters, Object body) {
        return doRequestForListOfStandard(responseClass, PUT, url, headers, parameters, false, body);
    }

    // ------------------ delete ------------------
    public <T> T delete(Class<T> responseClass, String url) {
        return doRequestForObject(responseClass, DELETE, url, Headers.of(), Parameters.of(), true, null);
    }

    public <T> List<T> deleteList(Class<T> responseClass, String url) {
        return doRequestForList(responseClass, DELETE, url, Headers.of(), Parameters.of(), true, null);
    }


    public <T> T deleteOfStandard(Class<T> responseClass, String url) {
        return doRequestForObjectOfStandard(responseClass, DELETE, url, Headers.of(), Parameters.of(), true, null);
    }

    public <T> List<T> deleteListOfStandard(Class<T> responseClass, String url) {
        return doRequestForListOfStandard(responseClass, DELETE, url, Headers.of(), Parameters.of(), true, null);
    }


    public <T> T delete(Class<T> responseClass, String url, Parameters parameters) {
        return doRequestForObject(responseClass, DELETE, url, Headers.of(), parameters, true, null);
    }

    public <T> List<T> deleteList(Class<T> responseClass, String url, Parameters parameters) {
        return doRequestForList(responseClass, DELETE, url, Headers.of(), parameters, true, null);
    }


    public <T> T deleteOfStandard(Class<T> responseClass, String url, Parameters parameters) {
        return doRequestForObjectOfStandard(responseClass, DELETE, url, Headers.of(), parameters, true, null);
    }

    public <T> List<T> deleteListOfStandard(Class<T> responseClass, String url, Parameters parameters) {
        return doRequestForListOfStandard(responseClass, DELETE, url, Headers.of(), parameters, true, null);
    }


    public <T> T delete(Class<T> responseClass, String url, Headers headers) {
        return doRequestForObject(responseClass, DELETE, url, headers, Parameters.of(), true, null);
    }

    public <T> List<T> deleteList(Class<T> responseClass, String url, Headers headers) {
        return doRequestForList(responseClass, DELETE, url, headers, Parameters.of(), true, null);
    }


    public <T> T deleteOfStandard(Class<T> responseClass, String url, Headers headers) {
        return doRequestForObjectOfStandard(responseClass, DELETE, url, headers, Parameters.of(), true, null);
    }

    public <T> List<T> deleteListOfStandard(Class<T> responseClass, String url, Headers headers) {
        return doRequestForListOfStandard(responseClass, DELETE, url, headers, Parameters.of(), true, null);
    }


    public <T> T delete(Class<T> responseClass, String url, Headers headers, Parameters parameters) {
        return doRequestForObject(responseClass, DELETE, url, headers, parameters, true, null);
    }

    public <T> List<T> deleteList(Class<T> responseClass, String url, Headers headers, Parameters parameters) {
        return doRequestForList(responseClass, DELETE, url, headers, parameters, true, null);
    }


    public <T> T deleteOfStandard(Class<T> responseClass, String url, Headers headers, Parameters parameters) {
        return doRequestForObjectOfStandard(responseClass, DELETE, url, headers, parameters, true, null);
    }

    public <T> List<T> deleteListOfStandard(Class<T> responseClass, String url, Headers headers, Parameters parameters) {
        return doRequestForListOfStandard(responseClass, DELETE, url, headers, parameters, true, null);
    }


    public void delete(String url) {
        doRequestForObject(null, DELETE, url, Headers.of(), Parameters.of(), true, null);
    }

    public void deleteList(String url) {
        doRequestForList(null, DELETE, url, Headers.of(), Parameters.of(), true, null);
    }


    public void deleteOfStandard(String url) {
        doRequestForObjectOfStandard(null, DELETE, url, Headers.of(), Parameters.of(), true, null);
    }

    public void deleteListOfStandard(String url) {
        doRequestForListOfStandard(null, DELETE, url, Headers.of(), Parameters.of(), true, null);
    }


    public void delete(String url, Parameters parameters) {
        doRequestForObject(null, DELETE, url, Headers.of(), parameters, true, null);
    }

    public void deleteList(String url, Parameters parameters) {
        doRequestForList(null, DELETE, url, Headers.of(), parameters, true, null);
    }


    public void deleteOfStandard(String url, Parameters parameters) {
        doRequestForObjectOfStandard(null, DELETE, url, Headers.of(), parameters, true, null);
    }

    public void deleteListOfStandard(String url, Parameters parameters) {
        doRequestForListOfStandard(null, DELETE, url, Headers.of(), parameters, true, null);
    }


    public void delete(String url, Headers headers) {
        doRequestForObject(null, DELETE, url, headers, Parameters.of(), true, null);
    }

    public void deleteList(String url, Headers headers) {
        doRequestForList(null, DELETE, url, headers, Parameters.of(), true, null);
    }


    public void deleteOfStandard(String url, Headers headers) {
        doRequestForObjectOfStandard(null, DELETE, url, headers, Parameters.of(), true, null);
    }

    public void deleteListOfStandard(String url, Headers headers) {
        doRequestForListOfStandard(null, DELETE, url, headers, Parameters.of(), true, null);
    }


    public void delete(String url, Headers headers, Parameters parameters) {
        doRequestForObject(null, DELETE, url, headers, parameters, true, null);
    }

    public void deleteList(String url, Headers headers, Parameters parameters) {
        doRequestForList(null, DELETE, url, headers, parameters, true, null);
    }


    public void deleteOfStandard(String url, Headers headers, Parameters parameters) {
        doRequestForObjectOfStandard(null, DELETE, url, headers, parameters, true, null);
    }

    public void deleteListOfStandard(String url, Headers headers, Parameters parameters) {
        doRequestForListOfStandard(null, DELETE, url, headers, parameters, true, null);
    }


    public void delete(String url, Object body) {
        doRequestForObject(null, DELETE, url, Headers.of(), Parameters.of(), true, body);
    }

    public void deleteOfStandard(String url, Object body) {
        doRequestForObjectOfStandard(null, DELETE, url, Headers.of(), Parameters.of(), true, body);
    }

    public void delete(String url, Parameters parameters, Object body) {
        doRequestForObject(null, DELETE, url, Headers.of(), parameters, true, body);
    }

    public void deleteOfStandard(String url, Parameters parameters, Object body) {
        doRequestForObjectOfStandard(null, DELETE, url, Headers.of(), parameters, true, body);
    }

    public void delete(String url, Headers headers, Object body) {
        doRequestForObject(null, DELETE, url, headers, Parameters.of(), true, body);
    }

    public void deleteOfStandard(String url, Headers headers, Object body) {
        doRequestForObjectOfStandard(null, DELETE, url, headers, Parameters.of(), true, body);
    }

    public void delete(String url, Headers headers, Parameters parameters, Object body) {
        doRequestForObject(null, DELETE, url, headers, parameters, true, body);
    }

    public void deleteOfStandard(String url, Headers headers, Parameters parameters, Object body) {
        doRequestForObjectOfStandard(null, DELETE, url, headers, parameters, true, body);
    }


    public <T> T delete(Class<T> responseClass, String url, Object body) {
        return doRequestForObject(responseClass, DELETE, url, Headers.of(), Parameters.of(), true, body);
    }

    public <T> List<T> deleteList(Class<T> responseClass, String url, Object body) {
        return doRequestForList(responseClass, DELETE, url, Headers.of(), Parameters.of(), true, body);
    }


    public <T> T deleteOfStandard(Class<T> responseClass, String url, Object body) {
        return doRequestForObjectOfStandard(responseClass, DELETE, url, Headers.of(), Parameters.of(), true, body);
    }

    public <T> List<T> deleteListOfStandard(Class<T> responseClass, String url, Object body) {
        return doRequestForListOfStandard(responseClass, DELETE, url, Headers.of(), Parameters.of(), true, body);
    }


    public <T> T delete(Class<T> responseClass, String url, Parameters parameters, Object body) {
        return doRequestForObject(responseClass, DELETE, url, Headers.of(), parameters, true, body);
    }

    public <T> List<T> deleteList(Class<T> responseClass, String url, Parameters parameters, Object body) {
        return doRequestForList(responseClass, DELETE, url, Headers.of(), parameters, true, body);
    }


    public <T> T deleteOfStandard(Class<T> responseClass, String url, Parameters parameters, Object body) {
        return doRequestForObjectOfStandard(responseClass, DELETE, url, Headers.of(), parameters, true, body);
    }

    public <T> List<T> deleteListOfStandard(Class<T> responseClass, String url, Parameters parameters, Object body) {
        return doRequestForListOfStandard(responseClass, DELETE, url, Headers.of(), parameters, true, body);
    }


    public <T> T delete(Class<T> responseClass, String url, Headers headers, Object body) {
        return doRequestForObject(responseClass, DELETE, url, headers, Parameters.of(), true, body);
    }

    public <T> List<T> deleteList(Class<T> responseClass, String url, Headers headers, Object body) {
        return doRequestForList(responseClass, DELETE, url, headers, Parameters.of(), true, body);
    }


    public <T> T deleteOfStandard(Class<T> responseClass, String url, Headers headers, Object body) {
        return doRequestForObjectOfStandard(responseClass, DELETE, url, headers, Parameters.of(), true, body);
    }

    public <T> List<T> deleteListOfStandard(Class<T> responseClass, String url, Headers headers, Object body) {
        return doRequestForListOfStandard(responseClass, DELETE, url, headers, Parameters.of(), true, body);
    }


    public <T> T delete(Class<T> responseClass, String url, Headers headers, Parameters parameters, Object body) {
        return doRequestForObject(responseClass, DELETE, url, headers, parameters, true, body);
    }

    public <T> List<T> deleteList(Class<T> responseClass, String url, Headers headers, Parameters parameters, Object body) {
        return doRequestForList(responseClass, DELETE, url, headers, parameters, true, body);
    }


    public <T> T deleteOfStandard(Class<T> responseClass, String url, Headers headers, Parameters parameters, Object body) {
        return doRequestForObjectOfStandard(responseClass, DELETE, url, headers, parameters, true, body);
    }

    public <T> List<T> deleteListOfStandard(Class<T> responseClass, String url, Headers headers, Parameters parameters, Object body) {
        return doRequestForListOfStandard(responseClass, DELETE, url, headers, parameters, true, body);
    }

    // ------------------ patch ------------------
    public void patch(String url, Object body) {
        doRequestForObject(null, PATCH, url, Headers.of(), Parameters.of(), false, body);
    }

    public void patchOfStandard(String url, Object body) {
        doRequestForObjectOfStandard(null, PATCH, url, Headers.of(), Parameters.of(), false, body);
    }

    public void patch(String url, Parameters parameters, Object body) {
        doRequestForObject(null, PATCH, url, Headers.of(), parameters, false, body);
    }

    public void patchOfStandard(String url, Parameters parameters, Object body) {
        doRequestForObjectOfStandard(null, PATCH, url, Headers.of(), parameters, false, body);
    }

    public void patch(String url, Headers headers, Object body) {
        doRequestForObject(null, PATCH, url, headers, Parameters.of(), false, body);
    }

    public void patchOfStandard(String url, Headers headers, Object body) {
        doRequestForObjectOfStandard(null, PATCH, url, headers, Parameters.of(), false, body);
    }

    public void patch(String url, Headers headers, Parameters parameters, Object body) {
        doRequestForObject(null, PATCH, url, headers, parameters, false, body);
    }

    public void patchOfStandard(String url, Headers headers, Parameters parameters, Object body) {
        doRequestForObjectOfStandard(null, PATCH, url, headers, parameters, false, body);
    }


    public <T> T patch(Class<T> responseClass, String url, Object body) {
        return doRequestForObject(responseClass, PATCH, url, Headers.of(), Parameters.of(), false, body);
    }

    public <T> List<T> patchList(Class<T> responseClass, String url, Object body) {
        return doRequestForList(responseClass, PATCH, url, Headers.of(), Parameters.of(), false, body);
    }


    public <T> T patchOfStandard(Class<T> responseClass, String url, Object body) {
        return doRequestForObjectOfStandard(responseClass, PATCH, url, Headers.of(), Parameters.of(), false, body);
    }

    public <T> List<T> patchListOfStandard(Class<T> responseClass, String url, Object body) {
        return doRequestForListOfStandard(responseClass, PATCH, url, Headers.of(), Parameters.of(), false, body);
    }


    public <T> T patch(Class<T> responseClass, String url, Parameters parameters, Object body) {
        return doRequestForObject(responseClass, PATCH, url, Headers.of(), parameters, false, body);
    }

    public <T> List<T> patchList(Class<T> responseClass, String url, Parameters parameters, Object body) {
        return doRequestForList(responseClass, PATCH, url, Headers.of(), parameters, false, body);
    }


    public <T> T patchOfStandard(Class<T> responseClass, String url, Parameters parameters, Object body) {
        return doRequestForObjectOfStandard(responseClass, PATCH, url, Headers.of(), parameters, false, body);
    }

    public <T> List<T> patchListOfStandard(Class<T> responseClass, String url, Parameters parameters, Object body) {
        return doRequestForListOfStandard(responseClass, PATCH, url, Headers.of(), parameters, false, body);
    }


    public <T> T patch(Class<T> responseClass, String url, Headers headers, Object body) {
        return doRequestForObject(responseClass, PATCH, url, headers, Parameters.of(), false, body);
    }

    public <T> List<T> patchList(Class<T> responseClass, String url, Headers headers, Object body) {
        return doRequestForList(responseClass, PATCH, url, headers, Parameters.of(), false, body);
    }


    public <T> T patchOfStandard(Class<T> responseClass, String url, Headers headers, Object body) {
        return doRequestForObjectOfStandard(responseClass, PATCH, url, headers, Parameters.of(), false, body);
    }

    public <T> List<T> patchListOfStandard(Class<T> responseClass, String url, Headers headers, Object body) {
        return doRequestForListOfStandard(responseClass, PATCH, url, headers, Parameters.of(), false, body);
    }


    public <T> T patch(Class<T> responseClass, String url, Headers headers, Parameters parameters, Object body) {
        return doRequestForObject(responseClass, PATCH, url, headers, parameters, false, body);
    }

    public <T> List<T> patchList(Class<T> responseClass, String url, Headers headers, Parameters parameters, Object body) {
        return doRequestForList(responseClass, PATCH, url, headers, parameters, false, body);
    }


    public <T> T patchOfStandard(Class<T> responseClass, String url, Headers headers, Parameters parameters, Object body) {
        return doRequestForObjectOfStandard(responseClass, PATCH, url, headers, parameters, false, body);
    }

    public <T> List<T> patchListOfStandard(Class<T> responseClass, String url, Headers headers, Parameters parameters, Object body) {
        return doRequestForListOfStandard(responseClass, PATCH, url, headers, parameters, false, body);
    }
    // ------------------ options ------------------
    // 该协议okhttp暂时不支持
    // ------------------ trace ------------------
    // 该协议okhttp暂时不支持

    private <T> T doRequestForObject(Class<T> responseClass, String httpMethod, String url, Headers headers, Parameters parameters, Boolean allowNull, Object body) {
        Request request = new Request.Builder().method(httpMethod, getRequestBody(allowNull, body)).headers(headers).url(getBuilder(url, parameters).build()).build();
        try (Response response = call(request, url)) {
            try (ResponseBody responseBody = response.body()) {
                if (null == responseBody) {
                    return null;
                }
                return parseObject(responseClass, url, responseBody);
            }
        }
    }

    private <T> List<T> doRequestForList(Class<T> responseClass, String httpMethod, String url, Headers headers, Parameters parameters, Boolean allowNull, Object body) {
        Request request = new Request.Builder().method(httpMethod, getRequestBody(allowNull, body)).headers(headers).url(getBuilder(url, parameters).build()).build();
        try (Response response = call(request, url)) {
            try (ResponseBody responseBody = response.body()) {
                if (null == responseBody) {
                    return null;
                }
                return parseList(responseClass, url, responseBody);
            }
        }
    }

    private <T> T doRequestForObjectOfStandard(Class<T> responseClass, String httpMethod, String url, Headers headers, Parameters parameters, Boolean allowNull, Object body) {
        Request request = new Request.Builder().method(httpMethod, getRequestBody(allowNull, body)).headers(headers).url(getBuilder(url, parameters).build()).build();
        try (Response response = call(request, url)) {
            try (ResponseBody responseBody = response.body()) {
                if (null == responseBody) {
                    return null;
                }
                return parseObjectOfStandard(responseClass, url, responseBody);
            }
        }
    }

    private <T> List<T> doRequestForListOfStandard(Class<T> responseClass, String httpMethod, String url, Headers headers, Parameters parameters, Boolean allowNull, Object body) {
        Request request = new Request.Builder().method(httpMethod, getRequestBody(allowNull, body)).headers(headers).url(getBuilder(url, parameters).build()).build();
        try (Response response = call(request, url)) {
            try (ResponseBody responseBody = response.body()) {
                if (null == responseBody) {
                    return null;
                }
                return parseListOfStandard(responseClass, url, responseBody);
            }
        }
    }

    private HttpUrl.Builder getBuilder(String url, Parameters parameters) {
        HttpUrl.Builder httpBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        parameters.parameterMap.forEach(httpBuilder::addQueryParameter);
        return httpBuilder;
    }

    private RequestBody getRequestBody(Boolean allowNull, Object body) {
        if (null == body) {
            if (allowNull) {
                return null;
            }
            return RequestBody.create(MediaType.parse("application/json"), JSON.toJSONString(new HashMap<>()));
        }
        return RequestBody.create(MediaType.parse("application/json"), JSON.toJSONString(body));
    }

    private Response call(Request request, String url) {
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                log.error(LOG_PRE + "请求异常，url={}, code={}", url, response.code());
                throw new RuntimeException(response.code() + ", url=" + url + ", code=" + response.code());
            }
            return response;
        } catch (Throwable e) {
            if (null != response) {
                response.close();
            }
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private <T> T parseObject(Class<T> tClass, String url, ResponseBody responseBody) {
        try {
            if (null == tClass) {
                return null;
            }
            return ObjectUtil.cast(tClass, responseBody.string());
        } catch (IOException e) {
            log.error(LOG_PRE + "请求异常，url={}", url, e);
            throw new RuntimeException("请求异常");
        }
    }

    private <T> List<T> parseList(Class<T> tClass, String url, ResponseBody responseBody) {
        try {
            if (null == tClass) {
                return null;
            }
            return JSON.parseArray(responseBody.string(), tClass);
        } catch (IOException e) {
            log.error(LOG_PRE + "请求异常，url=" + url, e);
            throw new RuntimeException("请求异常");
        }
    }

    private <T> T parseObjectOfStandard(Class<T> tClass, String url, ResponseBody responseBody) {
        try {
            if (null == tClass) {
                return null;
            }
            StandardResponse<?> response = JSONObject.parseObject(responseBody.bytes(), StandardResponse.class);
            if (response.isSuccess()) {
                return ObjectUtil.cast(tClass, response.getData());
            } else {
                log.error("异常返回, code={}, message={}", response.getErrCode(), response.getErrMsg());
                throw new RuntimeException(response.getErrCode() + ", " + response.getErrMsg());
            }
        } catch (IOException e) {
            log.error("请求异常，url=" + url, e);
            throw new RuntimeException("请求异常");
        }
    }

    private <T> List<T> parseListOfStandard(Class<T> tClass, String url, ResponseBody responseBody) {
        try {
            if (null == tClass) {
                return null;
            }
            StandardResponse<?> response = JSONObject.parseObject(responseBody.bytes(), StandardResponse.class);
            if (response.isSuccess()) {
                return JSON.parseArray(JSON.toJSONString(response.getData()), tClass);
            } else {
                log.error("异常返回, code={}, message={}", response.getErrCode(), response.getErrMsg());
                throw new RuntimeException(response.getErrCode() + ", " + response.getErrMsg());
            }
        } catch (IOException e) {
            log.error("请求异常，url=" + url, e);
            throw new RuntimeException("请求异常");
        }
    }

    @Data
    public class StandardResponse<T> implements Serializable {
        private Object errCode;
        private String errMsg;

        private T data;

        public boolean isSuccess() {
            return null == errCode;
        }
    }

    /**
     * 参数的kv类型
     */
    public static class Parameters {

        private final Map<String, String> parameterMap = new ConcurrentHashMap<>();

        private Parameters() {}

        public static Boolean isUnEmpty(Parameters parameters) {
            return !isEmpty(parameters);
        }

        public static Boolean isEmpty(Parameters parameters) {
            if (null == parameters) {
                return true;
            }
            return parameters.toMap().isEmpty();
        }

        public static Parameters of(String... kvs) {
            if (kvs.length % 2 != 0) {
                log.error(LOG_PRE + "Parameters.of的参数需要是key-value-key-value...这种格式");
                return new Parameters();
            }

            Parameters parameters = new Parameters();
            for (int i = 0; i < kvs.length; i += 2) {
                if (null == kvs[i]) {
                    log.error(LOG_PRE + "map的key不可以为null");
                    return parameters;
                }

                if(null == kvs[i + 1]) {
                    log.error(LOG_PRE + "map的value不可以为null");
                    return parameters;
                }
                parameters.toMap().put(kvs[i], kvs[i + 1]);
            }
            return parameters;
        }

        public Map<String, String> toMap() {
            return parameterMap;
        }
    }
}
