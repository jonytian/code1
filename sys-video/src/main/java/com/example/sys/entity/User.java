package com.example.sys.entity;

import lombok.Data;
import java.util.Date;

@Data
public class User {

        /**
         * 用户id
         */
        private Integer id;

        /**
         * 用户名称
         */
        private String username;

        /**
         * 用户密码
         */
        private String password;

        /**
         * 创建时间
         */
        private Date createTime;


}
