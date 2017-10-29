package com.iskinfor.servicedata.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 订单
 * @author Administrator
 */
public class Order implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String orderId;//订单编号
	private String orderAccount;//订单金额
	private String orderDate;//下单时间
	private String ordeState; //订单状态 00:有效 01:无效 
	private String tranState;//交易状态 00:未支付 01:支付完成
	private String proId;//商品id
	private String proName;//商品名称
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderAccount() {
		return orderAccount;
	}
	public void setOrderAccount(String orderAccount) {
		this.orderAccount = orderAccount;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrdeState() {
		return ordeState;
	}
	public void setOrdeState(String ordeState) {
		this.ordeState = ordeState;
	}
	public String getTranState() {
		return tranState;
	}
	public void setTranState(String tranState) {
		this.tranState = tranState;
	}
	public String getProId() {
		return proId;
	}
	public void setProId(String proId) {
		this.proId = proId;
	}

	

	

}
