package com.oregonscientific.meep.store2.object;

import java.util.ArrayList;

public class ItemVersionReportList {

	
	private  ArrayList<ItemVersionReport> apps;

	public ItemVersionReportList( ArrayList<ItemVersionReport> apps){
		setApps(apps);
	}
	
	public ArrayList<ItemVersionReport> getApps() {
		return apps;
	}

	public void setApps(ArrayList<ItemVersionReport> apps) {
		this.apps = apps;
	}
	
	
	
}
