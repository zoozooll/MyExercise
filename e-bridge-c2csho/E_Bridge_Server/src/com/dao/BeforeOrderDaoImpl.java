package com.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vo.OrderStatus;
import com.vo.Payway;

public class BeforeOrderDaoImpl extends HibernateDaoSupport implements IBeforeOrderDao {

	//�鿴���ʽѡ��
	public List<Payway> findPayWay() {
          List<Payway> payway=this.getHibernateTemplate().find("from Payway");
          System.out.println("dao�鿴�ĸ��ʽ====>?"+payway);
		return payway;
	}
	
    //�鿴���еĶ���״̬ѡ��
/*	public List<OrderStatus> findOrderStatus() { 
		return  this.getHibernateTemplate().find("from OrderStatus");
	}*/
	
	//�������ֲ��Ҷ���״̬
/*	public List<OrderStatus> findStatusByName(String name) {
		 String hql="from OrderStatus o where o.statStatus='"+name+"'";
		return this.getHibernateTemplate().find(hql);
	}*/
	 

}
