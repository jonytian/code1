package com.tyj.venus.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
@Data
@Entity
@Table(name = "t_user_msg")
public class UserMsg implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty(message = "参数错误,姓名")
    @Column(name = "name",columnDefinition = "VARCHAR(50) COMMENT '用户姓名'")
    private String name;

    @NotEmpty(message = "参数错误,邮箱")
    @Column(name = "email",columnDefinition = "VARCHAR(50) COMMENT '邮箱'")
    private String email;
    @NotEmpty(message = "参数错误,手机号码")
    @Column(name = "mobile",columnDefinition = "VARCHAR(12) COMMENT '电话'")
    private String mobile;

    @Column(name = "content",columnDefinition = "TEXT COMMENT '内容'")
    private String content;

    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "send_email",columnDefinition = "VARCHAR(20) COMMENT '发送邮箱'")
    private String sendEmail;


}
