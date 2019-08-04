package com.oregonscientific.meep.store2.ctrl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.http.AndroidHttpClient;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.format.Time;
import android.util.Log;

import com.google.gson.Gson;
import com.oregonscientific.meep.MEEPEnvironment;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.global.Global.AppType;
import com.oregonscientific.meep.global.object.EncodingBase64;
import com.oregonscientific.meep.message.common.MeepAppMessage;
import com.oregonscientific.meep.message.common.MeepAppMessage.Category;
import com.oregonscientific.meep.message.common.MeepLogger;
import com.oregonscientific.meep.notification.Notification;
import com.oregonscientific.meep.notification.NotificationManager;
import com.oregonscientific.meep.ota.OtaUpgradeUtility;
import com.oregonscientific.meep.ota.OtaUpgradeUtility.ProgressListener;
import com.oregonscientific.meep.store2.R;
import com.oregonscientific.meep.store2.ctrl.notification.NotificationGenerator;
import com.oregonscientific.meep.store2.db.TableMeepStoreDownloadQueue;
import com.oregonscientific.meep.store2.global.MeepStoreApplication;
import com.oregonscientific.meep.store2.global.MeepStoreLog;
import com.oregonscientific.meep.store2.object.DownloadStoreItem;
import com.oregonscientific.meep.store2.object.MeepStoreItem;

public class StoreItemDownloadCtrl {
	
//	private final String SAVE_PATH = Environment.getExternalStorageDirectory()+"/data/home/";
	private final String SAVE_PATH = MEEPEnvironment.getMediaStorageDirectory().getAbsolutePath()+"/";
	private int SLEEP_TIME = 3000;
	DownloadListener mDownloadListener;
	List<DownloadListener> mDownloadListeners;
	Thread mHttpThread;
	AndroidHttpClient mHttpClient;
	private DownloadStoreItem mDownloadingItem;
	private SQLiteDatabase mDb;
	int mRetryCount = 0;
	Context mContext;
	//system download notification
	Notification mNotification = null;
	long notificationId = 10001;
	long notificationErrorId = 10002;
	BroadcastReceiver mMsgReceiver;
	int mInstalling = 0;
	boolean mStopDownload = false;
	MeepStoreApplication mApp;
	NotificationManager mNotificationManager = null;
	Notification.Style mNotificationStyle;
	Notification.Builder mNotificationBuilder;
	
	private Handler mHandler = new DownloadHandler(this);
	
	private static class DownloadHandler extends Handler {
		private final WeakReference<StoreItemDownloadCtrl> mControl;
		
		DownloadHandler(StoreItemDownloadCtrl control) {
			mControl = new WeakReference<StoreItemDownloadCtrl>(control);
		}
		
		@Override
		public void handleMessage(Message msg) {

			StoreItemDownloadCtrl control = mControl.get();
			if (msg.what == 0) {
				control.unzipEbook();
			}
			
			super.handleMessage(msg);
		}
	}
	
	public interface DownloadListener
	{
		public abstract void onDownloadCompleted(boolean downloadAborted, DownloadStoreItem item);
		public abstract void onDownloadProgress(String name, int percentage);
		public abstract void onNoSpace();
	}
	
	public DownloadListener getDownloadListener() {
		return mDownloadListener;
	}

	public void setDownloadListener(DownloadListener downloadListener) {
		this.mDownloadListener = downloadListener;
	}
	
	public void addDownloadListeners(DownloadListener listener){
		mDownloadListeners.add(listener);
	}
	public void removeDownloadListeners(DownloadListener listener){
		mDownloadListeners.remove(listener);
	}
	
	public List<DownloadListener> getDownloadListeners(){
		return mDownloadListeners;
	}
	
	public StoreItemDownloadCtrl(Context context, SQLiteDatabase db) {
		mDb = db;
		mContext = context;
		mApp = (MeepStoreApplication) context.getApplicationContext();

		mHttpThread = new Thread(mHttpDownloadRunnable);
		mHttpClient = AndroidHttpClient.newInstance("AndroidDownloader");
		mNotificationManager = (NotificationManager) ServiceManager.getService(mContext, ServiceManager.NOTIFICATION_SERVICE);
//		mDownloadListeners = new ArrayList<StoreItemDownloadCtrl.DownloadListener>();
		mDownloadListeners = new Vector<StoreItemDownloadCtrl.DownloadListener>();
		initPackageListener();
		initMessageReceiver();
	}
	
