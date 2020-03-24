package com.reacheng.rc.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
@Data
@Entity
@Table(name = "sys_system")
public class Systems implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name",columnDefinition = "varchar(50) comment '系统名称'")
    private String name;

    @Column(name = "ipc",columnDefinition = "varchar(100) comment '备案号'")
    private String ipc;

    @Column(name = "keywords",columnDefinition = "varchar(255) comment '关键字'")
    private String keywords;

    @Column(name = "description",columnDefinition = "text comment '描述'")
    private String description;

    @Column(name = "url",columnDefinition = "varchar(100) comment '域名'")
    private String url;

    @Column(name = "email",columnDefinition = "varchar(50) comment '公司邮箱'")
    private String email;

    @Column(name = "mobile",columnDefinition = "varchar(20) comment '联系电话'")
    private String mobile;

    @Column(name = "address",columnDefinition = "varchar(200) comment '联系地址'")
    private String address;

    @Column(name = "statistics",columnDefinition = "text comment '统计代码'")
    private String statistics;

    @Column(name = "logo",columnDefinition = "varchar(255) comment 'LOGO'")
    private String logo;

    @Column(name = "company",columnDefinition = "varchar(20) comment '公司名称'")
    private String company;

    @Column(name = "version",columnDefinition = "varchar(20) comment '系统版本'")
    private String version;
}
