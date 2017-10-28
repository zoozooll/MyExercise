package com.dao;

import java.util.List;

import com.vo.DeliveryBill;

public interface IDeliveryBillDao {
	
	 //在创建交货单前
	public DeliveryBill beforeCreateDeliveryBill();
	//卖家生成交货单
	public void createDeliverybill(DeliveryBill deliverybill);
	
	//卖家查看所有交货单
	public List<DeliveryBill> findAllDeliveryBill();
	
	
	
}
