package com.gexingw.shop.auth.handler;

import com.gexingw.shop.auth.util.ResponseUtil;
import com.gexingw.shop.common.core.util.RespCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * smart-logistics-saas.
 *
 * @author W.sf
 * @date 2023/7/21 14:52
 */
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
        ResponseUtil.jsonResponse(httpResponse, RespCode.UN_AUTHORIZATION);
    }
}
