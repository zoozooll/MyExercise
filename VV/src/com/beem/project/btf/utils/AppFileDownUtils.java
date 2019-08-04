package com.beem.project.btf.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.constant.SettingKey;

public class AppFileDownUtils extends Thread {
	private Context mContext;
	private String mDownloadUrl; // 文件下载url，已做非空检查
	private String mFileName;
	private Message msg;
	private final String APP_FOLDER = "DownDemo"; // sd卡应用目录
	private final String APK_FOLDER = "apkFile"; // 下载apk文件目录
	public static final int MSG_UNDOWN = 302; // 未开始下载
	public static final int MSG_DOWNING = 306; // 下载中
	public static final int MSG_FINISH = 308; // 下载完成
	public static final int MSG_FAILURE = 307;// 下载失败
	private NotificationManager mNotifManager;
	private Notification mDownNotification;
	private RemoteViews mContentView; // 下载进度View
	private PendingIntent mDownPendingIntent;

	public AppFileDownUtils(Context context, String downloadUrl, String fileName) {
		mContext = context;
		mDownloadUrl = downloadUrl;
		mFileName = fileName;
		mNotifManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		msg = new Message();
	}
	@Override
	public void run() {
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				Message downingMsg = new Message();
				SharedPrefsUtil.putValue(mContext, SettingKey.IsDowningState,
						true);
				//SD卡准备好
				File sdcardDir = Environment.getExternalStorageDirectory();
				//文件存放路径： sdcard/DownDemo/apkFile
				File folder = new File(sdcardDir + File.separator + APP_FOLDER
						+ File.separator + APK_FOLDER);
				//创建存放目录
				if (!folder.exists()) {
					folder.mkdirs();
				}
				File saveFilePath = new File(folder, mFileName);
				//				System.out.println(saveFilePath);
				mDownNotification = new Notification(
						android.R.drawable.stat_sys_download,
						mContext.getString(R.string.notif_down_file),
						System.currentTimeMillis());
				mDownNotification.flags = Notification.FLAG_ONGOING_EVENT;
				mDownNotification.flags = Notification.FLAG_AUTO_CANCEL;
				mContentView = new RemoteViews(mContext.getPackageName(),
						R.layout.custom_notification);
				mContentView.setImageViewResource(R.id.downLoadIcon,
						android.R.drawable.stat_sys_download);
				mDownPendingIntent = PendingIntent.getActivity(mContext, 0,
						new Intent(), 0);
				File file = new File(mFileName);
				boolean downSuc = downloadFile(mDownloadUrl, saveFilePath);
				if (downSuc) {
					SharedPrefsUtil.putValue(mContext,
							SettingKey.IsDowningState, false);
					Notification notification = new Notification(
							android.R.drawable.stat_sys_download_done,
							mContext.getString(R.string.downloadSuccess),
							System.currentTimeMillis());
					notification.flags = Notification.FLAG_ONGOING_EVENT;
					notification.flags = Notification.FLAG_AUTO_CANCEL;
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setDataAndType(Uri.fromFile(saveFilePath),
							"application/vnd.android.package-archive");
					((Activity) mContext).startActivityForResult(intent, 0);
					// 升级删除Cartoon文件
					DataCleanManager.deleteFolderFile(new File(BeemApplication
							.getContext().getFilesDir(), "Cartoon").getPath(),
							true);
					PendingIntent contentIntent = PendingIntent.getActivity(
							mContext, 0, intent, 0);
					notification.setLatestEventInfo(mContext,
							mContext.getString(R.string.downloadSuccess), null,
							contentIntent);
					mNotifManager.notify(R.drawable.ic_launcher, notification);
				} else {
					SharedPrefsUtil.putValue(mContext,
							SettingKey.IsDowningState, false);
					Notification notification = new Notification(
							android.R.drawable.stat_sys_download_done,
							mContext.getString(R.string.downloadFailure),
							System.currentTimeMillis());
					notification.flags = Notification.FLAG_AUTO_CANCEL;
					PendingIntent contentIntent = PendingIntent.getActivity(
							mContext, 0, new Intent(), 0);
					notification.setLatestEventInfo(mContext,
							mContext.getString(R.string.downloadFailure), null,
							contentIntent);
					mNotifManager.notify(R.drawable.ic_launcher, notification);
				}
			} else {
				/*Toast.makeText(mContext, Environment.getExternalStorageState(),
						Toast.LENGTH_SHORT).show();*/
			}
		} catch (Exception e) {
			SharedPrefsUtil
					.putValue(mContext, SettingKey.IsDowningState, false);
		} finally {
		}
	}
	/**
	 * Desc:文件下载
	 * @param downloadUrl 下载URL
	 * @param saveFilePath 保存文件路径
	 * @return ture:下载成功 false:下载失败
	 */
	public boolean downloadFile(String downloadUrl, File saveFilePath) {
		int fileSize = -1;
		int downFileSize = 0;
		boolean result = false;
		int progress = 0;
		URL url;
		try {
			url = new URL(downloadUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if (null == conn) {
				return false;
			}
			// 读取超时时间 毫秒级
			conn.setReadTimeout(10000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				fileSize = conn.getContentLength();
				InputStream is = conn.getInputStream();
				FileOutputStream fos = new FileOutputStream(saveFilePath);
				byte[] buffer = new byte[1024];
				int i = 0;
				int tempProgress = -1;
				while ((i = is.read(buffer)) != -1) {
					downFileSize = downFileSize + i;
					// 下载进度
					progress = (int) (downFileSize * 100.0 / fileSize);
					fos.write(buffer, 0, i);
					synchronized (this) {
						if (downFileSize == fileSize) {
							// 下载完成
							mNotifManager.cancel(R.id.downLoadIcon);
						} else if (tempProgress != progress) {
							// 下载进度发生改变，则发送Message
							mContentView.setTextViewText(R.id.progressPercent,
									progress + "%");
							mContentView.setProgressBar(R.id.downLoadProgress,
									100, progress, false);
							mDownNotification.contentView = mContentView;
							mDownNotification.contentIntent = mDownPendingIntent;
							mNotifManager.notify(R.id.downLoadIcon,
									mDownNotification);
							tempProgress = progress;
						}
					}
				}
				fos.flush();
				fos.close();
				is.close();
				result = true;
			} else {
				result = false;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
