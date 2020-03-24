package com.reacheng.rc.entity;

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
@Table(name = "sys_user")
public class User implements Serializable {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @NotEmpty(message = "用户名不能为空")
    @Column(name = "user_name",columnDefinition="varchar(50) COMMENT '用户名'")
    private String userName;


    @Column(name = "role_name",columnDefinition="varchar(50) COMMENT '角色名'")
    private String roleName;

    @Column(name = "email",columnDefinition = "varchar(50) COMMENT '邮箱'")
    private String email;

    @Length(min = 6, message = "密码长度不能少于6位")
    @Column(name = "password",columnDefinition = "varchar(255) COMMENT '密码'")
    private String password;

    @Transient
    private String confirmPassword;

    @Column(name = "remember_token",columnDefinition = "varchar(255) COMMENT '登录钥匙'")
    private String rememberToken;

    @Column(name = " department",columnDefinition = "varchar(100) COMMENT '部门'")
    private String  department;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt = new Date();

    @Column(name = "login_time")
    private Date loginTime;

    @Column(name = "login_num",columnDefinition = "int(11) DEFAULT 0 COMMENT '登录次数'")
    private Integer loginNum = 0;

    @Column(name = "is_super",columnDefinition = "tinyint(1) DEFAULT 0 COMMENT '是否是超级管理员'")
    private Boolean isSuper = false;

//    @ManyToOne
//    @NotFound(action = NotFoundAction.IGNORE)//由于超级管理账户为0所以要加上这句
//    @JoinColumn(name = "admin_gid",foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))//不需要添加外键,屏蔽实体外键
//    private Gadmin gadmin;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)//由于超级管理账户为0所以要加上这句
    @JoinColumn(name = "admin_gid",referencedColumnName="gid")
    private Gadmin gadmin;

    @Transient
    private Integer roleId;



}
