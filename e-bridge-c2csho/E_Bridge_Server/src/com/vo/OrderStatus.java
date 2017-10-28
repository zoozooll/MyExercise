package com.vo;

import java.util.HashSet;
import java.util.Set;

/**
 * 订单状态
 */

public class OrderStatus implements java.io.Serializable {

	// Fields

	private Integer statId;
	private String statStatus;
    //与订单是一对多的关系 
	 private Set<Order> order=new HashSet<Order>();
	 

	public Set<Order> getOrder() {
		return order;
	}

	public void setOrder(Set<Order> order) {
		this.order = order;
	}

	/** default constructor */
	public OrderStatus() {
	}

	/** full constructor */
	public OrderStatus(String statStatus) {
		this.statStatus = statStatus;
	}

	// Property accessors

	public Integer getStatId() {
		return this.statId;
	}

	public void setStatId(Integer statId) {
		this.statId = statId;
	}

	public String getStatStatus() {
		return this.statStatus;
	}

	public void setStatStatus(String statStatus) {
		this.statStatus = statStatus;
	}

}