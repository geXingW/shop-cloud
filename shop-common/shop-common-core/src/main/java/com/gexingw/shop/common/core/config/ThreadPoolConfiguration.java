package com.gexingw.shop.common.core.config;

import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/9/7 9:41
 */
@Configuration
public class ThreadPoolConfiguration {

    @Resource
    private TaskExecutionProperties executionProperties;

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        int processorCnt = Runtime.getRuntime().availableProcessors();
        TaskExecutionProperties.Pool poolProperties = executionProperties.getPool();
        TaskExecutionProperties.Shutdown shutdownProperties = executionProperties.getShutdown();

        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(processorCnt);
        threadPoolTaskExecutor.setMaxPoolSize(Math.min(Math.max(poolProperties.getMaxSize(), processorCnt), processorCnt * 2));
        threadPoolTaskExecutor.setThreadNamePrefix(executionProperties.getThreadNamePrefix());
        threadPoolTaskExecutor.setQueueCapacity(Math.min(poolProperties.getQueueCapacity(), 500));
        // 超过60秒未使用的线程，自动回收
        threadPoolTaskExecutor.setKeepAliveSeconds((int) Math.max(poolProperties.getKeepAlive().getSeconds(), 120));
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待任务完成再关闭线程池
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 等待任务完成的时间，超过时间强制关闭
        int terminalSeconds = shutdownProperties.getAwaitTerminationPeriod() != null ? (int) shutdownProperties.getAwaitTerminationPeriod().getSeconds() : 60;
        threadPoolTaskExecutor.setAwaitTerminationSeconds(terminalSeconds);

        return threadPoolTaskExecutor;
    }

}
