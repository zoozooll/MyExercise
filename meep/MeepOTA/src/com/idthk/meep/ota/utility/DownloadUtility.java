package com.idthk.meep.ota.utility;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;

import android.net.http.AndroidHttpClient;
import android.os.StatFs;
import android.util.Log;

import com.idthk.meep.ota.ui.Utils;

public class DownloadUtility {
	AndroidHttpClient mHttpClient;
	public DownloadUtility() {
		mHttpClient = AndroidHttpClient.newInstance("AndroidDownloader");
	}
	
	public interface DownloadListener{
		public void onProgress(int progress);
	}
	
	private DownloadListener mDownloadListener;
	
	 public DownloadListener getmDownloadListener() {
		return mDownloadListener;
	}

	public void setmDownloadListener(DownloadListener mDownloadListener) {
		this.mDownloadListener = mDownloadListener;
	}
	
	public File downloadOtaFile(String url, String localPath)
    {
    	int maxRetryCount = 8;
		int retryCount = 0;
		File downloadedFile = null;
		for (retryCount = 0; retryCount < maxRetryCount; retryCount++) {
			HttpHead head = new HttpHead(url);
			HttpResponse response = null;
			
			// Get header first
			try {
				response = mHttpClient.execute(head);
				
				//check status
				int code = response.getStatusLine().getStatusCode();
				if(!checkStatusCode(code)) {
					continue;
				}
				
				boolean resumable = false;
				long remoteFileSize = 0;
				String remoteMd5 = null;
				try {
					// accept_range ===>resumable
					if (response.getLastHeader("Accept-Ranges") != null)
						resumable = response.getLastHeader("Accept-Ranges").getValue().equals("bytes");
					// ETag ===> checksum
					if (response.getLastHeader("ETag") != null) {
						String regEx = "[^0-9a-z]";
						Pattern p = Pattern.compile(regEx);
						Matcher m = p.matcher(response.getLastHeader("ETag").getValue());
						remoteMd5 = m.replaceAll("").trim();
					}
					// content length ===>total size
					if (response.getLastHeader("Content-Length") != null)
						remoteFileSize = Long.parseLong(response.getLastHeader("Content-Length").getValue());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//Get local file
				downloadedFile = new File(localPath);
				// Create directory if it is not exist
				if(!downloadedFile.getParentFile().exists()){
					downloadedFile.getParentFile().mkdirs();
				}
				//Get size of local file
				long downloadedSize = downloadedFile.length();
				
				// Check available space
				if(!isEnoughSpace((long) (remoteFileSize*2.2 - downloadedSize))) {
					//Pop up:there's not enough space.
					return null;
				}
				
				if (!downloadedFile.exists()){
					// download file
					downloadedFile = download(url, downloadedFile, downloadedSize, remoteFileSize);
					downloadedSize = downloadedFile.length();
				}
					
					
				for (int idx=0; idx<maxRetryCount && downloadedSize < remoteFileSize; idx++) 
				{
					sleep((int)Math.pow(2, idx) * 500);
					// incomplete download
					if (!resumable) {
						if(downloadedFile.exists()){
							downloadedFile.delete();
						}
						downloadedSize = 0;
					}
					downloadedFile = download(url, downloadedFile, downloadedSize, remoteFileSize);
					downloadedSize = downloadedFile.length();
				}

				if(downloadedSize<remoteFileSize)
				{
					Log.d("downloadFile", "Downlaod Failure  download:"+downloadedSize+"  remote:"+remoteFileSize);
					return null;
				}
				//Change Permission
				int rtn = changeFilePermission(downloadedFile);
				Log.d("downloadFile", "Change file permission return: " + rtn);
				
				return downloadedFile;
				
			} catch (IOException e) {
				Log.d("storedownload", "RETRY: IOException");
				e.printStackTrace();
			} catch (NullPointerException e) {
				Log.d("storedownload", "RETRY: NullPointerException");
				e.printStackTrace();
			} catch (Exception e) {
				Log.d("storedownload", "RETRY: Exception");
				e.printStackTrace();
			}
			Log.d("downloadFile", "Sleeping for next retry...");
			sleep((int)Math.pow(2, retryCount) * 1000);
		}
		return null;
    	
    }
	    
	    public boolean checkStatusCode(int code)
		{
			Utils.printLogcatDebugMessage("code:"+code);
			//if 2XX/3XX
			if(code>=200&&code<400)
			{
				return true;
			}
			//if 4XX/5XX return false
			else if(code>=400&&code<600)
			{
				return false;
			}
			//else ???
			else
			{
				return false;
			}
		}
	    
	    
	    private boolean isEnoughSpace(long requiredSpace) {
			StatFs stats = new StatFs("/data");
			long availableBlocks = stats.getAvailableBlocks();
			long blockSizeInBytes = stats.getBlockSize();
			long freespace = availableBlocks * blockSizeInBytes;
			
			//long freespace = file.getFreeSpace();
			Log.d("OTA", "free space:" + freespace + "  required space:" + requiredSpace);
			return (freespace >= requiredSpace);
		}
	    
		private File download(String url,File file,long downloadedSize,long remoteFileSize){
			Log.i("download","Start/Resume Download");
			// Prepare to download file
	        HttpGet httpGet = new HttpGet(url);
	        InputStream inputStream = null;
			FileOutputStream fileOutputStream =null;
			OutputStream outputStream =null;
	        // Get file
			int percentage = 0;
	        HttpResponse response;
			try {
				if (downloadedSize > 0) {
					// Resume download logic
				    Log.i("download", "Resume download from " + downloadedSize);
				    httpGet.setHeader("Range", "bytes=" + downloadedSize + "-");
				}
				
				response = mHttpClient.execute(httpGet);
		        inputStream = response.getEntity().getContent();
				fileOutputStream = new FileOutputStream(file, true);
				outputStream = new BufferedOutputStream(fileOutputStream);
				
				byte[] buffer = new byte[1024];
				int byteRead = 0;
		       
		        
		        while ((byteRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, byteRead);
					downloadedSize += byteRead;
					
					// Get latest progress
					int updatedPercentage = (int) (((double) downloadedSize / (double) remoteFileSize) * 100);
					
					// Update progress bar logic
					if (percentage != updatedPercentage) {
						Log.d("download", "" + updatedPercentage);
						//TODO:update progress 
						percentage = updatedPercentage;
						if(mDownloadListener!=null)
							mDownloadListener.onProgress(percentage);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					outputStream.flush();
					outputStream.close();
					fileOutputStream.flush();
					fileOutputStream.close();
					inputStream.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			return file;
		}
		
		private void sleep(int time){
			try {
				Log.d("download", "sleep " + time );
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public static int changeFilePermission(File file) {
			int rtn = -1;
			for (int i = 0; i < 10; i++) {
				if (changeFilePermission2(file) == 0) {
					rtn = 0;
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return rtn;
			//int rtn = changeFilePermission2(file);
			
		}
		private static int changeFilePermission2(File file) {
			try {
				Class fileUtils = Class.forName("android.os.FileUtils");
				Method setPermissions = fileUtils.getMethod("setPermissions", String.class, int.class, int.class, int.class);
				return (Integer) setPermissions.invoke(null, file.getAbsolutePath(), 0777, -1, -1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1;
		}
		
		public void closeHttpClient()
		{
			if(mHttpClient != null)
				mHttpClient.close();
		}
}
