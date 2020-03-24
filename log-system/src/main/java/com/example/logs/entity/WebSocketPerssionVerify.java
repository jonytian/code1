package com.example.logs.entity;

import lombok.Data;

import java.util.Date;

@Data
public class WebSocketPerssionVerify {

    /**
     * 设备登录token
     */
    private String token;

    /**
     * 设备imei
     */
    private String imei;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 设备接入时间
     */
    private Date connectTime;

}
