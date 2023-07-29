package com.gexingw.shop.auth.convert;

import com.gexingw.shop.auth.provider.OAuth2PasswdCaptchaAuthenticationProvider;
import com.gexingw.shop.auth.token.OAuth2PasswdCaptchaAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/23 18:30
 */
public class OAuth2PasswdAuthenticationConvert extends AbstractOAuth2AuthenticationConvert{

    @Override
    public Authentication convert(HttpServletRequest request) {
        if (!OAuth2PasswdCaptchaAuthenticationProvider.GRANT_TYPE_PASSWORD_CAPTCHA.equals(request.getParameter(OAuth2ParameterNames.GRANT_TYPE))) {
            return null;
        }

        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> parameters = this.getRequestParameters(request);

        return new OAuth2PasswdCaptchaAuthenticationToken(clientPrincipal, parameters);
    }

}
