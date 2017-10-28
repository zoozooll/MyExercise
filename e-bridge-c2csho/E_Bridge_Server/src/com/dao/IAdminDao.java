package com.dao;

import java.util.List;

import com.vo.Admin;
import com.vo.Purchaser;
import com.vo.Vender;

public interface IAdminDao {
	//�����û���
	public void addUser(Object...obj) throws Exception;
	//��ѯ�û���
	public List<Purchaser> queryUser() throws Exception;
	//�޸��û���
	public void modifyUser(Purchaser purchaser) throws Exception;
	//ɾ���û�
	public void deleteUser(Integer venId) throws Exception;
	//����Ա��½
	public Admin adminLogin(String...strings) throws Exception;
	//����������Ϣ
	public Purchaser findUserById(Integer venId) throws Exception;
	//�޸�����״̬
	public void modifyStatus(Integer status,Integer venId) throws Exception;
	//�޸�����
	public void modifyPassword(String password,Integer adminId) throws Exception;
	//���ҹ���Ա��Ϣ
	public Admin findAdminById(Integer adminId)throws Exception;
	//�����û���ɾ���û�
	public void deleteUserByName(String delcondition)throws Exception;
	//�����û�
	public List<Purchaser> findUserByNameBlur(String condition)throws Exception;
	//�����û��������û��б�
	public List<Purchaser> findUserByName(String condition)throws Exception;
	//�޸Ĺ���Ա��Ϣ
	public Admin modifyAdmin(Admin admin)throws Exception;
}
