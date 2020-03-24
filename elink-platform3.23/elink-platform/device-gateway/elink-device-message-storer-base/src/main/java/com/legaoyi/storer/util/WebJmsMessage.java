package com.legaoyi.storer.util;

import java.io.Serializable;

import com.legaoyi.common.util.JsonUtil;

public class WebJmsMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final short ONLINE_MESSAGE_TYPE = 1;

    public static final short ALARM_MESSAGE_TYPE = 2;

    public static final short JTT809_MESSAGE_TYPE = 3;

    public static final short JTT808_GATEWAY_RESTART = 4;

    public static final short DATA_LIMIT_ALARM_MESSAGE_TYPE = 5;

    public static final short BIZSTATE_MESSAGE_TYPE = 6;

    public static final short OTHER_ALARM_MESSAGE_TYPE = 7;

    public static final short VIDEO_ALARM_MESSAGE_TYPE = 8;

    public static final short MEDIA_MESSAGE_TYPE = 9;

    public static final short FILE_UPLOAD_MESSAGE_TYPE = 10;

    public static final short DOWNSTREAM_RESP_MESSAGE_TYPE = 11;

    public static final short DOWNSTREAM_ERROR_MESSAGE_TYPE = 12;

    private short type;

    private Object message;

    public WebJmsMessage(short type, Object message) {
        this.type = type;
        this.message = message;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String toString() {
        return JsonUtil.covertObjectToStringWithoutNull(this);
    }

}
