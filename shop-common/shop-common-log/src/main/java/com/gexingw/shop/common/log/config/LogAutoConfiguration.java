package com.gexingw.shop.common.log.config;

import com.gexingw.shop.common.log.interceptor.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * smart-logistics-saas.
 *
 * @author W.sf
 * @date 2023/7/7 23:17
 */
@Configuration
public class LogAutoConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor());
    }

}
