package com.dao;

import java.util.List;

import com.vo.Order;
import com.vo.OrderLine;
import com.vo.Purchaser;
import com.vo.ReceiptBill;
import com.vo.Vender;
 

public interface IOrderDao {
	
	 //在创建订单前找到订单编号
	public Integer beforeCreateOrder();
	//在下订单的时候先查看收款单里面的数据，然后bo层对其加一，即生成收款单的相应字段
	public ReceiptBill findReceiptBill();
	// 买家提交订单
	public boolean createOrder(Order order);
	
	//买家查看自己的所有订单
	public List<OrderLine> showPurchaserrOrder(Purchaser pur);
	
	//卖家查看全部订单
	public List<OrderLine> showVenderOrder(Vender vender);
	
	// 通过订单号查找订单信息
	public Order getOrderById(Integer orderId);
	// 删除订单明细
	public void delOrder(int  orderlineId);
	
	//卖家修改订单状态
	public void updateOrderStatus(int orderid);
	 
}
