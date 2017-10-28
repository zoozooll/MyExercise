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
 * ����action
 */
public class OrderAction extends BasicAction {
 
	private IOrderBo orderBo;
	
	private String ordername; //��������
    private int ordercode;    //�������
	private String paylater;  //��������
	private String invoicetype;   //��Ʊ���� 
	private String invoicehead;    //��Ʊ̧ͷ
	private String sendto;//�ʹ﷽
	private String arrivetime;//Ҫ�󵽻�ʱ��
	private String doselfcode;//�ͻ��������
	private String ordermemo;//������ע
	private String seensms;//�Ƿ���Ҫ����֪ͨ 
	private String mobile1;//�ֻ�1
	private String mobile2;//�ֻ�2
	private String orderSource;  //������Դ
	
	private String pay;     //���ʽ
	
	//private String storeaddres;  //���� 
	//private String vendername;    //��������
	//private  String venderCode; //���ұ��
	//private double summoney;   //������ϸ����ܽ��
	
	private Integer orderlineId;   //������ϸid
    private Object user;   //�û�

    //<td>��Ʒ����</td><td>��Ʒ����</td> <td>���ҹ�˾����</td><td>���ұ��</td><td>����</td><td>�ܽ��</td>
    
    
     //�ڴ�������ǰ�ҵ��������
    public String beforeCreateOrder(){
    	HttpSession session=this.getRequest().getSession();
		Purchaser purchaser=(Purchaser) session.getAttribute("purchaser");
		
    	//�����û��½
		if(purchaser==null){
			return "login";
		} 
    	orderBo=(IOrderBo) this.getBean("orderBo");  
    	int code=orderBo.beforeCreateOrder();
    	this.getRequest().setAttribute("ordercode", code+1);
    	return "order";
    }

	//��������
	public String createOrder(){
		HttpSession session=this.getRequest().getSession();
		Purchaser purchaser=(Purchaser) session.getAttribute("purchaser");
		
		CartBoImpl cartBo=(CartBoImpl) session.getAttribute("cart");
		Map<Integer,CartItem> carts = cartBo.getCart(); 
		orderBo=(IOrderBo) this.getBean("orderBo");  
		 System.out.println("===========����action===========");
		boolean flag=orderBo.createOrder(ordername,carts,purchaser,ordercode,pay,orderSource,
				                          paylater,invoicetype,invoicehead,sendto,arrivetime,doselfcode,ordermemo,
				                          seensms,mobile1,mobile2);
		//�����ύ�ɹ�
		if(flag==true){
			carts.clear();
			session.setAttribute("cart", cartBo);
			return "success";
		}
		return "fail";
	}
	
    //����idɾ����Ӧ�Ķ�����ϸ
	public String delOrder(){
		orderBo=(IOrderBo) this.getBean("orderBo");  
		System.out.println("ɾ��������id��====>"+orderlineId);
		orderBo.delOrder(orderlineId);
		showPurchaserOrder();
		return "success";
	}
	
	 //�鿴����
	public String showPurchaserOrder() { 
		orderBo=(IOrderBo) this.getBean("orderBo");  
	    System.out.println("orderBo===>"+orderBo);
	         user= this.getRequest().getSession().getAttribute("purchaser");
	         List<OrderLine> orderlines=null;
	         //������
	         if(user instanceof Vender){
	        	  Vender v=(Vender)user;
	        	 orderlines= orderBo.showVenderOrder(v);
	         //����� 	  
	         }else if(user instanceof Purchaser){
	        	 Purchaser p=(Purchaser)user;
	        	 orderlines=orderBo.showPurchaserOrder(p);
	         }
	   
	   this.getRequest().setAttribute("userorderline", orderlines); 
		 return "success";
	} 
	  //�����޸Ķ�����ϸ״̬
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
