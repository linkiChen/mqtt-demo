package com.jacky.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Hello world!
 */
@SpringBootApplication
public class App {

    private final static Logger LOGGER = LoggerFactory.getLogger(App.class);
    private final static String CONTENT_PATH = "server.servlet.context-path";

    private final static String SERVER_PORT = "server.port";

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext ctx = SpringApplication.run(App.class, args);
        ConfigurableEnvironment env = ctx.getEnvironment();
        String serverPort = env.getProperty(SERVER_PORT);
        String contentPath = env.getProperty(CONTENT_PATH);
        String host = InetAddress.getLocalHost().getHostAddress();

        LOGGER.info("服务启动成功:{}:{}", host, serverPort);
        LOGGER.info("content path:{}", contentPath);
    }
}
