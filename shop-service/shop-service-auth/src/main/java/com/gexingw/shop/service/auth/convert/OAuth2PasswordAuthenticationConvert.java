package com.gexingw.shop.service.auth.convert;

import com.gexingw.shop.service.auth.provider.OAuth2PasswordAuthenticationProvider;
import com.gexingw.shop.service.auth.token.OAuth2PasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/9 13:51
 */
public class OAuth2PasswordAuthenticationConvert extends AbstractOAuth2AuthenticationConvert {

    @Override
    public Authentication convert(HttpServletRequest request) {
        if (!OAuth2PasswordAuthenticationProvider.GRANT_TYPE_PASSWORD.equals(request.getParameter(OAuth2ParameterNames.GRANT_TYPE))) {
            return null;
        }

        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> parameters = this.getRequestParameters(request);

        // 判断scope
//        String scope = parameters.get(OAuth2ParameterNames.SCOPE);
//        if (StringUtils.hasText(scope) &&
//                parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
//
//            OAuth2EndpointUtils.throwError(
//                    OAuth2ErrorCodes.INVALID_REQUEST,
//                    OAuth2ParameterNames.SCOPE,
//                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
//        }
//
//        HashMap<String, Object> additionParameters = new HashMap<>(parameters.size());
//        additionParameters.putAll(parameters);



        return new OAuth2PasswordAuthenticationToken(clientPrincipal, parameters);
    }

}
