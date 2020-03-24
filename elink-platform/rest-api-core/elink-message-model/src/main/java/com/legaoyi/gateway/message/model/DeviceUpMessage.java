package com.legaoyi.gateway.message.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.legaoyi.management.model.BaseModel;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Entity(name = "deviceUpMessage")
@Table(name = "device_up_message")
@XmlRootElement
public class DeviceUpMessage extends BaseModel {

    private static final long serialVersionUID = 5300716328017161678L;

    public static final String ENTITY_NAME = "deviceUpMessage";

    /** 消息id **/
    @Column(name = "message_id")
    private String messageId;

    /** 消息流水号 **/
    @Column(name = "message_seq")
    private Integer messageSeq;

    /** 消息体 **/
    @Column(name = "message_body")
    private String messageBody;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Integer getMessageSeq() {
        return messageSeq;
    }

    public void setMessageSeq(Integer messageSeq) {
        this.messageSeq = messageSeq;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

}
