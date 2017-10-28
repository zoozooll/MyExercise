package com.bo;

import java.util.List;

import com.dao.IDeliveryBillDao;
import com.vo.DeliveryBill;

public class DeliveryBillBoImpl implements IDeliveryBillBo {

	private IDeliveryBillDao deliveryDao;
	
     ////在创建订单前找到订单编号
	  public DeliveryBill beforeDeliveryBillBoImpl(){
		  return deliveryDao.beforeCreateDeliveryBill();
	  }
	  
	//生成交货单
	public void createDeliverybill(DeliveryBill deliverybill) {
		deliveryDao.createDeliverybill(deliverybill);
		
	}
    //查看所有的交货单
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
