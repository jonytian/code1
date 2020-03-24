package com.legaoyi.file.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.legaoyi.common.disruptor.DisruptorEventConsumer;
import com.legaoyi.common.disruptor.DisruptorEventProducer;
import com.legaoyi.file.server.ServerDisruptorMessageHandler;
import com.legaoyi.file.server.ServerUpstreamMessageHandler;
import com.legaoyi.mq.MQMessageProducer;
import com.legaoyi.protocol.util.ServerRuntimeContext;
import com.legaoyi.rabbitmq.RabbitmqDirectExchangeMessageProducer;

@Configuration("rabbitmqConfiguration")
public class RabbitmqConfiguration {

    @Value("${rabbitmq.device.up.message.exchange}")
    private String upMessageExchange;

    @Value("${rabbitmq.media.up.queue}")
    private String mediaUpMessageQueue;

    @Value("${rabbitmq.message.durable}")
    private boolean durable = true;

    @Value("${lmax.disruptor.bufferSize}")
    private int bufferSize;

    @Autowired
    @Qualifier("serverRuntimeContext")
    protected ServerRuntimeContext serverRuntimeContext;

    @Autowired
    @Qualifier("serverDisruptorMessageHandler")
    private ServerDisruptorMessageHandler serverDisruptorMessageHandler;

    @Bean("mediaUpMessageQueue")
    public Queue mediaUpMessageQueue() {
        return new Queue(mediaUpMessageQueue, durable);
    }

    @Bean("upMessageExchange")
    public DirectExchange upMessageExchange() {
        return new DirectExchange(upMessageExchange, durable, false);
    }

    @Bean("commonUpMessageQueueBinding")
    public Binding commonUpMessageQueueBinding() {
        return BindingBuilder.bind(mediaUpMessageQueue()).to(upMessageExchange()).with("media");
    }

    @Bean("messageProducer")
    public MQMessageProducer initMessageProducer() {
        return new RabbitmqDirectExchangeMessageProducer(upMessageExchange);
    }

    @Bean("disruptorEventConsumer")
    public DisruptorEventConsumer initDisruptorEventConsumer() {
        return new DisruptorEventConsumer(serverDisruptorMessageHandler);
    }

    @Bean("disruptorEventProducer")
    public DisruptorEventProducer initDisruptorEventProducer() {
        return new DisruptorEventProducer(initDisruptorEventConsumer(), bufferSize);
    }

    @Bean("serverMessageHandler")
    public ServerUpstreamMessageHandler initServerMessageHandler() {
        ServerUpstreamMessageHandler serverMessageHandler = new ServerUpstreamMessageHandler();
        serverMessageHandler.setProducer(initDisruptorEventProducer());
        serverMessageHandler.setRoutingKey("media");
        return serverMessageHandler;
    }
}
