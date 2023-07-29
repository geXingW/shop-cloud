package com.gexingw.shop.service.auth.convert;

import org.springframework.security.web.authentication.AuthenticationConverter;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/9 14:35
 */
public abstract class AbstractOAuth2AuthenticationConvert implements AuthenticationConverter {

    public Map<String, Object> getRequestParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> parameters = new HashMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            for (String value : values) {
                parameters.put(key, value);
            }
        });

        return parameters;
    }


}
