package com.oregonscientific.meep.store2.adapter;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import com.oregonscientific.meep.store2.ctrl.StoreItemCacheCtrl;
import com.oregonscientific.meep.store2.db.TableStoreImageCache;

/**
 * This is an object that can load images from a URL on a thread.
 *
 */
public class ImageThreadLoader {
	private static final String TAG = "ImageThreadLoader";
	private static boolean shouldClear = false;
	// Global cache of images.
	// Using SoftReference to allow garbage collector to clean cache if needed
	private final HashMap<String, Bitmap> Cache = new HashMap<String,  Bitmap>();

	private final class QueueItem {
		public URL url;
		public ImageLoadedListener listener;
	}
	private final ArrayList<QueueItem> Queue = new ArrayList<QueueItem>();

	private final Handler handler = new Handler();	// Assumes that this is started from the main (UI) thread
	private Thread thread;
	private Object mLock = new Object();
	private QueueRunner runner = new QueueRunner();
	private int max;
	private SQLiteDatabase mdb;
	private StoreItemCacheCtrl mDbCacheCtrl;

	/** Creates a new instance of the ImageThreadLoader */
	public ImageThreadLoader(int max, SQLiteDatabase db) {
		this.max = max;
		mdb = db;
		if(db!= null){
			mDbCacheCtrl = new StoreItemCacheCtrl(db);
		}
	}

	/**
	 * Defines an interface for a callback that will handle
	 * responses from the thread loader when an image is done
	 * being loaded.
	 */
	public interface ImageLoadedListener {
		public void imageLoaded(Bitmap imageBitmap );
	}
	
	private void addQueueItem(QueueItem item) {
		synchronized(mLock) {
			Queue.add(item);
		}
	}
	
	private QueueItem getQueuedItem() {
		synchronized(mLock) {
			return Queue.remove(0);
		}
	}
	
	private synchronized boolean shouldFlushCache() {
		return shouldClear || Cache.size() > max;
	}

	/**
	 * Provides a Runnable class to handle loading
	 * the image from the URL and settings the
	 * ImageView on the UI thread.
	 */
	private class QueueRunner implements Runnable {
		public void run() {
//			synchronized (this) {
//				if (shouldClear) {
//					clearAndRecycle();
//				}
//				if (Cache.size() > max) {
//					clearAndRecycle();
//				}
//			}
			if (shouldFlushCache()) {
				clearAndRecycle();
			}
			
			final QueueItem item = getQueuedItem();
			// Remove memory cache
			final Bitmap cache = null;
//			final Bitmap cache = item == null ? null : getCachedImage(item.url.toString());

			// If in the cache, return that copy and be done
			if (cache != null) {
//			if( Cache.containsKey(item.url.toString()) && Cache.get(item.url.toString()) != null) {
//						Log.d(TAG, "read image from cache");
				// Use a handler to get back onto the UI thread for the update
				final ImageLoadedListener listener = item == null ? null : item.listener;
				handler.post(new Runnable() {
					public void run() {
						if (listener != null) {
							// NB: There's a potential race condition here where the cache item could get
							//     garbage collected between when we post the runnable and it's executed.
							//     Ideally we would re-run the network load or something.
							listener.imageLoaded(cache);
							
//							Bitmap ref = Cache.get(item.url.toString());
//							if( ref != null ) {
//								item.listener.imageLoaded(ref);
//							}
//									final Bitmap bmp = readBitmapFromNetwork(item.url);
//									if( bmp != null ) {
//										Cache.put(item.url.toString(), new SoftReference<Bitmap>(bmp));
//									}
						}
					}
				});
			} else {
				try {
					final Bitmap icon = item == null ? null : mDbCacheCtrl.getImageCache(item.url.toString());
					if (icon != null) {
						cacheImage(item.url.toString(), icon);
//						Cache.put(item.url.toString(), icon);
						Log.d(TAG, "icon download from db");
						final ImageLoadedListener listener = item == null ? null : item.listener;
						handler.post(new Runnable() {
							public void run() {
								if (listener != null) {
									listener.imageLoaded(icon);
								} else {
									Log.w(TAG, "Cannot download");

								}
							}
						});
						//if(item.listener!= null) item.listener.imageLoaded(icon);
					} else {
						final Bitmap bmp = item == null ? null : readBitmapFromNetwork(item.url);
						if (bmp != null) {
							Log.d(TAG, "icon download from network");
							cacheImage(item.url.toString(), bmp);
//							Cache.put(item.url.toString(), bmp);
							// Use a handler to get back onto the UI thread for the
							// update
							final ImageLoadedListener listener = item == null ? null : item.listener;
							handler.post(new Runnable() {
								public void run() {
									if (listener != null) {
										listener.imageLoaded(bmp);
										//Clear Record
										new Thread(new Runnable() {
											@Override
											public void run() {
												//Store bitmap into DB
												try {
													ByteArrayOutputStream stream = new ByteArrayOutputStream();
													bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
													byte[] byteArray = stream.toByteArray();
													stream.close();
													Log.w(TAG, "save image to db");
													mDbCacheCtrl.saveImageCache(item.url.toString(), byteArray);
													//Clear Data in DB
													TableStoreImageCache.clearRecords(mdb);
												} catch (Exception ex) {
													Log.e(TAG, ex.getMessage(), ex);
												}
											}
										}).start();
									} else {
										Log.w(TAG, "Cannot download");

									}
								}
							});
						}
					}
				}
				catch(OutOfMemoryError e) {
					shouldClear = true;
				}
				
			}
		}
	}
	
