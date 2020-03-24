package com.legaoyi.mq;

import java.util.Map;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-11-10
 */
public interface MQMessageProducer {

    public static final Integer MAX_PRIORITY = 10;

    public static final int DEFAULT_PRIORITY = 0;

    public void send(final Object message) throws Exception;

    public void send(final Object message, Map<String, Object> ext) throws Exception;

    public void send(String routingKey, final Object message) throws Exception;

    public void send(String routingKey, final Object message, Map<String, Object> ext) throws Exception;

    public void send(int priority, final Object message) throws Exception;

    public void send(int priority, final Object message, Map<String, Object> ext) throws Exception;

    public void send(String routingKey, int priority, final Object message) throws Exception;

    public void send(String routingKey, int priority, final Object message, Map<String, Object> ext) throws Exception;

}
