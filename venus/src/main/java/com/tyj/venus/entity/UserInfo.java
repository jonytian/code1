package com.tyj.venus.entity;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "t_user")
public class UserInfo implements Serializable {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "username")
    private String username;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "gender")
    private String gender;

    @Column(name = "password")
    private String password;

    @Column(name = "token")
    private String token;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "description")
    private String description;

    // 登录次数
    @Column(name = "login_count")
    private Integer loginCount;

    // 粉丝数
    @Column(name = "followers_count")
    private Integer followersCount;

    // 关注数
    @Column(name = "friends_count")
    private Integer friendsCount;

    // 帖子数
    @Column(name = "statuses_count")
    private Integer statusesCount;

    // 收藏数
    @Column(name = "favourites_count")
    private Integer favouritesCount;

    @Column(name = "login_at")
    private Date loginAt;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "update_at")
    private Date updateAt;




}
