package com.oregonscientific.meep.store2.ctrl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.oregonscientific.meep.store2.global.MeepStoreApplication;
import com.oregonscientific.meep.store2.global.MeepStoreLog;
import com.oregonscientific.meep.store2.object.BannerItem;
import com.oregonscientific.meep.store2.object.DownloadImageItem;
import com.oregonscientific.meep.store2.object.MeepStoreItem;

public class RestoreImageDownloadCtrl {

	ArrayDeque<DownloadImageItem> mDownloadQ;
	private boolean mIsDownloading = false;
	//MeepStoreItem mDownloadingItem =null;
	DownloadListener mDownloadListener;
	Thread mHttpThread;
	AndroidHttpClient mHttpClient;
	StoreItemCacheCtrl mCacheCtrl;
	Context mContext;
	boolean mStopDownload = false;
	private Object mLock = new Object();
	
	public interface DownloadListener
	{
		public abstract void onDownloadCompleted(boolean downloadAborted, DownloadImageItem item);
	}
	
	public DownloadListener getDownloadListener() {
		return mDownloadListener;
	}

	public void setDownloadListener(DownloadListener downloadListener) {
		this.mDownloadListener = downloadListener;
	}
	
	public RestoreImageDownloadCtrl(Context context, SQLiteDatabase db, String name)
	{
		mContext = context;
		mCacheCtrl = new StoreItemCacheCtrl(db);
		mDownloadQ = new ArrayDeque<DownloadImageItem>();
		mHttpThread = new Thread(mHttpDownloadRunnable);
		mHttpThread.setName(name);
		mHttpClient = AndroidHttpClient.newInstance("AndroidDownloader");
	}
	
	public void startDownload()
	{
		mHttpThread.start();
	}
	
	public void addToDownloadQ(DownloadImageItem item)
	{
		MeepStoreLog.logcatMessage("storeimagedownload", "add to downloadQ" + item.getId());
		Bitmap bmp = mCacheCtrl.getImageCache(item.getId());
		if(bmp != null){
			mDownloadListener.onDownloadCompleted(false, item);
			MeepStoreLog.logcatMessage("storeimagedownload", "found on db" + item.getId());
			return;
		}
		synchronized(mLock) {
			mDownloadQ.add(item);
		}
	}
	
	public boolean isDownloading() {
		return mIsDownloading;
	}

	public void setIsDownloading(boolean mIsDownloading) {
		this.mIsDownloading = mIsDownloading;
	}

	private Runnable mHttpDownloadRunnable = new Runnable() {

		@Override
		public void run() {
			while (true) {
				//MeepStoreLog.logcatMessage("storeimagedownload", "downlaod q size" +mDownloadQ.size() );
				if (mDownloadQ.size() > 0) {

					DownloadImageItem downloadingItem = null;
					synchronized(mLock) {
						downloadingItem = mDownloadQ.poll();
					}
					
					setIsDownloading(true);
					AndroidHttpClient client = AndroidHttpClient.newInstance("android/meep");
					//MeepStoreLog.logcatMessage("storeimagedownload", "start http download" + downloadingItem.getId());
					MeepStoreLog.logcatMessage("httpdownload", "start http download" + downloadingItem.getId());
					try {
						// get file
						HttpGet httpGet = new HttpGet(downloadingItem.getUrl());

						HttpResponse response = client.execute(httpGet);
						HttpEntity entity = response.getEntity();
						if (entity != null) {
							InputStream is = response.getEntity().getContent();
							byte[] bytes = null;
							if (is != null) {
								int contentLength = is.available();
								BitmapFactory.Options options = new BitmapFactory.Options();
								options.inPurgeable = true;
								BufferedInputStream bis = new BufferedInputStream(is);
								Bitmap downloadBitmap = BitmapFactory.decodeStream(bis, null, options);
								downloadingItem.setImage(downloadBitmap);
								
								bytes = readBytes(is);

								is.close();
								bis.close();
							}
							entity.consumeContent();
							
							if (bytes == null) {
								if (mDownloadListener != null)
									mDownloadListener.onDownloadCompleted(true, downloadingItem);
							} else {
								if (mDownloadListener != null)
									mDownloadListener.onDownloadCompleted(false, downloadingItem);
							}
						}
					} catch (Exception e) {
						if (mDownloadListener != null)
							mDownloadListener.onDownloadCompleted(true, downloadingItem);
//						MeepStoreLog.logcatMessage("test","3");
						Log.e("httpdownload",e.toString());
						e.printStackTrace();
						synchronized(mLock) {
							mDownloadQ.add(downloadingItem);
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException ex) {
							ex.printStackTrace();
							Log.e("httpdownload",ex.toString());
						}
					} finally {
						client.close();
						setIsDownloading(false);
					}
				} else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
						Log.e("httpdownload",e.toString());
					}
				}
			}
		}
	};
	
	private void clearCurrentDownload() {
		synchronized(mLock) {
			mDownloadQ.clear();
		}
		mStopDownload = true;
	}
	
	public void downloadMeepStoreItems(ArrayList<MeepStoreItem> itemList){
		clearCurrentDownload();
		for(int i=0; i<itemList.size(); i++){
			MeepStoreItem item = itemList.get(i);
			DownloadImageItem dlItem = new DownloadImageItem(item.getItemId(), getIconUrl(item.getIconUrl()));
			synchronized(mLock) {
				mDownloadQ.add(dlItem);
			}
		}
	}
	
	public void addMeepStoreItems(ArrayList<MeepStoreItem> itemList){
		for(int i=0; i<itemList.size(); i++){
			MeepStoreItem item = itemList.get(i);
			DownloadImageItem dlItem = new DownloadImageItem(item.getItemId(), getIconUrl(item.getIconUrl()));
			synchronized(mLock) {
				mDownloadQ.add(dlItem);
			}
		}
	}
	
	private String getIconUrl(String iconUrl){

		MeepStoreApplication app = (MeepStoreApplication)mContext.getApplicationContext();
		String prefix  = app.getLoginInfo().url_prefix;
		if(!iconUrl.toLowerCase().contains("http")){
			return prefix +  iconUrl ;
		}
		return iconUrl;
	}
	
	
	
	public void downloadBanner(ArrayList<BannerItem> bannerList) {
		clearCurrentDownload();
		for (int i = 0; i < bannerList.size(); i++) {
			BannerItem banner = bannerList.get(i);

			DownloadImageItem item = new DownloadImageItem(banner.id, banner.image);
			synchronized(mLock) {
				mDownloadQ.add(item);
			}
		}
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
		while (inputStream.available() > 0 && (len = inputStream.read(buffer)) != -1) {
			if (mStopDownload) {
				mStopDownload = false;
				return null;
			}
			byteBuffer.write(buffer, 0, len);
		}

		// and then we can return your byte array.
		return byteBuffer.toByteArray();
	}
}
