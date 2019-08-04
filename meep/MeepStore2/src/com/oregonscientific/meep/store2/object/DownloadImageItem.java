package com.oregonscientific.meep.store2.object;

import android.graphics.Bitmap;

public class DownloadImageItem {

	private String id;
	private String url;
	private Bitmap image;
	
	public DownloadImageItem(String id, String url){
		setId(id);
		setUrl(url);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	
	
}
