package com.oregonscientific.meep.store2.object;

public class PurchaseFeedback {
	private int code;
	private String status;
	private int coins;
	
	private String url;
	private String type;
	private String name;
	private String item_id;
	private String image;
	private String package_name;
	
	public String getItem_id() {
		return item_id;
	}
	
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPackage_name() {
		return package_name;
	}

	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}

	public PurchaseFeedback(int code, String status, int coins){
		this.code = code;
		this.status = status;
		this.coins = coins;
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
	public int getCoins() {
		return coins;
	}
	public void setCoins(int coins) {
		this.coins = coins;
	}
	
	
}
