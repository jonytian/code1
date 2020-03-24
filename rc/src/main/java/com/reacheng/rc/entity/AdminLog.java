package com.reacheng.rc.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "sys_admin_log")
public class AdminLog implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "content", columnDefinition = "varchar(100) comment '日志内容'")
    private String content;
    @Column(name = "admin_id", columnDefinition = "int(11) default 0 comment '管理员ID'")
    private Integer adminId;
    @Column(name = "admin_name", columnDefinition = "varchar(20) comment '管理员名字'")
    private String adminName;
    @Column(name = "ip", columnDefinition = "varchar(20) comment 'IP地址'")
    private String ip;
    @Column(name = "created_at")
    private Date createdAt;


}
