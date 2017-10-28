package com.vo;

import java.util.HashSet;
import java.util.Set;

/**
 * 付款方式
 */

public class Payway implements java.io.Serializable {

	// Fields

	private Integer payId;
	private String payName;
    //与订单关系
	private Set<Order> order=new HashSet<Order>();
	 

	public Set<Order> getOrder() {
		return order;
	}

	public void setOrder(Set<Order> order) {
		this.order = order;
	}

	/** default constructor */
	public Payway() {
	}

	/** full constructor */
	public Payway(String payName) {
		this.payName = payName;
	}

	// Property accessors

	public Integer getPayId() {
		return this.payId;
	}

	public void setPayId(Integer payId) {
		this.payId = payId;
	}

	public String getPayName() {
		return this.payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

}