package com.gexingw.shop.service.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/30 11:25
 */
@SpringBootApplication
@ComponentScan({"com.gexingw.shop.service.system", "com.gexingw.shop.common"})
public class ShopServiceSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopServiceSystemApplication.class);
    }

}
