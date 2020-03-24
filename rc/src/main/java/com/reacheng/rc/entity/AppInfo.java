package com.reacheng.rc.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "t_app_info")
public class AppInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "package_name")
    private String packageName;

    @Column(name = "create_time")
    private Date createTime;


}
