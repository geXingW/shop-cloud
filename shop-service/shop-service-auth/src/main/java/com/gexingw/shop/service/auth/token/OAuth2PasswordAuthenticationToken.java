package com.gexingw.shop.service.auth.token;

import com.gexingw.shop.service.auth.provider.OAuth2PasswdCaptchaAuthenticationProvider;
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

    private static final long serialVersionUID = 2234557139388874078L;

    /**
     * Sub-class constructor.
     *
     * @param clientPrincipal      the authenticated client principal
     * @param additionalParameters the additional parameters
     */
    public OAuth2PasswordAuthenticationToken(Authentication clientPrincipal, Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType(OAuth2PasswdCaptchaAuthenticationProvider.GRANT_TYPE_PASSWORD_CAPTCHA), clientPrincipal, additionalParameters);
    }

}
