package com.example.logs.service;


import com.example.logs.entity.User;
import com.example.logs.entity.UserSearch;
import com.example.logs.mapper.UserMapper;
import com.example.logs.util.PageDataResult;
import com.example.logs.util.Tools;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * Created by tyj on 2019/08/15.
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;

	@Override
	public int update(User user) {
		return userMapper.update(user);
	}

	@Override
	public int save(User user) {
		return userMapper.save(user);
	}

	@Override
	public int delete(User user) {
		return  userMapper.delete(user);
	}

	@Override
	public int deleteById(Integer id) {
		return  userMapper.deleteById(id);
	}

	@Override
	public User findById(Integer id) {
		return userMapper.findById(id);
	}

	@Override
	public List<User> findAll() {
		return userMapper.findAll();
	}

	/**用户列表展示*/
	@Override
	public PageDataResult getUsers(UserSearch userSearch, int page, int limit) {

		PageDataResult pdr = new PageDataResult();
		PageHelper.startPage(page, limit);
		List<User> urList = userMapper.getUsers(userSearch);
		// 获取分页查询后的数据
		PageInfo<User> pageInfo = new PageInfo<>(urList);
		// 设置获取到的总记录数total：
		pdr.setTotals(Long.valueOf(pageInfo.getTotal()).intValue());

		pdr.setList(urList);
		return pdr;
	}

	@Override
	public User findUser(String username, String password) {
		return userMapper.findUser(username,password);
	}

	@Override
	public User findByName(String username) {
		return userMapper.findByName(username);
	}

	@Override
	public int updatePwd(User user) {
		return userMapper.update(user);
	}

	/**用户登录*/
	@Override
	public User login(String username, String password) {

		User user = userMapper.findUser(username, Tools.Md5(password));
		if (user != null) {
			user.setLoginTime(new Date());
			user.setLoginNum(user.getLoginNum()+1);
			userMapper.update(user);

			return user;
		}
		return null;

	}








}
