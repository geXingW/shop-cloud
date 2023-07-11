package com.gexingw.shop.auth.token;

import com.gexingw.shop.auth.provider.OAuth2PasswordAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Map;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/9 13:51
 */
public class OAuth2PasswordAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    /**
     * Sub-class constructor.
     *
     * @param clientPrincipal      the authenticated client principal
     * @param additionalParameters the additional parameters
     */
    public OAuth2PasswordAuthenticationToken(Authentication clientPrincipal, Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType(OAuth2PasswordAuthenticationProvider.GRANT_TYPE_PASSWORD), clientPrincipal, additionalParameters);
    }

}
