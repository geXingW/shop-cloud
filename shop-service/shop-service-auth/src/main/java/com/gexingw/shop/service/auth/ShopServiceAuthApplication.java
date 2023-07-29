package com.gexingw.shop.service.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/29 22:47
 */
@SpringBootApplication
@ComponentScan({"com.gexingw.shop.service.auth", "com.gexingw.shop.common"})
public class ShopServiceAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopServiceAuthApplication.class);
    }

}
