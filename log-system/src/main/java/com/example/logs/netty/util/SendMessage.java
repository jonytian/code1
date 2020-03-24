package com.example.logs.netty.util;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SendMessage {

    private String code;

    private String sendType;

    private String content;

    private Date sendTime;

    private List<String> receivers;

}
