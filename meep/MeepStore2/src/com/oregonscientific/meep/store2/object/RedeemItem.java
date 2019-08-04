package com.oregonscientific.meep.store2.object;

public class RedeemItem {
	private String id;
	private String icon;
	private String name;
	
	public RedeemItem(String id, String icon , String name){
		this.id = id;
		this.icon = icon;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
