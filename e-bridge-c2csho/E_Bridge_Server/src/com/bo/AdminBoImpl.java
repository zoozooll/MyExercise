package com.bo;

import java.util.List;

import com.dao.IAdminDao;
import com.vo.Admin;
import com.vo.Purchaser;
import com.vo.Vender;

public class AdminBoImpl implements IAdminBo{
	private IAdminDao adminDao;
	
	public IAdminDao getAdminDao() {
		return adminDao;
	}

	public void setAdminDao(IAdminDao adminDao) {
		this.adminDao = adminDao;
	}

	public void addUser(Object... obj) throws Exception {
		adminDao.addUser(obj);
	}

	public void deleteUser(Integer venId) throws Exception {
		adminDao.deleteUser(venId);
	}

	public void modifyUser(Purchaser purchaser) throws Exception {
		adminDao.modifyUser(purchaser);
	}

	public List<Purchaser> queryUser() throws Exception {
		return adminDao.queryUser();
	}
	
	public Admin adminLogin(String...strings) throws Exception{
		return adminDao.adminLogin(strings);
	}

	public Purchaser findUserById(Integer purId) throws Exception {
		return adminDao.findUserById(purId);
	}

	public void modifyPassword(String password, Integer adminId)
			throws Exception {
		adminDao.modifyPassword(password, adminId);
		
	}

	public void modifyStatus(Integer status, Integer venId) throws Exception {
		adminDao.modifyStatus(status, venId);
	}

	public Admin findAdminById(Integer adminId) throws Exception {		
		return adminDao.findAdminById(adminId);
	}

	public void deleteUserByName(String delcondition) throws Exception {
		adminDao.deleteUserByName(delcondition);
		
	}

	public List<Purchaser> findUserByName(String condition) throws Exception {
		return adminDao.findUserByName(condition);
		
	}

	public List<Purchaser> findUserByNameBlur(String condition) throws Exception {
		return adminDao.findUserByNameBlur(condition);
	}

	public Admin modifyAdmin(Admin admin) throws Exception {		
		return adminDao.modifyAdmin(admin);
	}
	
}
