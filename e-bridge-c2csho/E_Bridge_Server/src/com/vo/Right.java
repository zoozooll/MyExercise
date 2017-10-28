package com.vo;

import java.util.HashSet;
import java.util.Set;

/**
 * Right entity. @author MyEclipse Persistence Tools
 */

public class Right implements java.io.Serializable {

	// Fields

	private Integer rightId;
	private Right right;
	private String rolename;
	private String action;
	private String rolecode;
	private String memo;
	private Set rights = new HashSet(0);
	private Set roleRights = new HashSet(0);

	// Constructors

	/** default constructor */
	public Right() {
	}

	/** full constructor */
	public Right(Right right, String rolename, String action, String rolecode,
			String memo, Set rights, Set roleRights) {
		this.right = right;
		this.rolename = rolename;
		this.action = action;
		this.rolecode = rolecode;
		this.memo = memo;
		this.rights = rights;
		this.roleRights = roleRights;
	}

	// Property accessors

	public Integer getRightId() {
		return this.rightId;
	}

	public void setRightId(Integer rightId) {
		this.rightId = rightId;
	}

	public Right getRight() {
		return this.right;
	}

	public void setRight(Right right) {
		this.right = right;
	}

	public String getRolename() {
		return this.rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getRolecode() {
		return this.rolecode;
	}

	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Set getRights() {
		return this.rights;
	}

	public void setRights(Set rights) {
		this.rights = rights;
	}

	public Set getRoleRights() {
		return this.roleRights;
	}

	public void setRoleRights(Set roleRights) {
		this.roleRights = roleRights;
	}

}