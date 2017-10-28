package com.vo;

import java.util.HashSet;
import java.util.Set;

/**
 * ����״̬
 */

public class OrderStatus implements java.io.Serializable {

	// Fields

	private Integer statId;
	private String statStatus;
    //�붩����һ�Զ�Ĺ�ϵ 
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