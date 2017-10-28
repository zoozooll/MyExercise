package com.vo;

import java.sql.Timestamp; 
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
 

/**
 * 订单
 */

public class Order implements java.io.Serializable {

	// Fields

	private Integer orderId;
	private Integer orderCode;
	private String orderName;
	private String orderSource; 
	private Integer payId;
 
	private String paylater;    
	private String invoicetype; 
	private String invoicehead; 
	private String sendto;
	private String arrivetime;
	private String doselfcode;
	private String ordermemo;
	private String seensms;
	private String mobile1;
	private String mobile2;
	private Integer statId;
	private Date createddate;
	
	
	private String payway;   //付款方式
   
	 
	//与付款方式是多对一的关系
	//private Payway payway;
	//设置Order与OrderLine之间的一对多关系
	private Set<OrderLine> items = new HashSet<OrderLine>();
	
	 
	public Set<OrderLine> getItems() {
		return items;
	}

	public void setItems(Set<OrderLine> items) {
		this.items = items;
	}

	/** default constructor */
	public Order(String orderName ) {
		this.orderName = orderName;
	}
    
	public String getPayway() {
		return payway;
	}

	public void setPayway(String payway) {
		this.payway = payway;
	}

	public Order() {
		super();
	}

	/** full constructor */
	public Order(Integer orderCode, String venderName, String venderCode,
			String orderName, String orderSource,
			Integer payId,  String paylater,
			String storeaddres, String invoicetype, 
			String invoicehead, String sendto, String arrivetime,
			String doselfcode, String ordermemo, String seensms,String payway,
			String mobile1, String mobile2, Integer statId,
			Timestamp createddate) {
		this.orderCode = orderCode;
	 
		this.orderName = orderName;
		this.orderSource = orderSource; 
		this.payId = payId;
		this.payway=payway;
		this.paylater = paylater; 
		this.invoicetype = invoicetype; 
		this.invoicehead = invoicehead;
		this.sendto = sendto;
		this.arrivetime = arrivetime;
		this.doselfcode = doselfcode;
		this.ordermemo = ordermemo;
		this.seensms = seensms;
		this.mobile1 = mobile1;
		this.mobile2 = mobile2;
		 
		this.statId = statId;
		this.createddate = createddate;
	}

	// Property accessors

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	} 
	
	
	
	
	public Integer getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderName() {
		return this.orderName;
	}


	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getOrderSource() {
		return this.orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

 

	public Integer getPayId() {
		return this.payId;
	}

	public void setPayId(Integer payId) {
		this.payId = payId;
	}
 
	public String getPaylater() {
		return this.paylater;
	}

	public void setPaylater(String paylater) {
		this.paylater = paylater;
	}
 
	public String getInvoicetype() {
		return this.invoicetype;
	}

	public void setInvoicetype(String invoicetype) {
		this.invoicetype = invoicetype;
	}
 
	public String getInvoicehead() {
		return this.invoicehead;
	}

	public void setInvoicehead(String invoicehead) {
		this.invoicehead = invoicehead;
	}

	public String getSendto() {
		return this.sendto;
	}

	public void setSendto(String sendto) {
		this.sendto = sendto;
	}

	public String getArrivetime() {
		return this.arrivetime;
	}

	public void setArrivetime(String arrivetime) {
		this.arrivetime = arrivetime;
	}

	public String getDoselfcode() {
		return this.doselfcode;
	}

	public void setDoselfcode(String doselfcode) {
		this.doselfcode = doselfcode;
	}

	public String getOrdermemo() {
		return this.ordermemo;
	}

	public void setOrdermemo(String ordermemo) {
		this.ordermemo = ordermemo;
	}

	public String getSeensms() {
		return this.seensms;
	}

	public void setSeensms(String seensms) {
		this.seensms = seensms;
	}

	public String getMobile1() {
		return this.mobile1;
	}

	public void setMobile1(String mobile1) {
		this.mobile1 = mobile1;
	}

	public String getMobile2() {
		return this.mobile2;
	}

	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}

	public Integer getStatId() {
		return this.statId;
	}

	public void setStatId(Integer statId) {
		this.statId = statId;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}
 
 


}