package com.legaoyi.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import com.legaoyi.common.util.JsonUtil;
import com.legaoyi.mq.MQMessageProducer;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2017-07-25
 */
public class RabbitmqFanoutExchangeMessageProducer implements MQMessageProducer {

    private static final String DEFAULT_CHARSET = "UTF-8";

    private static final String EXT_KEY_EXCHANGE = "exchange";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private String exchange = "legaoyi.elink.fanout.exchange";

    public RabbitmqFanoutExchangeMessageProducer() {}

    public RabbitmqFanoutExchangeMessageProducer(String exchange) {
        this.exchange = exchange;
    }

    @Override
    public void send(final Object message) throws Exception {
        this.send(null, DEFAULT_PRIORITY, message);
    }

    @Override
    public void send(String routingKey, final Object message) throws Exception {
        this.send(routingKey, DEFAULT_PRIORITY, message);
    }

    @Override
    public void send(int priority, final Object message) throws Exception {
        this.send(null, priority, message);
    }

    @Override
    public void send(String routingKey, int priority, final Object message) throws Exception {
        send(exchange, routingKey, priority, message);
    }

    @Override
    public void send(Object message, Map<String, Object> ext) throws Exception {
        String exchange = null;
        if (ext != null) {
            exchange = (String) ext.get(EXT_KEY_EXCHANGE);
        }
        send(exchange, null, DEFAULT_PRIORITY, message);
    }

    @Override
    public void send(String routingKey, Object message, Map<String, Object> ext) throws Exception {
        String exchange = null;
        if (ext != null) {
            exchange = (String) ext.get(EXT_KEY_EXCHANGE);
        }
        send(exchange, routingKey, DEFAULT_PRIORITY, message);
    }

    @Override
    public void send(int priority, Object message, Map<String, Object> ext) throws Exception {
        String exchange = null;
        if (ext != null) {
            exchange = (String) ext.get(EXT_KEY_EXCHANGE);
        }
        send(exchange, null, priority, message);
    }

    @Override
    public void send(String routingKey, int priority, Object message, Map<String, Object> ext) throws Exception {
        String exchange = null;
        if (ext != null) {
            exchange = (String) ext.get(EXT_KEY_EXCHANGE);
        }
        send(exchange, routingKey, priority, message);
    }

    private void send(String exchange, String routingKey, int priority, Object message) throws Exception {
        if (priority > MAX_PRIORITY) {
            priority = MAX_PRIORITY;
        }
        if (priority < 0) {
            priority = MessageProperties.DEFAULT_PRIORITY;
        }
        if (exchange == null) {
            exchange = this.exchange;
        }
        MessageProperties messageProperties = MessagePropertiesBuilder.newInstance().build();
        messageProperties.setPriority(priority);
        byte[] bytes;
        if (message instanceof String) {
            bytes = ((String) message).getBytes(DEFAULT_CHARSET);
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        } else if (message instanceof byte[]) {
            bytes = (byte[]) message;
        } else {
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            String json = JsonUtil.covertObjectToStringWithoutEmpty(message);
            bytes = json.getBytes(DEFAULT_CHARSET);
        }
        rabbitTemplate.send(exchange, null, new Message(bytes, messageProperties));
    }

}
