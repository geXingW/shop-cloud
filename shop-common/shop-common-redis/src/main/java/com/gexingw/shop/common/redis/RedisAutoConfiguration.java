package com.gexingw.shop.common.redis;

import com.gexingw.shop.common.redis.config.RedisSerializeConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.Resource;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/22 17:29
 */
@AutoConfiguration(after = RedisSerializeConfiguration.class)
public class RedisAutoConfiguration {

    @Resource
    private RedisSerializer<Object> valueSerializer;

    @Resource
    private RedisSerializer<String> keySerializer;

    @Bean
    @Primary
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);

        return redisTemplate;
    }

}
