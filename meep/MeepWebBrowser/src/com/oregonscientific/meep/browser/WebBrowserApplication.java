package com.oregonscientific.meep.browser;

import com.oregonscientific.meep.browser.ui.adapter.ImageDownloader;

import android.app.Application;

public class WebBrowserApplication extends Application {

	private static final String IMAGE_CACHE_DIR = "images";
	private ImageDownloader imageDownloader = null; 
	
	public ImageDownloader getImageDownloader() {
		if (imageDownloader == null) {
			imageDownloader = new ImageDownloader(this, IMAGE_CACHE_DIR);
		}
		return imageDownloader;
	}
}
