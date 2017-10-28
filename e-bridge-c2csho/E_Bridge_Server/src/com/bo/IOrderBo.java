package com.bo;

import java.util.List;
import java.util.Map;

import com.vo.CartItem;
import com.vo.Order; 
import com.vo.OrderLine;
import com.vo.Purchaser;
import com.vo.Vender;

public interface IOrderBo {

	////�ڴ�������ǰ�ҵ��������
	  public int beforeCreateOrder();
		   
	// ����ύ����
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
	 //��Ҳ鿴��ʷ����
	public List<OrderLine> showPurchaserOrder(Purchaser p);  
	//���Ҳ鿴��ʷ����
	public List<OrderLine> showVenderOrder(Vender vender);
	
	// ͨ�������Ų��Ҷ�����Ϣ
	public Order getOrderById(Integer orderId);
	// ɾ������
	public void delOrder(int orderlineId);
	 
	//�����޸Ķ���״̬
	public void updateOrderStatus(int orderid); 
}
