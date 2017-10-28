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
	 //�ڴ�������ǰ�ҵ��������
	public Integer beforeCreateOrder(){ 
		List<Order> order=this.getHibernateTemplate().find("from Order");  
	    Order o=order.get(order.size()-1); 
	    System.out.println("�ڴ�������ǰ�õ���id=="+o.getOrderCode());
		return o.getOrderCode(); 
	}
	
	//���¶�����ʱ���Ȳ鿴�տ��������ݣ�Ȼ��bo������һ���������տ����Ӧ�ֶ�
	public ReceiptBill findReceiptBill(){
		List<ReceiptBill> receiptBill=this.getHibernateTemplate().find("from ReceiptBill");
		ReceiptBill r=receiptBill.get(receiptBill.size()-1);
		return r;
	}
	
	// ����ύ����
	public boolean createOrder(Order order) { 
		//��������
		//HibernateUtil.save(order);
		this.getHibernateTemplate().save(order); 
		return true;
	}
	//ɾ��������ϸ
	public void delOrder(int orderlineId) {
		String hql="from OrderLine o where o.lineId= "+orderlineId; 
		List<Order> order=this.getHibernateTemplate().find(hql);
		System.out.println("Ҫɾ���Ķ����� ====��"+order.get(0));
		
		if(order.size()>0&&order!=null){
			this.getHibernateTemplate().delete(order.get(0));
		}
		
	}
	// ͨ�������Ų��Ҷ�����Ϣ
	public Order getOrderById(Integer orderId) {
		 Order order=(Order) this.getHibernateTemplate().get(Order.class, orderId);
		return order;
	}
	//��� �鿴���ж���
	public  List<OrderLine> showPurchaserrOrder(Purchaser p) { 
	     System.out.println("�鿴������ʷ����.............����� .."+p.getPurName());
		 String hql="from Order o where o.orderSource='"+p.getPurName()+"'";
		 List<Order> orders=this.getHibernateTemplate().find(hql);  
		 System.out.println("�������order��===>"+orders);
		 List<OrderLine> line=new ArrayList<OrderLine>() ;
		 for(Order or:orders){
			 System.out.println("��orderLine");
			 String hql2="from OrderLine  where order.orderId ="+or.getOrderId();
			 List<OrderLine> orderlines=this.getHibernateTemplate().find(hql2);			
				 for (OrderLine orderLine : orderlines) {
					line.add(orderLine);
				 }
			
			 System.out.println("orderLine��===>"+line);
		 }
		 System.out.println("��ʷ������ϸ��===>"+line); 
		 return line;
	}
	//���Ҳ鿴ȫ������
	public List<OrderLine> showVenderOrder(Vender vender){
		 System.out.println("�鿴������ʷ����...............������..."+vender.getPurchaser().getPurName());
		 String hql="from OrderLine o where o.venderName='"+vender.getVenShortname()+"'";
		 List<OrderLine> OrderLines=this.getHibernateTemplate().find(hql); 
		/* List<OrderLine> orders=null;
		for(OrderLine line: OrderLines){
			String hq="from Order or where or.orderId='"+line.getOrderId()+"'";
			List<Order> o=this.getHibernateTemplate().find(hq); 
			 orders.add(o.get(0)); 
			 System.out.println("o.get(o)...�õ���.."+o.get(0)); 
		} */
		 System.out.println("����״̬��ļ�¼��===>"+OrderLines);
		 return OrderLines;
	}
	
	//�����޸Ķ���״̬ 
	public void updateOrderStatus(int orderlineid) { 
		System.out.println("====111111111111111111=======bo====?"+orderlineid); 
	 
		 final String hql="update OrderLine o set o.state='���ͨ��' where o.lineId='"+orderlineid+"'";
		 System.out.println("====222222222222222=======bo====?"); 
		 System.out.println(hql);
		 this.getHibernateTemplate().execute(new HibernateCallback(){ 
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query=session.createQuery(hql);
				int i = query.executeUpdate();
				System.out.println(i+"����¼��������.....");
				return null;
			} 
		 });
		 System.out.println("====33333333333=======bo====?"); 
	} 

}
