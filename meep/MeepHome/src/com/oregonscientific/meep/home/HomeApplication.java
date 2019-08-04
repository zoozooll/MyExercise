/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.app.Application;

import com.oregonscientific.meep.home.internal.VolumeManager;
import com.oregonscientific.meep.util.ImageDownloader;

/**
 * The main application for MEEP! Home
 */
public class HomeApplication extends Application {
	
	public static final String CACHE_PROFILE = "profile";
	public static final String CACHE_STORE = "store";
	
	/**
	 * The downloader caches
	 */
	private Map<String, ImageDownloader> mDownloaders;
	
	/**
	 * The handle that controls device volume
	 */
	private VolumeManager mVolumeManager;
	
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
	
	/**
	 * Return handle to the volume service
	 * 
	 * @return the volume service handle
	 */
	public VolumeManager getVolumeService() {
		if (mVolumeManager == null) {
			mVolumeManager = new VolumeManager(this);
		}
		return mVolumeManager;
	}
	
	/**
	 * Initiate shutdown to services started previously
	 */
	public void shutdown() {
		if (mVolumeManager != null) {
			mVolumeManager.stopAssert();
			mVolumeManager = null;
		}
	}

}
