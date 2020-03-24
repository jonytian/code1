package com.legaoyi.common.disruptor;

public class DisruptorMessage {

    private Object message;

    public void setMessage(Object message) {
        this.message = message;
    }

    public Object getMessage() {
        return message;
    }

}
