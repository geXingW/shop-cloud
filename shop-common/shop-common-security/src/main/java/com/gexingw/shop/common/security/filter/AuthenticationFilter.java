package com.gexingw.shop.common.security.filter;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson2.JSON;
import com.gexingw.shop.common.core.component.AuthInfo;
import com.gexingw.shop.common.core.constant.AuthConstant;
import com.gexingw.shop.common.security.component.Authentication;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

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
            String authHeader = request.getHeader(AuthConstant.HEADER_AUTH_USER);
            AuthInfo authInfo = new AuthInfo();
            if (!StringUtils.isBlank(authHeader)) {
                authInfo = JSON.parseObject(Base64.decode(authHeader), AuthInfo.class);
            }

            Authentication authentication = Authentication.builder().authInfo(authInfo).authorities(new ArrayList<>(0)).build();
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        //noinspection DataFlowIssue
        filterChain.doFilter(request, response);
    }
}
