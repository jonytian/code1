package com.legaoyi.common.disruptor;

import com.lmax.disruptor.EventFactory;

public class DisruptorEventFactory implements EventFactory<DisruptorMessage> {

    @Override
    public DisruptorMessage newInstance() {
        return new DisruptorMessage();
    }

}
