package com.gexingw.shop.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/8 16:19
 */
@EnableWebFlux
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan({"com.gexingw.shop.gateway", "com.gexingw.shop.common"})
public class ShopGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopGatewayApplication.class);
    }

}
