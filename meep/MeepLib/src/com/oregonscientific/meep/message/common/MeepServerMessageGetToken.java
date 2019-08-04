package com.oregonscientific.meep.message.common;

public class MeepServerMessageGetToken {

	private String status = null;
	private int code = -1;
	private String mac_address = null;
	private String token = null;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMacAddress() {
		return mac_address;
	}
	public void setMacAddress(String mac_address) {
		this.mac_address = mac_address;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
}
