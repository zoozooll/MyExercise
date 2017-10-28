package com.vo;

import java.util.Date;

/**
 * 收款单
 */

public class ReceiptBill implements java.io.Serializable {

	// Fields

	private Integer receiId;
	private Integer orderLineId;
	private Integer orderCode;
	private Integer receiptcode;
	private Integer invoiceno;
	private Date invoicedate;
	private String productcode;
	private String purchasername;
	private Integer amount;
	private double price;
	private double money;
	private Date receiptdate;
	private Date salesdate;
	private String detailmemo;
	private Double owemoney;
	private Double alreadymoney;
	private Date termdate;
	private Date happendate;
 
	//与订单详细之间一对一 
	private OrderLine orderLine;
	
	 
	public OrderLine getOrderLine() {
		return orderLine;
	}

	public void setOrderLine(OrderLine orderLine) {
		this.orderLine = orderLine;
	}

	/** default constructor */
	public ReceiptBill() {
	}

	/** full constructor */
	public ReceiptBill(Integer orderLineId, Integer orderCode, Integer receiptcode,
			Integer invoiceno, Date invoicedate, String productcode,
			String purchasername, Integer amount, double price, double money,
			Date receiptdate, Date salesdate, String detailmemo,
			Double owemoney, Double alreadymoney, Date termdate,
			Date happendate) {
		this.orderLineId = orderLineId;
		this.orderCode = orderCode;
		this.receiptcode = receiptcode;
		this.invoiceno = invoiceno;
		this.invoicedate = invoicedate;
		this.productcode = productcode;
		this.purchasername = purchasername;
		this.amount = amount;
		this.price = price;
		this.money = money;
		this.receiptdate = receiptdate;
		this.salesdate = salesdate;
		this.detailmemo = detailmemo;
		this.owemoney = owemoney;
		this.alreadymoney = alreadymoney;
		this.termdate = termdate;
		this.happendate = happendate;
	}

	// Property accessors

	public Integer getReceiId() {
		return this.receiId;
	}

	public void setReceiId(Integer receiId) {
		this.receiId = receiId;
	}
 
 

	public Integer getOrderLineId() {
		return orderLineId;
	}

	public void setOrderLineId(Integer orderLineId) {
		this.orderLineId = orderLineId;
	}

	public Integer getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}
 

	public Integer getReceiptcode() {
		return receiptcode;
	}

	public void setReceiptcode(Integer receiptcode) {
		this.receiptcode = receiptcode;
	}

	public Integer getInvoiceno() {
		return invoiceno;
	}

	public void setInvoiceno(Integer invoiceno) {
		this.invoiceno = invoiceno;
	}
 
 

	public String getProductcode() {
		return this.productcode;
	}

	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}

	public String getPurchasername() {
		return this.purchasername;
	}

	public void setPurchasername(String purchasername) {
		this.purchasername = purchasername;
	}

	public Integer getAmount() {
		return this.amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
 

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
 
 

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}
 
	public String getDetailmemo() {
		return this.detailmemo;
	}

	public void setDetailmemo(String detailmemo) {
		this.detailmemo = detailmemo;
	}

	public Double getOwemoney() {
		return this.owemoney;
	}

	public void setOwemoney(Double owemoney) {
		this.owemoney = owemoney;
	}

	public Double getAlreadymoney() {
		return this.alreadymoney;
	}

	public void setAlreadymoney(Double alreadymoney) {
		this.alreadymoney = alreadymoney;
	}
 
 

	public Date getInvoicedate() {
		return invoicedate;
	}

	public void setInvoicedate(Date invoicedate) {
		this.invoicedate = invoicedate;
	}

	public Date getReceiptdate() {
		return receiptdate;
	}

	public void setReceiptdate(Date receiptdate) {
		this.receiptdate = receiptdate;
	}

	public Date getSalesdate() {
		return salesdate;
	}

	public void setSalesdate(Date salesdate) {
		this.salesdate = salesdate;
	}

	public Date getTermdate() {
		return termdate;
	}

	public void setTermdate(Date termdate) {
		this.termdate = termdate;
	}

	public Date getHappendate() {
		return happendate;
	}

	public void setHappendate(Date happendate) {
		this.happendate = happendate;
	}

}