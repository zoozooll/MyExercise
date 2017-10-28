package com.bo;

import java.util.List;

import com.vo.Purchaser;

public interface IUserBo {
	//�û���¼
	public Purchaser login(String name,String password)throws Exception;
	
	//�û�ע��
	public void register(Object... objects)throws Exception;
	
	//�û���Ϣ�޸�
	public void update(Object... objects)throws Exception;
	 
	
	//�����û��������û���ϸ��Ϣ
	public Purchaser findByName(String name)throws Exception;
	 
	//�õ��û��б�
	 public List<Purchaser> getAllPurchasers()throws Exception;
	 
	 //�����û�����
	 public void modifyPassword(String password,Integer purId)throws Exception;
	 public String[] findUserName(String name) throws Exception ;

	public Purchaser purchasertoVender(Object... objects) throws Exception ;
}
