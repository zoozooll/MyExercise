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
package com.oregonscientific.meep.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
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
	private static final String TAG = "ImageDownloader";
	private static final String CLIENT_AGENT = "Meep/3.0";
	private ImageCache cache;
	private final Context context;
	
	// Default in sample size of the bitmap
	private static final int DEFAULT_SAMPLE_SIZE = Integer.MAX_VALUE;
	
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
		this(context, ImageCache.getDiskCacheDir(context, uniqueName));
	}
	
	public ImageDownloader(Context ctx, File diskCacheDir) {
		ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(diskCacheDir);
		cacheParams.initDiskCacheOnCreate = true;
		cacheParams.setMemCacheSizePercent(0.1f); // Set memory cache to 10% of app memory
		cache = new ImageCache(cacheParams);
		context = ctx;
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
		download(url, DEFAULT_SAMPLE_SIZE, DEFAULT_SAMPLE_SIZE, imageView);
	}
	
	/**
	 * Download the specified image from the Internet and binds it to the provided
	 * ImageView. The binding is immediate if the image is found in the cache and
	 * will be done asynchronously otherwise. A null bitmap will be associated to
	 * the ImageView if an error occurs.
	 * 
	 * @param url
	 *          The URL of the image to download.
	 * @param maxWidth maximum width of the image
	 * @param maxHeight maximum height of the image
	 * @param imageView
	 *          The ImageView to bind the downloaded image to.
	 */
	public void download(String url, int maxWidth, int maxHeight, ImageView imageView) {
		download(url, -1, maxWidth, maxHeight, imageView);
	}
	
	/**
	 * Downloads image as specified in {@code url}.If no Internet connection is available, or the remote resource
	 * cannot be accessed, set the {@code imageView} to use the given resource.
	 * 
	 * @param url Absolute URL to the remote resource
	 * @param defaultResId The resource ID to use if the remote resource cannot be accessed
	 * @param maxWidth maximum width of the image
	 * @param maxHeight maximum height of the image
	 * @param imageView the {@link ImageView} to hold the bitmap
	 */
	public void download(String url, int defaultResId, int maxWidth, int maxHeight, ImageView imageView) {
		// Quick return if the request cannot be processed
		if (imageView == null) {
			return;
		}
		
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url, maxWidth, maxHeight);
		
		if (bitmap == null) {
			forceDownload(url, defaultResId, maxWidth, maxHeight, imageView);
		} else {
			cancelPotentialDownload(url, imageView);
			imageView.setImageBitmap(bitmap);
		}
	}

	/*
	 * Same as download but the image is always downloaded and the cache is not
	 * used. Kept private at the moment as its interest is not clear. private void
	 * forceDownload(String url, ImageView view) { forceDownload(url, view, null);
	 * }
	 */

	/**
	 * Same as download but the image is always downloaded and the cache is not
	 * used. Kept private at the moment as its interest is not clear.
	 */
	private void forceDownload(String url, int resId, int maxWidth, int maxHeight, ImageView imageView) {
		// State sanity: url is guaranteed to never be null in DownloadedDrawable
		// and cache keys.
		if (url == null) {
			imageView.setImageDrawable(null);
			return;
		}

		if (cancelPotentialDownload(url, imageView)) {
			switch (mode) {
			case NO_ASYNC_TASK:
				Bitmap bitmap = downloadBitmap(url, maxWidth, maxHeight);
				addBitmapToCache(url, bitmap);
				if (imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
				break;

			case NO_DOWNLOADED_DRAWABLE:
				BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
				task.execute(
						url, 
						Integer.valueOf(resId).toString(), 
						Integer.valueOf(maxWidth).toString(),
						Integer.valueOf(maxHeight).toString());
				break;

			case CORRECT:
				task = new BitmapDownloaderTask(imageView);
				DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
				if (imageView != null) {
					imageView.setImageDrawable(downloadedDrawable);
				}
				task.execute(
						url, 
						Integer.valueOf(resId).toString(), 
						Integer.valueOf(maxWidth).toString(),
						Integer.valueOf(maxHeight).toString());
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
			if (drawable instanceof DownloadedDrawable) {
				DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}
	
	public byte[] readBytes(InputStream inputStream) throws IOException {
		// this dynamically extends to take the bytes you read
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

		// this is storage overwritten on each iteration with bytes
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];

		// we need to know how may bytes were read to write them to the
		// byteBuffer
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			byteBuffer.write(buffer, 0, len);
		}

		// and then we can return your byte array.
		return byteBuffer.toByteArray();
	}

	Bitmap downloadBitmap(String url, int maxWidth, int maxHeight) {
		// AndroidHttpClient is not allowed to be used from the main thread
		final HttpClient client = (mode == Mode.NO_ASYNC_TASK) ? new DefaultHttpClient()
				: AndroidHttpClient.newInstance(CLIENT_AGENT);
		HttpGet getRequest = null;
		
		try {
			getRequest = new HttpGet(url);
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w(TAG, "Error " + statusCode + " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					// Bug on slow connections, fixed in future release.
					byte[] buffer = readBytes(new BufferedInputStream(inputStream));
					return BitmapUtils.decodeSampledBitmapFromByteArray(buffer, 0, buffer.length, maxWidth, maxHeight);
					
			    // Bug fix: decodeStream without downsampling could cause outOfMemory error
					// return BitmapFactory.decodeStream(new FlushedInputStream(inputStream), null, options);
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (IOException e) {
			if (getRequest != null) {
				getRequest.abort();
			}
			Log.w(TAG, "I/O error while retrieving bitmap from " + url, e);
		} catch (IllegalStateException e) {
			if (getRequest != null) {
				getRequest.abort();
			}
			Log.w(TAG, "Incorrect URL: " + url);
		} catch (Exception e) {
			if (getRequest != null) {
				getRequest.abort();
			}
			Log.w(TAG, "Error while retrieving bitmap from " + url, e);
		} finally {
			if ((client instanceof AndroidHttpClient)) {
				((AndroidHttpClient) client).close();
			}
		}
		return null;
	}

	/*
	 * An InputStream that skips the exact number of bytes provided, unless it
	 * reaches EOF.
	 */
	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

	/**
	 * The actual AsyncTask that will asynchronously download the image.
	 */
	class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		private String url;
		private int resId;
		private int maxWidth;
		private int maxHeight;
		private final WeakReference<ImageView> imageViewReference;

		public BitmapDownloaderTask(ImageView imageView) {
			imageViewReference = imageView == null ? null : new WeakReference<ImageView>(imageView);
		}

		/**
		 * Actual download method.
		 */
		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap result = null;
			try {
				if (params.length == 4) {
					url = params[0];
					resId = Integer.parseInt(params[1]);
					maxWidth = Integer.parseInt(params[2]);
					maxHeight = Integer.parseInt(params[3]);
					return downloadBitmap(url, maxWidth, maxHeight);
				}
			} catch (Exception ex) {
				Log.e(TAG, "Cannot download bitmap because " + ex);
			}
			return result;
		}

		/**
		 * Once the image is downloaded, associates it to the imageView
		 */
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}

			addBitmapToCache(url, bitmap);

			if (imageViewReference != null) {
				ImageView imageView = imageViewReference.get();
				BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
				// Change bitmap only if this process is still associated with it
				// Or if we don't use any bitmap to task association
				// (NO_DOWNLOADED_DRAWABLE mode)
				if ((this == bitmapDownloaderTask) || (mode != Mode.CORRECT)) {
					if (bitmap == null && resId != -1) {
						bitmap = BitmapUtils.decodeSampledBitmapFromResource(context.getResources(), resId, maxWidth, maxHeight);
					}
					imageView.setImageBitmap(bitmap);
				}
			}
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
	static class DownloadedDrawable extends ColorDrawable {
		private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

		public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
			super(Color.TRANSPARENT);
			bitmapDownloaderTaskReference = new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
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
	private Bitmap getBitmapFromCache(String url, int maxWidth, int maxHeight) {
		// First try the memory cache
		Bitmap bitmap = cache.getBitmapFromMemCache(url);
		if (bitmap != null && bitmap.getWidth() <= maxWidth && bitmap.getHeight() <= maxHeight) {
			return bitmap;
		}
		
		// Then try the soft reference cache
		bitmap = cache.getBitmapFromDiskCache(url, maxWidth, maxHeight);
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
}
