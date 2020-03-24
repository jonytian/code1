package com.example.logs.entity;

import lombok.Data;

@Data
public class OrderVO {

    /**消息代码*/
    private String code;

    /**设备编号*/
    private String imei;

    /**指令集*/
    private String test ; //测试心跳连接

    private String restart  ; //重启

    private String download  ; //下载

    private String status  ; //测试心跳

    private String oc;//开关MTKLOG

    private String grab  ; //测试心跳

    private String clear ; //测试心跳

    private String projectName ; //测试心跳


}
