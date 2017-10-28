package com.vo;

/**
 * 产品明细
 */

public class ProductDesc implements java.io.Serializable {

	// Fields

	private Integer prodecId;
	private Integer deliverybillId;
	private String productCode;
	private String productName;
	private Integer productCount;
	private Double weight;
	private Double solidity;
	private String factory;
	private String storeaddress;

	//多对一的关系
	private DeliveryBill deliveryBill;
	
	
	
	
	// Constructors

	/** default constructor */
	public ProductDesc() {
	}

	/** full constructor */
	public ProductDesc(Integer deliverybillId, String productCode,
			String productName, Integer productCount, Double weight,
			Double solidity, String factory, String storeaddress) {
		this.deliverybillId = deliverybillId;
		this.productCode = productCode;
		this.productName = productName;
		this.productCount = productCount;
		this.weight = weight;
		this.solidity = solidity;
		this.factory = factory;
		this.storeaddress = storeaddress;
	}

	// Property accessors

	public Integer getProdecId() {
		return this.prodecId;
	}

	public void setProdecId(Integer prodecId) {
		this.prodecId = prodecId;
	}

	public Integer getDeliverybillId() {
		return this.deliverybillId;
	}

	public void setDeliverybillId(Integer deliverybillId) {
		this.deliverybillId = deliverybillId;
	}

	public String getProductCode() {
		return this.productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductCount() {
		return this.productCount;
	}

	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}

	public Double getWeight() {
		return this.weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getSolidity() {
		return this.solidity;
	}

	public void setSolidity(Double solidity) {
		this.solidity = solidity;
	}

	public String getFactory() {
		return this.factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public String getStoreaddress() {
		return this.storeaddress;
	}

	public void setStoreaddress(String storeaddress) {
		this.storeaddress = storeaddress;
	}

	public DeliveryBill getDeliveryBill() {
		return deliveryBill;
	}

	public void setDeliveryBill(DeliveryBill deliveryBill) {
		this.deliveryBill = deliveryBill;
	}

}