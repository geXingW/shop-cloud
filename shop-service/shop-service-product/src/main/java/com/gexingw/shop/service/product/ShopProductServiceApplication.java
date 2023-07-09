package com.gexingw.shop.service.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/8 15:58
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ShopProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopProductServiceApplication.class);
    }

}
