package com.gexingw.shop.common.openfeign.config;

import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import com.gexingw.shop.common.core.constant.LogConstant;
import com.gexingw.shop.common.core.constant.RequestConstant;
import feign.RequestInterceptor;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/9/7 17:47
 */
public class OpenFeignAutoConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes == null) {
                return;
            }

            HttpServletRequest request = requestAttributes.getRequest();
            requestTemplate.header(RequestConstant.HEADER_AUTH_USER, request.getHeader(RequestConstant.HEADER_AUTH_USER));
            // 增加traceId
            requestTemplate.header(LogConstant.HEADER_TRACE_ID, request.getHeader(LogConstant.HEADER_TRACE_ID));
            // 内部调用增加RPC标记
            requestTemplate.header(RequestConstant.HEADER_REQUEST_TYPE, RequestConstant.HEADER_REQUEST_TYPE_RPC);
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public ConnectionPool httpClientConnectionPool() {
        return new ConnectionPool(5, 5, TimeUnit.MINUTES);
    }

    @Bean
    @ConditionalOnMissingBean
    OkHttpClient okHttpClient(ConnectionPool connectionPool) {
        return new OkHttpClient.Builder()
                .connectionPool(connectionPool)
                .connectTimeout(Duration.ofSeconds(60))
                .readTimeout(Duration.ofSeconds(60))
                .writeTimeout(Duration.ofSeconds(60))
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder, OkHttpClient okHttpClient) {
        RestTemplate restTemplate = restTemplateBuilder.requestFactory(() -> new OkHttp3ClientHttpRequestFactory(okHttpClient)).build();
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        messageConverters.add(new FastJsonHttpMessageConverter());

        return restTemplate;
    }

}
