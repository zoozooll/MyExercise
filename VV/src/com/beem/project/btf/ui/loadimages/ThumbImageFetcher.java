/**
 * 
 */
package com.beem.project.btf.ui.loadimages;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.beem.project.btf.R;

/**
 * @author Aaron Lee Created at 上午11:00:48 2015-9-11
 */
public class ThumbImageFetcher extends ImageResizer {
	private boolean isInitialized;
	private volatile static ThumbImageFetcher instance;
	private static final float MEMORY_CACHE_PERCENT = 0.125F;
	private static final int DISK_CACHE_SIZE = 1024 * 64;

	public static ThumbImageFetcher getInstance(Context c) {
		if (instance == null) {
			synchronized (ThumbImageFetcher.class) {
				if (instance == null) {
					instance = new ThumbImageFetcher(c);
				}
			}
		}
		return instance;
	}
	public static void destroy() {
		instance.isInitialized = false;
		instance.setExitTasksEarly(true);
		instance.clearCache();
		instance.flushCache();
		instance.closeCache();
		instance = null;
	}
	/**
	 * @param context
	 * @param imageWidth
	 * @param imageHeight
	 */
	private ThumbImageFetcher(Context context) {
		super(context, -1, -1);
	}
	public void init(Context context, int width, int height) {
		isInitialized = true;
		ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(
				context, ImageLoaderConfigers.CACHE_LOACL_PATH);
		setLoadingImage(R.drawable.deafult_imgloading);
		setImageSize(width, height);
		Runtime runtime = Runtime.getRuntime();
		//Log.w(TAG, "memory max " + runtime.maxMemory() + " total " + runtime.totalMemory() + " free " + runtime.freeMemory());
		//cacheParams.setMemCacheSizePercent(MEMORY_CACHE_PERCENT); // Set memory cache to 12.5% of app memory
		cacheParams.setMemCacheSize((int) Math.min(
				((runtime.maxMemory() - runtime.totalMemory() + runtime
						.freeMemory()) * 0.875f / 1024.f),
				(runtime.maxMemory() * MEMORY_CACHE_PERCENT) / 1024.f));
		cacheParams.diskCacheSize = DISK_CACHE_SIZE;
		addImageCache(cacheParams);
	}
	@Override
	public void loadImage(Object path, ImageView imageView) {
		if (!isInitialized) {
			return;
		}
		if (!(path instanceof String)) {
			return;
		}
		super.loadImage(path, imageView);
	}
	@Override
	protected Bitmap processBitmap(Object data) {
		if (data instanceof String) {
			return processBitmap(String.valueOf(data));
		}
		return null;
	}
	private Bitmap processBitmap(String data) {
		return decodeSampledBitmapFromFile(data, mImageWidth, mImageHeight,
				getImageCache());
	}
}
