package com.jacky.mqtt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
public @interface MqttTopicListener {

    /**
     * @return
     */
    String value() default "";

    /**
     * 监听多个主题
     *
     * @return
     */
    String[] topics() default {};

    /**
     * 指定处理消息的beanName
     *
     * @return
     */
    String processBeanName() default "";

    /**
     * 指定处理消息的方法,如果是在类上使用时可以指定方法来处理消息
     *
     * @return
     */
    String processMethod() default "";
}
