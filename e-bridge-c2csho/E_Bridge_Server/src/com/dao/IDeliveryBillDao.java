package com.dao;

import java.util.List;

import com.vo.DeliveryBill;

public interface IDeliveryBillDao {
	
	 //�ڴ���������ǰ
	public DeliveryBill beforeCreateDeliveryBill();
	//�������ɽ�����
	public void createDeliverybill(DeliveryBill deliverybill);
	
	//���Ҳ鿴���н�����
	public List<DeliveryBill> findAllDeliveryBill();
	
	
	
}
