package com.gexingw.shop.common.log.interceptor;

import com.gexingw.shop.common.core.constant.LogConstant;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * smart-logistics-saas.
 *
 * @author W.sf
 * @date 2023/7/7 22:43
 */
@SuppressWarnings("NullableProblems")
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        MDC.put(LogConstant.MDC_TRACE_ID, request.getHeader(LogConstant.HEADER_TRACE_ID));

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        MDC.remove(LogConstant.MDC_TRACE_ID);
    }

}
