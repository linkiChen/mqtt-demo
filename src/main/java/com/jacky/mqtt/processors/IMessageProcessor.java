package com.jacky.mqtt.processors;

import org.springframework.messaging.MessageHeaders;

public interface IMessageProcessor {

    void processMessage(MessageHeaders headers,String topic,String payload);
}
