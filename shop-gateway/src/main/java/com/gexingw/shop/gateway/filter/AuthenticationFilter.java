package com.gexingw.shop.gateway.filter;

import com.gexingw.shop.common.security.entity.AuthConstant;
import com.gexingw.shop.common.security.entity.User;
import com.gexingw.shop.gateway.component.AuthenticationConfigProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Principal;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/10 21:31
 */
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final static String HEADER_TOKEN = "Authorization";

    private final static String QUERY_TOKEN = "access_token";

    private final static String TOKEN_TYPE = "Bearer";

    private final AuthenticationConfigProperties authenticationConfigProperties;


    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 不需要认证的地址
        if (authenticationConfigProperties.shouldNotFilter(request.getURI().getPath())) {
            return chain.filter(exchange);
        }

        String token = this.getToken(request);
        if (StringUtils.isBlank(token)) {
            throw new RuntimeException("请先登录！");
        }

        OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        if (oAuth2Authorization == null) {
            throw new RuntimeException("Token不存在！");
        }

        UsernamePasswordAuthenticationToken principal = oAuth2Authorization.getAttribute(Principal.class.getName());
        if (principal == null) {
            throw new RuntimeException("未找到Principal");
        }
        ReactiveSecurityContextHolder.withAuthentication(principal);
        User user = (User) principal.getPrincipal();
        request.mutate().header(AuthConstant.HEADER_USER_ID, user.getId().toString());

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    public String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(HEADER_TOKEN);
        if (StringUtils.isBlank(token)) {
            token = request.getQueryParams().getFirst(QUERY_TOKEN);
        }

        if (StringUtils.isBlank(token)) {
            return "";
        }

        if (StringUtils.startsWith(token, TOKEN_TYPE)) {
            token = token.substring(7);
        }

        return token;
    }

}
