package com.legaoyi.common.disruptor;

public class DisruptorEventConsumer {

    private DisruptorMessageHandler hander;

    public DisruptorEventConsumer(DisruptorMessageHandler hander) {
        this.hander = hander;
    }

    public void consume(DisruptorMessage message) throws Exception {
        if (hander != null) {
            hander.handle(message.getMessage());
        }
    }
}
