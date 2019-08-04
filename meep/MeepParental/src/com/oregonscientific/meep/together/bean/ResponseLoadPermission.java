package com.oregonscientific.meep.together.bean;

public class ResponseLoadPermission extends ResponseBasic
{
	//reload Permission
	private Permissions permission;

	public Permissions getPermission() {
		return permission;
	}
	public void setPermission(Permissions permission) {
		this.permission = permission;
	}

}
