package com.oregonscientific.meep.store2.inapp.object;

import java.util.ArrayList;

public class ResponseAvaliableSkus extends Response{
	private ArrayList<SkuDetails> result;
	private String package_name;
	private long count;
	private double ts;
	
	public ArrayList<SkuDetails> getResult() {
		return result;
	}
	public void setResult(ArrayList<SkuDetails> result) {
		this.result = result;
	}
	public String getPackage_name() {
		return package_name;
	}
	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public double getTs() {
		return ts;
	}
	public void setTs(double ts) {
		this.ts = ts;
	}
	
}
