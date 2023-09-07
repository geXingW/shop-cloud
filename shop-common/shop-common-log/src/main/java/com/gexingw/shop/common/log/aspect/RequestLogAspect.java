package com.gexingw.shop.common.log.aspect;

import com.alibaba.fastjson2.JSON;
import com.gexingw.shop.common.core.constant.LogConstant;
import com.gexingw.shop.common.log.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * smart-logistics-saas.
 *
 * @author W.sf
 * @date 2023/7/7 14:31
 */
@Slf4j
@Aspect
@Component
public class RequestLogAspect {

    @Around("execution(!static com.gexingw.shop.common.core.util.R *(..)) && (@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController))")
    public Object aroundApi(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest httpServletRequest = this.getHttpRequest();
        if (httpServletRequest == null) {
            return point.proceed();
        }

        // 打印请求信息
        this.handleRequestLog(point, httpServletRequest);

        try {
            Object result = point.proceed();
            // 打印响应信息
            this.handlerResponseLog(httpServletRequest, result);

            return result;
        } catch (Exception e) {
            // 打印响应信息
            this.handlerResponseLog(httpServletRequest, null);

            // 异常交由上层处理
            throw e;
        }
    }

    private void handlerResponseLog(HttpServletRequest httpServletRequest, Object result) {
        HashMap<String, Object> params = new HashMap<>(20);
        params.put("traceId", httpServletRequest.getHeader(LogConstant.HEADER_TRACE_ID));
        params.put("uri", httpServletRequest.getRequestURI());
        params.put("method", httpServletRequest.getMethod());

        if (result == null) {
            log.info("Response:{}", JSON.toJSONString(params));
            return;
        }

        params.put("body", result);
        log.info("Response:{}", JSON.toJSONString(params));
    }

    private void handleRequestLog(ProceedingJoinPoint point, HttpServletRequest httpServletRequest) {
        HashMap<String, Object> params = new HashMap<>(20);
        params.put("traceId", httpServletRequest.getHeader(LogConstant.HEADER_TRACE_ID));
        params.put("uri", httpServletRequest.getRequestURI());
        params.put("method", httpServletRequest.getMethod());

        // 解析出请求参数
        MethodSignature ms = (MethodSignature) point.getSignature();
        Method method = ms.getMethod();
        Object[] args = point.getArgs();
        for (int i = 0; i < args.length; i++) {
            MethodParameter methodParam = ClassUtil.getMethodParameter(method, i);

            // Path参数Url中存在，不重复打印
            PathVariable pathVariable = methodParam.getParameterAnnotation(PathVariable.class);
            if (pathVariable != null) {
                continue;
            }

            // query参数
            RequestParam requestParam = methodParam.getParameterAnnotation(RequestParam.class);
            if (requestParam != null) {
                params.put(methodParam.getParameterName(), args[i]);
            }

            // body参数
            RequestBody requestBody = methodParam.getParameterAnnotation(RequestBody.class);
            if (requestBody != null) {
                params.put("body", args[i]);
            }
        }

        log.info("Request: {}", JSON.toJSONString(params));
    }

    private HttpServletRequest getHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : ((ServletRequestAttributes) requestAttributes).getRequest();
    }

}
