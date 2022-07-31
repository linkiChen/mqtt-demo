package com.jacky.mqtt.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DeviceRunStateProcessor extends AbstractMessageProcessor {

    private final Logger LOGGER = LoggerFactory.getLogger(DeviceRunStateProcessor.class);

    @Override
    public void doProcess(String topic, String payload) {
        LOGGER.info("DeviceRunStateProcessor#doProcess: topic:{},message:{}", topic, payload);
    }

    @Override
    public void beforeProcess() {
        LOGGER.info("DeviceRunStateProcessor#beforeProcess ....");
    }

    @Override
    public void afterProcess() {
        LOGGER.info("DeviceRunStateProcessor#afterProcess ....");
    }
}
