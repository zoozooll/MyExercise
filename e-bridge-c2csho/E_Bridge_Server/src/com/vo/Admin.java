package com.vo;

import java.util.Set;

/**
 * User entity. @author MyEclipse Persistence Tools
 */

public class Admin implements java.io.Serializable {

	// Fields

	private Integer adminId;
	private String adminName;
	private String adminPassword;
	private String adminAddress;
	private String adminIdcard;
	private String adminPhone;
	private String adminEmail;
	private String adminRemark;
	private ProductGroup prductGroup;
	private Channel channel;
	private Organization organization;

	// Constructors

	
	// Property accessors

	public Integer getAdminId() {
		return this.adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return this.adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAdminPassword() {
		return this.adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getAdminAddress() {
		return this.adminAddress;
	}

	public void setAdminAddress(String adminAddress) {
		this.adminAddress = adminAddress;
	}

	public String getAdminIdcard() {
		return this.adminIdcard;
	}

	public void setAdminIdcard(String adminIdcard) {
		this.adminIdcard = adminIdcard;
	}

	public String getAdminPhone() {
		return this.adminPhone;
	}

	public void setAdminPhone(String adminPhone) {
		this.adminPhone = adminPhone;
	}

	public String getAdminEmail() {
		return this.adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public String getAdminRemark() {
		return this.adminRemark;
	}

	public void setAdminRemark(String adminRemark) {
		this.adminRemark = adminRemark;
	}

	
	public ProductGroup getPrductGroup() {
		return prductGroup;
	}

	public void setPrductGroup(ProductGroup prductGroup) {
		this.prductGroup = prductGroup;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}


	
}