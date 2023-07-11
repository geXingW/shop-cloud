package com.gexingw.shop.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/9 11:56
 */
@SpringBootApplication
@ComponentScan({"com.gexingw.shop.auth", "com.gexingw.shop.common"})
public class ShopAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopAuthApplication.class);
    }

}
