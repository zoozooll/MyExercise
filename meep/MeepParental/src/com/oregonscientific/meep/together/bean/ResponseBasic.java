package com.oregonscientific.meep.together.bean;

public class ResponseBasic {
	//basic
	public static String CODE_NAME = "code";
	public static String STATUS_NAME = "status";
	public static int SUCCESS = 200;
	
	private int code;
	private String status;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
