package com.bo;

import java.util.List;

import com.vo.DeliveryBill;

public interface IDeliveryBillBo {
	
	 public DeliveryBill beforeDeliveryBillBoImpl();

	//卖家生成交货单
	public void createDeliverybill(DeliveryBill deliverybill);
	
	//卖家查看所有交货单
	public List<DeliveryBill> findAllDeliveryBill();
}
