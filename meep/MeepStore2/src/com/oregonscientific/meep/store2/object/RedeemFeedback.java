package com.oregonscientific.meep.store2.object;

import java.util.ArrayList;

public class RedeemFeedback {
	public static final String TYPE_CONTENTS = "contents";
	public static final String TYPE_COINS = "coins";
	
	private int code;
	private String status;
	private int remaining;
	private ArrayList<RedeemItem> results;
	private String type;
	private int coins;
	
	public RedeemFeedback(int code, String status, int remaining, ArrayList<RedeemItem> results, String type)
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
	public ArrayList<RedeemItem> getResults() {
		return results;
	}
	public void setResults(ArrayList<RedeemItem> results) {
		this.results = results;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}
	
	
}
