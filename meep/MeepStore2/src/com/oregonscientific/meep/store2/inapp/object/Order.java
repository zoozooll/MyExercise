package com.oregonscientific.meep.store2.inapp.object;

public class Order {
	private String product_id;
	private int quantity;
	
	public String getProductId() {
		return product_id;
	}
	public void setProductId(String productId) {
		this.product_id = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
