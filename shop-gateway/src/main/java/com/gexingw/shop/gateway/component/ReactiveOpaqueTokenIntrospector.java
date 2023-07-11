package com.gexingw.shop.gateway.component;

import com.gexingw.shop.common.core.util.SpringUtil;
import com.gexingw.shop.common.security.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import reactor.core.publisher.Mono;

import java.security.Principal;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/9 21:24
 */
@AllArgsConstructor
public class ReactiveOpaqueTokenIntrospector implements org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector {

    private OAuth2AuthorizationService oAuth2AuthorizationService;

    @Override
    public Mono<OAuth2AuthenticatedPrincipal> introspect(String token) {
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

        ServerHttpRequest httpRequest = SpringUtil.getBean(ServerHttpRequest.class);
        System.out.println(httpRequest);

        return Mono.just(user);
    }

}
