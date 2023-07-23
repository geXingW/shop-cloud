package com.gexingw.shop.auth.service;

import com.gexingw.shop.auth.entity.User;
import com.gexingw.shop.common.core.component.AuthInfo;
import com.gexingw.shop.common.core.constant.AuthConstant;
import com.gexingw.shop.common.redis.util.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.security.Principal;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;

/**
 * smart-logistics-saas.
 *
 * @author W.sf
 * @date 2023/7/18 16:24
 */
public class CustomOAuth2AuthorizationService implements OAuth2AuthorizationService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedisSerializer<Object> valueSerializer;

    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        redisTemplate.setValueSerializer(RedisSerializer.java());

        // 当前的秒数
        long curTimeSeconds = System.currentTimeMillis() / 1000;
        if (authorization.getAttribute(OAuth2ParameterNames.STATE) != null) {
            String token = authorization.getAttribute(OAuth2ParameterNames.STATE);

            redisTemplate.opsForValue().set(String.format(AuthConstant.OAUTH_TOKEN_SATE_CACHE_NAME, token), authorization, Duration.ofMinutes(10).getSeconds());
        }

        if (authorization.getToken(OAuth2AuthorizationCode.class) != null) {
            OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
            if (authorizationCode != null) {
                OAuth2AuthorizationCode authorizationCodeToken = authorizationCode.getToken();
                long expireSeconds = Optional.ofNullable(authorizationCodeToken.getExpiresAt()).orElse(Instant.now()).getEpochSecond();

                redisTemplate.opsForValue().set(String.format(AuthConstant.OAUTH_TOKEN_AUTHORIZATION_CODE_CACHE_NAME, authorizationCodeToken), authorization, getDiffSeconds(expireSeconds, curTimeSeconds));
            }
        }

        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
        if (refreshToken != null) {
            String refreshTokenValue = refreshToken.getToken().getTokenValue();
            long refreshTokenExpiresSeconds = Optional.ofNullable(refreshToken.getToken().getExpiresAt()).orElse(Instant.now()).getEpochSecond();

            redisTemplate.opsForValue().set(String.format(AuthConstant.OAUTH_TOKEN_REFRESH_TOKEN_CACHE_NAME, refreshTokenValue), authorization, getDiffSeconds(refreshTokenExpiresSeconds, curTimeSeconds));
        }

        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
        if (accessToken != null) {
            String accessTokenValue = accessToken.getToken().getTokenValue();

            long accessTokenExpireSeconds = Optional.ofNullable(accessToken.getToken().getExpiresAt()).orElse(Instant.now()).getEpochSecond();
            Duration accessTokenExpireDuration = getDiffSeconds(accessTokenExpireSeconds, curTimeSeconds);
            redisTemplate.opsForValue().set(String.format(AuthConstant.OAUTH_TOKEN_ACCESS_TOKEN_CACHE_NAME, accessTokenValue), authorization, accessTokenExpireDuration);

            this.cacheAuthInfo(authorization, accessTokenValue, accessTokenExpireDuration.getSeconds());
        }

        redisTemplate.setValueSerializer(valueSerializer);
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        if (authorization.getAttribute(OAuth2ParameterNames.STATE) != null) {
            String token = authorization.getAttribute(OAuth2ParameterNames.STATE);
            RedisUtil.del(String.format(AuthConstant.OAUTH_TOKEN_ACCESS_TOKEN_CACHE_NAME, token));
        }

        if (authorization.getToken(OAuth2AuthorizationCode.class) != null) {
            OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
            if (authorizationCode != null) {
                OAuth2AuthorizationCode authorizationCodeToken = authorizationCode.getToken();
                RedisUtil.del(String.format(AuthConstant.OAUTH_TOKEN_AUTHORIZATION_CODE_CACHE_NAME, authorizationCodeToken));
            }
        }

        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
        if (refreshToken != null) {
            RedisUtil.del(getRedisKey(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken.getToken().getTokenValue()));
            RedisUtil.del(String.format(AuthConstant.OAUTH_TOKEN_REFRESH_TOKEN_CACHE_NAME, refreshToken.getToken().getTokenValue()));
        }

        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
        if (accessToken != null) {
            RedisUtil.del(String.format(AuthConstant.OAUTH_TOKEN_ACCESS_TOKEN_CACHE_NAME, accessToken.getToken().getTokenValue()));
        }
    }

    /**
     * 将AccessToken的认证信息保存到Redis
     *
     * @param authorization 授权信息
     * @param accessToken   access_token
     * @param expireSeconds 过期秒数
     */
    private void cacheAuthInfo(OAuth2Authorization authorization, String accessToken, long expireSeconds) {
        // client_id
        String authClientId = authorization.getRegisteredClientId();
        // client绑定的Scope
        Set<String> authorizedScopes = authorization.getAuthorizedScopes();
        AuthInfo authInfo = AuthInfo.builder().clientId(authClientId).scopes(authorizedScopes).build();

        // 密码模式，缓存用户信息
        UsernamePasswordAuthenticationToken authenticationToken = authorization.getAttribute(Principal.class.getName());
        if (authenticationToken != null) {
            User user = (User) authenticationToken.getPrincipal();
            authInfo.setId(user.getId()).setUsername(user.getUsername()).setPhone(user.getPhone());
        }

        redisTemplate.setValueSerializer(valueSerializer);
        RedisUtil.set(String.format(AuthConstant.OAUTH_TOKEN_AUTH_INFO_CACHE_NAME, accessToken), authInfo, expireSeconds);
    }

    @Override
    public OAuth2Authorization findById(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
        Assert.notNull(tokenType, "tokenType cannot be empty");

        redisTemplate.setValueSerializer(RedisSerializer.java());
        Object redisObject = redisTemplate.opsForValue().get(getRedisKey(tokenType.getValue(), token));
        redisTemplate.setValueSerializer(valueSerializer);

        return (OAuth2Authorization) redisObject;
    }

    private String getRedisKey(String type, String arg) {
        return String.format("oauth2:%s:%s", type, arg);
    }

    private Duration getDiffSeconds(long expireSeconds, long currentSeconds) {
        return Duration.ofSeconds(expireSeconds - currentSeconds);
    }

}
