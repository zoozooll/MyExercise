package com.oregonscientific.meep.store2.object;

import java.util.ArrayList;

public class PurchasedItems {

	private int code;
	private String status;
	private ArrayList<MeepStoreItem> purchased;
	
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
	public ArrayList<MeepStoreItem> getPurchased() {
		return purchased;
	}
	public void setPurchased(ArrayList<MeepStoreItem> purchased) {
		this.purchased = purchased;
	}
	
}
