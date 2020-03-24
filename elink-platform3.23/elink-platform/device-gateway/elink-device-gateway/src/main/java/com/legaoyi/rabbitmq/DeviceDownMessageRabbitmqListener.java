package com.legaoyi.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import com.legaoyi.mq.MQMessageHandler;

@RabbitListener(queues = "${rabbitmq.message.down.queue}")
@Component("deviceDownMessageRabbitmqListener")
public class DeviceDownMessageRabbitmqListener {

    private static final Logger logger = LoggerFactory.getLogger(DeviceDownMessageRabbitmqListener.class);

    private static final String DEFAULT_CHARSET = "UTF-8";

    @Value("${rabbitmq.common.down.message.exchange}")
    private String downMessageExchange;

    @Value("${rabbitmq.message.down.queue}")
    private String commonDownMessageQueue;

    @Value("${elink.gateway.id}")
    private String gatewayId;

    @Autowired
    @Qualifier("platformDownMessageHandler")
    private MQMessageHandler messageHandler;

    @Value("${rabbitmq.message.durable}")
    private boolean durable = true;

    @Bean("commonDownMessageQueue")
    public Queue commonDownMessageQueue() {
        return new Queue(commonDownMessageQueue, durable);
    }

    @Bean("downMessageExchange")
    public DirectExchange downMessageExchange() {
        return new DirectExchange(downMessageExchange, durable, false);
    }

    @Bean("commonDownMessageQueueBinding")
    public Binding commonDownMessageQueueBinding() {
        // gatewayId作为下行消息队列路由key
        return BindingBuilder.bind(commonDownMessageQueue()).to(downMessageExchange()).with(gatewayId);
    }

    @RabbitHandler
    public void onMessage(byte[] bytes) {
        String json = null;
        try {
            json = new String(bytes, DEFAULT_CHARSET);
            if (logger.isInfoEnabled()) {
                logger.info(json);
            }
            messageHandler.handle(json);
        } catch (Exception e) {
            logger.error("handle mq Message error,message={}", json, e);
        }
    }

    @RabbitHandler
    public void onMessage(String json) {
        try {
            if (logger.isInfoEnabled()) {
                logger.info(json);
            }
            messageHandler.handle(json);
        } catch (Exception e) {
            logger.error("handle mq Message error,message={}", json, e);
        }
    }

}
