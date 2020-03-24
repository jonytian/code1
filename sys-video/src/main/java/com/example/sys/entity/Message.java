package com.example.sys.entity;

import lombok.Data;

@Data
public class Message {

    private Integer msgid;

    private Integer seq;

    private String terminal;

    private Integer channel;

    private Integer timeinterval;

    private String alarmId;

    private String seialNo;

    private String fileName;

    private String startTime;

    private String endTime;


}
