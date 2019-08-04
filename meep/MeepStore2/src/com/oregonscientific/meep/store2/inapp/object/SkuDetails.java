package com.oregonscientific.meep.store2.inapp.object;

public class SkuDetails {
	private int coins;
	private String name;
	private String description;
	private boolean consumable;
	private String productId;
	
	public int getCoins() {
		return coins;
	}
	public void setCoins(int coins) {
		this.coins = coins;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isConsumable() {
		return consumable;
	}
	public void setConsumable(boolean consumable) {
		this.consumable = consumable;
	}
}
