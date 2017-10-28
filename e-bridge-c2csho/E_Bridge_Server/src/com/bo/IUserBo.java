package com.bo;

import java.util.List;

import com.vo.Purchaser;

public interface IUserBo {
	//用户登录
	public Purchaser login(String name,String password)throws Exception;
	
	//用户注册
	public void register(Object... objects)throws Exception;
	
	//用户信息修改
	public void update(Object... objects)throws Exception;
	 
	
	//根据用户名查找用户详细信息
	public Purchaser findByName(String name)throws Exception;
	 
	//得到用户列表
	 public List<Purchaser> getAllPurchasers()throws Exception;
	 
	 //更改用户密码
	 public void modifyPassword(String password,Integer purId)throws Exception;
	 public String[] findUserName(String name) throws Exception ;

	public Purchaser purchasertoVender(Object... objects) throws Exception ;
}
