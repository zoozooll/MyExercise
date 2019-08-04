package com.oregonscientific.meep.store2.inapp.object;

public class ResponsePurchase extends Response{
	private String product_id;
	private String transaction_id;
	private int quantity;
	public String getProductId() {
		return product_id;
	}
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
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
