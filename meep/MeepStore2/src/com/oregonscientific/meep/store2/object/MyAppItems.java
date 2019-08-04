package com.oregonscientific.meep.store2.object;

import java.util.ArrayList;

public class MyAppItems {
	private ArrayList<MeepStoreItem> purchased;
	private ArrayList<MeepStoreItem> preloaded;
	public ArrayList<MeepStoreItem> getPurchasedItems() {
		return purchased;
	}
	public void setPurchasedItems(ArrayList<MeepStoreItem> purchased) {
		this.purchased = purchased;
	}
	public ArrayList<MeepStoreItem> getPreloadedItems() {
		return preloaded;
	}
	public void setPreloadedItems(ArrayList<MeepStoreItem> preloaded) {
		this.preloaded = preloaded;
	}
	
	
}
