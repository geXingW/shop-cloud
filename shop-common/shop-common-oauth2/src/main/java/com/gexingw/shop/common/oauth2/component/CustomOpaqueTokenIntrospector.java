package com.gexingw.shop.common.oauth2.component;

import com.gexingw.shop.common.security.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;

import java.security.Principal;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/9 21:24
 */
@AllArgsConstructor
public class CustomOpaqueTokenIntrospector implements org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector {

    private OAuth2AuthorizationService oAuth2AuthorizationService;

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        System.out.println(oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN));
        OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        System.out.println(oAuth2Authorization);

        if (oAuth2Authorization == null) {
            throw new RuntimeException("Token不存在！");
        }

        UsernamePasswordAuthenticationToken principal = oAuth2Authorization.getAttribute(Principal.class.getName());
        if (principal == null) {
            throw new RuntimeException("未找到Principal");
        }

        return (User) principal.getPrincipal();
    }

}
