package com.gexingw.shop.common.redis.config;

import com.alibaba.fastjson2.support.spring.data.redis.GenericFastJsonRedisSerializer;
import com.gexingw.shop.common.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/22 22:00
 */
@AutoConfiguration(before = RedisAutoConfiguration.class)
@EnableConfigurationProperties(RedisConfigProperties.class)
public class RedisSerializeConfiguration {

    @Bean("valueSerializer")
    @ConditionalOnProperty(value = "spring.redis.serialize-type", havingValue = "json", matchIfMissing = true)
    public RedisSerializer<Object> jsonValueSerializer() {
        return new GenericFastJsonRedisSerializer();
    }

    @Bean("valueSerializer")
    @ConditionalOnProperty(value = "spring.redis.serialize-type", havingValue = "jdk")
    public RedisSerializer<Object> jdkValueSerializer() {
        return new JdkSerializationRedisSerializer();
    }

    @Bean("keySerializer")
    public RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

}
