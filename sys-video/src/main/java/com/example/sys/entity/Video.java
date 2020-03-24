package com.example.sys.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Video {

    private Integer id;
    //消息id
    private Integer msgid;

    private Integer seq;
    //终端手机号
    private String terminal;
    //通道
    private Integer channel;
    //录制时长
    private Integer timeinterval;
    //视频录像的序列号
    private String seialNo;
    //文件路径
    private String fileName;
    //文件名
    private String file;

    //开始时间
    private String startTime;
    //结束时间
    private String endTime;

    /**
     * 创建时间
     */
    private Date createTime;

}
