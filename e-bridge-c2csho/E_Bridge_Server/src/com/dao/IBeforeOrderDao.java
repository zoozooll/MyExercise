package com.dao;

import java.util.List;

import com.vo.OrderStatus;
import com.vo.Payway;

public interface IBeforeOrderDao {
    //�鿴���� �ĸ��ʽ
	public List<Payway> findPayWay();
	/*//�������״̬
	public List<OrderStatus> findOrderStatus();
	//�������ֲ��Ҷ���״̬
	 List<OrderStatus> findStatusByName(String name);
 */
}
