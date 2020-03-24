package com.reacheng.rc.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "t_app_log")
public class AppLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "imei")
    private String imei;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "package_name")
    private String packageName;

    @Column(name = "versionCode")
    private String versionCode;

    @Column(name = "status")
    private String status;

    @Column(name = "create_time")
    private Date createTime;

}
