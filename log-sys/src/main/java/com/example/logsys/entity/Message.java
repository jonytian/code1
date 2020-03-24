package com.example.logsys.entity;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;

@Data
public class Message {

    private Integer id;

    /**消息代码*/
    private String code;

    /**设备编号*/
    private String imei;

    /**通道ID*/
    private String channelId;

    /**消息内容*/
    private String content;

    /**发送者*/
    private String sender;

    /**发送者*/
    private String receiver;

    /**发送时间*/
    private Date sendTime;
}
