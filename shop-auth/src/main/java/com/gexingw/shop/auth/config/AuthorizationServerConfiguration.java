package com.gexingw.shop.auth.config;

import com.gexingw.shop.auth.convert.OAuth2PasswordAuthenticationConvert;
import com.gexingw.shop.auth.provider.OAuth2PasswordAuthenticationProvider;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientCredentialsAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2RefreshTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.UUID;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/9 13:23
 */
@Configuration
@AllArgsConstructor
@AutoConfigureAfter(OAuth2AuthorizationServerConfigurer.class)
public class AuthorizationServerConfiguration {

    private static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/consent";

    PasswordEncoder passwordEncoder;

    UserDetailsService userDetailsService;

    @Bean
    @SneakyThrows
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity httpSecurity, OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<OAuth2Token> tokenGenerator
    ) {
         OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);
         OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(Customizer.withDefaults());

        // 自定义授权页面
        authorizationServerConfigurer
                .authorizationEndpoint(endpoint -> endpoint.consentPage(CUSTOM_CONSENT_PAGE_URI))
                .tokenEndpoint(endpoint -> endpoint
                        .accessTokenRequestConverter(delegatingAuthenticationConverter())
                        .authenticationProvider(new OAuth2AuthorizationCodeAuthenticationProvider(authorizationService, tokenGenerator))
                        .authenticationProvider(new OAuth2RefreshTokenAuthenticationProvider(authorizationService, tokenGenerator))
                        .authenticationProvider(new OAuth2ClientCredentialsAuthenticationProvider(authorizationService, tokenGenerator))
                        .authenticationProvider(new OAuth2PasswordAuthenticationProvider(authorizationService, tokenGenerator, userDetailsService, passwordEncoder))
                );


        httpSecurity.exceptionHandling((exceptions) -> exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")));
        httpSecurity.apply(authorizationServerConfigurer);

        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        httpSecurity.requestMatcher(endpointsMatcher)
//                .authorizeRequests(authorizeRequests ->
//                        authorizeRequests.anyRequest().authenticated()
//                )
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher));

        return httpSecurity.build();
    }

    public DelegatingAuthenticationConverter delegatingAuthenticationConverter() {
        return new DelegatingAuthenticationConverter(Arrays.asList(
                new OAuth2PasswordAuthenticationConvert(),
                new OAuth2AuthorizationCodeAuthenticationConverter(),
                new OAuth2ClientCredentialsAuthenticationConverter(),
                new OAuth2RefreshTokenAuthenticationConverter(),
                new OAuth2AuthorizationCodeRequestAuthenticationConverter()
        ));
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);

        return new ImmutableJWKSet<>(jwkSet);
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

        return keyPair;
    }

    @Bean
    public AuthorizationServerSettings providerSettings() {
        return AuthorizationServerSettings.builder().issuer("http://localhost:9001").build();
    }

    @Bean
    OAuth2TokenGenerator<OAuth2Token> oAuth2TokenGenerator(JwtEncoder jwtEncoder) {
        JwtGenerator generator = new JwtGenerator(jwtEncoder);

        return new DelegatingOAuth2TokenGenerator(generator, new OAuth2RefreshTokenGenerator());
    }

    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

}
