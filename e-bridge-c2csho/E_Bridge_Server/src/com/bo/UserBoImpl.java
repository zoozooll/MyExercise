package com.bo;

import java.util.List;

import com.dao.IUserDao;
import com.vo.Purchaser;

public class UserBoImpl implements IUserBo {
	private IUserDao userDao;

	public IUserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	public Purchaser login(String name, String password)throws Exception {
		return userDao.login(name, password);
	}

	public void register(Object...objects)throws Exception {
		userDao.register(objects);
	}

	public void update(Object... objects) throws Exception{
		userDao.update(objects);
	}
		
	

	public Purchaser findByName(String name)throws Exception {
		return userDao.findByName(name);
	}

	public List<Purchaser> getAllPurchasers() throws Exception {
		return userDao.getAllPurchasers();
	}
	
	//更改用户密码
	 public void modifyPassword(String password,Integer purId)throws Exception{
		 userDao.modifyPassword(password, purId);
	 }
	 
	 public String[] findUserName(String name) throws Exception{
		 return userDao.findUserName(name);
	 }

	public Purchaser purchasertoVender(Object... objects) throws Exception {
		return userDao.purchasertoVender(objects);
	}
}
