package com.vo;

import java.util.Set;

/**
 * ²Ö¿â
 */

public class StoreHouse implements java.io.Serializable {

	// Fields

	private Integer storId;
	private String storeName;
	private String storeCharger;
	private String storeContactphone;
	private String storeAddress;
	private String storeMemo;
	
	private Set<Stock> stocks;

	// Constructors

	/** default constructor */
	public StoreHouse() {
	}

	/** full constructor */
	public StoreHouse(String storeName, String storeCharger,
			String storeContactphone, String storeAddress, String storeMemo) {
		this.storeName = storeName;
		this.storeCharger = storeCharger;
		this.storeContactphone = storeContactphone;
		this.storeAddress = storeAddress;
		this.storeMemo = storeMemo;
	}

	// Property accessors

	public Integer getStorId() {
		return this.storId;
	}

	public void setStorId(Integer storId) {
		this.storId = storId;
	}

	public String getStoreName() {
		return this.storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreCharger() {
		return this.storeCharger;
	}

	public void setStoreCharger(String storeCharger) {
		this.storeCharger = storeCharger;
	}

	public String getStoreContactphone() {
		return this.storeContactphone;
	}

	public void setStoreContactphone(String storeContactphone) {
		this.storeContactphone = storeContactphone;
	}

	public String getStoreAddress() {
		return this.storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public String getStoreMemo() {
		return this.storeMemo;
	}

	public void setStoreMemo(String storeMemo) {
		this.storeMemo = storeMemo;
	}

	public Set<Stock> getStocks() {
		return stocks;
	}

	public void setStocks(Set<Stock> stocks) {
		this.stocks = stocks;
	}

}