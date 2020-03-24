package com.example.logsys.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Log implements Serializable {

    private Integer logId;

    // 异常名称
    private String  exception;

    // 异常描述
    private String description;

    // 异常包名
    private String  packageName;

    // 异常发生时间
    private String  happenTime;

    // 截至时间
    private Date  expirationTime;

    // 日志路径
    private String logPath;

    // 文件名
    private String fileName;

    // 文件路径
    private String filePath;

    // ROM版本
    private String romVersion;

    // 安卓版本
    private String androidVersion;

    // 创建时间
    private Date createTime;

    // 设备ID(many-to-one)
    private Integer deviceId;

}
