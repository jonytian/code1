package com.legaoyi.platform.service;

import java.util.List;

import com.legaoyi.platform.model.User;

/**
 * @author gaoshengbo
 */
public interface UserService {

    public boolean isExistAccount(String userAccount) throws Exception;

    public User findUserById(String userId) throws Exception;

    public List<?> findUserRoleByUser(String userId) throws Exception;

}
