/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oregonscientific.meep.tool;

import java.io.File;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;


import com.oregonscientific.meep.global.Global.AppType;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.ImageView;

/**
 ******************************************************************************
 * Adapted from the Android Developer source code, can be found in:
 * http://code.google.com/p/android-imagedownloader/
 * or direct link:
 * http://android-imagedownloader.googlecode.com/svn/trunk/src/com/example/android/imagedownloader/ImageDownloader.java
 ******************************************************************************
 * This helper class download images from the Internet and binds those with the
 * provided ImageView.
 * 
 * <p>
 * It requires the INTERNET permission, which should be added to your
 * application's manifest file.
 * </p>
 * 
 * A local cache of downloaded images is maintained internally to improve
 * performance.
 */
public class ImageDownloader {
	private static final String LOG_TAG = "ImageDownloader";
	private ImageCache cache;
	
	// Default in sample size of the bitmap
	private static final int DEFAULT_SAMPLE_SIZE = Integer.MAX_VALUE;
	private ImageDownloadListener mImageDownloadListener;
	
	public enum Mode {
		NO_ASYNC_TASK, NO_DOWNLOADED_DRAWABLE, CORRECT
	}

	private Mode mode = Mode.CORRECT;
	
	/**
	 * Creates a new instance of ImageDownloader using the given unique name for
	 * caching the images
	 * 
	 * @param context The context to use
	 * @param uniqueName
	 *          A unique directory name to append to the cache dir
	 */
	public ImageDownloader(Context context, String uniqueName) {
		this(ImageCache.getDiskCacheDir(context, uniqueName));
	}
	
	public ImageDownloader(File diskCacheDir) {
		ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(diskCacheDir);
		cacheParams.initDiskCacheOnCreate = true;
//		cacheParams.memoryCacheEnabled = false;	// Disable memory cache
		cacheParams.setMemCacheSizePercent(0.1f); // Set memory cache to 10% of app memory
		cache = new ImageCache(cacheParams);
	}
	
	
	/**
	 * Download the specified image from the Internet and binds it to the provided
	 * ImageView. The binding is immediate if the image is found in the cache and
	 * will be done asynchronously otherwise. A null bitmap will be associated to
	 * the ImageView if an error occurs.
	 * 
	 * @param url
	 *          The URL of the image to download.
	 * @param imageView
	 *          The ImageView to bind the downloaded image to.
	 */
	
	public void download(String url, ImageView imageView) {
		download(url, imageView, null, null);
	}
	
	public void download(String url, ImageView imageView, ImageLoadingListener listener) {
		download(url, imageView, null, listener);
	}
	
	public void download(String path, ImageView imageView, Bitmap defaultImg, ImageLoadingListener listener) {
		
		// State sanity: url is guaranteed to never be null in DownloadedDrawable
		// and cache keys.
		if (path == null) {
			if (mImageDownloadListener != null) {
				mImageDownloadListener.onDownloadFail(imageView);
			}
			return;
		}
				
		resetPurgeTimer();
		//long begin = System.currentTimeMillis();
		Bitmap bitmap = getBitmapFromCache(path);
		//long end = System.currentTimeMillis();
		//Log.d("aaron","getBitmapFromCache spend "+(end-begin));
		
		if (bitmap == null) {
			/*try {
				//long begin2 = System.currentTimeMillis();
				bitmap = MeepStorageCtrl.getPrivateSmallIcon(fileName, path, AppType.Photo, bmpBg, bmpTop);
				//long end2 = System.currentTimeMillis();
				//Log.d("cdf2","getPrivateSmallIcon spend "+(end2-begin2));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			addBitmapToCache(path +"small", bitmap);*/
			forceDownload(path,imageView, defaultImg,listener);
			
		} else {
			cancelPotentialDownload(path, imageView);
			if (mImageDownloadListener != null) {
				mImageDownloadListener.onDownloadFromCache(imageView, bitmap);
			}
		}
	}
	
	
	/**
	 * Same as download but the image is always downloaded and the cache is not
	 * used. Kept private at the moment as its interest is not clear.
	 */	
	private void forceDownload(String url, ImageView imageView, Bitmap defaultImg, ImageLoadingListener listener) {
		// State sanity: url is guaranteed to never be null in DownloadedDrawable
		// and cache keys.
		if (url == null) {
			imageView.setImageDrawable(null);
			return;
		}
		
		if (cancelPotentialDownload(url, imageView)) {
			switch (mode) {
			case NO_ASYNC_TASK:
				Bitmap bitmap = downloadBitmap(url);
				addBitmapToCache(url, bitmap);
				if (imageView != null && mImageDownloadListener != null) {
					if (bitmap != null) {
						mImageDownloadListener.onDownloadSuccessed(imageView, bitmap);
					} else {
						mImageDownloadListener.onDownloadFail(imageView);
					}
				}
				break;
				
			case NO_DOWNLOADED_DRAWABLE:
				BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
				task.execute(url);
				break;
				
			case CORRECT:
				//cdf 传参
				task = new BitmapDownloaderTask(imageView);
				DownloadedDrawables downloadedDrawable = new DownloadedDrawables(task, defaultImg);
				if (imageView != null) {
					imageView.setImageDrawable(downloadedDrawable);
					imageView.setMinimumHeight(156);
				}
				//task.execute(url, Integer.valueOf(maxWidth).toString(), Integer.valueOf(maxHeight).toString());
				//
				task.execute(url);
				break;
			}
		}
	}

