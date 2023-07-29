package com.gexingw.shop.service.auth.handler;

import com.gexingw.shop.service.auth.util.ResponseUtil;
import com.gexingw.shop.common.core.util.RespCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/22 17:07
 */
@Component
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);

        ResponseUtil.jsonResponse(httpResponse, RespCode.PERMISSION_DENIED);
    }

}

