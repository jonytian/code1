package com.legaoyi.rabbitmq;

import java.util.ArrayList;
import java.util.List;

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
import com.legaoyi.gateway.message.handler.ExchangeMessageHandler;
import com.legaoyi.mq.MQMessageHandler;
import com.legaoyi.mq.MQMessageProducer;
import com.legaoyi.protocol.server.ServerDisruptorMessageHandler;
import com.legaoyi.protocol.server.ServerUpstreamMessageHandler;
import com.legaoyi.protocol.util.ServerRuntimeContext;

@Configuration("rabbitmqConfiguration")
public class RabbitmqConfiguration {

    @Value("${rabbitmq.upstream.message.exchange}")
    private String upstreamMessageExchange;

    @Value("${rabbitmq.upstream.common.queue}")
    private String upstreamCommonQueue;

    @Value("${rabbitmq.upstream.urgent.queue}")
    private String upstreamUrgentQueue;

    @Value("${rabbitmq.upstream.media.queue}")
    private String upstreamMediaQueue;

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

    @Bean("upstreamCommonQueue")
    public Queue upstreamCommonQueue() {
        return new Queue(upstreamCommonQueue, durable);
    }

    @Bean("upstreamUrgentQueue")
    public Queue upstreamUrgentQueue() {
        return new Queue(upstreamUrgentQueue, durable);
    }

    @Bean("upstreamMediaQueue")
    public Queue upstreamMediaQueue() {
        return new Queue(upstreamMediaQueue, durable);
    }

    @Bean("upstreamMessageExchange")
    public DirectExchange upstreamMessageExchange() {
        return new DirectExchange(upstreamMessageExchange, durable, false);
    }

    @Bean("upstreamCommonQueueBinding")
    public Binding upstreamCommonQueueBinding() {
        return BindingBuilder.bind(upstreamCommonQueue()).to(upstreamMessageExchange()).with("common");
    }

    @Bean("upstreamUrgentQueueBinding")
    public Binding upstreamUrgentQueueBinding() {
        return BindingBuilder.bind(upstreamUrgentQueue()).to(upstreamMessageExchange()).with("urgent");
    }

    @Bean("upstreamMediaQueueBinding")
    public Binding upstreamMediaQueueBinding() {
        return BindingBuilder.bind(upstreamMediaQueue()).to(upstreamMessageExchange()).with("media");
    }

    @Bean("messageProducer")
    public MQMessageProducer initMessageProducer() {
        return new RabbitmqDirectExchangeMessageProducer(upstreamMessageExchange);
    }

    @Bean("disruptorEventConsumer")
    public DisruptorEventConsumer disruptorEventConsumer() {
        return new DisruptorEventConsumer(serverDisruptorMessageHandler);
    }

    @Bean("disruptorEventProducer")
    public DisruptorEventProducer disruptorEventProducer() {
        return new DisruptorEventProducer(disruptorEventConsumer(), bufferSize);
    }

    @Bean("commonUpstreamMessageHandler")
    public ServerUpstreamMessageHandler commonUpstreamMessageHandler() {
        ServerUpstreamMessageHandler serverUpstreamMessageHandler = new ServerUpstreamMessageHandler();
        serverUpstreamMessageHandler.setProducer(disruptorEventProducer());
        serverUpstreamMessageHandler.setRoutingKey("common");
        return serverUpstreamMessageHandler;
    }

    @Bean("urgentUpstreamMessageHandler")
    public ServerUpstreamMessageHandler urgentUpstreamMessageHandler() {
        ServerUpstreamMessageHandler serverUpstreamMessageHandler = new ServerUpstreamMessageHandler();
        serverUpstreamMessageHandler.setProducer(disruptorEventProducer());
        serverUpstreamMessageHandler.setRoutingKey("urgent");
        return serverUpstreamMessageHandler;
    }

    @Bean("mediaUpstreamMessageHandler")
    public ServerUpstreamMessageHandler mediaUpstreamMessageHandler() {
        ServerUpstreamMessageHandler serverUpstreamMessageHandler = new ServerUpstreamMessageHandler();
        serverUpstreamMessageHandler.setProducer(disruptorEventProducer());
        serverUpstreamMessageHandler.setRoutingKey("media");
        return serverUpstreamMessageHandler;
    }

    @Bean("downstreamMqMessageHandler")
    public MQMessageHandler downstreamMqMessageHandler() {
        DownstreamMqMessageHandler downstreamMqMessageHandler = new DownstreamMqMessageHandler();
        List<ExchangeMessageHandler> messageHandleList = new ArrayList<ExchangeMessageHandler>();
        messageHandleList.add(ServerRuntimeContext.getBean("downstreamMessageHandler", ExchangeMessageHandler.class));
        messageHandleList.add(ServerRuntimeContext.getBean("authResponseMessageHandler", ExchangeMessageHandler.class));
        messageHandleList.add(ServerRuntimeContext.getBean("deviceLogoffMessageHandler", ExchangeMessageHandler.class));
        messageHandleList.add(ServerRuntimeContext.getBean("removeBlackListMessageHandler", ExchangeMessageHandler.class));
        downstreamMqMessageHandler.setMessageHandleList(messageHandleList);
        downstreamMqMessageHandler.setUrgentMessageHandler(urgentUpstreamMessageHandler());
        return downstreamMqMessageHandler;
    }
}
