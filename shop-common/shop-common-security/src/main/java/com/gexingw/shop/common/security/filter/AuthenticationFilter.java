package com.gexingw.shop.common.security.filter;

import com.gexingw.shop.common.core.constant.AuthConstant;
import com.gexingw.shop.common.security.component.AuthInfo;
import com.gexingw.shop.common.security.component.Authentication;
import lombok.SneakyThrows;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
            Long userId = Long.valueOf(Optional.ofNullable(request.getHeader(AuthConstant.HEADER_USER_ID)).orElse("-1"));
            String username = Optional.ofNullable(request.getHeader(AuthConstant.HEADER_USERNAME)).orElse("");
            String phone = Optional.ofNullable(request.getHeader(AuthConstant.HEADER_USER_PHONE)).orElse("");

            // 获取用户权限
            List<GrantedAuthority> authorities = new ArrayList<>(Arrays.asList(
                    new SimpleGrantedAuthority("ROLE_1"), new SimpleGrantedAuthority("ROLE_2")
            ));


            AuthInfo authInfo = AuthInfo.builder().id(userId).username(username).phone(phone).build();
            Authentication authentication = Authentication.builder().authInfo(authInfo).authorities(authorities).build();
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        //noinspection DataFlowIssue
        filterChain.doFilter(request, response);
    }

}
