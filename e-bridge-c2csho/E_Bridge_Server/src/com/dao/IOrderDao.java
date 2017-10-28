package com.dao;

import java.util.List;

import com.vo.Order;
import com.vo.OrderLine;
import com.vo.Purchaser;
import com.vo.ReceiptBill;
import com.vo.Vender;
 

public interface IOrderDao {
	
	 //�ڴ�������ǰ�ҵ��������
	public Integer beforeCreateOrder();
	//���¶�����ʱ���Ȳ鿴�տ��������ݣ�Ȼ��bo������һ���������տ����Ӧ�ֶ�
	public ReceiptBill findReceiptBill();
	// ����ύ����
	public boolean createOrder(Order order);
	
	//��Ҳ鿴�Լ������ж���
	public List<OrderLine> showPurchaserrOrder(Purchaser pur);
	
	//���Ҳ鿴ȫ������
	public List<OrderLine> showVenderOrder(Vender vender);
	
	// ͨ�������Ų��Ҷ�����Ϣ
	public Order getOrderById(Integer orderId);
	// ɾ��������ϸ
	public void delOrder(int  orderlineId);
	
	//�����޸Ķ���״̬
	public void updateOrderStatus(int orderid);
	 
}
