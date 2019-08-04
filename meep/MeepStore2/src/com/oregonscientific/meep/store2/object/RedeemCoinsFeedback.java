package com.oregonscientific.meep.store2.object;

public class RedeemCoinsFeedback {
	private int code;
	private String status;
	private int remaining;
	private int results;
	private String type;
	
	public RedeemCoinsFeedback(int code, String status, int remaining, int results, String type)
	{
		this.code = code;
		this.status = status;
		this.remaining = remaining;
		this.results = results;
		this.type = type;
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
	public int getRemaining() {
		return remaining;
	}
	public void setRemaining(int remaining) {
		this.remaining = remaining;
	}
	public int getResults() {
		return results;
	}
	public void setResults(int results) {
		this.results = results;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
