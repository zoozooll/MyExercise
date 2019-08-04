package com.oregonscientific.meep.store2.object;

import android.graphics.Bitmap;

public class PromotionItem {
	public String package_name;
	public String image;
	
	public PromotionItem(String package_name, String image){
		this.package_name = package_name;
		this.image = image;
	}

	public String getPackage_name() {
		return package_name;
	}

	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	


}
