package com.reacheng.rc.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "t_application")
public class Application implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "descpt")
    private String descpt;

    @Column(name = "icon")
    private String icon;

    @Column(name = "package_name")
    private String packageName;

    @Column(name = "version_code")
    private Integer versionCode;

    @Column(name = "version")
    private String version;

    @Column(name = "filename")
    private String filename;

    @Column(name = "md5")
    private String md5;

    @Column(name = "status")
    private String status; //状态  待审核 已审核

    @Column(name = "type")
    private Integer type; //应用启动类名类型 0 ，className为空的时候传-1  0,1 'Activity':'Service'

    @Column(name = "class_type")
    private Integer classType;

    @Column(name = "class_name")
    private String className;

    @Column(name = "download_url")
    private String downloadUrl;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "handle_status")
    private String handleStatus;

    @Transient
    private String[] ids;

    @Transient
    private String upMd5;

    @Column(name = "test_imei")
    private String testImei;

    @Column(name = "all_push")
    private Integer allPush;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id",referencedColumnName="id")
    private Project project;





}
