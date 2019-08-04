package com.oregonscientific.meep.store;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class HttpDownloader {
	private Thread mHttpThread = null;
	private boolean mIsDownloading = false;
	private String mStorePath = null;
	private	boolean mIsStoreToInternal = false;
	private String mUrl = null;
	private String name = "";
	Context mContext = null;
	private OnDownloadListener mOnDownloadListener = null;
	
	public interface OnDownloadListener
	{
		public abstract void onDownloadCompleted(boolean downloadAborted);
		public void onDownloadProgress(String name, int downloadedBytes, int contentLength);
	}
	
	public HttpDownloader(Context context)
	{
		mContext = context;
		mHttpThread = new Thread(mHttpDownloadRunnable);
	}
	
	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		this.mUrl = url;
	}
	
	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void startDownload()
	{
		mHttpThread.start();
	}

	public OnDownloadListener getOnDownloadListener() {
		return mOnDownloadListener;
	}

	public void setOnDownloadListener(OnDownloadListener onDownloadListener) {
		this.mOnDownloadListener = onDownloadListener;
	}

	public String getStorePath() {
		return mStorePath;
	}

	public void setStorePath(String storePath) {
		this.mStorePath = storePath;
	}

	public boolean isStoreToInternal() {
		return mIsStoreToInternal;
	}

	public void setIsStoreToInternal(boolean isStoreToInternal) {
		this.mIsStoreToInternal = isStoreToInternal;
	}

	private Runnable mHttpDownloadRunnable = new Runnable() {

		@Override
		public void run() {
			Log.d("httpdownload", "start http download");
			try {
	            HttpClient httpClient = new DefaultHttpClient();
	            HttpGet httpGet = new HttpGet(mUrl);
				HttpResponse response = httpClient.execute(httpGet);
				InputStream is = response.getEntity().getContent();
				int contentLength = Integer.parseInt(response.getHeaders("Content-Length")[0].getValue());
				Log.v("httpdownload", "File size: " + contentLength);
				File root = Environment.getExternalStorageDirectory();
					
				String localFilePath = root.getPath() + "/temp";
				if(mStorePath!=null)
				{
					localFilePath = mStorePath;//root.getPath() + "/tempApk.apk";
				}
				FileOutputStream fos = null;
				if (isStoreToInternal()) {
					
					String fileName = mUrl.substring(mUrl.lastIndexOf('/')+1);
					Log.d("httpdownload", "save file : file name = " + fileName);
					try
					{
					fos = mContext.openFileOutput(fileName,  Context.MODE_PRIVATE);
					}catch (Exception e) {
						Log.d("httpdownload", "save file error:" + e.toString());
					}
					Log.d("httpdownload", "save file 1");
				} else {
					Log.d("httpdownload", "save file 2");
					fos = new FileOutputStream(localFilePath, false);
					Log.d("httpdownload", "save file 3");
				}
				//FileOutputStream fos = mContext.openFileOutput("tempApk.apk", Context.MODE_PRIVATE);
				OutputStream os = new BufferedOutputStream(fos);
				
				byte[] buffer = new byte[1024];
				 int byteRead = 0;
				 int totalByte = 0;
				 long lastNotify = System.currentTimeMillis(); 
				 while ((byteRead = is.read(buffer)) != -1) {
					 os.write(buffer, 0, byteRead);
					 totalByte += byteRead;
					 Log.d("httpdownload", "byte read:" + totalByte);
					 if (getOnDownloadListener() != null && contentLength > 0 && (System.currentTimeMillis() - lastNotify) >= 1000) {
					     // we send download progress at most in 1000ms interval
				         getOnDownloadListener().onDownloadProgress(name, totalByte, contentLength);
				         lastNotify = System.currentTimeMillis();
					 }
				 }
				 os.flush();
				 fos.flush();

				 os.close();
				 fos.close();

				 is.close();
				if (getOnDownloadListener() != null) {
					getOnDownloadListener().onDownloadCompleted(false);
				}
            } catch (Exception e) {
                Log.d("httpdownload", "http download:" + e.toString());
                e.printStackTrace();
                if (getOnDownloadListener() != null) {
                    getOnDownloadListener().onDownloadCompleted(true);
                }
            }
		}
	};
}
