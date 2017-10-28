package com.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vo.Order;
import com.vo.OrderLine;
import com.vo.Purchaser;
import com.vo.ReceiptBill;
import com.vo.Vender;

public class OrderDaoImpl extends HibernateDaoSupport implements IOrderDao 
{
	 //在创建订单前找到订单编号
	public Integer beforeCreateOrder(){ 
		List<Order> order=this.getHibernateTemplate().find("from Order");  
	    Order o=order.get(order.size()-1); 
	    System.out.println("在创建订单前得到的id=="+o.getOrderCode());
		return o.getOrderCode(); 
	}
	
	//在下订单的时候先查看收款单里面的数据，然后bo层对其加一，即生成收款单的相应字段
	public ReceiptBill findReceiptBill(){
		List<ReceiptBill> receiptBill=this.getHibernateTemplate().find("from ReceiptBill");
		ReceiptBill r=receiptBill.get(receiptBill.size()-1);
		return r;
	}
	
	// 买家提交订单
	public boolean createOrder(Order order) { 
		//加入事务
		//HibernateUtil.save(order);
		this.getHibernateTemplate().save(order); 
		return true;
	}
	//删除订单明细
	public void delOrder(int orderlineId) {
		String hql="from OrderLine o where o.lineId= "+orderlineId; 
		List<Order> order=this.getHibernateTemplate().find(hql);
		System.out.println("要删除的对象是 ====》"+order.get(0));
		
		if(order.size()>0&&order!=null){
			this.getHibernateTemplate().delete(order.get(0));
		}
		
	}
	// 通过订单号查找订单信息
	public Order getOrderById(Integer orderId) {
		 Order order=(Order) this.getHibernateTemplate().get(Order.class, orderId);
		return order;
	}
	//买家 查看所有订单
	public  List<OrderLine> showPurchaserrOrder(Purchaser p) { 
	     System.out.println("查看所有历史订单.............是买家 .."+p.getPurName());
		 String hql="from Order o where o.orderSource='"+p.getPurName()+"'";
		 List<Order> orders=this.getHibernateTemplate().find(hql);  
		 System.out.println("查出来的order是===>"+orders);
		 List<OrderLine> line=new ArrayList<OrderLine>() ;
		 for(Order or:orders){
			 System.out.println("查orderLine");
			 String hql2="from OrderLine  where order.orderId ="+or.getOrderId();
			 List<OrderLine> orderlines=this.getHibernateTemplate().find(hql2);			
				 for (OrderLine orderLine : orderlines) {
					line.add(orderLine);
				 }
			
			 System.out.println("orderLine是===>"+line);
		 }
		 System.out.println("历史订单明细是===>"+line); 
		 return line;
	}
	//卖家查看全部订单
	public List<OrderLine> showVenderOrder(Vender vender){
		 System.out.println("查看所有历史订单...............是卖家..."+vender.getPurchaser().getPurName());
		 String hql="from OrderLine o where o.venderName='"+vender.getVenShortname()+"'";
		 List<OrderLine> OrderLines=this.getHibernateTemplate().find(hql); 
		/* List<OrderLine> orders=null;
		for(OrderLine line: OrderLines){
			String hq="from Order or where or.orderId='"+line.getOrderId()+"'";
			List<Order> o=this.getHibernateTemplate().find(hq); 
			 orders.add(o.get(0)); 
			 System.out.println("o.get(o)...得到的.."+o.get(0)); 
		} */
		 System.out.println("订单状态表的记录是===>"+OrderLines);
		 return OrderLines;
	}
	
	//卖家修改订单状态 
	public void updateOrderStatus(int orderlineid) { 
		System.out.println("====111111111111111111=======bo====?"+orderlineid); 
	 
		 final String hql="update OrderLine o set o.state='审核通过' where o.lineId='"+orderlineid+"'";
		 System.out.println("====222222222222222=======bo====?"); 
		 System.out.println(hql);
		 this.getHibernateTemplate().execute(new HibernateCallback(){ 
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query=session.createQuery(hql);
				int i = query.executeUpdate();
				System.out.println(i+"条记录被跟新了.....");
				return null;
			} 
		 });
		 System.out.println("====33333333333=======bo====?"); 
	} 

}
