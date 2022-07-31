package com.jacky.mqtt.processors;

import org.springframework.messaging.MessageHeaders;

public abstract class AbstractMessageProcessor implements IMessageProcessor {

    private MessageHeaders messageHeaders;

    @Override
    public void processMessage(MessageHeaders headers, String topic, String payload) {
        this.messageHeaders = headers;
        this.beforeProcess();
        this.doProcess(topic, payload);
        this.afterProcess();
    }

    public abstract void doProcess(String topic, String payload);

    /**
     * 在实际处理消息前需要做的事件,子类不覆盖此方法就表示在处理消息前不需要做一些预处理
     */
    public void beforeProcess() {

    }

    /**
     * 消息处理完成后需要做的事件,子类不覆盖此方法则表示消息处理完成后不需要再做什么其他的处理了
     */
    public void afterProcess() {

    }
}
