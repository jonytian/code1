package com.legaoyi.common.disruptor;

import com.lmax.disruptor.EventHandler;

public class DisruptorEventHandler implements EventHandler<DisruptorMessage> {

    private DisruptorEventConsumer consumer;

    public DisruptorEventHandler(DisruptorEventConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void onEvent(DisruptorMessage message, long sequence, boolean endOfBatch) throws Exception {
        consumer.consume(message);
    }

}
