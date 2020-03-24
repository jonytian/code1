package com.reacheng.rc.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "t_pro_app")
public class ProjectApp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "app_id")
    private Integer appId;

    @Column(name = "check_id")
    private Integer checkId;

    @Column(name = "check_time")
    private Date checkTime;

    @Column(name = "pub_id")
    private Integer pubId;

    @Column(name = "pub_time")
    private Date pubTime;

    @Column(name = "is_lock")
    private Integer isLock;

    @Column(name = "handle_status")
    private Integer handleStatus;

    @Column(name = "status")
    private Integer status;

    @Column(name = "create_time")
    private Date createTime;

    /*应用升级配置表名*/
//    @Column(name = "table")
//    private String table;



}
