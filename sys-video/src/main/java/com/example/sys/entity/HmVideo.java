package com.example.sys.entity;

import lombok.Data;

import java.util.Date;

@Data
public class HmVideo {

    // ID
    private Integer id;
    // 终端IMEI
    private String imei;
    // 录制时长
    private Integer timeInterval;
    // 视频录像的序列号
    private String serialNumber;
    // 文件名
    private String fileName;
    // 文件路径
    private String filePath;
    // 开始时间
    private Date startTime;
    // 结束时间
    private Date endTime;
    // 创建时间
    private Date createTime;

}
