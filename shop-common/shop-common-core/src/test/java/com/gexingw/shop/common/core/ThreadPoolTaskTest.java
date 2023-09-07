package com.gexingw.shop.common.core;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/9/7 10:16
 */
@SpringBootTest
public class ThreadPoolTaskTest {

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Test
    public void testSubmit() {
        for (int i = 0; i < 200; i++) {
            threadPoolTaskExecutor.execute(() -> {
                try {
                    Thread.sleep(2 * 1000);
                    System.out.println(Thread.currentThread().getName() + " finished.");
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            });
        }
    }

}
