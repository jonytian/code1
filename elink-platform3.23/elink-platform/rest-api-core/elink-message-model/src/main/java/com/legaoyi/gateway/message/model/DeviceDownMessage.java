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
@Entity(name = "deviceDownMessage")
@Table(name = "device_down_message")
@XmlRootElement
public class DeviceDownMessage extends BaseModel {

    private static final long serialVersionUID = 5300716328017161678L;

    public static final String ENTITY_NAME = "deviceDownMessage";

    /** 消息id **/
    @Column(name = "message_id")
    private String messageId;

    /** 消息流水号 **/
    @Column(name = "message_seq")
    private Integer messageSeq;

    /** 消息体 **/
    @Column(name = "message_body")
    private String messageBody;

    /** 消息状态：0：消息发送成功；1：终端不在线；2：消息不合法；3：未知错误；4：等待网关处理；5：终端已应答 **/
    @Column(name = "state")
    private Short state = 4;

    @Column(name = "create_user")
    private String createUser;

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

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

}
