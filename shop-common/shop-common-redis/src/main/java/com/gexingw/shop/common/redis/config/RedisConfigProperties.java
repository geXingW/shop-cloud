package com.gexingw.shop.common.redis.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/22 21:55
 */
@Getter
@Setter
@ConfigurationProperties("spring.redis")
public class RedisConfigProperties {

    private SerializeTypeEnum serializeType = SerializeTypeEnum.JSON;

    public enum SerializeTypeEnum {
        /**
         * Json序列化
         */
        JSON,

        /**
         * JDK序列化
         */
        JDK,
    }

}
