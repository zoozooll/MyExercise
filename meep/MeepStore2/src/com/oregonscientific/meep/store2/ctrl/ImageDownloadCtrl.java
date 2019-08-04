package com.oregonscientific.meep.store2.ctrl;

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

public class ImageDownloadCtrl {

	ArrayDeque<DownloadImageItem> mDownloadQ;
	private boolean mIsDownloading = false;
	//MeepStoreItem mDownloadingItem =null;
	DownloadListener mDownloadListener;
	Thread mHttpThread;
	AndroidHttpClient mHttpClient;
	StoreItemCacheCtrl mCacheCtrl;
	Context mContext;
	boolean mStopDownload = false;
	
	public interface DownloadListener
	{
		public abstract void onDownloadCompleted(boolean downloadAborted, DownloadImageItem item);
	}
	
	public synchronized DownloadListener getDownloadListener() {
		return mDownloadListener;
	}

	public synchronized void setDownloadListener(DownloadListener downloadListener) {
		this.mDownloadListener = downloadListener;
	}
	
	public ImageDownloadCtrl(Context context, SQLiteDatabase db, String name)
	{
		mContext = context;
		mCacheCtrl = new StoreItemCacheCtrl(db);
		mDownloadQ = new ArrayDeque<DownloadImageItem>();
	
		mHttpClient = AndroidHttpClient.newInstance("AndroidDownloader");
	}
	
	public void startDownload()
	{	mHttpThread = new Thread(mHttpDownloadRunnable);
		mHttpThread.start();
	}
	
	public synchronized void addToDownloadQ(DownloadImageItem item)
	{
		MeepStoreLog.logcatMessage("restoreimagedownload", "add to downloadQ" + item.getId());
		Bitmap bmp = mCacheCtrl.getImageCache(item.getId());
		if(bmp != null){
			mDownloadListener.onDownloadCompleted(false, item);
			MeepStoreLog.logcatMessage("restoreimagedownload", "found on db" + item.getId());
			return;
		}
		mDownloadQ.add(item);
	}
	
	public synchronized DownloadImageItem getDownloadItemFromQueue() {
		return mDownloadQ.poll();
	}
	
	public synchronized boolean isDownloading() {
		return mIsDownloading;
	}

	public synchronized void setIsDownloading(boolean mIsDownloading) {
		this.mIsDownloading = mIsDownloading;
	}
	
	private Runnable mHttpDownloadRunnable = new Runnable() {

		@Override
		public void run() {
			while (true) {
				//MeepStoreLog.logcatMessage("storeimagedownload", "downlaod q size" +mDownloadQ.size() );
				DownloadImageItem downloadingItem = getDownloadItemFromQueue();
				if (downloadingItem != null) {
					AndroidHttpClient client = AndroidHttpClient.newInstance("android/meep");
					setIsDownloading(true);
					
					//MeepStoreLog.logcatMessage("storeimagedownload", "start http download" + downloadingItem.getId());
					MeepStoreLog.logcatMessage("restoreimagedownload", "start http download" + downloadingItem.getId());
					
					try {
						// get file
						HttpGet httpGet = new HttpGet(downloadingItem.getUrl());
						HttpResponse response = client.execute(httpGet);
						HttpEntity entity = response.getEntity();
						
						if (entity != null) {
							InputStream is = entity.getContent();
							Bitmap downloadBitmap = BitmapFactory.decodeStream(is);
							downloadingItem.setImage(downloadBitmap);
							byte[] bytes = readBytes(is);
							
							is.close();
							if (bytes == null) {
								DownloadListener listener = getDownloadListener();
								if (listener != null)
									listener.onDownloadCompleted(true, downloadingItem);
							} else {
								DownloadListener listener = getDownloadListener();
								if (listener != null)
									listener.onDownloadCompleted(false, downloadingItem);
							}
							entity.consumeContent();
						}
					} catch (Exception e) {
						DownloadListener listener = getDownloadListener();
						if (listener != null)
							listener.onDownloadCompleted(false, downloadingItem);

						Log.e("restoreimagedownload",e.toString());
						e.printStackTrace();

					} finally {
						client.close();
						setIsDownloading(false);
					}
				} else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e("restoreimagedownload", e.toString());
					}
				}
			}
		}
	};
	
	private synchronized void clearCurrentDownload(){
		mDownloadQ.clear();
		mStopDownload = true;
	}
	
	public synchronized void downloadMeepStoreItems(ArrayList<MeepStoreItem> itemList){
		clearCurrentDownload();
		for (int i = 0; i < itemList.size(); i++) {
			MeepStoreItem item = itemList.get(i);
			DownloadImageItem dlItem = new DownloadImageItem(item.getItemId(), getIconUrl(item.getIconUrl()));
			mDownloadQ.add(dlItem);
		}
	}
	
	public synchronized void addMeepStoreItems(ArrayList<MeepStoreItem> itemList){
		for(int i=0; i<itemList.size(); i++){
			MeepStoreItem item = itemList.get(i);
			DownloadImageItem dlItem = new DownloadImageItem(item.getItemId(), getIconUrl(item.getIconUrl()));
			mDownloadQ.add(dlItem);
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
	
	
	
	public synchronized void downloadBanner(ArrayList<BannerItem> bannerList) {
		clearCurrentDownload();
		for (int i = 0; i < bannerList.size(); i++) {
			BannerItem banner = bannerList.get(i);

			DownloadImageItem item = new DownloadImageItem(banner.id, banner.image);
			mDownloadQ.add(item);
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
			if(mStopDownload){
				mStopDownload = false;
				return null;
			}
			byteBuffer.write(buffer, 0, len);
		}

		// and then we can return your byte array.
		return byteBuffer.toByteArray();
	}
}
