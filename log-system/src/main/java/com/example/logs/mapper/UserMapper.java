package com.example.logs.mapper;
import com.example.logs.entity.User;
import com.example.logs.entity.UserSearch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface UserMapper {

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
	List<User> getUsers(@Param("userSearch") UserSearch userSearch);

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
	 * @param id
	 * @param password
	 * @return
	 */
	int updatePwd(@Param("id") Integer id, @Param("password") String password);




}