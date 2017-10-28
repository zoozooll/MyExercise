package com.dao;

import java.util.List;

import com.vo.OrderStatus;
import com.vo.Payway;

public interface IBeforeOrderDao {
    //查看所有 的付款方式
	public List<Payway> findPayWay();
	/*//查出订单状态
	public List<OrderStatus> findOrderStatus();
	//根据名字查找订单状态
	 List<OrderStatus> findStatusByName(String name);
 */
}
