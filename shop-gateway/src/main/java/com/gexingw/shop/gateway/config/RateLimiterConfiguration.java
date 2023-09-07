package com.gexingw.shop.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/9/7 15:55
 */
@Slf4j
@Configuration
public class RateLimiterConfiguration {

    @Bean
    public KeyResolver remoteIpResolver() {
        return exchange -> {
            String hostAddress = Objects.requireNonNull(exchange.getRequest()
                    .getRemoteAddress()).getAddress().getHostAddress();

            log.info("Gateway rate limiter host: {}", hostAddress);
            return Mono.just(hostAddress);
        };
    }

}
