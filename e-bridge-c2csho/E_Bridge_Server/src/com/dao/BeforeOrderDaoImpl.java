package com.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vo.OrderStatus;
import com.vo.Payway;

public class BeforeOrderDaoImpl extends HibernateDaoSupport implements IBeforeOrderDao {

	//查看付款方式选项
	public List<Payway> findPayWay() {
          List<Payway> payway=this.getHibernateTemplate().find("from Payway");
          System.out.println("dao查看的付款方式====>?"+payway);
		return payway;
	}
	
    //查看所有的订单状态选项
/*	public List<OrderStatus> findOrderStatus() { 
		return  this.getHibernateTemplate().find("from OrderStatus");
	}*/
	
	//根据名字查找订单状态
/*	public List<OrderStatus> findStatusByName(String name) {
		 String hql="from OrderStatus o where o.statStatus='"+name+"'";
		return this.getHibernateTemplate().find(hql);
	}*/
	 

}
