package com.example.logs.entity;

import lombok.Data;

@Data
public class MsgAgreement {

    private String toChannelId; //发送给某人，某人channelId
    private String content;     //消息内容

    public MsgAgreement() {
    }

    public MsgAgreement(String toChannelId, String content) {
        this.toChannelId = toChannelId;
        this.content = content;
    }


    
}
