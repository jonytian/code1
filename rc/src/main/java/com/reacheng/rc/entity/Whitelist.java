package com.reacheng.rc.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "t_whitelist")
public class Whitelist implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Transient
    private Integer appId;

    @Column(name = "imei")
    private String imei;

    @Column(name = "status")
    private String status;

    @Column(name = "create_time")
    private Date createTime;


    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id",referencedColumnName="id")
    private Application application;

    @Column(name = "project_id")
    private Integer projectId;

}
