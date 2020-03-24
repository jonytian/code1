package com.legaoyi.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.legaoyi.platform.dwr.DwrMessagePusher;

@Component
@RabbitListener(queues = "${rabbitmq.platform.notify.queue}")
public class NotifyMessageRabbitmqListener {

    private static final Logger logger = LoggerFactory.getLogger(NotifyMessageRabbitmqListener.class);

    private static final String DEFAULT_CHARSET = "UTF-8";

    @Value("${rabbitmq.platform.notify.queue}")
    private String notifyMessageQueue;

    @Value("${rabbitmq.notify.message.exchange}")
    private String notifyMessageExchange;

    @Autowired
    private DwrMessagePusher dwrMessagePusher;

    @RabbitHandler
    public void onMessage(byte[] bytes) {
        String json = null;
        try {
            json = new String(bytes, DEFAULT_CHARSET);
            onMessage(json);
        } catch (Exception e) {
            logger.error("handle mq Message error,message={}", json, e);
        }
    }

    @RabbitHandler
    public void onMessage(String json) {
        if (logger.isInfoEnabled()) {
            logger.info(json);
        }
        dwrMessagePusher.pushMessage(json);
    }

    @Bean
    public Queue notifyMessageQueue() {
        return new Queue(notifyMessageQueue);
    }

    @Bean
    public FanoutExchange notifyMessageExchange() {
        return new FanoutExchange(notifyMessageExchange);
    }

    @Bean
    public Binding commonDownMessageQueueBinding() {
        // gatewayId作为下行消息队列路由key
        return BindingBuilder.bind(notifyMessageQueue()).to(notifyMessageExchange());
    }
}
