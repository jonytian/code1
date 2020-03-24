package com.legaoyi.storer.util;

public class BatchMessage {

    private String type;

    private Object message;

    public BatchMessage(String type, Object message) {
        this.type = type;
        this.message = message;
    }

    public BatchMessage(int type, Object message) {
        this.type = String.valueOf(type);
        this.message = message;
    }

    public final String getType() {
        return type;
    }

    public final void setType(String type) {
        this.type = type;
    }

    public final Object getMessage() {
        return message;
    }

    public final void setMessage(Object message) {
        this.message = message;
    }

}
