package com.oregonscientific.meep.store2.object;

public class ContentRecoveryFeedback {

	private int code;
	private String status;
	
	public ContentRecoveryFeedback(int code, String status){
		setCode(code);
		setStatus(status);
	}
	
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
