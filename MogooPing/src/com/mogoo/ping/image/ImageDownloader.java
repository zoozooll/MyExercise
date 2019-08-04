package com.mogoo.ping.image;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.mogoo.ping.R;
import com.mogoo.ping.view.ExAsyncTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
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
 * 
 * @author feng.deliang@qq.com
 */
public class ImageDownloader {
	private static final String LOG_TAG = "ImageDownloader";
	private static final int IO_BUFFER_SIZE = 1024;
	
	private static final String IMAGE_CACHE_DIR = "images";
	
	private ImageCache mImageCache;
	
	public enum Mode {
		TOUCH_TO_LOAD, NO_DOWNLOADED_DRAWABLE, CORRECT
	}

	private Mode mode = Mode.CORRECT;
	private static Bitmap defaultImage;
	private static Bitmap loadingImage;
	private static Bitmap touch2loadImage;

	private static Context mContext;
	
	private ImageDownloader(Context cxt) {
		mContext = cxt;
		defaultImage = BitmapFactory.decodeResource(cxt.getResources(),
				R.drawable.ic_launcher);
		loadingImage = BitmapFactory.decodeResource(cxt.getResources(),
				R.drawable.defautl_pic_loading);
		touch2loadImage = BitmapFactory.decodeResource(cxt.getResources(),
				R.drawable.defautl_pic_loading);
		mImageCache = new ImageCache(mContext, IMAGE_CACHE_DIR);
	}

	private static ImageDownloader instance;

	public static ImageDownloader getInstance(Context ctx) {
		if (instance == null) {
			instance = new ImageDownloader(ctx);
		}
//		reStoreDefaultRes();
		return instance;
	}
	
//	public void setDefaultRes(int defaultImgid, int loadingImgid, int touch2loadImgid) {
//		defaultImage = BitmapFactory.decodeResource(mContext.getResources(),
//				defaultImgid);
//		loadingImage = BitmapFactory.decodeResource(mContext.getResources(),
//				loadingImgid);
//		touch2loadImage = BitmapFactory.decodeResource(mContext.getResources(),
//				touch2loadImgid);
//	}
//	
//	public static void reStoreDefaultRes() {
//		defaultImage = BitmapFactory.decodeResource(mContext.getResources(),
//				R.drawable.defautl_list_itme_pic_loading);
//		loadingImage = BitmapFactory.decodeResource(mContext.getResources(),
//				R.drawable.defautl_list_itme_pic_loading);
//		touch2loadImage = BitmapFactory.decodeResource(mContext.getResources(),
//				R.drawable.defautl_list_itme_pic_loading);
//	}
	
	/**
	 * Download the specified image from the Internet and binds it to the
	 * provided ImageView. The binding is immediate if the image is found in the
	 * cache and will be done asynchronously otherwise. (A null bitmap will be
	 * associated to the ImageView if an error occurs.)no，我改了，就设置默认图片
	 * ，图片高度大于200则默认加载缩略图
	 * 
	 * @param url
	 *            The URL of the image to download.
	 * @param imageView
	 *            The ImageView to bind the downloaded image to.
	 */
//	public void download(String url, ImageView imageView) {
//		download(url, imageView, defaultImage);
//	}

	public void download(String url, ImageView imageView, Bitmap defaultImg) {
		download(url, imageView, false, defaultImg);
	}
	
	/**
	 * Download the specified image from the Internet and binds it to the
	 * provided ImageView. The binding is immediate if the image is found in the
	 * cache and will be done asynchronously otherwise. (A null bitmap will be
	 * associated to the ImageView if an error occurs.)no，我改了，就设置默认图片
	 * ，图片高度大于200则默认加载缩略图
	 * 
	 * @param url
	 *            The URL of the image to download.
	 * @param imageView
	 *            The ImageView to bind the downloaded image to.
	 * @param dispatchTouchEvent2Parent
	 *            是否传递触摸事件给ImageView的父控件，仅在模式为{@link #mode = TOUCH_TO_LOAD}时使用
	 */
	public void download(String url, ImageView imageView,
			boolean dispatchTouchEvent2Parent) {
		download(url, imageView, false, defaultImage);
	}

