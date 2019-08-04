package com.oregonscientific.meep.store2.inapp.object;

public class ContentPurchase {
	private String package_name;
	private String order_json;
	private String signature;
	
	public String getPackage_name() {
		return package_name;
	}
	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}
	public String getOrder_json() {
		return order_json;
	}
	public void setOrder_json(String order_json) {
		this.order_json = order_json;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}

}
