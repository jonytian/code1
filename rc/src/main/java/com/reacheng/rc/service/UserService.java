package com.reacheng.rc.service;

import com.reacheng.rc.entity.User;

public interface UserService extends BaseService<User,Integer>  {

   /**
    * 后台用户密码登录
    * @param userName 用户名
    * @param password  密码
    * @return 验证通过返回用户信 否则 null
    */
   User adminLogin(String userName, String password);

   User findByName(String userName);
   User findByEmail(String email);
}
