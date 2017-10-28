package com.bo;

import java.util.List;
import java.util.Map;

import com.vo.CartItem;
import com.vo.Order; 
import com.vo.OrderLine;
import com.vo.Purchaser;
import com.vo.Vender;

public interface IOrderBo {

	////在创建订单前找到订单编号
	  public int beforeCreateOrder();
		   
	// 买家提交订单
	public boolean createOrder(String ordername,Map<Integer, CartItem> carts,
			Purchaser purchaser,Integer ordercode , String payway,String orderSource,
			String paylater,
			String invoicetype,
			String invoicehead,
			String sendto,
			String arrivetime,
			String doselfcode,
			String ordermemo, 
			String seensms,
			
			String mobile1,String mobile2);
	 //买家查看历史订单
	public List<OrderLine> showPurchaserOrder(Purchaser p);  
	//卖家查看历史订单
	public List<OrderLine> showVenderOrder(Vender vender);
	
	// 通过订单号查找订单信息
	public Order getOrderById(Integer orderId);
	// 删除订单
	public void delOrder(int orderlineId);
	 
	//卖家修改订单状态
	public void updateOrderStatus(int orderid); 
}
