package com.vo;

import java.util.Set;

/**
 * OrderLine entity. @author MyEclipse Persistence Tools
 */

public class OrderLine implements java.io.Serializable {

	// Fields

	private Integer lineId;
	private Integer orderId;
	private Integer proId;
	private Integer amount;
	private String venderName;   //卖家名称
	private String venderCode;   //卖家
	private String storeaddres;  //库存地
	private double  sumMoney;   //总金额
	 private String state;      //订单状态
	 //与产品的一对多关系;
	 private Set<Product> products;


	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	//与订单之间的关系
    private Order order;
    public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	//与收款单的关系一对一
    private ReceiptBill receipt;
     
	public ReceiptBill getReceipt() {
		return receipt;
	}

	public void setReceipt(ReceiptBill receipt) {
		this.receipt = receipt;
	}

	public String getStoreaddres() {
		return storeaddres;
	}

	public void setStoreaddres(String storeaddres) {
		this.storeaddres = storeaddres;
	}

	public String getVenderName() {
		return venderName;
	}

	public void setVenderName(String venderName) {
		this.venderName = venderName;
	}

	public String getVenderCode() {
		return venderCode;
	}

	public void setVenderCode(String venderCode) {
		this.venderCode = venderCode;
	}
 
	public OrderLine( Integer lineId, Integer orderId,Integer proId,Integer amount,Order order,
			  String storeaddres, String venderCode, String state,String venderName,double sumMoney) {
		super();
		this.amount = amount;
		this.lineId = lineId;	
		this.orderId = orderId;
		this.proId = proId;
		this.state=state;
		this.storeaddres = storeaddres;
		this.venderCode = venderCode;
		this.venderName = venderName;
		this.sumMoney=sumMoney;
	}

	public double getSumMoney() {
		return sumMoney;
	}

	public void setSumMoney(double sumMoney) {
		this.sumMoney = sumMoney;
	}

	/** default constructor */
	public OrderLine() {
	}
 

	// Property accessors

	public Integer getLineId() {
		return this.lineId;
	}

	public void setLineId(Integer lineId) {
		this.lineId = lineId;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getProId() {
		return this.proId;
	}

	public void setProId(Integer proId) {
		this.proId = proId;
	}

	public Integer getAmount() {
		return this.amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}