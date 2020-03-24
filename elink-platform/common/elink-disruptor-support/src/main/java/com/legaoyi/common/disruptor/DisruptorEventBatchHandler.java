package com.legaoyi.common.disruptor;

import java.util.ArrayList;
import java.util.List;

import com.lmax.disruptor.EventHandler;

public class DisruptorEventBatchHandler implements EventHandler<DisruptorMessage> {

    private DisruptorEventBatchConsumer consumer;

    private static final int MAX_BATCH_SIZE = 300;

    private List<Object> batch = new ArrayList<Object>();

    public DisruptorEventBatchHandler(DisruptorEventBatchConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void onEvent(DisruptorMessage message, long sequence, boolean endOfBatch) throws Exception {
        batch.add(message.getMessage());
        if (endOfBatch || batch.size() >= MAX_BATCH_SIZE){
            processBatch(batch);
        }
    }
 
    private void processBatch(List<Object> batch) throws Exception{
        consumer.consume(batch);
        batch.clear();
    }

}
