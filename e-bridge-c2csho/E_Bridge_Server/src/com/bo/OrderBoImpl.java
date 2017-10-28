package com.bo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dao.IOrderDao;
import com.vo.CartItem;
import com.vo.Order;
import com.vo.OrderLine;
import com.vo.Purchaser;
import com.vo.ReceiptBill;
import com.vo.Vender;

public class OrderBoImpl implements IOrderBo { 
	
	private IOrderDao orderDao;
	
	   //�ڴ�������ǰ�ҵ��������
	public int beforeCreateOrder() { 
		return orderDao.beforeCreateOrder();
	}
	//�ύ����
	public boolean createOrder(String ordername,Map<Integer, CartItem> carts,
			Purchaser purchaser,Integer ordercode, String payway,String orderSource,
			String paylater,
			String invoicetype,
			String invoicehead,
			String sendto,
			String arrivetime,
			String doselfcode,
			String ordermemo, 
			String seensms, 
			String mobile1,String mobile2) {  
		
		    boolean flag = false;
			OrderLine orderline = null;  //������
			Order order=null;           //���� 
			ReceiptBill  receiptbill=null;  //�տ
			
			if(payway != null ){ 
				 /******����******/
				order=new Order();         
				order.setOrderName(ordername);   //�������� 
				order.setOrderCode(ordercode);    //�������(��������) 
				
				order.setOrderSource(orderSource); //������Դ
				order.setPayway(payway);    //���ʽ 
				order.setPaylater(paylater);  //����
			    order.setInvoicetype(invoicetype);  //��Ʊ����   :1 ��ֵ˰ר�÷�Ʊ��2 ��������ͳһ��Ʊ��3 ��������ҵר�÷�Ʊ ',
				order.setInvoicehead(invoicehead); //��Ʊ̧ͷ:��Ʊ̧ͷ��ָ��ȡ��Ʊ�Ĺ�˾���ƻ����������'
			    order.setSendto(sendto);   //�ʹ﷽
			    order.setArrivetime(arrivetime);  //Ҫ�󵽻�ʱ��
			    order.setDoselfcode(doselfcode);  //�ͻ��������
			    order.setOrdermemo(ordermemo);   //������ע
			    order.setSeensms(seensms);    //�Ƿ���Ҫ����֪ͨ
			    order.setMobile1(mobile1);      //�ֻ��绰1
			    order.setMobile2(mobile2);   //�ֻ�2 
			    order.setCreateddate(new Date());   //����ʱ�� 
			   
			    
			    if(carts != null && order != null){
			    for(CartItem item: carts.values()){
			    	//private String storeaddres;  //���� 
			    	//private String vendername;    //��������
			    	//private  String venderCode; //���ұ��
			    	//private double summoney;   //������ϸ����ܽ��
			    	
			    	System.out.println("venderName��=============��"+item.getProduct().getProductGroup().getVender().getVenShortname());
					 System.out.println("sumMoney��=============��"+item.getSumMoney());
					 
					 /***������ϸ**/
					orderline=new OrderLine();                  //����һ��������ϸ
					 
					orderline.setProId(item.getProduct().getProId());                   //��Ʒid
					orderline.setAmount(item.getProductSum());   //��Ʒ����
					System.out.println("item===>"+item);
					System.out.println("item.getProduct()===>"+item.getProduct());
					
					 System.out.println("item.getProduct().getStock()"+item.getProduct().getStock());
					 System.out.println("item.getProduct().getStock().getStoreHouse()"+item.getProduct().getStock().getStoreHouse());
					 
					 
					orderline.setStoreaddres(item.getProduct().getStock().getStoreHouse().getStoreAddress());       //����
					orderline.setVenderName(item.getProduct().getProductGroup().getVender().getVenShortname()); //��������
					orderline.setVenderCode(String.valueOf(item.getProduct().getProId()));        //���ұ��
					orderline.setSumMoney(item.getSumMoney());                //�ܽ��
					orderline.setState("�����");    //����״̬)
					
					//ReceiptBill r=orderDao.findReceiptBill(); 
					/***�տ**/
					receiptbill=new ReceiptBill(null, null, null, null, null, null, null, null, 0, 0,
												null, null, null, null,null, null, null);   
					/*receiptbill.setOrderCode(r.getOrderCode()+1); //������� 
					receiptbill.setReceiptcode(r.getReceiptcode()+1);   //�տ���;
					receiptbill.setInvoiceno(r.getInvoiceno()+1);    //��Ʊ��
					receiptbill.setInvoicedate(new Date());   //��Ʊ����
					receiptbill.setProductcode(String.valueOf(item.getProduct().getProId()));   //��Ʒ����(��Ʒid)
					receiptbill.setPurchasername(item.getProduct().getProductGroup().getVender().getVenShortname());
					receiptbill.setAmount(item.getProductSum());   //��Ʒ����
					receiptbill.setPrice(item.getBaseprice());   //��Ʒ����
					receiptbill.setMoney(item.getSumMoney());            //�ܽ��
					receiptbill.setReceiptdate(new Date(System.currentTimeMillis()+30*24*60*60*1000));  //�տ�����
					receiptbill.setSalesdate(new Date());   //��������
					receiptbill.setDetailmemo("��ϸ˵��");  //��ϸ˵��
					receiptbill.setOwemoney(888888.8);   //Ƿ����
					receiptbill.setAlreadymoney(2000.0); //�Ѹ����
					receiptbill.setTermdate(new Date(System.currentTimeMillis()+30*24*60*60*1000));   //������
					receiptbill.setHappendate(new Date());  //�������� 
				*/	
					orderline.setReceipt(receiptbill);  //
					receiptbill.setOrderLine(orderline);  //���ö�����ϸ�븶�֮��Ĺ�ϵ
					
					orderline.setOrder(order);   //�����붩���Ĺ�ϵ 
					order.getItems().add(orderline); //���ù�ϵ
					 
				}
			    System.out.println("commit 1111111111111111111111...");
			    flag=orderDao.createOrder(order); 
			    System.out.println("commit 22222222222222222222222222...");
			}
			} 
			return flag; 
	}
       //ɾ��������ϸ
	public void delOrder(int  orderlineId) { 
		orderDao.delOrder(orderlineId);
	}
   //����id�õ�����
	public Order getOrderById(Integer orderId) {
		Order order=orderDao.getOrderById(orderId);
		return order;
	}
   //��Ҳ鿴��ʷ����
	public List<OrderLine> showPurchaserOrder(Purchaser p){ 
		return orderDao.showPurchaserrOrder(p);
	} 
	//���Ҳ鿴��ʷ����
	public List<OrderLine> showVenderOrder(Vender vender){
		return orderDao.showVenderOrder(vender);
	}
    //�����޸Ķ���״̬
	public void updateOrderStatus(int orderid) { 
		System.out.println("===========bo====?"+orderid);
		orderDao.updateOrderStatus(orderid);
	}
	 

    public IOrderDao getOrderDao() {
		return orderDao;
	}
	public void setOrderDao(IOrderDao orderDao) {
		this.orderDao = orderDao;
	} 
}
