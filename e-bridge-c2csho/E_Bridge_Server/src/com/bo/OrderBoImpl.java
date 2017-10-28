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
	
	   //在创建订单前找到订单编号
	public int beforeCreateOrder() { 
		return orderDao.beforeCreateOrder();
	}
	//提交订单
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
			OrderLine orderline = null;  //订单项
			Order order=null;           //订单 
			ReceiptBill  receiptbill=null;  //收款单
			
			if(payway != null ){ 
				 /******订单******/
				order=new Order();         
				order.setOrderName(ordername);   //订单名称 
				order.setOrderCode(ordercode);    //订单编号(订单代码) 
				
				order.setOrderSource(orderSource); //订单来源
				order.setPayway(payway);    //付款方式 
				order.setPaylater(paylater);  //帐期
			    order.setInvoicetype(invoicetype);  //发票类型   :1 增值税专用发票、2 货物销售统一发票、3 货物运输业专用发票 ',
				order.setInvoicehead(invoicehead); //发票抬头:发票抬头是指收取发票的公司名称或个人姓名。'
			    order.setSendto(sendto);   //送达方
			    order.setArrivetime(arrivetime);  //要求到货时间
			    order.setDoselfcode(doselfcode);  //客户自主编号
			    order.setOrdermemo(ordermemo);   //订单备注
			    order.setSeensms(seensms);    //是否需要短信通知
			    order.setMobile1(mobile1);      //手机电话1
			    order.setMobile2(mobile2);   //手机2 
			    order.setCreateddate(new Date());   //创建时间 
			   
			    
			    if(carts != null && order != null){
			    for(CartItem item: carts.values()){
			    	//private String storeaddres;  //库存地 
			    	//private String vendername;    //卖家名称
			    	//private  String venderCode; //卖家编号
			    	//private double summoney;   //订单明细里的总金额
			    	
			    	System.out.println("venderName是=============》"+item.getProduct().getProductGroup().getVender().getVenShortname());
					 System.out.println("sumMoney是=============》"+item.getSumMoney());
					 
					 /***订单明细**/
					orderline=new OrderLine();                  //生成一个订单明细
					 
					orderline.setProId(item.getProduct().getProId());                   //产品id
					orderline.setAmount(item.getProductSum());   //产品数量
					System.out.println("item===>"+item);
					System.out.println("item.getProduct()===>"+item.getProduct());
					
					 System.out.println("item.getProduct().getStock()"+item.getProduct().getStock());
					 System.out.println("item.getProduct().getStock().getStoreHouse()"+item.getProduct().getStock().getStoreHouse());
					 
					 
					orderline.setStoreaddres(item.getProduct().getStock().getStoreHouse().getStoreAddress());       //库存地
					orderline.setVenderName(item.getProduct().getProductGroup().getVender().getVenShortname()); //卖家名称
					orderline.setVenderCode(String.valueOf(item.getProduct().getProId()));        //卖家编号
					orderline.setSumMoney(item.getSumMoney());                //总金额
					orderline.setState("待审核");    //订单状态)
					
					//ReceiptBill r=orderDao.findReceiptBill(); 
					/***收款单**/
					receiptbill=new ReceiptBill(null, null, null, null, null, null, null, null, 0, 0,
												null, null, null, null,null, null, null);   
					/*receiptbill.setOrderCode(r.getOrderCode()+1); //订单编号 
					receiptbill.setReceiptcode(r.getReceiptcode()+1);   //收款单编号;
					receiptbill.setInvoiceno(r.getInvoiceno()+1);    //发票号
					receiptbill.setInvoicedate(new Date());   //核票日期
					receiptbill.setProductcode(String.valueOf(item.getProduct().getProId()));   //产品代码(产品id)
					receiptbill.setPurchasername(item.getProduct().getProductGroup().getVender().getVenShortname());
					receiptbill.setAmount(item.getProductSum());   //产品数量
					receiptbill.setPrice(item.getBaseprice());   //产品单价
					receiptbill.setMoney(item.getSumMoney());            //总金额
					receiptbill.setReceiptdate(new Date(System.currentTimeMillis()+30*24*60*60*1000));  //收款日期
					receiptbill.setSalesdate(new Date());   //售销日期
					receiptbill.setDetailmemo("详细说明");  //详细说明
					receiptbill.setOwemoney(888888.8);   //欠款金额
					receiptbill.setAlreadymoney(2000.0); //已付金额
					receiptbill.setTermdate(new Date(System.currentTimeMillis()+30*24*60*60*1000));   //到期日
					receiptbill.setHappendate(new Date());  //发生日期 
				*/	
					orderline.setReceipt(receiptbill);  //
					receiptbill.setOrderLine(orderline);  //设置订单明细与付款单之间的关系
					
					orderline.setOrder(order);   //设置与订单的关系 
					order.getItems().add(orderline); //设置关系
					 
				}
			    System.out.println("commit 1111111111111111111111...");
			    flag=orderDao.createOrder(order); 
			    System.out.println("commit 22222222222222222222222222...");
			}
			} 
			return flag; 
	}
       //删除订单明细
	public void delOrder(int  orderlineId) { 
		orderDao.delOrder(orderlineId);
	}
   //根据id得到订单
	public Order getOrderById(Integer orderId) {
		Order order=orderDao.getOrderById(orderId);
		return order;
	}
   //买家查看历史订单
	public List<OrderLine> showPurchaserOrder(Purchaser p){ 
		return orderDao.showPurchaserrOrder(p);
	} 
	//卖家查看历史订单
	public List<OrderLine> showVenderOrder(Vender vender){
		return orderDao.showVenderOrder(vender);
	}
    //卖家修改订单状态
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
