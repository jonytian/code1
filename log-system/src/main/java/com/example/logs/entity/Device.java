package com.example.logs.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Device implements Serializable {

    // 设备ID
    private Integer deviceId;
    // 设备IMEI
    private String imei;
    // 项目名称
    private String projectName;
    // 连接时间
    private Date connectTime;
    // 断开时间
    private Date disconnectTime;
    // 是否踢出
    private Integer kicked;
    // 授权码
    private String token;
    // 是否在线
    private Integer online;
    // 是否重启
    private Integer restart;
    // MTKLog状态
    private Integer mtklogStatus;
    // 创建时间
    private Date createTime;
}
