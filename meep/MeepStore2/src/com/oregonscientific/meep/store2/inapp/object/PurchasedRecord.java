package com.oregonscientific.meep.store2.inapp.object;

import com.google.gson.Gson;

public class PurchasedRecord {
	
	private String transaction_id;
	
	private String product_id;
	
	private String name;
	
	private String description;
	
	private boolean consumable;
	
	private int coins;
	
	private int quantity;
	
	private long dts;

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isConsumable() {
		return consumable;
	}

	public void setConsumable(boolean consumable) {
		this.consumable = consumable;
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public long getDts() {
		return dts;
	}

	public void setDts(long dts) {
		this.dts = dts;
	}
	private static Gson mGson = new Gson();
	public SkuDetails generateSkuDetails()
	{
		SkuDetails sku = new SkuDetails();
		sku.setConsumable(isConsumable());
		sku.setCoins(getCoins());
		sku.setDescription(getDescription());
		sku.setName(getName());
		sku.setProductId(getProduct_id());
		return sku;
	}
	public String generateSkuDetailsJson()
	{
		SkuDetails sku = generateSkuDetails();
		return mGson.toJson(sku);
	}
	
	public PurchasedItem generatePurchaseItem(String packageName)
	{
		PurchasedItem item = new PurchasedItem();
		item.setOrderId(getTransaction_id());
		item.setPackageName(packageName);
		item.setProductId(getProduct_id());
		item.setPurchaseTime(Long.toString(getDts()));
		return item;
	}
	public String generatePurchaseItemJson(String packageName)
	{
		PurchasedItem item = generatePurchaseItem(packageName);
		return mGson.toJson(item);
	}
	
}
