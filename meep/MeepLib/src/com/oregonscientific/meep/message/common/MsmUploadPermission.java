package com.oregonscientific.meep.message.common;

public class MsmUploadPermission extends MeepServerMessage{

	public MsmUploadPermission(String proc, String opcode) {
		super(proc, opcode);
	}
	
	private PermissionEntry permission;

	public PermissionEntry getPermission() {
		return permission;
	}

	public void setPermission(PermissionEntry permission) {
		this.permission = permission;
	}
	
	public int getLast_update() {
		return last_update;
	}

	public void setLast_update(int last_update) {
		this.last_update = last_update;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	private int last_update;
	private String token;

}
