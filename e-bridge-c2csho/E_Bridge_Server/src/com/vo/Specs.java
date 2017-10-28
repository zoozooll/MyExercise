package com.vo;

/**
 * 参数规格
 */

public class Specs implements java.io.Serializable {

	// Fields

	private Integer specId;
	//private Integer productId;
	private String specName;
	private String specParam;
	
	private Product product;

	// Constructors

	/** default constructor */
	public Specs() {
	}



	// Property accessors

	public Integer getSpecId() {
		return this.specId;
	}

	public void setSpecId(Integer specId) {
		this.specId = specId;
	}


	public String getSpecName() {
		return this.specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public String getSpecParam() {
		return this.specParam;
	}

	public void setSpecParam(String specParam) {
		this.specParam = specParam;
	}



	public Product getProduct() {
		return product;
	}



	public void setProduct(Product product) {
		this.product = product;
	}

}