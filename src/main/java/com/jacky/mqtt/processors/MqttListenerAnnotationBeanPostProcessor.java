package com.jacky.mqtt.processors;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 参考 KafkaListenerAnnotationBeanPostProcessor
 */

public class MqttListenerAnnotationBeanPostProcessor implements BeanPostProcessor {


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);


        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
