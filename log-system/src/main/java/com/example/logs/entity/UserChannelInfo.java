package com.example.logs.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserChannelInfo {

    private String ip;        //服务端：IP
    private int port;         //服务端：port
    private String channelId; //channelId
    private Date linkDate;    //链接时间

    public UserChannelInfo(String ip, int port, String channelId, Date linkDate) {
        this.ip = ip;
        this.port = port;
        this.channelId = channelId;
        this.linkDate = linkDate;
    }


}