	private DownloadStoreItem getFirstDownloadingRecordFromDb() {

		DownloadStoreItem first = null;
		String sql = TableMeepStoreDownloadQueue.getSelectFirstImcompletedSql();

		Cursor cursor = mDb.rawQuery(sql, null);
		int rowNum = cursor.getCount();
		if (rowNum != 0) {
			cursor.moveToFirst();
			String id = cursor.getString(cursor.getColumnIndex(TableMeepStoreDownloadQueue.S_ID));
			String name = cursor.getString(cursor.getColumnIndex(TableMeepStoreDownloadQueue.S_NAME));
			String type = cursor.getString(cursor.getColumnIndex(TableMeepStoreDownloadQueue.S_TYPE));
			String imageUrl = cursor.getString(cursor.getColumnIndex(TableMeepStoreDownloadQueue.S_IMAGE_URL));
			String fileUrl = cursor.getString(cursor.getColumnIndex(TableMeepStoreDownloadQueue.S_URL));
			String checksum = cursor.getString(cursor.getColumnIndex(TableMeepStoreDownloadQueue.S_CHECKSUM));
			String packageName = cursor.getString(cursor.getColumnIndex(TableMeepStoreDownloadQueue.S_PACKAGE_NAME));
			String localPath = cursor.getString(cursor.getColumnIndex(TableMeepStoreDownloadQueue.S_STORE_PATH));
			String imagePath = cursor.getString(cursor.getColumnIndex(TableMeepStoreDownloadQueue.S_IMAGE_PATH));
			first = new DownloadStoreItem(id, name, type, imageUrl, fileUrl, checksum, packageName);
			first.setLocalFilePath(localPath);
			first.setLocalImagePath(imagePath);
			MeepStoreLog.logcatMessage("storedownload", "got first item from db: id=" + id +  " name="+ name + " url="+ fileUrl + " localPath="+ localPath + " type=" + type);
		}
		return first;
	}
	
	private boolean deleteDbItem(String id){
		String sql = TableMeepStoreDownloadQueue.getDeleteItemByIdSql(id);
		try{
			mDb.execSQL(sql);
			MeepStoreLog.logcatMessage("storedownload", "DB:Delete Success id:"+id);
			return true;
		} catch(Exception ex) {
			Log.e("storedownload", "DB:Delete Error:" + ex.toString());
		}
		return false;
	}
	
	public void startDownload()
	{
		Log.e("storedownload", "download thread start...");
		mHttpThread.start();
	}
	
	public synchronized DownloadStoreItem getDownloadingItem(){
		return mDownloadingItem;
	}
	
	private Runnable mHttpDownloadRunnable = new Runnable() {
		
		@Override
		public void run() {
			while (true) {
				sleep(SLEEP_TIME);
				
				// check if we have WiFi connection
				if (!isWifiAvailable()) {
					continue;
				}

				// reset installing flag if longer than 10 minutes
				if (mInstalling >= (10 * 60 * 1000 / SLEEP_TIME)) {
					mInstalling = 0;
				}

				// check if another installation is in progress
				if (mInstalling != 0) {
					mInstalling++;
					continue;
				}
                
				// Get first queue item
				mDownloadingItem = getFirstDownloadingRecordFromDb();
				if (mDownloadingItem != null) {
					//Update table AppsCategory
					sendUpdateTableAppsCategory(mDownloadingItem.getPackageName(),mDownloadingItem.getType());
					//Reset Notification
					mNotification = null;
					// check if we have Internet connection (to prevent Captive Portal)
					if (!isServerConnected()) {
						continue;
					}
					
					// remove the processing item
					deleteDbItem(mDownloadingItem.getId());
					if (!mDownloadingItem.getType().equals(DownloadStoreItem.TYPE_OTA)) {
						// always delete and download image
					    File imageFile = new File(mDownloadingItem.getLocalImagePath());
					    if (imageFile.exists()) {
					        imageFile.delete();
					    }
						imageFile = downloadFile(mDownloadingItem.getName(), mDownloadingItem.getId(), mDownloadingItem.getImageUrl(), mDownloadingItem.getLocalImagePath());
						if (imageFile == null) {
							MeepStoreLog.logcatMessage("storedownload", "Unable to download image file, abort.");
							if (mDownloadListeners != null) {
								for (DownloadListener l : mDownloadListeners) {
									l.onDownloadCompleted(true, mDownloadingItem);
								}
							}
							showErrorNotification();
							continue;
						}
					}
					
					// download file
					File file = downloadFile(mDownloadingItem.getName(), mDownloadingItem.getId(), mDownloadingItem.getItemUrl(), mDownloadingItem.getLocalFilePath());
					if (file == null) {
						MeepStoreLog.logcatMessage("storedownload", "Unable to download file, abort");
						if (mDownloadListeners != null) {
							for(DownloadListener l :mDownloadListeners){
								l.onDownloadCompleted(true, mDownloadingItem);
							}
						}
						showErrorNotification();
						continue;
					}
					
					// all download verifications succeed, proceed to installation
					if (mDownloadingItem.getType().equals(DownloadStoreItem.TYPE_GAME) || mDownloadingItem.getType().equals(DownloadStoreItem.TYPE_APP)) {
						// File is an apk, install app
						mInstalling = 1;
						AppInstallationCtrl.installApp(mContext, mDownloadingItem.getLocalFilePath());
						cancelProgressNotification();
					} else if (mDownloadingItem.getType().equals(DownloadStoreItem.TYPE_EBOOK)) {
						// File is a epub, unzip file
						mInstalling = 1;
						if (Global.FILE_TYPE_EPUB.equals(mDownloadingItem.getItemUrl().substring(mDownloadingItem.getItemUrl().lastIndexOf('.')))) {
						    mHandler.sendEmptyMessage(0);
						} else {
							showProgress(mDownloadingItem.getType(), 100, 2);
						}
						MeepStoreLog.logcatMessage("test","ext:"+mDownloadingItem.getItemUrl().substring(mDownloadingItem.getItemUrl().lastIndexOf('.')));
					} else if (mDownloadingItem.getType().equals(DownloadStoreItem.TYPE_OTA)) {
						mInstalling = 1;
						OtaUpgradeUtility ota = new OtaUpgradeUtility(mContext);
						MeepLogger meepLogger = new MeepLogger(mContext);
						if (ota.verifyPackage(mDownloadingItem.getLocalFilePath())) {
							//verify ota ok
							MeepStoreLog.logcatMessage("run_command", "package verified, installing OTA package");
							meepLogger.s("has verified the package, begins OTA upgrade");
							ota.beginUpgrade(mDownloadingItem.getLocalFilePath(), new ProgressListener() {
								@Override
								public void onProgress(int progress) {
									MeepStoreLog.logcatMessage("OtaUpgradeUtility", "onProgress: " + progress);
								}

								@Override
								public void onVerifyFailed(int errorCode, Object object) {
									MeepStoreLog.logcatMessage("OtaUpgradeUtility", "onVerifyFailed: " + errorCode);
			                        mInstalling = 0;
								}

								@Override
								public void onCopyProgress(int progress) {
									MeepStoreLog.logcatMessage("OtaUpgradeUtility", "onCopyProgress: " + progress);
								}

								@Override
								public void onCopyFailed(int errorCode, Object object) {
									MeepStoreLog.logcatMessage("OtaUpgradeUtility", "onCopyFailed:" + errorCode);
			                        mInstalling = 0;
								}
							});
							cancelProgressNotification();
						} else {
							//verify ota fail
//							String title = mContext.getResources().getString(R.string.mainpage_ota_fail_title);
//							String msg = mContext.getResources().getString(R.string.mainpage_ota_fail_msg);
//
//							showPopup(title, msg);

							// sendErrorNotification(title, msg);
							meepLogger.s("has failed package verification, installation aborted.");
							new File(mDownloadingItem.getLocalFilePath()).delete();
							showErrorNotification();
						}
						
					}
				}
			}
		}
	};
	
