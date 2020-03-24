package com.tyj.venus.entity;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "t_admin")
public class User implements Serializable {
    @Id
    @Column(name = "admin_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminId;

    @NotEmpty(message = "用户名不能为空")
    @Column(name = "user_name",columnDefinition="varchar(50) COMMENT '用户名'")
    private String userName;

    @Column(name = "email",columnDefinition = "varchar(50) COMMENT '邮箱'")
    private String email;

    @Length(min = 6, message = "密码长度不能少于6位")
    @Column(name = "password",columnDefinition = "varchar(255) COMMENT '密码'")
    private String password;

    @Column(name = "remember_token",columnDefinition = "varchar(255) COMMENT '登录钥匙'")
    private String rememberToken;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt = new Date();

    @Column(name = "admin_login_time")
    private Date adminLoginTime;

    @Column(name = "admin_login_num",columnDefinition = "int(11) DEFAULT 0 COMMENT '登录次数'")
    private Integer adminLoginNum = 0;

    @Column(name = "admin_is_super",columnDefinition = "tinyint(1) DEFAULT 0 COMMENT '是否是超级管理与那'")
    private Boolean adminIsSuper = false;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)//由于超级管理账户为0所以要加上这句
    @JoinColumn(name = "admin_gid",foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))//不需要添加外键
    private Gadmin gadmin;




}
