package com.gexingw.shop.auth.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.security.Principal;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/9 13:53
 */
public abstract class AbstractOAuth2AuthenticationProvider implements AuthenticationProvider {

    protected final OAuth2AuthorizationService oAuth2AuthorizationService;

    protected final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    protected final UserDetailsService userDetailsService;

    protected final PasswordEncoder passwordEncoder;

    public AbstractOAuth2AuthenticationProvider(OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.oAuth2AuthorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2AuthorizationGrantAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2ClientAuthenticationToken principal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        RegisteredClient registeredClient = principal.getRegisteredClient();
        if (registeredClient == null) {
            throw new RuntimeException("客户端信息错误！");
        }

        Authentication authenticatedInfo = this.getAuthenticatedInfo(authentication);

        // AccessToken
        DefaultOAuth2TokenContext accessTokenContext = DefaultOAuth2TokenContext.builder().registeredClient(registeredClient)
                .principal(authenticatedInfo).tokenType(OAuth2TokenType.ACCESS_TOKEN).authorizedScopes(registeredClient.getScopes())
                .authorizationGrantType(new AuthorizationGrantType(this.getGrantType())).build();
        OAuth2Token accessToken = tokenGenerator.generate(accessTokenContext);
        if (accessToken == null) {
            throw new RuntimeException("AccessToken生成失败!");
        }

        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessToken.getTokenValue(), accessToken.getIssuedAt(), accessToken.getExpiresAt(), accessTokenContext.getAuthorizedScopes());

        // 生成refreshToken
        DefaultOAuth2TokenContext refreshTokenContext = DefaultOAuth2TokenContext.builder().registeredClient(registeredClient)
                .tokenType(OAuth2TokenType.REFRESH_TOKEN).authorizationGrantType(new AuthorizationGrantType(this.getGrantType()))
                .build();
        OAuth2Token refreshToken = tokenGenerator.generate(refreshTokenContext);
        if (refreshToken == null) {
            throw new RuntimeException("RefreshToken生成失败!");
        }
        OAuth2RefreshToken oAuth2RefreshToken = (OAuth2RefreshToken) refreshToken;

        oAuth2AuthorizationService.save(
                OAuth2Authorization.withRegisteredClient(registeredClient)
                        .principalName(authentication.getName()).attribute(Principal.class.getName(), authenticatedInfo)
                        .authorizationGrantType(new AuthorizationGrantType(this.getGrantType()))
                        .accessToken(oAuth2AccessToken).refreshToken(oAuth2RefreshToken).build()
        );

        SecurityContextHolder.clearContext();

        return new OAuth2AccessTokenAuthenticationToken(registeredClient, principal, oAuth2AccessToken, oAuth2RefreshToken);
    }

    protected abstract Authentication getAuthenticatedInfo(Authentication authentication);

    protected abstract String getGrantType();


}
