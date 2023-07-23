package com.gexingw.shop.common.security.filter;

import com.alibaba.fastjson2.JSON;
import com.gexingw.shop.common.core.component.AuthInfo;
import com.gexingw.shop.common.core.constant.AuthConstant;
import com.gexingw.shop.common.core.util.RespCode;
import com.gexingw.shop.common.security.component.Authentication;
import com.gexingw.shop.common.security.util.ResponseUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/12 22:22
 */
@Component
public class AuthenticationFilter extends OncePerRequestFilter {


    @Override
    @SneakyThrows
    protected void doFilterInternal(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable FilterChain filterChain) {
        if (request != null) {

            String authUserJsonStr = request.getHeader(AuthConstant.HEADER_AUTH_USER);
            if (StringUtils.isBlank(authUserJsonStr)) {
                this.handlerUnAuthorizationError(response);
                return;
            }

            AuthInfo authInfo = JSON.parseObject(authUserJsonStr, AuthInfo.class);
            List<GrantedAuthority> authorities = new ArrayList<>();

            Authentication authentication = Authentication.builder().authInfo(authInfo).authorities(authorities).build();
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        //noinspection DataFlowIssue
        filterChain.doFilter(request, response);
    }

    private void handlerUnAuthorizationError(HttpServletResponse response) {
        try (ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response)) {
            httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
            ResponseUtil.jsonResponse(httpResponse, RespCode.UN_AUTHORIZATION);
        }
    }

}
