package com.gexingw.shop.service.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/8 15:58
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.gexingw.shop.api.*.feign"})
@ComponentScan({"com.gexingw.shop.service", "com.gexingw.shop.common", "com.gexingw.shop.api"})
public class ShopProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopProductServiceApplication.class);
    }

}
