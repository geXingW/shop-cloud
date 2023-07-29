package com.gexingw.shop.service.auth.handler;

import com.gexingw.shop.service.auth.util.ResponseUtil;
import com.gexingw.shop.common.core.util.RespCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * smart-logistics-saas.
 *
 * @author W.sf
 * @date 2023/7/18 11:55
 */
public class AuthenticationFailureHandler implements org.springframework.security.web.authentication.AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);

        OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
        if ("invalid_client".equals(error.getErrorCode())) {
            ResponseUtil.jsonResponse(httpResponse, RespCode.INVALID_CLIENT.getCode(), "client_id或client_secret错误！");
        }

        ResponseUtil.jsonResponse(httpResponse, error.getErrorCode(), error.getDescription());
    }

}