	/**
	 * Returns true if the current download has been canceled or if there was no
	 * download in progress on this image view. Returns false if the download in
	 * progress deals with the same url. The download is not stopped in that case.
	 */
	private static boolean cancelPotentialDownload(String url, ImageView imageView) {
		BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

		if (bitmapDownloaderTask != null) {
			String bitmapUrl = bitmapDownloaderTask.url;
			if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
				bitmapDownloaderTask.cancel(true);
			} else {
				// The same URL is already being downloaded.
				return false;
			}
		}
		return true;
	}

	/**
	 * @param imageView
	 *          Any imageView
	 * @return Retrieve the currently active download task (if any) associated
	 *         with this imageView. null if there is no such task.
	 */
	private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof DownloadedDrawables) {
				DownloadedDrawables downloadedDrawable = (DownloadedDrawables) drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}
	

	private Bitmap downloadBitmap(String url) {

		if (mImageDownloadListener != null) {
			return mImageDownloadListener.loadImageFromUrl(url);
		}
		return null;
	}


	/**
	 * The actual AsyncTask that will asynchronously download the image.
	 */
	class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		private String url;
		private ImageLoadingListener mImageLoadingListener;
		private final WeakReference<ImageView> imageViewReference;

		public BitmapDownloaderTask(ImageView imageView) {
			imageViewReference = imageView == null ? null : new WeakReference<ImageView>(imageView);
		}
		
		public BitmapDownloaderTask(ImageView imageView, ImageLoadingListener listener) {
			imageViewReference = imageView == null ? null : new WeakReference<ImageView>(imageView);
			mImageLoadingListener = listener;
		}

		public BitmapDownloaderTask(ImageView imageView,AppType appType,Bitmap bmpBg,Bitmap bmpTop) {
			imageViewReference = imageView == null ? null : new WeakReference<ImageView>(imageView);
		}
		/**
		 * Actual download method.
		 */
		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap result = null;
			/*if (params.length == 3) {
				url = params[0];
				return downloadBitmap(url);
			}
			else if(params.length == 2){
				//cdf 
				String fileName = params[0];
				String path = params[1];
				//加载图片
				Bitmap b = null;
				try {
					b = MeepStorageCtrl.getPrivateSmallIcon(fileName, path, AppType.Photo, bmpBg, bmpTop);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return b;
			}*/
			if (params.length > 0) {
				url = params[0];
				result = downloadBitmap(url);
			}
			
			return result;
		}

		/**
		 * Once the image is downloaded, associates it to the imageView
		 */
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				ImageView imageView = imageViewReference.get();
				if (imageView != null && mImageLoadingListener != null) {
					mImageLoadingListener.onLoadingCancelled(url, imageView);
				}
				bitmap = null;
				return;
			}

			addBitmapToCache(url, bitmap);

			if (imageViewReference != null) {
				ImageView imageView = imageViewReference.get();
				BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
				// Change bitmap only if this process is still associated with it
				// Or if we don't use any bitmap to task association
				// (NO_DOWNLOADED_DRAWABLE mode)
				if ((this == bitmapDownloaderTask) || (mode != Mode.CORRECT)) {
					//2013-4-3 -Amy- Default picture displayed when the picture is bad
					if(bitmap != null){
						
						if (mImageLoadingListener != null) {
							mImageLoadingListener.onLoadingComplete(url, imageView, bitmap);
						}
						if (mImageDownloadListener != null) {
							mImageDownloadListener.onDownloadSuccessed(imageView, bitmap);
						}
					}else{
						if (mImageDownloadListener != null) {
							mImageDownloadListener.onDownloadFail(imageView);
						}
					}
					
				}
			}
		}

		/**
		 * @param mImageLoadingListener the mImageLoadingListener to set
		 */
		public void setmImageLoadingListener(ImageLoadingListener mImageLoadingListener) {
			this.mImageLoadingListener = mImageLoadingListener;
		}
	}

	/**
	 * A fake Drawable that will be attached to the imageView while the download
	 * is in progress.
	 * 
	 * <p>
	 * Contains a reference to the actual download task, so that a download task
	 * can be stopped if a new binding is required, and makes sure that only the
	 * last started download process can bind its result, independently of the
	 * download finish order.
	 * </p>
	 */
	/*static class DownloadedDrawable extends ColorDrawable {
		private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

		public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
//			super(Color.WHITE);
			bitmapDownloaderTaskReference = new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
		}

		public BitmapDownloaderTask getBitmapDownloaderTask() {
			return bitmapDownloaderTaskReference.get();
		}
	}*/
	//2013-4-3 -Amy- Default picture displayed in the loading process
	private static class DownloadedDrawables extends BitmapDrawable {
        private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

        public DownloadedDrawables(BitmapDownloaderTask bitmapDownloaderTask, Bitmap loadingImage) {
                super(loadingImage);
                //super(bitmapDownloaderTask.getDefaultImage());
                bitmapDownloaderTaskReference = new WeakReference<BitmapDownloaderTask>(
                                bitmapDownloaderTask);
        }

        public BitmapDownloaderTask getBitmapDownloaderTask() {
                return bitmapDownloaderTaskReference.get();
        }
	}
	
	public void setMode(Mode mode) {
		this.mode = mode;
		clearCache();
	}

	/*
	 * Cache-related fields and methods.
	 * 
	 * We use a hard and a soft cache. A soft reference cache is too aggressively
	 * cleared by the Garbage Collector.
	 */

	private static final int HARD_CACHE_CAPACITY = 10;
	private static final int DELAY_BEFORE_PURGE = 10 * 1000; // in milliseconds
	
	// Hard cache, with a fixed maximum capacity and a life duration
	private final HashMap<String, Bitmap> sHardBitmapCache = 
		new LinkedHashMap<String, Bitmap>(HARD_CACHE_CAPACITY / 2, 0.75f, true) {
		@Override
		protected boolean removeEldestEntry(LinkedHashMap.Entry<String, Bitmap> eldest) {
			if (size() > HARD_CACHE_CAPACITY) {
				// Entries push-out of hard reference cache are transferred to soft
				// reference cache
				sSoftBitmapCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
				return true;
			} else
				return false;
		}
	};

	// Soft cache for bitmaps kicked out of hard cache
	private final static ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache = 
		new ConcurrentHashMap<String, SoftReference<Bitmap>>(HARD_CACHE_CAPACITY / 2);

	private final Handler purgeHandler = new Handler();

	private final Runnable purger = new Runnable() {
		public void run() {
			// TODO: clear cache
			// clearCache();
		}
	};

	/**
	 * Adds this bitmap to the cache.
	 * 
	 * @param bitmap
	 *          The newly downloaded bitmap.
	 */
	private void addBitmapToCache(String url, Bitmap bitmap) {
		cache.addBitmapToCache(url, bitmap);
	}

	/**
	 * @param url
	 *          The URL of the image that will be retrieved from the cache.
	 * @return The cached bitmap or null if it was not found.
	 */
	private Bitmap getBitmapFromCache(String url) {
		// First try the memory cache
		Bitmap bitmap = cache.getBitmapFromMemCache(url);
		if (bitmap != null) {
			return bitmap;
		}
		// cache get bitmap from disk cache is wrong ,the bitmap is black frame. 2013-03-11 write by aaron
		// Then try the soft reference cache
		//bitmap = cache.getBitmapFromDiskCache(url, maxWidth, maxHeight);
		return bitmap;
	}

	/**
	 * Clears the image cache used internally to improve performance. Note that
	 * for memory efficiency reasons, the cache will automatically be cleared
	 * after a certain inactivity delay.
	 */
	public void clearCache() {
		cache.clearCache();
	}

	/**
	 * Allow a new delay before the automatic cache clear is done.
	 */
	private void resetPurgeTimer() {
		purgeHandler.removeCallbacks(purger);
		purgeHandler.postDelayed(purger, DELAY_BEFORE_PURGE);
	}
	
	/**
	 * @return the mImageDownloadListener
	 */
	public ImageDownloadListener getmImageDownloadListener() {
		return mImageDownloadListener;
	}

	/**
	 * @param mImageDownloadListener the mImageDownloadListener to set
	 */
	public void setmImageDownloadListener(
			ImageDownloadListener mImageDownloadListener) {
		this.mImageDownloadListener = mImageDownloadListener;
	}
	
	/**
	 * ImageDownloadListener
	 * @author aaronli at Apr7 2013
	 *
	 */
	public interface ImageDownloadListener {
		
		/**
		 * diong when load image from cache
		 * @param view
		 * @param bitmapFromCache
		 */
		public void onDownloadFromCache(ImageView view, Bitmap bitmapFromCache);
		
		/**
		 * doing when the download return fail image
		 * @param view
		 */
		public void onDownloadFail(ImageView view);
		
		/**
		 * loading bitmap from url.the method should be called from client
		 * @param url: the url used to load image
		 * @return: the result bitmap,if return null,it should be fail status
		 */
		public Bitmap loadImageFromUrl(String url);
		
		/**
		 * doing when the download return success image
		 * @param view
		 * @param bitmap
		 */
		public void onDownloadSuccessed(ImageView view, Bitmap bitmap);
	}

}
