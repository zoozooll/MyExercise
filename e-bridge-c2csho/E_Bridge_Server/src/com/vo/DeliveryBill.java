package com.vo;

import java.util.HashSet;
import java.util.Set;

/**
 * 交货单
 */

public class DeliveryBill implements java.io.Serializable {

	// Fields

	private Integer deliId;
	private Integer orderLineId;   //订单明细id
	private String deliveryCode;
	private Integer amount;
	private String clientname;
	private String clientcode;
	private String invoiceno;
	private String province;
	private String city;
	private String address;
	private String contactor;
	private String sendtype;
	private String carryplace;
	private String specialnote;
	private String signstandard;
	private String sendcarnote;
	private String signnote;
	private String pickupman;
	private String contacttype;
	private String contactphone;

	//与产品明细是一对多的关系
	private Set<ProductDesc> productsdesc=new HashSet<ProductDesc>();
	
	
	
	// Constructors

	/** default constructor */
	public DeliveryBill() {
	}

	/** full constructor */
	public DeliveryBill(Integer orderLineId, String deliveryCode,
			  Integer amount, String clientname,
			String clientcode, String invoiceno, String province, String city,
			String address, String contactor, String sendtype,
			String carryplace, String specialnote, String signstandard,
			String sendcarnote, String signnote, String pickupman,
			String contacttype, String contactphone) {
		this.orderLineId = orderLineId;
		this.deliveryCode = deliveryCode; 
		this.amount = amount;
		this.clientname = clientname;
		this.clientcode = clientcode;
		this.invoiceno = invoiceno;
		this.province = province;
		this.city = city;
		this.address = address;
		this.contactor = contactor;
		this.sendtype = sendtype;
		this.carryplace = carryplace;
		this.specialnote = specialnote;
		this.signstandard = signstandard;
		this.sendcarnote = sendcarnote;
		this.signnote = signnote;
		this.pickupman = pickupman;
		this.contacttype = contacttype;
		this.contactphone = contactphone;
	}

	// Property accessors

	public Integer getDeliId() {
		return this.deliId;
	}

	public void setDeliId(Integer deliId) {
		this.deliId = deliId;
	}
 

	public Integer getOrderLineId() {
		return orderLineId;
	}

	public void setOrderLineId(Integer orderLineId) {
		this.orderLineId = orderLineId;
	}

	public String getDeliveryCode() {
		return this.deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}
 

	public Integer getAmount() {
		return this.amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getClientname() {
		return this.clientname;
	}

	public void setClientname(String clientname) {
		this.clientname = clientname;
	}

	public String getClientcode() {
		return this.clientcode;
	}

	public void setClientcode(String clientcode) {
		this.clientcode = clientcode;
	}

	public String getInvoiceno() {
		return this.invoiceno;
	}

	public void setInvoiceno(String invoiceno) {
		this.invoiceno = invoiceno;
	}

	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactor() {
		return this.contactor;
	}

	public void setContactor(String contactor) {
		this.contactor = contactor;
	}

	public String getSendtype() {
		return this.sendtype;
	}

	public void setSendtype(String sendtype) {
		this.sendtype = sendtype;
	}

	public String getCarryplace() {
		return this.carryplace;
	}

	public void setCarryplace(String carryplace) {
		this.carryplace = carryplace;
	}

	public String getSpecialnote() {
		return this.specialnote;
	}

	public void setSpecialnote(String specialnote) {
		this.specialnote = specialnote;
	}

	public String getSignstandard() {
		return this.signstandard;
	}

	public void setSignstandard(String signstandard) {
		this.signstandard = signstandard;
	}

	public String getSendcarnote() {
		return this.sendcarnote;
	}

	public void setSendcarnote(String sendcarnote) {
		this.sendcarnote = sendcarnote;
	}

	public String getSignnote() {
		return this.signnote;
	}

	public void setSignnote(String signnote) {
		this.signnote = signnote;
	}

	public String getPickupman() {
		return this.pickupman;
	}

	public void setPickupman(String pickupman) {
		this.pickupman = pickupman;
	}

	public String getContacttype() {
		return this.contacttype;
	}

	public void setContacttype(String contacttype) {
		this.contacttype = contacttype;
	}

	public String getContactphone() {
		return this.contactphone;
	}

	public void setContactphone(String contactphone) {
		this.contactphone = contactphone;
	}

	public Set<ProductDesc> getProductsdesc() {
		return productsdesc;
	}

	public void setProductsdesc(Set<ProductDesc> productsdesc) {
		this.productsdesc = productsdesc;
	}

}