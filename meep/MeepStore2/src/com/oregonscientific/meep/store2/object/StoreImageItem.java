package com.oregonscientific.meep.store2.object;

public class StoreImageItem {

	private String id;
	private String name;
	private String type;
	private String iconUrl;
	
	public StoreImageItem(String id, String name, String type, String iconUrl){
		setId(id);
		setName(name);
		setType(type);
		setIconUrl(iconUrl);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
