package com.legaoyi.rabbitmq;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.legaoyi.mq.MQMessageProducer;

@Configuration("rabbitmqConfiguration")
public class RabbitmqConfiguration {

    @Value("${rabbitmq.notify.message.exchange}")
    private String notifyMessageExchange;

    @Value("${rabbitmq.message.durable}")
    private boolean durable = true;

    @Value("${lmax.disruptor.bufferSize}")
    private int bufferSize;

    @Value("${rabbitmq.common.down.message.exchange}")
    private String downMessageExchange;

    @Bean("notifyMessageExchange")
    public FanoutExchange notifyMessageExchange() {
        return new FanoutExchange(notifyMessageExchange, durable, false);
    }

    @Bean("platformNotifyProducer")
    public MQMessageProducer initMessageProducer() {
        return new RabbitmqFanoutExchangeMessageProducer(notifyMessageExchange);
    }

    @Bean("commonDownMessageProducer")
    public MQMessageProducer commonDownMessageProducer() {
        return new RabbitmqDirectExchangeMessageProducer(downMessageExchange);
    }
}
