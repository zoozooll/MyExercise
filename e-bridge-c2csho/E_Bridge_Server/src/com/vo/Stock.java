package com.vo;

import java.util.Set;

/**
 * ²úÆ·¿â´æ
 */

public class Stock implements java.io.Serializable {

	// Fields

	private Integer stoId;
	//private Integer productId;
	//private Integer storeId;
	private Integer stoAmount;
	private Integer stoMax;
	private Integer stoMin;
	private Integer stoBuyprice;
	private Integer stoSellprice;
	private Product product;
	
	private StoreHouse storeHouse;

	// Constructors

	/** default constructor */
	public Stock() {
	}

	/** full constructor */


	// Property accessors

	public Integer getStoId() {
		return this.stoId;
	}

	public void setStoId(Integer stoId) {
		this.stoId = stoId;
	}



	public Integer getStoAmount() {
		return this.stoAmount;
	}

	public void setStoAmount(Integer stoAmount) {
		this.stoAmount = stoAmount;
	}

	public Integer getStoMax() {
		return this.stoMax;
	}

	public void setStoMax(Integer stoMax) {
		this.stoMax = stoMax;
	}

	public Integer getStoMin() {
		return this.stoMin;
	}

	public void setStoMin(Integer stoMin) {
		this.stoMin = stoMin;
	}

	public Integer getStoBuyprice() {
		return this.stoBuyprice;
	}

	public void setStoBuyprice(Integer stoBuyprice) {
		this.stoBuyprice = stoBuyprice;
	}

	public Integer getStoSellprice() {
		return this.stoSellprice;
	}

	public void setStoSellprice(Integer stoSellprice) {
		this.stoSellprice = stoSellprice;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public StoreHouse getStoreHouse() {
		return storeHouse;
	}

	public void setStoreHouse(StoreHouse storeHouse) {
		this.storeHouse = storeHouse;
	}

}