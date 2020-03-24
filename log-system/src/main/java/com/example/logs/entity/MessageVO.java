package com.example.logs.entity;

import lombok.Data;

import java.util.HashMap;

@Data
public class MessageVO {

    /**消息代码*/
    private String code;

    /**设备编号*/
    private String imei;

    /**指令集*/
    private HashMap<String,String> data;


}