	public void download(String url, ImageView imageView,
			boolean dispatchTouchEvent2Parent, Bitmap defaultImg) {
		if (null == url || "".equals(url))
		{
			Log.e(this.getClass().getSimpleName(), "image url can not be null..................");
			if(defaultImg != null) {
				imageView.setImageBitmap(defaultImg);
			}else {
				imageView.setImageBitmap(defaultImage);
			}
			
			return;
		}
		resetBitmapPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);
		imageView.setOnTouchListener(null);
		if (bitmap == null) {
			forceDownload(url, imageView, false, dispatchTouchEvent2Parent, defaultImg);
		} else {
			cancelPotentialDownload(url, imageView);
			imageView.setImageBitmap(bitmap);
			// imageView.setOnTouchListener(null);
		}
	}
	
	/**
	 * Download the specified image thumbnail from the Internet and binds it to
	 * the provided ImageView. The binding is immediate if the image is found in
	 * the cache and will be done asynchronously otherwise. A null bitmap will
	 * be associated to the ImageView if an error occurs.
	 * 
	 * @param url
	 *            The URL of the image to download.
	 * @param imageView
	 *            The ImageView to bind the downloaded image to.
	 */
	public void downloadThumbnail(String url, ImageView imageView) {
		downloadThumbnail(url, imageView, false);
	}

	/**
	 * Download the specified image thumbnail from the Internet and binds it to
	 * the provided ImageView. The binding is immediate if the image is found in
	 * the cache and will be done asynchronously otherwise. A null bitmap will
	 * be associated to the ImageView if an error occurs.
	 * 
	 * @param url
	 *            The URL of the image to download.
	 * @param imageView
	 *            The ImageView to bind the downloaded image to.
	 * @param dispatchTouchEvent2Parent
	 *            是否传递触摸事件给ImageView的父控件，仅在模式为{@link #mode 为 TOUCH_TO_LOAD}时使用
	 */
	public void downloadThumbnail(String url, ImageView imageView,
			boolean dispatchTouchEvent2Parent) {
		resetThumbnailPurgeTimer();
		// 先从大图的缓存中找，找不到再从缩略图缓存中找
		Bitmap bitmap = getBitmapFromCache(url);
		imageView.setOnTouchListener(null);
		if (bitmap == null) {
			bitmap = getThumbnailFromCache(url);
		}
		// 仍然没有找到，去下载
		if (bitmap == null) {
			forceDownload(url, imageView, true, dispatchTouchEvent2Parent);
		} else {
			cancelPotentialDownload(url, imageView);
			imageView.setImageBitmap(bitmap);
			// imageView.setOnTouchListener(null);
		}
	}

	/*
	 * Same as download but the image is always downloaded and the cache is not
	 * used. Kept private at the moment as its interest is not clear. private
	 * void forceDownload(String url, ImageView view) { forceDownload(url, view,
	 * null); }
	 */

	/**
	 * Same as download but the image is always downloaded and the cache is not
	 * used. Kept private at the moment as its interest is not clear.
	 */
	private void forceDownload(final String url, final ImageView imageView,
			final boolean thumbnails, final boolean dispatchTouchEvent2Parent) {
		forceDownload(url, imageView, thumbnails, dispatchTouchEvent2Parent, defaultImage);
	}

	private void forceDownload(final String url, final ImageView imageView,
			final boolean thumbnails, final boolean dispatchTouchEvent2Parent, Bitmap defaultImg) {
		final Bitmap defaultBg = (defaultImg != null) ? defaultImg : defaultImage;
		// State sanity: url is guaranteed to never be null in
		// DownloadedDrawable and cache keys.
		if (url == null || "".equals(url)) {
			imageView.setImageBitmap(defaultBg);
			return;
		}

		if (cancelPotentialDownload(url, imageView)) {
			switch (mode) {
			case TOUCH_TO_LOAD:
				imageView.setOnTouchListener(new OnTouchListener() {

					public boolean onTouch(View v, MotionEvent event) {
						BitmapDownloaderTask task = new BitmapDownloaderTask(
								imageView, thumbnails, defaultBg);
						DownloadedDrawable downloadedDrawable = new DownloadedDrawable(
								task);
						imageView.setImageDrawable(downloadedDrawable);
						imageView.setMinimumHeight(138);
						task.execute(url);
						// 注意下面是否定式，不要删除取反的感叹号
						return !dispatchTouchEvent2Parent;
					}

				});
				imageView.setImageBitmap(touch2loadImage);
				break;

			case NO_DOWNLOADED_DRAWABLE:
				imageView.setMinimumHeight(138);
				BitmapDownloaderTask task = new BitmapDownloaderTask(imageView,
						thumbnails, defaultBg);
				task.execute(url);
				break;

			case CORRECT:
				task = new BitmapDownloaderTask(imageView, thumbnails, defaultBg);
				DownloadedDrawable downloadedDrawable = new DownloadedDrawable(
						task);
				imageView.setImageDrawable(downloadedDrawable);
//				imageView.setMinimumHeight(138);
				task.execute(url);
				break;
			}
		}
	}
	
	/**
	 * Returns true if the current download has been canceled or if there was no
	 * download in progress on this image view. Returns false if the download in
	 * progress deals with the same url. The download is not stopped in that
	 * case.
	 */
	private static boolean cancelPotentialDownload(String url,
			ImageView imageView) {
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
	 *            Any imageView
	 * @return Retrieve the currently active download task (if any) associated
	 *         with this imageView. null if there is no such task.
	 */
	private static BitmapDownloaderTask getBitmapDownloaderTask(
			ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof DownloadedDrawable) {
				DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}

	/**
	 * @param url
	 *            图片路径
	 * @param thumbnails
	 *            加载缩略图
	 * @return
	 */
	Bitmap downloadBitmap(String url, boolean thumbnails) {

		// AndroidHttpClient is not allowed to be used from the main thread
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);// 设置连接超时时间为3秒
		HttpConnectionParams.setSoTimeout(httpParameters, 5000);// 设置socket超时时间为5秒
		final HttpClient client = new DefaultHttpClient(httpParameters);
		url = url.replace('\\', '/');
		HttpGet getRequest = null;

		try {
			getRequest = new HttpGet(url);
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				BufferedOutputStream out = null;
				try {
					inputStream = new BufferedInputStream(entity.getContent(),
							IO_BUFFER_SIZE);

					// return BitmapFactory.decodeStream(inputStream);
					// Bug on slow connections, fixed in future release.
					int minHeight = 200;
					// 加载缩略图
					if (thumbnails) {
						minHeight = 50;
					}

					InputStream in = new FlushedInputStream(inputStream);
					final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
					out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
					copy(in, out);
					out.flush();
					final byte[] data = dataStream.toByteArray();

					BitmapFactory.Options options = new BitmapFactory.Options();
					options.outHeight = minHeight;// 缩略图默认的告诉设为80吧节省流量
					options.inJustDecodeBounds = true;

					BitmapFactory
							.decodeByteArray(data, 0, data.length, options);
					options.inJustDecodeBounds = false;
					int be = options.outHeight / (minHeight / 10);
					if (be % 10 != 0) {
						be += 10;
					}
					be = be / 10;
					if (be <= 0) {
						be = 1;
					}
					options.inSampleSize = be;
					return BitmapFactory.decodeByteArray(data, 0, data.length,
							options);

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
			Log.w(LOG_TAG, "I/O error while retrieving bitmap from " + url, e);
		} catch (IllegalStateException e) {
			if (getRequest != null) {
				getRequest.abort();
			}
			Log.w(LOG_TAG, "Incorrect URL: " + url);
		} catch (Exception e) {
			if (getRequest != null) {
				getRequest.abort();
			}
			Log.w(LOG_TAG, "Error while retrieving bitmap from " + url, e);
		} finally {

		}
		return null;
	}

	/**
	 * Copy the content of the input stream into the output stream, using a
	 * temporary byte array buffer whose size is defined by
	 * {@link #IO_BUFFER_SIZE}.
	 * 
	 * @param in
	 *            The input stream to copy from.
	 * @param out
	 *            The output stream to copy to.
	 * @throws IOException
	 *             If any error occurs during the copy.
	 */
	private static void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] b = new byte[IO_BUFFER_SIZE];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}

	/*
	 * 注意：旧版的BitmapFactory.decodeStream有个bug，可能使得在网络较慢的时候无法正常工作。
	 * 可以使用FlushedInputStream(inputStream)代替原始的inputStream来解决这个问题。
	 * 这个类可以保证skip()确实跳过了参数提供的字节数，直到流文件的末尾。 下面是这个helper class的实现： An InputStream
	 * that skips the exact number of bytes provided, unless it reaches EOF.
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
	class BitmapDownloaderTask extends ExAsyncTask<String, Void, Bitmap> {
		private String url;
		private boolean thumbnails;
		private final WeakReference<ImageView> imageViewReference;
		private Bitmap mDefaultImg;

		public BitmapDownloaderTask(ImageView imageView, boolean thumbnails, Bitmap defaultImg) {
			imageViewReference = new WeakReference<ImageView>(imageView);
			this.thumbnails = thumbnails;
			this.mDefaultImg = defaultImg;
		}

		public Bitmap getDefaultImage() {
			return mDefaultImg;
		}
		
		/**
		 * Actual download method.
		 */
		@Override
		protected Bitmap doInBackground(String... params) {
			url = params[0];
			Bitmap bitmap = null;
			if(mImageCache != null) {
				bitmap = mImageCache.getBitmapFromDiskCache(url);
			}
			if(bitmap == null) {
				bitmap = downloadBitmap(url, thumbnails);
			}
			
			if(bitmap != null && mImageCache != null) {
				mImageCache.addBitmapToCache(url, bitmap);
			}
			return bitmap;
		}

		/**
		 * Once the image is downloaded, associates it to the imageView
		 */
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}
			if (bitmap != null) {
				if (thumbnails) {
					addThumbnailToCache(url, bitmap);
				} else {
					//bitmap = toRoundCorner(bitmap, 10);   //去掉圆角
					addBitmapToCache(url, bitmap);
				}
			}

			if (imageViewReference != null) {
				ImageView imageView = imageViewReference.get();
				BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
				// Change bitmap only if this process is still associated with
				// it
				// Or if we don't use any bitmap to task association
				// (NO_DOWNLOADED_DRAWABLE mode)
				if (imageView != null) {
					if (((this == bitmapDownloaderTask) || (mode == Mode.NO_DOWNLOADED_DRAWABLE))
							&& bitmap != null) {
						imageView.setImageBitmap(bitmap);
						imageView.setOnTouchListener(null);
					} else if (bitmap == null) {
						imageView.setImageBitmap(mDefaultImg);
					}
				}
			}
		}
	}

	/**
	 * 为了计步器这个项目单独加的方法 把图片变成圆角
	 * 
	 * @param bitmap
	 *            需要修改的图片
	 * @param pixels
	 *            圆角的弧度
	 * @return 圆角图片
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		int width = bitmap.getWidth() < 100 ? bitmap.getWidth() : 100;
		int height = bitmap.getHeight() < 100 ? bitmap.getHeight() : 100;
		Bitmap output = Bitmap.createBitmap(width, height,
				android.graphics.Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, width, width);
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(
				android.graphics.PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		bitmap.recycle();
		return output;
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
	static class DownloadedDrawable extends BitmapDrawable {
		private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

		public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
			//super(loadingImage);
			super(bitmapDownloaderTask.getDefaultImage());
			bitmapDownloaderTaskReference = new WeakReference<BitmapDownloaderTask>(
					bitmapDownloaderTask);
		}

		public BitmapDownloaderTask getBitmapDownloaderTask() {
			return bitmapDownloaderTaskReference.get();
		}
	}

	public void setMode(Mode mode) {
		this.mode = mode;
		// clearBitmapCache();
		// clearThumbnailCache();
	}

	public Mode getMode() {
		return this.mode;
	}

	/*
	 * Cache-related fields and methods.
	 * 
	 * We use a hard and a soft cache. A soft reference cache is too
	 * aggressively cleared by the Garbage Collector.
	 */
	private static final int HARD_BITMAP_CACHE_CAPACITY = 10;
	private static final int HARD_THUMBNAIL_CACHE_CAPACITY = 20;
	private static final int DELAY_BEFORE_PURGE = 3 * 60 * 1000; // in
																	// milliseconds

	// Hard cache, with a fixed maximum capacity and a life duration
	private final HashMap<String, Bitmap> sHardBitmapCache = new LinkedHashMap<String, Bitmap>(
			HARD_BITMAP_CACHE_CAPACITY / 2, 0.75f, true) {

		private static final long serialVersionUID = 7139452013189129333L;

		@Override
		protected boolean removeEldestEntry(
				LinkedHashMap.Entry<String, Bitmap> eldest) {
			if (size() > HARD_BITMAP_CACHE_CAPACITY) {
				// Entries push-out of hard reference cache are transferred to
				// soft reference cache
				sSoftBitmapCache.put(eldest.getKey(),
						new SoftReference<Bitmap>(eldest.getValue()));
				return true;
			} else
				return false;
		}
	};

	// Hard cache, with a fixed maximum capacity and a life duration
	private final HashMap<String, Bitmap> sHardThumbnailCache = new LinkedHashMap<String, Bitmap>(
			HARD_THUMBNAIL_CACHE_CAPACITY / 2, 0.75f, true) {

		private static final long serialVersionUID = 5580214240087944286L;

		@Override
		protected boolean removeEldestEntry(
				LinkedHashMap.Entry<String, Bitmap> eldest) {
			if (size() > HARD_THUMBNAIL_CACHE_CAPACITY) {
				// Entries push-out of hard reference cache are transferred to
				// soft reference cache
				sSoftThumbnailCache.put(eldest.getKey(),
						new SoftReference<Bitmap>(eldest.getValue()));
				return true;
			} else
				return false;
		}
	};

	// Soft cache for bitmaps kicked out of hard cache
	private final static ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(
			HARD_BITMAP_CACHE_CAPACITY / 2);

	// Soft cache for bitmaps kicked out of hard cache
	private final static ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftThumbnailCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(
			HARD_THUMBNAIL_CACHE_CAPACITY / 2);

	private final Handler bitmapPurgeHandler = new Handler();
	private final Handler thumbnailPurgeHandler = new Handler();

	private final Runnable bitmapPurger = new Runnable() {
		public void run() {
			clearBitmapCache();
		}
	};

	private final Runnable thumbnailPurger = new Runnable() {
		public void run() {
			clearThumbnailCache();
		}
	};

	/**
	 * Adds this bitmap to the cache.
	 * 
	 * @param bitmap
	 *            The newly downloaded bitmap.
	 */
	private void addBitmapToCache(String url, Bitmap bitmap) {
		if (bitmap != null) {
			synchronized (sHardBitmapCache) {
				sHardBitmapCache.put(url, bitmap);
			}
		}
	}

	/**
	 * Adds this Thumbnail to the cache.
	 * 
	 * @param bitmap
	 *            The newly downloaded bitmap.
	 */
	private void addThumbnailToCache(String url, Bitmap bitmap) {
		if (bitmap != null) {
			synchronized (sHardThumbnailCache) {
				sHardThumbnailCache.put(url, bitmap);
			}
		}
	}

	/**
	 * @param url
	 *            The URL of the image that will be retrieved from the cache.
	 * @return The cached bitmap or null if it was not found.
	 */
	private Bitmap getBitmapFromCache(String url) {
		// First try the hard reference cache
		synchronized (sHardBitmapCache) {
			final Bitmap bitmap = sHardBitmapCache.get(url);
			if (bitmap != null) {
				// Bitmap found in hard cache
				// Move element to first position, so that it is removed last
				sHardBitmapCache.remove(url);
				sHardBitmapCache.put(url, bitmap);
				return bitmap;
			}
		}

		// Then try the soft reference cache
		SoftReference<Bitmap> bitmapReference = sSoftBitmapCache.get(url);
		if (bitmapReference != null) {
			final Bitmap bitmap = bitmapReference.get();
			if (bitmap != null) {
				// Bitmap found in soft cache
				return bitmap;
			} else {
				// Soft reference has been Garbage Collected
				sSoftBitmapCache.remove(url);
			}
		}

		return null;
	}

	/**
	 * @param url
	 *            The URL of the image that will be retrieved from the cache.
	 * @return The cached bitmap or null if it was not found.
	 */
	private Bitmap getThumbnailFromCache(String url) {
		// First try the hard reference cache
		synchronized (sHardThumbnailCache) {
			final Bitmap bitmap = sHardThumbnailCache.get(url);
			if (bitmap != null) {
				// Bitmap found in hard cache
				// Move element to first position, so that it is removed last
				sHardThumbnailCache.remove(url);
				sHardThumbnailCache.put(url, bitmap);
				return bitmap;
			}
		}

		// Then try the soft reference cache
		SoftReference<Bitmap> bitmapReference = sSoftThumbnailCache.get(url);
		if (bitmapReference != null) {
			final Bitmap bitmap = bitmapReference.get();
			if (bitmap != null) {
				// Bitmap found in soft cache
				return bitmap;
			} else {
				// Soft reference has been Garbage Collected
				sSoftThumbnailCache.remove(url);
			}
		}

		return null;
	}

	/**
	 * Clears the image cache used internally to improve performance. Note that
	 * for memory efficiency reasons, the cache will automatically be cleared
	 * after a certain inactivity delay.
	 */
	public void clearBitmapCache() {
		sHardBitmapCache.clear();
		sSoftBitmapCache.clear();
	}

	/**
	 * Clears the image cache used internally to improve performance. Note that
	 * for memory efficiency reasons, the cache will automatically be cleared
	 * after a certain inactivity delay.
	 */
	public void clearThumbnailCache() {
		sHardThumbnailCache.clear();
		sSoftThumbnailCache.clear();
	}

	/**
	 * Allow a new delay before the automatic cache clear is done.
	 */
	private void resetBitmapPurgeTimer() {
		bitmapPurgeHandler.removeCallbacks(bitmapPurger);
		bitmapPurgeHandler.postDelayed(bitmapPurger, DELAY_BEFORE_PURGE);
	}

	/**
	 * Allow a new delay before the automatic cache clear is done.
	 */
	private void resetThumbnailPurgeTimer() {
		thumbnailPurgeHandler.removeCallbacks(thumbnailPurger);
		thumbnailPurgeHandler.postDelayed(thumbnailPurger, DELAY_BEFORE_PURGE);
	}

	/**
	 * 提供给外部使用的直接获取已经缓存了的位图图像， 没有直接返回null，此方法不会触发下载 。
	 * 
	 * @return Bitmap 缓存中的Bitmap
	 */
	public Bitmap getCachedBitmap(String url) {
		resetThumbnailPurgeTimer();
		// 先从大图的缓存中找，找不到再从缩略图缓存中找
		Bitmap bitmap = getBitmapFromCache(url);
		if (bitmap == null) {
			bitmap = getThumbnailFromCache(url);
		}
		return bitmap;
	}
}
