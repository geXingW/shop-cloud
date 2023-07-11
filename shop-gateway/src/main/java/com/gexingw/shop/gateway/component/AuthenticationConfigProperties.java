package com.gexingw.shop.gateway.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/11 10:20
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties("shop.auth")
public class AuthenticationConfigProperties {

    private final List<String> skipUrl = new ArrayList<>();

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    public boolean shouldNotFilter(String path) {
        for (String item : skipUrl) {
            if (ANT_PATH_MATCHER.match(item, path)) {
                return true;
            }
        }

        return false;
    }

}
