package com.example.logsys.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class SystemLog implements Serializable {

    private Integer id;
    // 内容
    private String content;
    // 用户ID
    private Integer userId;
    // 用户名
    private String username;
    // ip
    private String ip;
    // 创建时间
    private Date createTime;


}
