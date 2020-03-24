package com.legaoyi.common.disruptor;

import java.util.List;

public interface DisruptorMessageBatchHandler {

    public void handle(List<Object> list);

}
