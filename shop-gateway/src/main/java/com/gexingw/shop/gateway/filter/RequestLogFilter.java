package com.gexingw.shop.gateway.filter;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/9/7 15:13
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class RequestLogFilter implements GlobalFilter, Ordered {

    @SuppressWarnings("ReactiveStreamsUnusedPublisher")
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String traceId = IdUtil.simpleUUID();
        // 请求开始时间
        long reqStartTime = System.currentTimeMillis();
        // 请求ID
        String host = exchange.getRequest().getURI().getHost();
        String path = exchange.getRequest().getURI().getPath();
        String method = request.getMethodValue();
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String query = exchange.getRequest().getQueryParams().toString();

        // 写入请求Header
        request.mutate().header("traceId", traceId);
        // 记录带Body参数的日志
        if (Arrays.asList("POST", "PUT", "DELETE").contains(method) && request.getHeaders().getContentLength() > 0) {
            return DataBufferUtils.join(exchange.getRequest().getBody())
                    .flatMap(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        String bodyString = new String(bytes, StandardCharsets.UTF_8);

                        logRequest(traceId, host, path, method, headers, query, bodyString);
                        exchange.getAttributes().put("POST_BODY", bodyString);
                        DataBufferUtils.release(dataBuffer);
                        Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                            return Mono.just(buffer);
                        });

                        ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(request) {
                            @Override
                            @SuppressWarnings("NullableProblems")
                            public Flux<DataBuffer> getBody() {
                                return cachedFlux;
                            }
                        };

                        return chain.filter(exchange.mutate().request(mutatedRequest).build()).then(logResponse(exchange, traceId, reqStartTime));
                    });
        } else {
            logRequest(traceId, host, path, method, headers, query, "");
        }

        // 记录响应信息
        return chain.filter(exchange).then(logResponse(exchange, traceId, reqStartTime));
    }

    private static void logRequest(String traceId, String host, String path, String method, HttpHeaders headers, String query, String bodyString) {
        if (StringUtils.isNotBlank(bodyString)) {
            bodyString = bodyString.replace("\n", "").replace("\r", "").replace(" ", "");
        }

        MDC.put("traceId", traceId);
        // 记录进入请求
        log.info("Request start: TraceId:{} Host:{} Path:{} Method:{} Headers:{} Query:{} Body:{}", traceId, host, path, method, headers, query, bodyString);
        MDC.remove("traceId");
    }

    private static Mono<Void> logResponse(ServerWebExchange exchange, String traceId, long reqStartTime) {
        return Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();

            HttpStatus statusCode = Optional.ofNullable(response.getStatusCode()).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
            long reqEndTime = System.currentTimeMillis();

            // 记录结束请求
            MDC.put("traceId", traceId);
            log.info("Request end: TraceId:{} Status:{} Cost:{}", traceId, statusCode.value(), reqEndTime - reqStartTime);
            MDC.remove("traceId");
        });
    }

    @Override
    public int getOrder() {
        return -101;
    }

}