	private boolean isServerConnected() {
		final String TIMESTAMP_URL = "http://portal.meeptablet.com/timestamp";
		HttpGet httpGet = new HttpGet(TIMESTAMP_URL);
		AndroidHttpClient client = AndroidHttpClient.newInstance("android/meep");
		StringBuilder sb = new StringBuilder();
		boolean result = false;
		
		try {
			HttpResponse response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			
			if (entity != null) {
				InputStream is = entity.getContent();
				if (is != null) {
					byte[] buffer = new byte[1024];
					
					while (is.read(buffer) != -1) {
						String s = new String(buffer);
						sb.append(s);
					}
					is.close();
				}
				entity.consumeContent();
				
				Time t = new Time();
				t.set(Long.valueOf(sb.toString().trim()));
				result = t != null;
			}
			
		} catch (Exception ex) {
			MeepStoreLog.logcatMessage("STORE DOWNLOAD", ex.getMessage());
		} finally {
			client.close();
		}
		
		return result;
	}
	
//	private boolean isServerConnected(){
//		final String TIMESTAMP_URL = "http://portal.meeptablet.com/timestamp";
//		HttpGet httpGet = new HttpGet(TIMESTAMP_URL);
//		HttpResponse response;
//		try {
//			response = mHttpClient.execute(httpGet);
//			InputStream inputStream = response.getEntity().getContent();
//			byte[] buffer = new byte[1024];
//			StringBuilder sb = new StringBuilder();
//			while (inputStream.read(buffer) != -1) {
//				String s = new String(buffer);
//				sb.append(s);
//			}
//			inputStream.close();
//			String timestr = sb.toString().trim();
//			try {
//				Time time = new Time();
//				time.set(Long.valueOf(timestr));
//				MeepStoreLog.logcatMessage("storedownload", "check server connected:" + time.toString());
//				return time!= null;
//			} catch (Exception ex) {
//				return false;
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
	
