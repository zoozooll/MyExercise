package com.dao;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.vo.DeliveryBill; 

//交货单dao
public class DeliveryBillDaoImpl extends HibernateDaoSupport implements IDeliveryBillDao {

	//在创建交货单前找到交货单编号,发票编号最大的那个对象
	public DeliveryBill beforeCreateDeliveryBill(){ 
		List<DeliveryBill> deliveryBill=this.getHibernateTemplate().find("from DeliveryBill");  
		DeliveryBill d=deliveryBill.get(deliveryBill.size()-1); 
	   // System.out.println("在创建交货单前得到的交货单编号=="+d.getDeliveryCode());
		return d; 
	}
	//创建交货单
	public void createDeliverybill(DeliveryBill deliverybill) {
		 this.getHibernateTemplate().save(deliverybill);

	}
    //查看所有交货单
	public List<DeliveryBill> findAllDeliveryBill() {
		 
		 String hql="from DeliveryBill ";
		 List<DeliveryBill> db=this.getHibernateTemplate().find(hql);
		 if(db!=null&&db.size()>0){
			 return  db;
		 }
		return null;
		
	}

}
