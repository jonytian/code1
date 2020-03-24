package com.legaoyi.protocol.message;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.common.util.JsonUtil;

/**
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
public class Message implements Cloneable, Serializable {

    private static final long serialVersionUID = -8356596029069390414L;

    public static final byte FLAG = 0x7e;

    public static final byte ESCAPE_CHARACTER_0X7D = 0x7d;

    public static final byte ESCAPE_CHARACTER_0X01 = 0x01;

    public static final byte ESCAPE_CHARACTER_0X02 = 0x02;

    public static final int MAX_MESSAGE_LENGTH = 1043;

    public static final String DEFULT_PROTOCOL_VERSION = "2011";

    /** 消息头 **/
    @JsonProperty("messageHeader")
    private MessageHeader messageHeader;

    /** 消息体 **/
    @JsonProperty("messageBody")
    private MessageBody messageBody;

    /** 消息长度 **/
    @JsonProperty("length")
    private int length;

    public void setMessageHeader(MessageHeader messageHeader) {
        this.messageHeader = messageHeader;
    }

    public MessageHeader getMessageHeader() {
        return messageHeader;
    }

    public void setMessageBody(MessageBody messageBody) {
        this.messageBody = messageBody;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public Message clone() {
        Message jtt = null;
        try {
            jtt = (Message) super.clone();
            if (messageHeader != null) {
                jtt.setMessageHeader(messageHeader.clone());
            }

            if (messageBody != null) {
                jtt.setMessageBody(messageBody.clone());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jtt;
    }

    @Override
    public String toString() {
        return JsonUtil.covertObjectToString(this);
    }

}
