/**
 * @2009-4-6 ����
 */
package com.bo;

import java.util.List;

import com.vo.Admin;
import com.vo.Purchaser;
import com.vo.Vender;

public interface IAdminBo {
	/**
	 * �����̻�
	 * @param obj
	 * @throws Exception
	 */
	public void addUser(Object...obj) throws Exception;
	/**
	 * ��ѯ�̻�
	 * @param hql
	 * @throws Exception
	 */	
	public List<Purchaser> queryUser() throws Exception;
	/**
	 * �޸��̻�
	 * @param obj
	 * @throws Exception
	 */
	public void modifyUser(Purchaser purchaser) throws Exception;
	/**
	 * ɾ���û�
	 * @param obj
	 * @throws Exception
	 */
	//ͨ��IDɾ���û�
	public void deleteUser(Integer venId) throws Exception;
	//����Ա��¼
	public Admin adminLogin(String...strings) throws Exception;
	//ͨ��ID�����û�
	public Purchaser findUserById(Integer venId)throws Exception;
	
	//�޸�����״̬
	public void modifyStatus(Integer status,Integer venId) throws Exception;
	//�޸Ĺ���Ա����
	public void modifyPassword(String password,Integer adminId) throws Exception;
	//ͨ��ID���ҹ���Ա
	public Admin findAdminById(Integer adminId)throws Exception;
	//������ɾ���û�
	public void deleteUserByName(String delcondition)throws Exception;
	//������ģ�������û�
	public List<Purchaser> findUserByNameBlur(String condition)throws Exception;
	//�����Ʋ����û�
	public List<Purchaser> findUserByName(String condition)throws Exception;
	public Admin modifyAdmin(Admin admin)throws Exception;
}
