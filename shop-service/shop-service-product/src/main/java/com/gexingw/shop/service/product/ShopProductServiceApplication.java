package com.gexingw.shop.service.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/8 15:58
 */
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan({"com.gexingw.shop.service.product", "com.gexingw.shop.common"})
public class ShopProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopProductServiceApplication.class);
    }

}
