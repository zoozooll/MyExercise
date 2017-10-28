package com.bo;

import java.util.List;

import com.dao.IDeliveryBillDao;
import com.vo.DeliveryBill;

public class DeliveryBillBoImpl implements IDeliveryBillBo {

	private IDeliveryBillDao deliveryDao;
	
     ////�ڴ�������ǰ�ҵ��������
	  public DeliveryBill beforeDeliveryBillBoImpl(){
		  return deliveryDao.beforeCreateDeliveryBill();
	  }
	  
	//���ɽ�����
	public void createDeliverybill(DeliveryBill deliverybill) {
		deliveryDao.createDeliverybill(deliverybill);
		
	}
    //�鿴���еĽ�����
	public List<DeliveryBill> findAllDeliveryBill() { 
		return deliveryDao.findAllDeliveryBill();
	}

	
	public IDeliveryBillDao getDeliveryDao() {
		return deliveryDao;
	}
	public void setDeliveryDao(IDeliveryBillDao deliveryDao) {
		this.deliveryDao = deliveryDao;
	}
	
}
