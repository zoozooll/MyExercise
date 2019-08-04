package com.oregonscientific.meep.store2.object;

import java.util.ArrayList;

public class MeepStoreItemDetails {

	private String icon = null;
	private String category = null;
	private String _id=null;
	private String purchase_status  = null;
	private String coins;
	private String recommends;
	private ArrayList<String> screenshots = null;
	private String developer = null; 
	private String description =  null;
	private String name = null;
	private String badge = null;
	private String type = null;
	public String getPackage_name() {
		return package_name;
	}
	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}
	private String package_name = null;
	private float size;
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getId() {
		return _id;
	}
	public void setId(String id) {
		this._id = id;
	}
	public String getPurchaseStatus() {
		return purchase_status;
	}
	public void setPurchaseStatus(String purchaseStatus) {
		this.purchase_status = purchaseStatus;
	}
	public String getCoins() {
		return coins;
	}
	public void setCoins(String coins) {
		this.coins = coins;
	}
	public String getRecommends() {
		return recommends;
	}
	public void setRecommends(String recommends) {
		this.recommends = recommends;
	}
	public ArrayList<String> getScreenshots() {
		return screenshots;
	}
	public void setScreenshots(ArrayList<String> screenshots) {
		this.screenshots = screenshots;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBadge() {
		return badge;
	}
	public void setBadge(String badge) {
		this.badge = badge;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public float getSize() {
	    return this.size;
	}
	public void setSize(float size) {
		this.size = size;
	}
}
