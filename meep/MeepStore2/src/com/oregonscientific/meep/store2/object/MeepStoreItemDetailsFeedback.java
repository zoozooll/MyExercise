package com.oregonscientific.meep.store2.object;


public class MeepStoreItemDetailsFeedback{

	private int code;
	private String status;
	private MeepStoreItemDetails result;
	
	public MeepStoreItemDetailsFeedback(int code, String status, MeepStoreItemDetails result){
		setCode(code);
		setStatus(status);
		setResult(result);
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
	public MeepStoreItemDetails getResult() {
		return result;
	}
	public void setResult(MeepStoreItemDetails result) {
		this.result = result;
	}
	
	
	
	

}