	private boolean isWifiAvailable() {
		ConnectivityManager conMan = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMan != null) {
			// wifi
			State wifi = conMan.getNetworkInfo(1).getState();
			if (wifi != null) {
				return wifi == NetworkInfo.State.CONNECTED;
			}
		}
		return false;
	}
	
	
	private void sleep(int time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private boolean isEnoughSpace(long requiredSpace) {
		StatFs stats = new StatFs("/data");
		long availableBlocks = stats.getAvailableBlocks();
		long blockSizeInBytes = stats.getBlockSize();
		long freespace = availableBlocks * blockSizeInBytes;
		
		//long freespace = file.getFreeSpace();
		MeepStoreLog.logcatMessage("storedownload","available:"+availableBlocks);
		MeepStoreLog.logcatMessage("storedownload","blocksize:"+blockSizeInBytes);
		MeepStoreLog.logcatMessage("storedownload", "free space:" + freespace + "  required space:" + requiredSpace);
		return (freespace >= requiredSpace);
	}
	
	private boolean isValidChecksum(String localChecksum, String remoteChecksum){
		if(remoteChecksum!=null && localChecksum!= null){
			if(remoteChecksum.equals(localChecksum))
			{
				MeepStoreLog.logcatMessage("storedownload","Checksum match");
				return true;
			}
			else
			{
				MeepStoreLog.logcatMessage("storedownload","Checksum mismatch");
				return false;
			}
		}else if (remoteChecksum == null){
			MeepStoreLog.logcatMessage("storedownload","remote Checksum is null");
			return true;
		}else{
			MeepStoreLog.logcatMessage("storedownload","local Checksum is null");
			return false;
		}
	}
	
	// TODO: Not implemented
	private String formatName(String name){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<name.length(); i++){
			if (name.charAt(i) == '\'' || name.charAt(i)== '='){
				sb.append(name.charAt(i));
			}
		}
		return sb.toString();
	}
	
	private File download(String id, String name, String url, File file, long downloadedSize, long remoteFileSize){
		Log.i("storedownload", "Start/Resume Download");
		
		// Prepare to download file
		HttpGet httpGet = new HttpGet(url);
		BufferedInputStream inputStream = null;
		FileOutputStream fileOutputStream = null;
		OutputStream outputStream = null;
		// Get file
		int percentage = 0;
		AndroidHttpClient client = AndroidHttpClient.newInstance("android/meep");
		HttpResponse response;
		
		try {
			if (downloadedSize > 0) {
				// Resume download logic
			    Log.i("storedownload", "Resume download from " + downloadedSize);
			    httpGet.setHeader("Range", "bytes=" + downloadedSize + "-");
			}
			
			response = client.execute(httpGet);
			inputStream = new BufferedInputStream(response.getEntity().getContent());
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
					MeepStoreLog.logcatMessage("storedownload", "File download progress: " + updatedPercentage);
					percentage = updatedPercentage;
					
//					if(mDownloadListener != null) {
//						mDownloadListener.onDownloadProgress(id, percentage);
//					}
					int progressType = 1;
					if(file.getName().contains(Global.FILE_TYPE_PNG)) {
						progressType = 0;
					}

					showProgress(mDownloadingItem.getType(), percentage, progressType);
				}
			} // End While

	    // Close all stream
			response.getEntity().consumeContent();
	       
		} catch (Exception e) {
			Log.w("StoreItemDownloadCtrl", "Error while retrieving file from " + url, e);
			httpGet.abort();
		} finally {
			client.close();
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
//		if (percentage == 100) {
//			return file;
//		} else {
//			return null;
//		}
	}
	/**
	 * 
	 * @param itemType
	 * @param progress
	 * @param progressType  0:image 1:file 2:unzip
	 * 
	 */
	private int mProgress = 0;

	private void showProgress(String itemType, int progress, int progressType) {
		int updateProgress = 0;
		if (DownloadStoreItem.TYPE_OTA.equals(itemType)) {
			updateProgress = progress;
		} else {
			switch (progressType) {
			case 0:
				updateProgress = (int) (progress * 0.1f);
				break;
			case 1:
				if (MeepStoreItem.TYPE_EBOOK.equals(itemType)) {
					updateProgress = (int) (10 + progress * 0.7f);
				} else if (MeepStoreItem.TYPE_APP.equals(itemType) || MeepStoreItem.TYPE_GAME.equals(itemType)) {
					updateProgress = (int) (10 + progress * 0.9f);
				}
				break;
			case 2:
				if (MeepStoreItem.TYPE_EBOOK.equals(itemType)) {
					updateProgress = (int) (80 + progress * 0.2f);
				}
				break;
			default:
				break;
			}
		}
		
		// Update progress bar logic
		if (mProgress != updateProgress) {
			mProgress = updateProgress;
			MeepStoreLog.logcatMessage("storedownload", "Download bar progress: " + mProgress);

			if (mDownloadListeners != null) {
				for (DownloadListener l : mDownloadListeners) {
					if (mProgress == 100) {
						l.onDownloadCompleted(false, mDownloadingItem);
					} else {
						l.onDownloadProgress(mDownloadingItem.getId(), mProgress);
					}
				}
			}
			
			MeepStoreLog.logcatMessage("teststore",mProgress+"");
			if (mNotification == null) {
				MeepStoreLog.logcatMessage("teststore","create notification");
				String displayName = "<unknown>";
				try {
					displayName = URLDecoder.decode(mDownloadingItem.getName(), "UTF-8");
				} catch (Exception e) {
				}
				//create notification
				mNotification = NotificationGenerator.getProgressNotification(
						100, 
						mProgress,
						false, displayName,
						String.format(mContext.getResources().getString(R.string.download_msg), displayName));
				//get id of new downlaod notification 
				notificationId = mNotificationManager.notifyBlocking(mApp.getAccountID(), mNotification);
			} else {
				//update notification
				if (mNotification.progress != 100) {
					mNotification.id = notificationId;
					mNotification.progress = mProgress;
					mNotificationManager.notify(mApp.getAccountID(), mNotification);
				} else {
					cancelProgressNotification();
				}
							
			}
		}
		
	}
	
	private File downloadFile(String name, String id, String url, String localPath){
		MeepStoreLog.logcatMessage("downloadFile", "received download request - name:" + name + "  id:" + id + "  url:" + url + "   localpath:" + localPath);
		File downloadedFile = null;
		int maxRetryCount = 8;
		int retryCount = 0;
		AndroidHttpClient client = AndroidHttpClient.newInstance("android/meep");
		
		for (retryCount = 0; retryCount < maxRetryCount; retryCount++) {
			HttpHead head = new HttpHead(url);
			HttpResponse response = null;
			
			MeepStoreLog.logcatMessage("downloadFile", "Begin main loop, retries #" + retryCount);
			try {
				// Get header first
				response = client.execute(head);
				//check status
				int code = response.getStatusLine().getStatusCode();
//				Header[] header = response.getAllHeaders();
//				Log.v("Headertest","Headertest start");
//				for(Header h: header)
//				{
//					Log.v("Headertest",new Gson().toJson(h));
//				}
//				Log.v("Headertest","Headertest end");
				if (!checkStatusCode(code)) {
					continue;
				}
				
				boolean resumable = false;
				long remoteFileSize = 0;
				String remoteMd5 = null;
				try {
					// accept_range ===>resumable
					if (response.getLastHeader("Accept-Ranges") != null)
						resumable = response.getLastHeader("Accept-Ranges").getValue().equals("bytes");
					MeepStoreLog.logcatMessage("downloadFile", "Accept-Ranges: " + resumable);
					// ETag ===> checksum
					if (response.getLastHeader("ETag") != null) {
						String regEx = "[^0-9a-z]";
						Pattern p = Pattern.compile(regEx);
						Matcher m = p.matcher(response.getLastHeader("ETag").getValue());
						remoteMd5 = m.replaceAll("").trim();
					}
					MeepStoreLog.logcatMessage("downloadFile", "ETag: " + remoteMd5);
					// content length ===>total size
					if (response.getLastHeader("Content-Length") != null)
						remoteFileSize = Long.parseLong(response.getLastHeader("Content-Length").getValue());
					MeepStoreLog.logcatMessage("downloadFile", "Content-Length: " + remoteFileSize);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//Get local file
				downloadedFile = new File(localPath);
				MeepStoreLog.logcatMessage("downloadFile", "Locally downloaded file: " + downloadedFile.getName());
				// Create directory if it is not exist
				if (!downloadedFile.getParentFile().exists()) {
					downloadedFile.getParentFile().mkdirs();
				}
				//Get size of local file
				long downloadedSize = downloadedFile.length();
				MeepStoreLog.logcatMessage("downloadFile", "File size: " + downloadedSize  + " (content length:" + remoteFileSize + ")");
				
				// Check available space
				// Why 2.2 = space for storing downloaded file (1) + space for install app (1) + buffer (0.2) 
				if (!isEnoughSpace((long) (remoteFileSize * 2.2 - downloadedSize))) {
					MeepStoreLog.logcatMessage("downloadFile", "Insufficient space, abort download.");
					//if(mDownloadListener!=null) mDownloadListener.onNoSpace();
					showNoSpaceNotification();
					if (mDownloadListeners != null) {
						for (DownloadListener l : mDownloadListeners) {
							l.onNoSpace();
						}
					}
					return null;
				}
				
				if (!downloadedFile.exists()) {
					// download file
					MeepStoreLog.logcatMessage("downloadFile", "Begin file download.");
					downloadedFile = download(id, name, url, downloadedFile, downloadedSize, remoteFileSize);
					downloadedSize = downloadedFile.length();
					MeepStoreLog.logcatMessage("downloadFile", "File download ended, downloaded size: " + downloadedSize);
				}
					
					
				for (int idx = 0; idx < maxRetryCount && downloadedSize < remoteFileSize; idx++) {
					sleep((int)Math.pow(2, idx) * 500);
					// incomplete download
					MeepStoreLog.logcatMessage("downloadFile", "Incomplete file download, retries #" + idx);
					if (!resumable) {
						MeepStoreLog.logcatMessage("downloadFile", "Non-resumable, removing file: " + downloadedFile.getAbsolutePath());
						if (downloadedFile.exists()) {
							downloadedFile.delete();
						}
						downloadedSize = 0;
					}
					MeepStoreLog.logcatMessage("downloadFile", "Begin file download.");
					downloadedFile = download(id, name, url, downloadedFile, downloadedSize, remoteFileSize);
					downloadedSize = downloadedFile.length();
					MeepStoreLog.logcatMessage("downloadFile", "File download ended, downloaded size: " + downloadedSize);
				}

				// Check checksum if local file size >= remote download size
				MeepStoreLog.logcatMessage("downloadFile", "Calculating file checksum.");
				String localMd5 = getMD5Checksum(downloadedFile);
				if (!isValidChecksum(localMd5, remoteMd5)) {
					MeepStoreLog.logcatMessage("downloadFile", "Checksum mismatch, abort.");
					if (downloadedFile.exists()) {
						downloadedFile.delete();
					}
					return null;
				}
					
				// change file permission
//				int rtn = MeepStorageCtrl.changeFilePermission(downloadedFile);
//				MeepStoreLog.logcatMessage("downloadFile", "Change file permission return: " + rtn);
//				sleep(1000);
//				if(!MeepStorageCtrl.chmodFile(downloadedFile))
//				{
//					return null;
//				}
//				sleep(1000);
//				
//				// verify file permission
//				int[] modeUidGid = MeepStorageCtrl.getFilePermission(downloadedFile);
//				if (modeUidGid[0] != 33279) {
//				    return null;
//				}
				
				// for updating status when file exists
				if (mProgress != 100) {
					int progressType = 1;
					if (downloadedFile.getName().contains(Global.FILE_TYPE_PNG))
						progressType = 0;
					showProgress(mDownloadingItem.getType(), 100, progressType);
				}
				
				return downloadedFile;
			} catch (IOException e) {
				MeepStoreLog.logcatMessage("storedownload", "RETRY: IOException");
				e.printStackTrace();
			} catch (NullPointerException e) {
				MeepStoreLog.logcatMessage("storedownload", "RETRY: NullPointerException");
				e.printStackTrace();
			} catch (Exception e) {
				MeepStoreLog.logcatMessage("storedownload", "RETRY: Exception");
				e.printStackTrace();
			} finally {
				client.close();
			}
			
			MeepStoreLog.logcatMessage("downloadFile", "Sleeping for next retry...");
			sleep((int)Math.pow(2, retryCount) * 1000);
		}

		return null;
	}
	
	public static String getMD5Checksum(File file) {
		StringBuilder sb = new StringBuilder();

		try {
			byte[] b = createChecksum(file);
			for (int i = 0; i < b.length; i++) {
				sb.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
			}
		} catch (Exception e) {
			Log.e("storedownload", "Checksum calculation error");
		}

		return sb.toString();
	}
	
	public static byte[] createChecksum(File file) throws Exception {
	    InputStream fis = new FileInputStream(file);

	    byte[] buffer = new byte[1024];
	    MessageDigest complete = MessageDigest.getInstance("MD5");
	    int numRead;

	    do {
	        numRead = fis.read(buffer);
	        if (numRead > 0) {
	            complete.update(buffer, 0, numRead);
	        }
	    } while (numRead != -1);

	    fis.close();
	    return complete.digest();
	}
	
	public void stopDownload(){
		// TODO: DOWNLOAD_ABORTED, DOWNLOAD_FAILED
		
//		mPendingDownloadList.add(mDownloadingItem);	// TODO: Seems not implemented
		mStopDownload = true;
	}
	
	
	public void addStoreDownloadItem(DownloadStoreItem item){
		try
		{
		    if (!AppInstallationCtrl.isPackageInstalled(mContext, item.getPackageName())) {
    			// check download record exist
    			String selectSql = TableMeepStoreDownloadQueue.getSelectItemByIdSql(item.getId());
    			Cursor c = mDb.rawQuery(selectSql, null);
    			if (c.getCount() == 0) {
    				// not exist -> add to db
    				String remoteFileName = item.getItemUrl().substring(item.getItemUrl().lastIndexOf('/') + 1);
                    String remoteFileExt = item.getItemUrl().substring(item.getItemUrl().lastIndexOf('.'));
                    
                    //Move all game item into app folder
                    if(item.getType().equals(MeepStoreItem.TYPE_GAME))
                    {
                    	item.setType(MeepStoreItem.TYPE_APP);
                    }
                    
    				// TODO:local file path
    				String localPath = SAVE_PATH + item.getType() + "/data/" + remoteFileName;
    				if(item.getType().equals(DownloadStoreItem.TYPE_OTA))
    				{
    					localPath = "/cache/update.zip";
    				}
    				String remoteImageName = item.getImageUrl().substring(item.getImageUrl().lastIndexOf('/') + 1);
    				String localImagePath = SAVE_PATH + item.getType() + "/icon/";
    				if (item.getPackageName() != null && item.getPackageName().length() > 0) {
    				    localImagePath += item.getPackageName() + Global.FILE_TYPE_PNG;
    				} else {
    				    localImagePath += remoteImageName;
    				}
    	
    				if (item.getType().equals(MeepStoreItem.TYPE_EBOOK)) {
    					localPath = SAVE_PATH + item.getType() + "/data/" + EncodingBase64.encode(item.getName()) + remoteFileExt;
    					localImagePath = SAVE_PATH + item.getType() + "/icon/" + EncodingBase64.encode(item.getName()) + Global.FILE_TYPE_PNG;
    				}
    	
    				item.setLocalFilePath(localPath);
    				item.setLocalImagePath(localImagePath);
    	
    				TableMeepStoreDownloadQueue tbItem = new TableMeepStoreDownloadQueue(item.getId(), item.getType(), item.getName(), item.getItemUrl(),
    						item.getLocalFilePath(), item.getImageUrl(), item.getLocalImagePath(), 0, 0, item.getChecksum(), item.getPackageName());
    				tbItem.StorePath = localPath;
    				tbItem.ImagePath = localImagePath;
    	
    				String sql = tbItem.getInsertSql();
    				MeepStoreLog.logcatMessage("storeItemdownload", "sql: " + sql);
    				mDb.execSQL(sql);
    			} else {
    				MeepStoreLog.logcatMessage("storeItemdownload", "download record exist - id: " + item.getId());
    			}
		    } else {
		        MeepStoreLog.logcatMessage("storeItemdownload", "package '" + item.getPackageName() + "' has already been installed, skipping.");
		    }
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
//	private boolean isDuplicatedRecord(DownloadStoreItem newItem){
//		for(DownloadStoreItem item: mDownloadQ){
//			if(item.getId().equals(newItem.getId())){
//				return true;
//			}
//		}
//		return false;
//	}
	
	public boolean isInDownloadQ(String id){
		String sql = TableMeepStoreDownloadQueue.getSelectItemByIdSql(id);
		try
		{
			Cursor cursor = mDb.rawQuery(sql, null);
			int rowNum = cursor.getCount();
			if(rowNum > 0)
				return true;
		}
		catch(Exception e)
		{
			return false;
		}
		return false;
	}
	
//	public boolean removeItemInDownloadQ(String id)
//	{
//		for(DownloadStoreItem item : mDownloadQ){
//			if(item.getId().equals(id)){
//				mDownloadQ.remove(item);
//				return true;
//			}
//		}
//		return false;
//	}
	public boolean removeItemInDownloadTable(String id)
	{
		String sql = TableMeepStoreDownloadQueue.getDeleteSql(id);
		try
		{
			mDb.execSQL(sql);
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
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
	
//	public ArrayDeque<DownloadStoreItem> getDownloadQ(){
//		return mDownloadQ;
//	}
	
	private void initPackageListener(){
		MeepStoreApplication app = (MeepStoreApplication)mContext.getApplicationContext();
		AppInstallationCtrl ctrl = app.getAppCtrl();
		MeepStoreLog.logcatMessage("storedownload", "listen init 1");
		ctrl.addPackageListener(mpackageListener);
//		ctrl.setPackageListener(new AppInstallationCtrl.PakageListener() {
//			
//			@Override
//			public void onpackageReplaced(String packageName) {
//				removeApks(packageName);
//			}
//			
//			@Override
//			public void onpackageRemoved(String packageName) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onpackageAdded(String packageName) {
//				MeepStoreLog.logcatMessage("storedownload", "listen init 1");
//				removeApks(packageName);
//			}
//		});
		
	}
	
	AppInstallationCtrl.PakageListener mpackageListener = new AppInstallationCtrl.PakageListener() {
		@Override
		public void onpackageAdded(String packageName) {
			MeepStoreLog.logcatMessage("storedownload", "package added: " + packageName);
			removeApks(packageName);
			mInstalling = 0;
		}

		@Override
		public void onpackageReplaced(String packageName) {
			MeepStoreLog.logcatMessage("storedownload", "package replaced: " + packageName);
			removeApks(packageName);
		}

		@Override
		public void onpackageRemoved(String packageName) {
			MeepStoreLog.logcatMessage("storedownload", "package removed: " + packageName);
			removeAppIcon(packageName);
		}
	};
	
	private void removeAppIcon(String packageName){
		
		String path1 = Global.PATH_DATA_HOME + "game/icon/" + packageName + Global.FILE_TYPE_PNG;
		String path2 = Global.PATH_DATA_HOME + "game/icon_ld/" + packageName + Global.FILE_TYPE_PNG;
		String path3 = Global.PATH_DATA_HOME + "game/icon_lm/" + packageName + Global.FILE_TYPE_PNG;
		String path4 = Global.PATH_DATA_HOME + "game/icon_s/" + packageName + Global.FILE_TYPE_PNG;
		String path5 = Global.PATH_DATA_HOME + "app/icon/" + packageName + Global.FILE_TYPE_PNG;
		String path6 = Global.PATH_DATA_HOME + "app/icon_ld/" + packageName + Global.FILE_TYPE_PNG;
		String path7 = Global.PATH_DATA_HOME + "app/icon_lm/" + packageName + Global.FILE_TYPE_PNG;
		String path8 = Global.PATH_DATA_HOME + "app/icon_s/" + packageName + Global.FILE_TYPE_PNG;
		String[] pathArr = new String[]{path1, path2, path3, path4, path5, path6, path7, path8  };
		for (String path : pathArr) {
			File file = new File(path);
			if(file.exists()) file.delete();
			MeepStoreLog.logcatMessage("storedownload", "delete file :" + file.getAbsolutePath());
		}
	}
	
	
	
	private void removeApks(String packageName){
//		MeepStoreLog.logcatMessage("storedownload", "remove apk file:" + packageName);
//		String path = "/data/home/app/data/" + packageName + ".apk";
//		File file = new File(path);
//		if(file.exists()){
//			file.delete();
//		}
//		
//		path = "/data/home/game/data/" + packageName + ".apk";
//		file = new File(path);
//		if(file.exists()){
//			file.delete();
//		}
		
		
		String path = "/data/home/app/data/";
		File dir = new File(path);
		if (dir.exists()) {
			File[] fileList = dir.listFiles();
			for (File file : fileList) {
				if (mDownloadingItem == null || !file.getName().equals(mDownloadingItem.getName())) {
					file.deleteOnExit();
				}
			}
		}

		path = "/data/home/game/data/";
		dir = new File(path);
		if (dir.exists()) {
			File[] fileList = dir.listFiles();
			for (File file : fileList) {
				if (mDownloadingItem == null || !file.getName().equals(mDownloadingItem.getName())) {
					file.deleteOnExit();
				}
			}
		}
	}
	

	private void initMessageReceiver() {
		mMsgReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
			 if (intent.getAction().equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) { //WIFI
		            if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)) {
		                //connected
		            	MeepStoreLog.logcatMessage("storedownload", "connected");
		            	//mStopDownload = false;
		            	
		            } else {
		            	//disconnected do nth
		            	MeepStoreLog.logcatMessage("storedownload", "wifi-disconnected");
		            }
		        }
			}
		};
		        
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
		mContext.registerReceiver(mMsgReceiver, filter);
	}
	
	private void unzipEbook(){
		File file = new File(mDownloadingItem.getLocalFilePath());
		String name = file.getName().substring(0, file.getName().length()-5);
		File dir = new File(SAVE_PATH + "ebook/unzip/");
		if(!dir.exists()){
			dir.mkdirs();
//			MeepStorageCtrl.changeFilePermission(dir);
		}
		
		UnZipper unzip = new UnZipper(mDownloadingItem.getLocalFilePath(), SAVE_PATH+ "ebook/unzip/" + name+ "/");
		
		unzip.addObserver(new Observer() {
			
			@Override
			public void update(Observable observable, Object data) {
				
				Bundle b = (Bundle)data;
				if(!b.getBoolean("complete")){
					int percent = b.getInt("percent");
					//if(mDownloadListener!=null && mDownloadingItem!= null) mDownloadListener.onUnzipProgress(mDownloadingItem.getId(), percent);
//					if(mDownloadListeners!= null && mDownloadingItem!= null){
//						for(DownloadListener l :mDownloadListeners){
//							l.onUnzipProgress(mDownloadingItem.getId(), percent);
//						}
//					}
					showProgress(mDownloadingItem.getType(), percent, 2);
				} else {
					int percent = b.getInt("percent");
					if (percent == 100) {
						//unzip completed, change permission and delete file
						File thefile = new File(mDownloadingItem.getLocalFilePath());
						String name = thefile.getName().substring(0, thefile.getName().length()-5);
						String filePath = SAVE_PATH + "ebook/unzip/" + name + "/";
						Log.i("storedownload", "unzip ok - " + filePath);
//						MeepStorageCtrl.changeFolderPermission(new File(filePath));
//						if (mDownloadListener != null)
//							mDownloadListener.onDownloadCompleted(false, mDownloadingItem);
//						if(mDownloadListeners!= null){
//							for(DownloadListener l :mDownloadListeners){
//								l.onDownloadCompleted(false, mDownloadingItem);
//							}
//						}
						
						File file = new File(mDownloadingItem.getLocalFilePath());
						try {
							if (file.exists()) {
								file.delete();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						showSuccessNotification();
						sendEbookDownloadSuccessToLauncher();
					} else {
						Log.w("storedownload", "unzip failed");
//						if (mDownloadListener != null)
//							mDownloadListener.onDownloadCompleted(true, mDownloadingItem);
						if (mDownloadListeners != null) {
							for (DownloadListener l : mDownloadListeners) {
								l.onDownloadCompleted(true, mDownloadingItem);
							}
						}
						showErrorNotification();
					}

					// reset
					Log.w("storedownload", "reset mdownloadingitem");
					mDownloadingItem = null;
					mInstalling = 0;
				}
			}
		});
		unzip.unzip();
	}
	
	public boolean checkStatusCode(int code)
	{
		MeepStoreLog.logcatMessage("test","code:"+code);
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

	public void sendUpdateTableAppsCategory(String packageName,String type)
	{
		if(DownloadStoreItem.TYPE_GAME.equals(type))
		{
			MeepAppMessage meepMsg = new MeepAppMessage(
					Category.DATABASE_ALTER,
					MeepAppMessage.OPCODE_UPDATE_APPS_CATEGORY, 
					packageName,
					Global.INTENT_MSG_PREFIX + AppType.Store);
			String meepMsgStr = new Gson().toJson(meepMsg);
			Intent i = new Intent();
			i.setAction(Global.INTENT_MSG_PREFIX + Global.AppType.MeepMessage);
			i.putExtra(Global.STRING_MESSAGE, meepMsgStr);
			mContext.sendBroadcast(i);
			MeepStoreLog.logcatMessage("database", "database request sent");
		}
	}
	
	private void cancelProgressNotification()
	{
		mNotificationManager.cancel(notificationId);
		mNotification = null;
	}
	
//	public Notification generateDownloadErrorNotification()
//	{
//		String displayName = "<unknown>";
//		try {
//			displayName = URLDecoder.decode(mDownloadingItem.getName(), "UTF-8");
//		} catch (Exception e) {
//		}
//		
//		String message = mContext.getResources().getString(R.string.download_error)+" - "+displayName;
//		return new Notification.Builder()
//		.setContentTitle(mContext.getResources().getString(R.string.download_error)+" - "+displayName)
//		.setIdentifier(notificationErrorId)
//		.build();
//		
//	}
	
	public void showSuccessNotification()
	{
		String title = mNotification.title;
		cancelProgressNotification();
		mNotificationManager.notify(mApp.getAccountID(),NotificationGenerator.generateNormalNotification(title, String.format(mContext.getResources().getString(R.string.download_success),title)));
	}
	public void showNormalNotification(String title,String message)
	{
		cancelProgressNotification();
		mNotificationManager.notify(mApp.getAccountID(),NotificationGenerator.generateNormalNotification(title, message));
	}
	public void showNoSpaceNotification()
	{
		String title = mNotification.title;
		cancelProgressNotification();
		mNotificationManager.notify(mApp.getAccountID(),NotificationGenerator.generateNormalNotification(title, String.format(mContext.getResources().getString(R.string.not_enough_space),title)));
	}
	public void showErrorNotification()
	{
		String title = mNotification.title;
		cancelProgressNotification();
		mNotificationManager.notify(mApp.getAccountID(),NotificationGenerator.generateNormalNotification(title, String.format(mContext.getResources().getString(R.string.download_failed),title)));
	}
	
	public void sendEbookDownloadSuccessToLauncher()
	{
		Intent intent = new Intent();
		intent.setAction("com.meepstore.action.DOWNLOAD_COMPLETE");
		mContext.sendBroadcast(intent); 
	}

}
