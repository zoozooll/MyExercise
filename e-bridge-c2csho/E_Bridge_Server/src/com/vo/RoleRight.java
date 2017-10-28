package com.vo;

/**
 * RoleRight entity. @author MyEclipse Persistence Tools
 */

public class RoleRight implements java.io.Serializable {

	// Fields

	private Integer rolerightId;
	private Right right;
	private Role role;

	// Constructors

	/** default constructor */
	public RoleRight() {
	}

	/** full constructor */
	public RoleRight(Right right, Role role) {
		this.right = right;
		this.role = role;
	}

	// Property accessors

	public Integer getRolerightId() {
		return this.rolerightId;
	}

	public void setRolerightId(Integer rolerightId) {
		this.rolerightId = rolerightId;
	}

	public Right getRight() {
		return this.right;
	}

	public void setRight(Right right) {
		this.right = right;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}