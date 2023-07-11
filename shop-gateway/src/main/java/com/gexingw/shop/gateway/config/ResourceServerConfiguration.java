package com.gexingw.shop.gateway.config;

import com.gexingw.shop.gateway.component.AuthenticationConfigProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/10 11:25
 */
@Configuration
@EnableWebFluxSecurity
@AllArgsConstructor
public class ResourceServerConfiguration {

    OAuth2AuthorizationService oAuth2AuthorizationService;

    AuthenticationConfigProperties authenticationConfigProperties;

    @Bean
    SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
                .authorizeExchange(authorizeRequests -> authorizeRequests
                        // OAuth拦截认证
//                        .pathMatchers("/test/**", "/oauth2/**").permitAll()
//                        .anyExchange().authenticated()

                        // Filter 拦截认真
                        .anyExchange().permitAll()
                );
//                .oauth2ResourceServer(oauth2 ->
//                        oauth2.opaqueToken(opaqueToken -> opaqueToken.introspector(new ReactiveOpaqueTokenIntrospector(oAuth2AuthorizationService)))
//                );

        httpSecurity.csrf().disable();

        return httpSecurity.build();
    }

}