	private synchronized void cacheImage(String key, Bitmap cache) {
		if (key != null) {
			Cache.put(key, cache);
		}
	}
	
	private synchronized Bitmap getCachedImage(String key) {
		Bitmap result = null;
		if (Cache.containsKey(key)) {
			result = Cache.get(key);
		}
		return result;
	}

	/**
	 * Queues up a URI to load an image from for a given image view.
	 *
	 * @param uri	The URI source of the image
	 * @param callback	The listener class to call when the image is loaded
	 * @throws MalformedURLException If the provided uri cannot be parsed
	 * @return A Bitmap image if the image is in the cache, else null.
	 */
	public Bitmap loadImage( final String uri, final ImageLoadedListener listener) throws MalformedURLException {
//		// If it's in the cache, just get it and quit it
//		if( Cache.containsKey(uri)) {
//			Bitmap ref = Cache.get(uri);
//			if( ref != null) {
//				return ref;
//			}
//			else
//			{
//				Log.d(TAG, "icon ref image is null");
//			}
//		}
//		else
//		{
//			Log.d(TAG, "icon uri:" + uri.toString());
//		}
		
		Bitmap cache = getCachedImage(uri);
		if (cache != null) {
			return cache;
		}

		QueueItem item = new QueueItem();
		item.url = new URL(uri);
		item.listener = listener;
		addQueueItem(item);
		
//		thread = new Thread(runner);
//		thread.start();
		new Thread(runner).start();
		
		return null;
	}

	/**
	 * Convenience method to retrieve a bitmap image from
	 * a URL over the network. The built-in methods do
	 * not seem to work, as they return a FileNotFound
	 * exception.
	 *
	 * Note that this does not perform any threading --
	 * it blocks the call while retrieving the data.
	 *
	 * @param url The URL to read the bitmap from.
	 * @return A Bitmap image or null if an error occurs.
	 */
	public static Bitmap readBitmapFromNetwork( URL url ) {
		InputStream is = null;
		BufferedInputStream bis = null;
		Bitmap bmp = null;
		try {
			URLConnection conn = url.openConnection();
			conn.connect();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPurgeable = true;
			options.inTempStorage = new byte[16 * 1024];
//			options.inSampleSize = 2;
			is = conn.getInputStream();
			bis = new BufferedInputStream(is);
			bmp = BitmapFactory.decodeStream(bis, null, options);
		} catch (MalformedURLException e) {
			Log.e(TAG, "Bad ad URL", e);
		} catch (IOException e) {
			Log.e(TAG, "Could not get remote ad image", e);
		} catch (OutOfMemoryError e) {
			Log.e(TAG, "OutOfMemory", e);
			shouldClear = true;
		} catch (Exception e) {
			Log.e(TAG, "Exception", e);
		} finally {
			try {
				if (is != null)
					is.close();
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				Log.w(TAG, "Error closing stream.");
			}
		}
		return bmp;
	}
	
	public synchronized void clearAndRecycle()
	{
		Log.w(TAG, "CLEAR IMAGE CACHE");
		for (int del = 0; del < Cache.size(); del++) {
			Bitmap delBitmap = Cache.get(del);
			if (delBitmap != null) {
				Log.v(TAG, "release position:" + del);
				Cache.remove(del);
				delBitmap.recycle();
			}
		}
		Cache.clear();
		shouldClear = false;
	}
	
	public void clearImageCacheInDB()
	{
		Log.w(TAG,"CLEAR IMAGE CACHE IN DB");
		mDbCacheCtrl.clearImageOutofDate();
	}
	

}