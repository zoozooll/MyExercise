/**
 * @2009-4-6 上午
 */
package com.bo;

import java.util.List;

import com.vo.Admin;
import com.vo.Purchaser;
import com.vo.Vender;

public interface IAdminBo {
	/**
	 * 增加商户
	 * @param obj
	 * @throws Exception
	 */
	public void addUser(Object...obj) throws Exception;
	/**
	 * 查询商户
	 * @param hql
	 * @throws Exception
	 */	
	public List<Purchaser> queryUser() throws Exception;
	/**
	 * 修改商户
	 * @param obj
	 * @throws Exception
	 */
	public void modifyUser(Purchaser purchaser) throws Exception;
	/**
	 * 删除用户
	 * @param obj
	 * @throws Exception
	 */
	//通过ID删除用户
	public void deleteUser(Integer venId) throws Exception;
	//管理员登录
	public Admin adminLogin(String...strings) throws Exception;
	//通过ID查找用户
	public Purchaser findUserById(Integer venId)throws Exception;
	
	//修改卖家状态
	public void modifyStatus(Integer status,Integer venId) throws Exception;
	//修改管理员密码
	public void modifyPassword(String password,Integer adminId) throws Exception;
	//通过ID查找管理员
	public Admin findAdminById(Integer adminId)throws Exception;
	//按名称删除用户
	public void deleteUserByName(String delcondition)throws Exception;
	//按名称模糊查找用户
	public List<Purchaser> findUserByNameBlur(String condition)throws Exception;
	//按名称查找用户
	public List<Purchaser> findUserByName(String condition)throws Exception;
	public Admin modifyAdmin(Admin admin)throws Exception;
}
