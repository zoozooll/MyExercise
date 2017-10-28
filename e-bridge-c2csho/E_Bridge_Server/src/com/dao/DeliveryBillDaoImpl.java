package com.dao;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.vo.DeliveryBill; 

//������dao
public class DeliveryBillDaoImpl extends HibernateDaoSupport implements IDeliveryBillDao {

	//�ڴ���������ǰ�ҵ����������,��Ʊ��������Ǹ�����
	public DeliveryBill beforeCreateDeliveryBill(){ 
		List<DeliveryBill> deliveryBill=this.getHibernateTemplate().find("from DeliveryBill");  
		DeliveryBill d=deliveryBill.get(deliveryBill.size()-1); 
	   // System.out.println("�ڴ���������ǰ�õ��Ľ��������=="+d.getDeliveryCode());
		return d; 
	}
	//����������
	public void createDeliverybill(DeliveryBill deliverybill) {
		 this.getHibernateTemplate().save(deliverybill);

	}
    //�鿴���н�����
	public List<DeliveryBill> findAllDeliveryBill() {
		 
		 String hql="from DeliveryBill ";
		 List<DeliveryBill> db=this.getHibernateTemplate().find(hql);
		 if(db!=null&&db.size()>0){
			 return  db;
		 }
		return null;
		
	}

}
