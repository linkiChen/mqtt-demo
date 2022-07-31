package com.jacky.mqtt.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * springframework 异步线程池配置
 */
@EnableAsync
@Configuration
public class AsyncConfig {

    private final String MQTT_EXECUTOR_PREFIX = "MQTT_";

    /**
     * 线程池核心线程大小
     */
    @Value("${spring.async.corePoolSize:10}")
    private Integer corePoolSize;
    /**
     * 线程池最大线程数
     */
    @Value("${spring.async.maxPoolSize:20}")
    private Integer maxPoolSize;
    /**
     * 队列大小
     */
    @Value("${spring.async.queueCapacity:20}")
    private Integer queueCapacity;


    @Bean
    public Executor mqttExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(MQTT_EXECUTOR_PREFIX);

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
