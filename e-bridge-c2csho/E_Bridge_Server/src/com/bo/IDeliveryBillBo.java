package com.bo;

import java.util.List;

import com.vo.DeliveryBill;

public interface IDeliveryBillBo {
	
	 public DeliveryBill beforeDeliveryBillBoImpl();

	//�������ɽ�����
	public void createDeliverybill(DeliveryBill deliverybill);
	
	//���Ҳ鿴���н�����
	public List<DeliveryBill> findAllDeliveryBill();
}
