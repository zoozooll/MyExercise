package com.oregonscientific.meep.store2.inapp.object;

import java.util.ArrayList;

public class ResponseGetPurchasedItem extends Response{

	private String package_name;
	
	private double time;
	
	private ArrayList<PurchasedRecord> iap_purchases;
	
	private long count;

	public String getPackage_name() {
		return package_name;
	}

	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public ArrayList<PurchasedRecord> getIap_purchases() {
		return iap_purchases;
	}

	public void setIap_purchases(ArrayList<PurchasedRecord> iap_purchases) {
		this.iap_purchases = iap_purchases;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
	
	
}
