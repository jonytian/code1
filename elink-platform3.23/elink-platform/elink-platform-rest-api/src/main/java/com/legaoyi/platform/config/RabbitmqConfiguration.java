package com.legaoyi.platform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.legaoyi.mq.MQMessageProducer;
import com.legaoyi.rabbitmq.RabbitmqDirectExchangeMessageProducer;
import com.legaoyi.rabbitmq.RabbitmqFanoutExchangeMessageProducer;

/**
 * @author gaoshengbo
 */
@Configuration("rabbitmqConfiguration")
public class RabbitmqConfiguration {

    @Value("${rabbitmq.common.down.message.exchange}")
    private String downMessageExchange;

    @Value("${rabbitmq.video.down.message.exchange}")
    private String videoMessageDownExchange;

    @Bean("commonDownMessageProducer")
    public MQMessageProducer commonDownMessageProducer() {
        RabbitmqDirectExchangeMessageProducer mqMessageProducer = new RabbitmqDirectExchangeMessageProducer(this.downMessageExchange);
        return mqMessageProducer;
    }

    @Bean("videoMessageDownProducer")
    public MQMessageProducer videoMessageDownProducer() {
        RabbitmqFanoutExchangeMessageProducer mqMessageProducer = new RabbitmqFanoutExchangeMessageProducer(this.videoMessageDownExchange);
        return mqMessageProducer;
    }
}
