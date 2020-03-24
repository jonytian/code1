package com.example.sys.mapper;


import com.example.sys.entity.User;
import com.example.sys.entity.UserSearch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * Created by tyj on 2018/08/14.
 */
@Mapper
public interface UserMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(User record);

	int insertSelective(User record);

	User selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);

	/**
	 * 分页查询用户数据
	 * @return
	 */
	List<User> getUsers(@Param("userSearch") UserSearch userSearch);

	/**
	 * 删除用户
	 * @param user
	 * @param
	 * @return
	 */

	int deleteUser(User user);
	/**
	 * 设置用户
	 * @param user
	 * @param
	 * @return
	 */
	int setLockUser(User user);






	/**
	 * 根据用户名和密码查找用户
	 * @param username
	 * @param password
	 * @return
	 */
	User findUser(@Param("username") String username,
                  @Param("password") String password);

	/**
	 *	根据手机号获取用户数据
	 * @param mobile
	 * @return
	 */
	User findUserByMobile(String mobile);

	/**
	 * 根据用户名获取用户数据
	 * @param username
	 * @return
	 */
	User findUserByName(String username);

	/**
	 * 修改用户密码
	 * @param id
	 * @param password
	 * @return
	 */
	int updatePwd(@Param("id") Integer id, @Param("password") String password);

	/**
	 * 是否锁定用户
	 * @param id
	 * @param isLock
	 * @return
	 */
	int setUserLockNum(@Param("id") Integer id, @Param("isLock") int isLock);
}