package com.jacky.mqtt.handlers;

import com.jacky.mqtt.processors.IMessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 消息接收处理器
 */
@Component
public class InboundMessageHandler implements MessageHandler {

    private Logger LOGGER = LoggerFactory.getLogger(InboundMessageHandler.class);

    private static final String MQTT_RECEIVED_TOPIC = "mqtt_receivedTopic";

    @Resource
    private IMessageProcessor messageProcessor;

    @Async(value = "mqttExecutor")
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        final String topic = message.getHeaders().get(MQTT_RECEIVED_TOPIC).toString();
        String payload = String.valueOf(message.getPayload());
        LOGGER.info("主题:{} 接收到数据:{}", topic, payload);
        messageProcessor.processMessage(message.getHeaders(), topic, payload);
    }
}
