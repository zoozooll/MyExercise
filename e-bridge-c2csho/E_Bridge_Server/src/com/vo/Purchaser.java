package com.vo;

import java.util.Set;

/**
 * Âò¼Ò
 */

public class Purchaser implements java.io.Serializable {

	// Fields

	private Integer purId;
	private String purName;
	private String purPassword;
	private String purTelephone;
	private String purProvince;
	private String purCity;
	private String purAddress;
	private String purPostalcode;
	private String purRemark;
	private String purIsvendot;
	private Vender vender;

	// Constructors

	/** default constructor */
	public Purchaser() {
		
	}

	/** minimal constructor */
	public Purchaser(String purName, String purPassword) {
		this.purName = purName;
		this.purPassword = purPassword;
	}

	/** full constructor */
	public Purchaser(String purName, String purPassword, String purTelephone,
			String purProvince, String purCity, String purAddress,
			String purPostalcode, String purRemark, String purIsvendot) {
		this.purName = purName;
		this.purPassword = purPassword;
		this.purTelephone = purTelephone;
		this.purProvince = purProvince;
		this.purCity = purCity;
		this.purAddress = purAddress;
		this.purPostalcode = purPostalcode;
		this.purRemark = purRemark;
		this.purIsvendot = purIsvendot;
		//this.vender = vender;
	}

	// Property accessors

	public Integer getPurId() {
		return this.purId;
	}

	public void setPurId(Integer purId) {
		this.purId = purId;
	}

	public String getPurName() {
		return this.purName;
	}

	public void setPurName(String purName) {
		this.purName = purName;
	}

	public String getPurPassword() {
		return this.purPassword;
	}

	public void setPurPassword(String purPassword) {
		this.purPassword = purPassword;
	}

	public String getPurTelephone() {
		return this.purTelephone;
	}

	public void setPurTelephone(String purTelephone) {
		this.purTelephone = purTelephone;
	}

	public String getPurProvince() {
		return this.purProvince;
	}

	public void setPurProvince(String purProvince) {
		this.purProvince = purProvince;
	}

	public String getPurCity() {
		return this.purCity;
	}

	public void setPurCity(String purCity) {
		this.purCity = purCity;
	}

	public String getPurAddress() {
		return this.purAddress;
	}

	public void setPurAddress(String purAddress) {
		this.purAddress = purAddress;
	}

	public String getPurPostalcode() {
		return this.purPostalcode;
	}

	public void setPurPostalcode(String purPostalcode) {
		this.purPostalcode = purPostalcode;
	}

	public String getPurRemark() {
		return this.purRemark;
	}

	public void setPurRemark(String purRemark) {
		this.purRemark = purRemark;
	}

	public String getPurIsvendot() {
		return this.purIsvendot;
	}

	public void setPurIsvendot(String purIsvendot) {
		this.purIsvendot = purIsvendot;
	}

	
	public Vender getVender() {
		return vender;
	}

	public void setVender(Vender vender) {
		this.vender = vender;
	}

	@Override
	public String toString() {		
		return "id: "+purId+"name: "+purName+"purisvendot: "+purIsvendot+"venName :";
	}

}