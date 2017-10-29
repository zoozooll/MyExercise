package com.iskinfor.servicedata.pojo;
/**
 * 朵朵记录
 * @author Administrator
 */
public class DuoDuoRecod {
	private String userId;//买家
	private String proName;//书名
	private String orderAccount;//成交价格
	private String tanVolum;//数量
	private String orderData;//交易时间
	public String getOrderData() {
		return orderData;
	}
	public void setOrderData(String orderData) {
		this.orderData = orderData;
	}
	public String getOrderState() {
		return orderState;
	}
	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}
	private String orderState;//交易状态

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public String getOrderAccount() {
		return orderAccount;
	}
	public void setOrderAccount(String orderAccount) {
		this.orderAccount = orderAccount;
	}
	public String getTanVolum() {
		return tanVolum;
	}
	public void setTanVolum(String tanVolum) {
		this.tanVolum = tanVolum;
	}
}
