/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.notification;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.oregonscientific.meep.util.ImageDownloader;

import android.app.Application;

/**
 * The main application for MEEP! Notification Center
 */
public class NotificationCenterApplication extends Application {
	
	public static final String CACHE_NOTIFICATION = "notification";
	
	/**
	 * The downloader caches
	 */
	private Map<String, ImageDownloader> mDownloaders;
	
	/**
	 * Retrieves the {@link ImageDownloader} with the given directory name
	 * 
	 * @param name the directory name for caching downloaded images
	 * @return the {@link ImageDownloader} object
	 */
	public ImageDownloader getDownloader(String name) {
		if (mDownloaders == null) {
			mDownloaders = new ConcurrentHashMap<String, ImageDownloader>();
		}
		
		ImageDownloader downloader = mDownloaders.get(name);
		if (downloader == null) {
			downloader = new ImageDownloader(this, name);
			mDownloaders.put(name, downloader);
		}
		return downloader;
	}

}
