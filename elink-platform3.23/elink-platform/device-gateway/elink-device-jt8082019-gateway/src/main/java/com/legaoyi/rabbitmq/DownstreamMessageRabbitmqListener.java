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

@RabbitListener(queues = "${rabbitmq.downstream.message.queue}")
@Component("downstreamMessageRabbitmqListener")
public class DownstreamMessageRabbitmqListener {

    private static final Logger logger = LoggerFactory.getLogger(DownstreamMessageRabbitmqListener.class);

    private static final String DEFAULT_CHARSET = "UTF-8";

    @Value("${rabbitmq.downstream.message.exchange}")
    private String downstreamMessageExchange;

    @Value("${rabbitmq.downstream.message.queue}")
    private String downstreamMessageQueue;

    @Value("${elink.gateway.id}")
    private String gatewayId;

    @Autowired
    @Qualifier("downstreamMqMessageHandler")
    private MQMessageHandler downstreamMqMessageHandler;

    @Value("${rabbitmq.message.durable}")
    private boolean durable = true;

    @Bean("downstreamMessageQueue")
    public Queue downstreamMessageQueue() {
        return new Queue(downstreamMessageQueue, durable);
    }

    @Bean("downstreamMessageExchange")
    public DirectExchange downstreamMessageExchange() {
        return new DirectExchange(downstreamMessageExchange, durable, false);
    }

    @Bean("downstreamMessageQueueBinding")
    public Binding downstreamMessageQueueBinding() {
        // gatewayId作为下行消息队列路由key
        return BindingBuilder.bind(downstreamMessageQueue()).to(downstreamMessageExchange()).with(gatewayId);
    }

    @RabbitHandler
    public void onMessage(byte[] bytes) {
        String json = null;
        try {
            json = new String(bytes, DEFAULT_CHARSET);
            if (logger.isInfoEnabled()) {
                logger.info(json);
            }
            downstreamMqMessageHandler.handle(json);
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
            downstreamMqMessageHandler.handle(json);
        } catch (Exception e) {
            logger.error("handle mq Message error,message={}", json, e);
        }
    }

}
