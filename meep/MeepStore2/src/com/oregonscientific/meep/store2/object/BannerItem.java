package com.oregonscientific.meep.store2.object;

import android.graphics.Bitmap;

public class BannerItem {
	public String id;
	public String image;
	private Bitmap imageBmp;
	private String package_name;
	
	public BannerItem(String id, String image){
		this.id = id;
		this.image = image;
	}
	
	public void setImageBitmap(Bitmap bmp){
		this.imageBmp  = bmp;
	}
	
	public Bitmap getImageBitmap() {
		return imageBmp;
	}

	public String getImage() {
		return image;
	}

	public String getId() {
		return id;
	}

	public String getPackage_name() {
		return package_name;
	}

	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}


}
