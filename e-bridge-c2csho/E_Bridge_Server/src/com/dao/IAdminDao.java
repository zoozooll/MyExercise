package com.dao;

import java.util.List;

import com.vo.Admin;
import com.vo.Purchaser;
import com.vo.Vender;

public interface IAdminDao {
	//增加用户；
	public void addUser(Object...obj) throws Exception;
	//查询用户；
	public List<Purchaser> queryUser() throws Exception;
	//修改用户；
	public void modifyUser(Purchaser purchaser) throws Exception;
	//删除用户
	public void deleteUser(Integer venId) throws Exception;
	//管理员登陆
	public Admin adminLogin(String...strings) throws Exception;
	//查找卖家信息
	public Purchaser findUserById(Integer venId) throws Exception;
	//修改卖家状态
	public void modifyStatus(Integer status,Integer venId) throws Exception;
	//修改密码
	public void modifyPassword(String password,Integer adminId) throws Exception;
	//查找管理员信息
	public Admin findAdminById(Integer adminId)throws Exception;
	//根据用户名删除用户
	public void deleteUserByName(String delcondition)throws Exception;
	//搜索用户
	public List<Purchaser> findUserByNameBlur(String condition)throws Exception;
	//根据用户名搜索用户列表
	public List<Purchaser> findUserByName(String condition)throws Exception;
	//修改管理员信息
	public Admin modifyAdmin(Admin admin)throws Exception;
}
