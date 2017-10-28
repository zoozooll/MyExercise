package com.vo;

import java.util.Set;

/**
 * ²úÆ·×é
 */

public class ProductGroup implements java.io.Serializable {

	// Fields

	private Integer progId;

	private String proGroupcode;

	private String progGroupname;

	private String progFullname;

	private String progPath;

	private Set<Admin> admins;

	public Set<Admin> getAdmins() {
		return admins;
	}

	public void setAdmins(Set<Admin> admins) {
		this.admins = admins;
	}

	private Set<Product> products;

	private Vender vender;

	// Constructors

	/** default constructor */
	public ProductGroup() {
	}

	// Property accessors

	public Integer getProgId() {
		return this.progId;
	}

	public void setProgId(Integer progId) {
		this.progId = progId;
	}

	public String getProGroupcode() {
		return this.proGroupcode;
	}

	public void setProGroupcode(String proGroupcode) {
		this.proGroupcode = proGroupcode;
	}

	public String getProgGroupname() {
		return this.progGroupname;
	}

	public void setProgGroupname(String progGroupname) {
		this.progGroupname = progGroupname;
	}

	public String getProgFullname() {
		return this.progFullname;
	}

	public void setProgFullname(String progFullname) {
		this.progFullname = progFullname;
	}

	public String getProgPath() {
		return this.progPath;
	}

	public void setProgPath(String progPath) {
		this.progPath = progPath;
	}

	/**
	 * @return the products
	 */
	public Set<Product> getProducts() {
		return products;
	}

	/**
	 * @param products
	 *            the products to set
	 */
	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public Vender getVender() {
		return vender;
	}

	public void setVender(Vender vender) {
		this.vender = vender;
	}

}