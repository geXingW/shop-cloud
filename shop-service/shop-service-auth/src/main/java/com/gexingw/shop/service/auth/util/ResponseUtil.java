package com.gexingw.shop.service.auth.util;

import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import com.gexingw.shop.common.core.util.R;
import com.gexingw.shop.common.core.util.RespCode;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpResponse;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/22 17:09
 */
public class ResponseUtil {

    @SneakyThrows
    public static void jsonResponse(ServletServerHttpResponse httpResponse, R<Object> r) {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        fastJsonHttpMessageConverter.write(r, MediaType.APPLICATION_JSON, httpResponse);
    }

    @SneakyThrows
    public static void jsonResponse(ServletServerHttpResponse httpResponse, RespCode respCode) {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        fastJsonHttpMessageConverter.write(R.fail(respCode), MediaType.APPLICATION_JSON, httpResponse);
    }

    @SneakyThrows
    public static void jsonResponse(ServletServerHttpResponse httpResponse, Integer respCode, String respMessage) {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        fastJsonHttpMessageConverter.write(R.fail(respCode, respMessage), MediaType.APPLICATION_JSON, httpResponse);
    }

    @SneakyThrows
    public static void jsonResponse(ServletServerHttpResponse httpResponse, String respCode, String respMessage) {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        fastJsonHttpMessageConverter.write(R.fail(Integer.parseInt(respCode), respMessage), MediaType.APPLICATION_JSON, httpResponse);
    }


}
