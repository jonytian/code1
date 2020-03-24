package com.legaoyi.common.disruptor;

import java.util.List;

public class DisruptorEventBatchConsumer {

    private DisruptorMessageBatchHandler hander;

    public DisruptorEventBatchConsumer(DisruptorMessageBatchHandler hander) {
        this.hander = hander;
    }

    public void consume(final List<Object> list) throws Exception {
        hander.handle(list);
    }
}
