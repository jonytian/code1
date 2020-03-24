package com.example.sys.entity;

import lombok.Data;

import java.util.Date;

@Data
public class HmVideoSearch {

    /**
     * 开始日期
     */
    private String insertTimeStart;
    /**
     * 结束日期
     */
    private String insertTimeEnd;

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
    private String startTime;
    // 结束时间
    private String endTime;
    // 创建时间
    private Date createTime;

}
