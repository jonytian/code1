package com.example.logs.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ServerInfo {

    private String ip;    //IP
    private int port;     //端口
    private Date openDate;//启动时间

    public ServerInfo(String ip, int port, Date openDate) {
        this.ip = ip;
        this.port = port;
        this.openDate = openDate;
    }


}
