package com.example.logsys.service;


import com.example.logsys.entity.User;
import com.example.logsys.entity.UserSearch;
import com.example.logsys.util.PageDataResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * Created by tyj on 2019/08/15.
 */
public interface UserService {

	User login(String username, String password);

	int save(User user);

	int update(User user);

	int delete(User user);

	int deleteById(Integer id);

	User findById(Integer id);

	List<User> findAll();

	/**
	 * 分页查询用户数据
	 * @return
	 */
	PageDataResult getUsers(UserSearch userSearch, int page, int limit) ;

	/**
	 * 根据用户名和密码查找用户
	 * @param username
	 * @param password
	 * @return
	 */
	User findUser(@Param("username") String username,
				  @Param("password") String password);

	/**
	 * 根据用户名获取用户数据
	 * @param username
	 * @return
	 */
	User findByName(String username);

	/**
	 * 修改用户密码
	 * @return
	 */
	int updatePwd(User user);


}
