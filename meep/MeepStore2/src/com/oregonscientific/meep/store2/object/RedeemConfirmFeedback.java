package com.oregonscientific.meep.store2.object;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RedeemConfirmFeedback {
	private String type;
	private int code;
	private String status;
	private int remaining;
	private int coins;
	private ArrayList<RedeemConfirmItem> contents;
	
	

	public RedeemConfirmFeedback(int code, String status, int remaining, String type, int coins,ArrayList<RedeemConfirmItem> contents){
		setType(type);
		setCode(code);
		setRemaining(remaining);
		setStatus(status);
		setCoins(coins);
		setContents(contents);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
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

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}
	
	public ArrayList<RedeemConfirmItem> getContents() {
		return contents;
	}

	public void setContents(ArrayList<RedeemConfirmItem> contents) {
		this.contents = contents;
	}
}
