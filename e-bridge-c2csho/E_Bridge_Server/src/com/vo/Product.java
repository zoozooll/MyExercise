package com.vo;

import java.util.Set;

/**
 * ²úÆ·
 */

public class Product implements java.io.Serializable {

	// Fields

	private Integer proId; // Ö÷¼ü

	private String proName;

	private String proCode;

	// private Integer signId;
	// private Integer typeId;
	// private Integer progroupId;
	private Double proPrice;

	private String proUnit;

	private String proImagepath;

	private String proFeature;

	private String proRemark;

	private OrderLine orderline;


	private Brand brand;

	private ProductType productType;

	private ProductGroup productGroup;

	private Set<Image> images;

	private Specs specs;

	private Stock stock;

	// Constructors

	/** default constructor */
	public Product() {
	}
	
	// Property accessors

	public Product(Integer proId, String proName, String proCode, Double proPrice, String proUnit, String proImagepath, String proFeature, String proRemark, OrderLine orderline, Brand brand, ProductType productType, ProductGroup productGroup, Set<Image> images, Specs specs, Stock stock) {
		super();
		this.proId = proId;
		this.proName = proName;
		this.proCode = proCode;
		this.proPrice = proPrice;
		this.proUnit = proUnit;
		this.proImagepath = proImagepath;
		this.proFeature = proFeature;
		this.proRemark = proRemark;
		this.orderline = orderline;
		this.brand = brand;
		this.productType = productType;
		this.productGroup = productGroup;
		this.images = images;
		this.specs = specs;
		this.stock = stock;
	}
	
	public Product(String proName, String proCode){
		this.proName=proName;
		this.proCode=proCode;
	}


	public Integer getProId() {
		return this.proId;
	}

	public void setProId(Integer proId) {
		this.proId = proId;
	}

	public String getProName() {
		return this.proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getProCode() {
		return this.proCode;
	}

	public void setProCode(String proCode) {
		this.proCode = proCode;
	}

	public Double getProPrice() {
		return this.proPrice;
	}

	public void setProPrice(Double proPrice) {
		this.proPrice = proPrice;
	}

	public String getProUnit() {
		return this.proUnit;
	}

	public void setProUnit(String proUnit) {
		this.proUnit = proUnit;
	}

	public String getProImagepath() {
		return this.proImagepath;
	}

	public void setProImagepath(String proImagepath) {
		this.proImagepath = proImagepath;
	}

	public String getProFeature() {
		return this.proFeature;
	}

	public void setProFeature(String proFeature) {
		this.proFeature = proFeature;
	}

	public String getProRemark() {
		return this.proRemark;
	}

	public void setProRemark(String proRemark) {
		this.proRemark = proRemark;
	}

	/**
	 * @return the brand
	 */
	public Brand getBrand() {
		return brand;
	}

	/**
	 * @param brand
	 *            the brand to set
	 */
	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	/**
	 * @return the producttype
	 */
	public ProductType getProductType() {
		return productType;
	}

	/**
	 * @return the productGroup
	 */
	public ProductGroup getProductGroup() {
		return productGroup;
	}

	/**
	 * @param productGroup
	 *            the productGroup to set
	 */
	public void setProductGroup(ProductGroup productGroup) {
		this.productGroup = productGroup;
	}

	/**
	 * @param productType
	 *            the productType to set
	 */
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}

	public Specs getSpecs() {
		return specs;
	}

	public void setSpecs(Specs specs) {
		this.specs = specs;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}
	public OrderLine getOrderline() {
		return orderline;
	}

	public void setOrderline(OrderLine orderline) {
		this.orderline = orderline;
	}


}