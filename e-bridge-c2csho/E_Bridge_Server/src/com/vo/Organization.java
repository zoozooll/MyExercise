package com.vo;

import java.util.Set;

/**
 * Organization entity. @author MyEclipse Persistence Tools
 */

public class Organization implements java.io.Serializable {

	// Fields

	private Integer id;
	private String oraniName;
	private String orgCode;
	private String orgShortname;
	private String orgRemark;
	private Set<Admin> admins;
	private City city;

	// Constructors


	public City getCity() {
		return city;
	}



	public void setCity(City city) {
		this.city = city;
	}



	/** default constructor */
	public Organization() {
	}



	public Set<Admin> getAdmins() {
		return admins;
	}



	public void setAdmins(Set<Admin> admins) {
		this.admins = admins;
	}



	/** full constructor */
	public Organization(String oraniName, String orgCode, String orgShortname,
			String orgRemark) {
		this.oraniName = oraniName;
		this.orgCode = orgCode;
		this.orgShortname = orgShortname;
		this.orgRemark = orgRemark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOraniName() {
		return this.oraniName;
	}

	public void setOraniName(String oraniName) {
		this.oraniName = oraniName;
	}

	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgShortname() {
		return this.orgShortname;
	}

	public void setOrgShortname(String orgShortname) {
		this.orgShortname = orgShortname;
	}

	public String getOrgRemark() {
		return this.orgRemark;
	}

	public void setOrgRemark(String orgRemark) {
		this.orgRemark = orgRemark;
	}

}