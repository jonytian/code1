package com.legaoyi.protocol.message;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 消息头
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
public class MessageHeader implements Cloneable, Serializable {

    private static final long serialVersionUID = 5951599974645691044L;

    /** 消息id **/
    @JsonProperty("messageId")
    private String messageId;

    /** 消息体长度 **/
    @JsonIgnore
    private int messageBodyLength;

    /** 加密方式 **/
    @JsonIgnore
    private short encrypt;

    /** 是否分包标志 **/
    @JsonIgnore
    private boolean isSubpackage;

    /** 保留位 **/
    @JsonIgnore
    private short undefinedBit;

    /** 终端手机号 **/
    @JsonProperty("simCode")
    private String simCode;

    /** 消息流水号 **/
    @JsonProperty("messageSeq")
    private int messageSeq;

    /** 分包总数 **/
    @JsonIgnore
    private int totalPackage;

    /** 当前分包的流水号 **/
    @JsonIgnore
    private int packageSeq;

    public final String getMessageId() {
        return messageId;
    }

    public final void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public final int getMessageBodyLength() {
        return messageBodyLength;
    }

    public final void setMessageBodyLength(int messageBodyLength) {
        this.messageBodyLength = messageBodyLength;
    }

    public final short getEncrypt() {
        return encrypt;
    }

    public final void setEncrypt(short encrypt) {
        this.encrypt = encrypt;
    }

    public final boolean getIsSubpackage() {
        return isSubpackage;
    }

    public final void setIsSubpackage(boolean isSubpackage) {
        this.isSubpackage = isSubpackage;
    }

    public final short getUndefinedBit() {
        return undefinedBit;
    }

    public final void setUndefinedBit(short undefinedBit) {
        this.undefinedBit = undefinedBit;
    }

    public final String getSimCode() {
        return simCode;
    }

    public final void setSimCode(String simCode) {
        this.simCode = simCode;
    }

    public final int getMessageSeq() {
        return messageSeq;
    }

    public final void setMessageSeq(int messageSeq) {
        this.messageSeq = messageSeq;
    }

    public final int getTotalPackage() {
        return totalPackage;
    }

    public final void setTotalPackage(int totalPackage) {
        this.totalPackage = totalPackage;
    }

    public final int getPackageSeq() {
        return packageSeq;
    }

    public final void setPackageSeq(int packageSeq) {
        this.packageSeq = packageSeq;
    }

    @Override
    public MessageHeader clone() {
        MessageHeader messageHeader = null;
        try {
            messageHeader = (MessageHeader) super.clone();
        } catch (Exception e) {
        }
        return messageHeader;
    }
}
