package com.gexingw.shop.service.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/8 16:00
 */
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan({"com.gexingw.shop.common", "com.gexingw.shop.service.user"})
public class ShopUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopUserServiceApplication.class);
    }

}
