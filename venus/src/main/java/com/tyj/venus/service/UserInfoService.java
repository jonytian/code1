package com.tyj.venus.service;

import com.tyj.venus.entity.UserInfo;

public interface UserInfoService extends BaseService<UserInfo,Integer>  {


   /**
    * 前台用户密码登录
    * @param username 用户名
    * @param password  密码
    * @return 验证通过返回用户信息,否则null
    */
   UserInfo login(String username, String password);
}
