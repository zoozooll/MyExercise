package com.vo;

/**
 * Vender entity. @author MyEclipse Persistence Tools
 */

public class Vender implements java.io.Serializable {

	// Fields
	private Integer venId;	
	private ProductGroup productgroup;
	private String venShortname;
	private Integer venShopcard;
	private String venFax;
	private String venLinkman;
	private String venLinkmanphone;
	private String venEmail;
	private Integer venStatus;
	private Purchaser purchaser;

	// Constructors

	/** default constructor */
	public Vender() {
	}

	/** minimal constructor */
	public Vender(Integer productgroupId,
			Integer venShopcard) {
		//this.purchaserId = purchaserId;		
		this.venShopcard = venShopcard;
	}

	/** full constructor */
	public Vender(String venShortname, Integer venShopcard, String venFax,
			String venLinkman, String venLinkmanphone, String venEmail,
			Integer venStatus, Purchaser purchaser) {
		//this.purchaserId = purchaserId;		
		this.venShortname = venShortname;
		this.venShopcard = venShopcard;
		this.venFax = venFax;
		this.venLinkman = venLinkman;
		this.venLinkmanphone = venLinkmanphone;
		this.venEmail = venEmail;
		this.venStatus = venStatus;
		this.purchaser = purchaser;
	}

	// Property accessors

	public Integer getVenId() {
		return this.venId;
	}

	public void setVenId(Integer venId) {
		this.venId = venId;
	}

	public String getVenShortname() {
		return this.venShortname;
	}

	public void setVenShortname(String venShortname) {
		this.venShortname = venShortname;
	}

	public Integer getVenShopcard() {
		return this.venShopcard;
	}

	public void setVenShopcard(Integer venShopcard) {
		this.venShopcard = venShopcard;
	}

	public String getVenFax() {
		return this.venFax;
	}

	public void setVenFax(String venFax) {
		this.venFax = venFax;
	}

	public String getVenLinkman() {
		return this.venLinkman;
	}

	public void setVenLinkman(String venLinkman) {
		this.venLinkman = venLinkman;
	}

	public String getVenLinkmanphone() {
		return this.venLinkmanphone;
	}

	public void setVenLinkmanphone(String venLinkmanphone) {
		this.venLinkmanphone = venLinkmanphone;
	}

	public String getVenEmail() {
		return this.venEmail;
	}

	public void setVenEmail(String venEmail) {
		this.venEmail = venEmail;
	}

	public Integer getVenStatus() {
		return this.venStatus;
	}

	public void setVenStatus(Integer venStatus) {
		this.venStatus = venStatus;
	}

	public Purchaser getPurchaser() {
		return purchaser;
	}

	public void setPurchaser(Purchaser purchaser) {
		this.purchaser = purchaser;
	}

	public ProductGroup getProductgroup() {
		return productgroup;
	}

	public void setProductgroup(ProductGroup productgroup) {
		this.productgroup = productgroup;
	}

	

}