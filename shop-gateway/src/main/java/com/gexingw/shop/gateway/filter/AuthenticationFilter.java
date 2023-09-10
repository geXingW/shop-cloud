package com.gexingw.shop.gateway.filter;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson2.JSON;
import com.gexingw.shop.common.core.component.AuthInfo;
import com.gexingw.shop.common.core.constant.AuthConstant;
import com.gexingw.shop.common.core.constant.RequestConstant;
import com.gexingw.shop.common.core.constant.TokenConstant;
import com.gexingw.shop.common.core.util.R;
import com.gexingw.shop.common.core.util.RespCode;
import com.gexingw.shop.common.redis.util.RedisUtil;
import com.gexingw.shop.gateway.component.AuthenticationConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/10 21:31
 */
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final AuthenticationConfigProperties authenticationConfigProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 不需要认证的地址
        if (authenticationConfigProperties.shouldNotFilter(request.getURI().getPath())) {
            return chain.filter(exchange);
        }

        // 获取请求投中的token信息
        String authToken = getToken(request.getHeaders().getFirst("Authorization"));

        // 获取认证信息
        if (!RedisUtil.hasKey(String.format(AuthConstant.OAUTH_TOKEN_ACCESS_TOKEN_CACHE_NAME, authToken))) {
            return this.handlerResponse(RespCode.UN_AUTHORIZATION, exchange.getResponse());
        }

        AuthInfo authInfo = RedisUtil.get(String.format(AuthConstant.OAUTH_TOKEN_AUTH_INFO_CACHE_NAME, authToken));
        if (authInfo == null) {
            return this.handlerResponse(RespCode.UN_AUTHORIZATION, exchange.getResponse());
        }
        request.mutate().header(RequestConstant.HEADER_AUTH_USER, Base64.encode(JSON.toJSONString(authInfo)));

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    public String getToken(String authToken) {
        int typeLength = TokenConstant.TYPE_BEARER.length();
        if (authToken != null && authToken.length() > typeLength) {
            String headStr = authToken.substring(0, typeLength).toLowerCase();
            if (headStr.compareTo(TokenConstant.TYPE_BEARER) == 0) {
                authToken = authToken.substring(typeLength + 1);
            }

            return authToken;
        } else {
            return null;
        }
    }

    @SuppressWarnings("SameParameterValue")
    private Mono<Void> handlerResponse(RespCode respCode, ServerHttpResponse response) {
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        DataBuffer buffer = response.bufferFactory().wrap(JSON.toJSONString(R.fail(respCode)).getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Flux.just(buffer));
    }

}
