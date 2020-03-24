package com.example.sys.service;


import com.example.sys.entity.User;
import com.example.sys.entity.UserSearch;
import com.example.sys.util.PageDataResult;

/**
 * Created by tyj on 2018/08/15.
 */
public interface UserService {
	/**
	 * 分页查询用户列表
	 * @param page
	 * @param limit
	 * @return
	 */
	PageDataResult getUsers(UserSearch userSearch, int page, int limit);

	/**
	 * 后台用户密码登录
	 * @param userName 用户名
	 * @param password  密码
	 * @return 验证通过返回用户信 否则 null
	 */
	User adminLogin(String userName, String password);

	/**
	 *	设置用户【新增或更新】
	 * @param user
	 * @param roleIds
	 * @return
	 */
	String setUser(User user, String roleIds);

	/**
	 * 设置用户是否禁用

	 * @return
	 */
	String setLockUser(User user);

	/**
	 * 删除用户
	 * @param id
	 * @param isDel
	 * @return
	 */
	String setDelUser(Integer id, Integer isDel, Integer insertUid,
                      Integer version);

	String   setDeleteUser(User user);


	/**
	 * 根据手机号查询用户数据
	 * @param mobile
	 * @return
	 */
	User findUserByName(String userName);

	/**
	 * 根据手机号发送短信验证码
	 * @param mobile
	 * @return
	 */
	String sendMessage(int userId, String mobile);

	/**
	 * 修改用户手机号
	 * @param id
	 * @param password
	 * @return
	 */
	int updatePwd(Integer id, String password);

	/**
	 * 锁定用户
	 * @param id
	 * @param isLock  0:解锁；1：锁定
	 * @return
	 */
	int setUserLockNum(Integer id, int isLock);
}
