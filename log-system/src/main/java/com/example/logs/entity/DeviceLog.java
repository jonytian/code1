package com.example.logs.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DeviceLog implements Serializable {

    private Integer id;
    // 设备ID
    private Integer deviceId;
    // 日志ID
    private Integer logId;
    // 创建时间
    private Date createTime;


}
