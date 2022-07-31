package com.jacky.mqtt.config;


import com.jacky.mqtt.handlers.InboundMessageHandler;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Component
@Configuration
@IntegrationComponentScan
public class MqttConfig {

    private final Logger LOGGER = LoggerFactory.getLogger(MqttConfig.class);

    private static final String MQTT_RECEIVED_TOPIC = "mqtt_receivedTopic";
    private static final String SP_INBOUND = "_inbound";
    private MqttPahoMessageDrivenChannelAdapter adapter;

    @Value(value = "${spring.mqtt.username:admin}")
    private String username;
    @Value(value = "${spring.mqtt.password:public}")
    private String password;
    @Value(value = "${spring.mqtt.host:tcp://127.0.0.1:1883}")
    private String host;
    @Value(value = "${spring.mqtt.clientId:iot-mqtt-service}")
    private String clientId;
    @Value(value = "${spring.mqtt.topic:defaultTopic}")
    private String defaultTopic;
    @Value(value = "${spring.mqtt.completionTimeout:3000}")
    private int completionTimeout;
    @Value(value = "${spring.mqtt.timeout:30}")
    private int connectTimeout;
    @Value(value = "${spring.mqtt.keepAlive:30}")
    private int keepAlive;


    /**
     * mqtt 客户端连接配置
     *
     * @return
     */
    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setServerURIs(new String[]{host});
        options.setKeepAliveInterval(keepAlive);
        options.setConnectionTimeout(connectTimeout);
        options.setCleanSession(false);
        LOGGER.info("init mqttConnectOptions ...");
        return options;
    }

    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions());
        LOGGER.info("init pahoClientFactory ...");
        return factory;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(clientId, mqttPahoClientFactory());
        handler.setAsync(true);
        handler.setDefaultTopic(defaultTopic);
        LOGGER.info("init mqttOutbound ...");
        return handler;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        adapter = new MqttPahoMessageDrivenChannelAdapter(clientId + SP_INBOUND,
                mqttPahoClientFactory(), defaultTopic);
        adapter.setCompletionTimeout(completionTimeout);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new InboundMessageHandler();
    }

    public void addListenTopic(String[] topicArr) {
        if (Objects.isNull(topicArr) || topicArr.length == 0) {
            LOGGER.info("添加的主题不能为空");
            return;
        }
        if (adapter == null) {
            adapter = new MqttPahoMessageDrivenChannelAdapter(clientId + SP_INBOUND,
                    mqttPahoClientFactory(), defaultTopic);
        }
        for (String topic : topicArr) {
            try {
                if (!Arrays.asList(adapter.getTopic()).contains(topic)) {
                    adapter.addTopic(topic, 0);
                }
            } catch (Exception e) {
                LOGGER.error("addListenTopic-添加主题失败:", e);
            }
        }
    }

    public void addListenTopic(String topic) {
        if (StringUtils.isEmpty(topic)) {
            LOGGER.info("添加的主题不能为空");
            return;
        }
        if (adapter == null) {
            adapter = new MqttPahoMessageDrivenChannelAdapter(clientId + SP_INBOUND,
                    mqttPahoClientFactory(), defaultTopic);
        }
        try {
            if (!Arrays.asList(adapter.getTopic()).contains(topic)) {
                adapter.addTopic(topic, 0);
            }
        } catch (Exception e) {
            LOGGER.error("addListenTopic-添加主题失败:", e);
        }
    }

    public void removeListenTopic(String topic) {
        if (adapter == null) {
            adapter = new MqttPahoMessageDrivenChannelAdapter(clientId + SP_INBOUND,
                    mqttPahoClientFactory(), defaultTopic);
        }
        try {
            adapter.removeTopic(topic);
        } catch (Exception e) {
            LOGGER.error("removeListenTopic-删除主题失败:", e);
        }
    }
}
