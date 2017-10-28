package com.vo;

/**
 * UserRole entity. @author MyEclipse Persistence Tools
 */

public class UserRole implements java.io.Serializable {

	// Fields

	private Integer userroleId;
	private Role role;
	private Integer userid;

	// Constructors

	/** default constructor */
	public UserRole() {
	}

	/** full constructor */
	public UserRole(Role role, Integer userid) {
		this.role = role;
		this.userid = userid;
	}

	// Property accessors

	public Integer getUserroleId() {
		return this.userroleId;
	}

	public void setUserroleId(Integer userroleId) {
		this.userroleId = userroleId;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

}