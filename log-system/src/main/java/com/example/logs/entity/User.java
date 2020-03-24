package com.example.logs.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {

        /**
         * 用户id
         */
        private Integer userId;

        /**
         * 用户名称
         */
        private String username;

        /**
         * 用户密码
         */
        private String password;

        /**
         * 是否管理员
         */
        private Integer isAdmin = 0;

        /**
         * 登录时间
         */
        private Date loginTime;

        /**
         * 登录次数
         */
        private Integer loginNum = 0;

        /**
         * 创建时间
         */
        private Date createTime;


}
