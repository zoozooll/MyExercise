package com.action;
 

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.bo.CartBoImpl;
import com.bo.IOrderBo;
import com.vo.CartItem;
import com.vo.Order;
import com.vo.OrderLine;
import com.vo.Payway;
import com.vo.Purchaser;
import com.vo.Vender;

/*
 * 订单action
 */
public class OrderAction extends BasicAction {
 
	private IOrderBo orderBo;
	
	private String ordername; //订单名称
    private int ordercode;    //订单编号
	private String paylater;  //付款日期
	private String invoicetype;   //发票类型 
	private String invoicehead;    //发票抬头
	private String sendto;//送达方
	private String arrivetime;//要求到货时间
	private String doselfcode;//客户自主编号
	private String ordermemo;//订单备注
	private String seensms;//是否需要短信通知 
	private String mobile1;//手机1
	private String mobile2;//手机2
	private String orderSource;  //订单来源
	
	private String pay;     //付款方式
	
	//private String storeaddres;  //库存地 
	//private String vendername;    //卖家名称
	//private  String venderCode; //卖家编号
	//private double summoney;   //订单明细里的总金额
	
	private Integer orderlineId;   //订单明细id
    private Object user;   //用户

    //<td>产品名称</td><td>产品数量</td> <td>卖家公司名称</td><td>卖家编号</td><td>库存地</td><td>总金额</td>
    
    
     //在创建订单前找到订单编号
    public String beforeCreateOrder(){
    	HttpSession session=this.getRequest().getSession();
		Purchaser purchaser=(Purchaser) session.getAttribute("purchaser");
		
    	//如果还没登陆
		if(purchaser==null){
			return "login";
		} 
    	orderBo=(IOrderBo) this.getBean("orderBo");  
    	int code=orderBo.beforeCreateOrder();
    	this.getRequest().setAttribute("ordercode", code+1);
    	return "order";
    }

	//创建订单
	public String createOrder(){
		HttpSession session=this.getRequest().getSession();
		Purchaser purchaser=(Purchaser) session.getAttribute("purchaser");
		
		CartBoImpl cartBo=(CartBoImpl) session.getAttribute("cart");
		Map<Integer,CartItem> carts = cartBo.getCart(); 
		orderBo=(IOrderBo) this.getBean("orderBo");  
		 System.out.println("===========订单action===========");
		boolean flag=orderBo.createOrder(ordername,carts,purchaser,ordercode,pay,orderSource,
				                          paylater,invoicetype,invoicehead,sendto,arrivetime,doselfcode,ordermemo,
				                          seensms,mobile1,mobile2);
		//订单提交成功
		if(flag==true){
			carts.clear();
			session.setAttribute("cart", cartBo);
			return "success";
		}
		return "fail";
	}
	
    //根据id删除相应的订单明细
	public String delOrder(){
		orderBo=(IOrderBo) this.getBean("orderBo");  
		System.out.println("删除订单的id是====>"+orderlineId);
		orderBo.delOrder(orderlineId);
		showPurchaserOrder();
		return "success";
	}
	
	 //查看订单
	public String showPurchaserOrder() { 
		orderBo=(IOrderBo) this.getBean("orderBo");  
	    System.out.println("orderBo===>"+orderBo);
	         user= this.getRequest().getSession().getAttribute("purchaser");
	         List<OrderLine> orderlines=null;
	         //是卖家
	         if(user instanceof Vender){
	        	  Vender v=(Vender)user;
	        	 orderlines= orderBo.showVenderOrder(v);
	         //是买家 	  
	         }else if(user instanceof Purchaser){
	        	 Purchaser p=(Purchaser)user;
	        	 orderlines=orderBo.showPurchaserOrder(p);
	         }
	   
	   this.getRequest().setAttribute("userorderline", orderlines); 
		 return "success";
	} 
	  //卖家修改订单明细状态
	public String updateOrderStatus() { 
		orderBo=(IOrderBo) this.getBean("orderBo");  
		System.out.println("orderid=====>"+orderlineId);
		orderBo.updateOrderStatus(orderlineId); 
		System.out.println("11111111111111111111111111111=====>"+new DeliveryBillAction().beforeCreateDeliveryBill());
		new DeliveryBillAction().beforeCreateDeliveryBill();
		showPurchaserOrder();
		 return "success";
	}

	public IOrderBo getOrderBo() {
		return orderBo;
	}
 
	public void setOrderBo(IOrderBo orderBo) {
		this.orderBo = orderBo;
	}
   
   
 
	public String getPay() {
		return pay;
	}

	public void setPay(String pay) {
		this.pay = pay;
	}

	public String getPaylater() {
		return paylater;
	}
 
	public void setPaylater(String paylater) {
		this.paylater = paylater;
	}




	public String getInvoicetype() {
		return invoicetype;
	}




	public void setInvoicetype(String invoicetype) {
		this.invoicetype = invoicetype;
	}


	
	
	public String getInvoicehead() {
		return invoicehead;
	}




	public void setInvoicehead(String invoicehead) {
		this.invoicehead = invoicehead;
	}



	public String getSendto() {
		return sendto;
	}




	public void setSendto(String sendto) {
		this.sendto = sendto;
	}




	public String getArrivetime() {
		return arrivetime;
	}




	public void setArrivetime(String arrivetime) {
		this.arrivetime = arrivetime;
	}




	public String getDoselfcode() {
		return doselfcode;
	}




	public void setDoselfcode(String doselfcode) {
		this.doselfcode = doselfcode;
	}




	public String getOrdermemo() {
		return ordermemo;
	}




	public void setOrdermemo(String ordermemo) {
		this.ordermemo = ordermemo;
	}




	public String getSeensms() {
		return seensms;
	}




	public void setSeensms(String seensms) {
		this.seensms = seensms;
	}

 
	public String getMobile1() {
		return mobile1;
	}
 

	public void setMobile1(String mobile1) {
		this.mobile1 = mobile1;
	}
 
	public String getMobile2() {
		return mobile2;
	}
 
	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}
 

	public Integer getOrderlineId() {
		return orderlineId;
	}

	public void setOrderlineId(Integer orderlineId) {
		this.orderlineId = orderlineId;
	}

	public Object getUser() {
		return user;
	}

	public void setUser(Object user) {
		this.user = user;
	}

	public String getOrdername() {
		return ordername;
	}

	public void setOrdername(String ordername) {
		this.ordername = ordername;
	}

	public int getOrdercode() {
		return ordercode;
	}

	public void setOrdercode(int ordercode) {
		this.ordercode = ordercode;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

}
