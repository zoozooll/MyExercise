package com.oregonscientific.meep.store2.object;

public class StoreFeedback {

	private int code;
	private String status;
	private int coins;
	
	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public StoreFeedback(int code, String status,int coins){
		setCode(code);
		setStatus(status);
		setCoins(coins);
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
