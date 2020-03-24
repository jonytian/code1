package com.tyj.venus.service.impl;


import com.tyj.venus.dao.UserRepository;
import com.tyj.venus.entity.User;
import com.tyj.venus.service.UserService;
import com.tyj.venus.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service(value = "userService")
public class UserServiceImpl extends BaseServiceImpl <User, Integer> implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User adminLogin(String userName, String password) {

        User user = userRepository.adminLogin(userName, Tools.Md5(password));
        if (user != null) {
            user.setAdminLoginTime(new Date());
            user.setAdminLoginNum(user.getAdminLoginNum()+1);
            userRepository.save(user);
            return user;
        }
        return null;
    }


}
