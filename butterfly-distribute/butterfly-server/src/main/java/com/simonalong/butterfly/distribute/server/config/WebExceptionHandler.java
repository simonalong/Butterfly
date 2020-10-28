package com.simonalong.butterfly.distribute.server.config;


import com.simonalong.butterfly.distribute.model.Response;
import com.simonalong.butterfly.distribute.server.exception.BusinessException;
import com.simonalong.butterfly.distribute.server.util.ExceptionUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.annotation.ManagedBean;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author shizi
 * @since 2020/6/16 4:50 PM
 */
@Slf4j
@RestControllerAdvice("com.simonalong.butterfly.distribute.server.controller")
public class WebExceptionHandler extends RequestBodyAdviceAdapter {

    @Autowired
    private RequestContext requestContext;

    public static final String INTERNAL_SERVER_ERROR_CODE = "internal.server.error";
    public static final String INTERNAL_SERVER_ERROR_MESSAGE_DEFAULT = "Oops_服务器开小差啦，过会儿再试吧";

    @Autowired
    private Environment environment;

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<?> handleMyException(HttpServletRequest request, BusinessException e) {
        log.error("请求：{}，参数: {}, 异常: ", request.getRequestURI(), getParam(request), e);
        return Response.fail(e.getErrCode(), e.getMessage());
    }

    /**
     * 内部异常
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<?> handleMyException(HttpServletRequest request, Throwable e) {
        log.error("请求：{}，参数: {}, 异常: ", request.getRequestURI(), getParam(request), e);
        String code;
        String message;
        if (Arrays.asList(environment.getActiveProfiles()).contains("pro")) {
            code = INTERNAL_SERVER_ERROR_CODE;
            message = INTERNAL_SERVER_ERROR_MESSAGE_DEFAULT;
        } else {
            code = INTERNAL_SERVER_ERROR_CODE;
            message = "【系统异常】\n" + ExceptionUtil.unwrapException(e).toString();
        }
        return Response.fail(code, message);
    }

    private String getParam(HttpServletRequest request) {
        return requestContext.getParam() != null ? requestContext.getParam().toString() : getParamFromRequest(request);
    }

    private static String getParamFromRequest(HttpServletRequest request) {
        return request.getParameterMap().entrySet().stream().map(e -> String.format("%s=%s", e.getKey(), StringUtils.join(e.getValue()))).collect(Collectors.joining(";"));
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        requestContext.setParam(body);
        return body;
    }

    @ManagedBean
    @RequestScope
    @Data
    public static class RequestContext {
        private Object param;
    }

}
