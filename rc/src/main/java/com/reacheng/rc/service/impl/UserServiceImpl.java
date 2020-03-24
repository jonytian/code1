package com.reacheng.rc.service.impl;

import com.reacheng.rc.dao.UserRepository;
import com.reacheng.rc.entity.User;
import com.reacheng.rc.service.UserService;
import com.reacheng.rc.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl extends BaseServiceImpl <User, Integer> implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User adminLogin(String userName, String password) {

        User user = userRepository.adminLogin(userName, Tools.Md5(password));
        if (user != null) {
            user.setLoginTime(new Date());
            user.setLoginNum(user.getLoginNum()+1);
            userRepository.save(user);
            return user;
        }
        return null;
    }

    @Override
    public User findByName(String userName) {
        return userRepository.findByName(userName);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
