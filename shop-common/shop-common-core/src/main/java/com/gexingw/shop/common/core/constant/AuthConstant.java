package com.gexingw.shop.common.core.constant;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/11 10:04
 */
public interface AuthConstant {

    /**
     * OAuth2的Access Token缓存
     */
    String OAUTH_TOKEN_ACCESS_TOKEN_CACHE_NAME = "cache:oauth2:access_token:%s";

    /**
     * OAuth2的Refresh Token缓存
     */
    String OAUTH_TOKEN_REFRESH_TOKEN_CACHE_NAME = "cache:oauth2:refresh_token:%s";

    /**
     * OAuth2的Authorization Code缓存
     */
    String OAUTH_TOKEN_AUTHORIZATION_CODE_CACHE_NAME = "cache:oauth2:authorization_code:%s";

    /**
     * OAuth2的State缓存
     */
    String OAUTH_TOKEN_SATE_CACHE_NAME = "cache:oauth2:state:%s";

    /**
     * OAuth2 AccessToken绑定的认证信息
     */
    String OAUTH_TOKEN_AUTH_INFO_CACHE_NAME = "oauth2:token:auth_info:%s";

}
