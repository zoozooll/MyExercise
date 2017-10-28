package com.vo;

import java.util.Set;

/**
 * Æ·ÅÆ
 */

public class Brand implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer brandId;
	private String brandName;
	private String brandDesc;
	private String brandImage;
	
	public String getBrandImage() {
		return brandImage;
	}

	public void setBrandImage(String brandImage) {
		this.brandImage = brandImage;
	}

	private Set<Product> products;

	// Constructors

	/** default constructor */
	public Brand() {
	}

	/** full constructor */
	public Brand(String brandName, String brandDesc) {
		this.brandName = brandName;
		this.brandDesc = brandDesc;
	}

	// Property accessors

	public Integer getBrandId() {
		return this.brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return this.brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandDesc() {
		return this.brandDesc;
	}

	public void setBrandDesc(String brandDesc) {
		this.brandDesc = brandDesc;
	}

	/**
	 * @return the products
	 */
	public Set<Product> getProducts() {
		return products;
	}

	/**
	 * @param products the products to set
	 */
	public void setProducts(Set<Product> products) {
		this.products = products;
	}

}