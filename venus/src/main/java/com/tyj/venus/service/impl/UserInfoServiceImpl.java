package com.tyj.venus.service.impl;


import com.tyj.venus.dao.UserInfoRepository;
import com.tyj.venus.dao.UserRepository;
import com.tyj.venus.entity.User;
import com.tyj.venus.entity.UserInfo;
import com.tyj.venus.service.UserInfoService;
import com.tyj.venus.service.UserService;
import com.tyj.venus.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service(value = "userInfoService")
public class UserInfoServiceImpl extends BaseServiceImpl <UserInfo, Integer> implements UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public UserInfo login(String username, String password) {

        UserInfo user = userInfoRepository.login(username, Tools.Md5(password));
        if (user != null) {
            user.setLoginAt(new Date());
            user.setLoginCount(user.getLoginCount()+1);
            userInfoRepository.save(user);
            return user;
        }
        return null;
    }


}
