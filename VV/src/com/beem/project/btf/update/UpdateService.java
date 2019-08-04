package com.beem.project.btf.update;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.activity.MainpagerActivity;
import com.beem.project.btf.utils.ThreadUtils;

/***
 * 更新版本
 * @author zhangjia
 */
public class UpdateService extends Service {
	private static final int TIMEOUT = 10 * 1000;// 超时
	private static final int DOWN_OK = 1;
	private static final int DOWN_ERROR = 0;
	private String app_name;
	private NotificationManager notificationManager;
	private Notification notification;
	private Intent updateIntent;
	private PendingIntent pendingIntent;
	private int notification_id = 0;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		app_name = intent.getStringExtra("app_name");
		Log.i("info", "app_name=" + app_name);
		// 创建文件
		FileUtilupdate.createFile(app_name);// 创建文件
		createNotification();// 首次创建
		createThread();// 线程下载
		return super.onStartCommand(intent, flags, startId);
	}
	/***
	 * 开线程下载
	 */
	public void createThread() {
		/***
		 * 更新UI
		 */
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case DOWN_OK:
						// 下载完成，点击安装
						Uri uri = Uri.fromFile(FileUtilupdate.updateFile);
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(uri,
								"application/vnd.android.package-archive");
						pendingIntent = PendingIntent.getActivity(
								UpdateService.this, 0, intent, 0);
						notification.setLatestEventInfo(UpdateService.this,
								app_name, "下载成功，点击安装", pendingIntent);
						notificationManager.notify(notification_id,
								notification);
						stopSelf();// 停止服务
						break;
					case DOWN_ERROR:
						notification.setLatestEventInfo(UpdateService.this,
								app_name, "下载失败", pendingIntent);
						stopSelf();// 停止服务
						break;
					default:
						stopSelf();// 停止服务
						break;
				}
			}
		};
		final Message message = new Message();
		ThreadUtils.executeTask(new Runnable() {
			@Override
			public void run() {
				try {
					// long downloadSize = downloadUpdateFile(down_url,
					// FileUtil.updateFile.toString());
					long downloadSize = downloadUpdateFile(
							ConfigUpdate.UPDATE_SERVER
									+ ConfigUpdate.UPDATE_APKNAME,
							FileUtilupdate.updateFile.toString());
					if (downloadSize > 0) {
						// 下载成功
						message.what = DOWN_OK;
						handler.sendMessage(message);
					}
				} catch (Exception e) {
					e.printStackTrace();
					message.what = DOWN_ERROR;
					handler.sendMessage(message);
				}
			}
		});
	}

	/***
	 * 创建通知栏
	 */
	RemoteViews contentView;

	public void createNotification() {
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notification = new Notification();
		notification.icon = R.drawable.appicon;// 这个图标必须要设置，不然下面那个RemoteViews不起作用.
		// 这个参数是通知提示闪出来的值.
		notification.tickerText = "开始下载";
		/***
		 * 在这里我们用自定的view来显示Notification
		 */
		contentView = new RemoteViews(getPackageName(),
				R.layout.notification_item);
		contentView.setTextViewText(R.id.notificationTitle, "正在下载");
		contentView.setTextViewText(R.id.notificationPercent, "0%");
		contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);
		notification.contentView = contentView;
		updateIntent = new Intent(this, MainpagerActivity.class);
		updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);
		notification.contentIntent = pendingIntent;
		notificationManager.notify(notification_id, notification);
	}
	/***
	 * 下载文件
	 * @return
	 * @throws MalformedURLException
	 */
	public long downloadUpdateFile(String down_url, String file)
			throws Exception {
		int down_step = 5;// 提示step
		int totalSize;// 文件总大小
		int downloadCount = 0;// 已经下载好的大小
		int updateCount = 0;// 已经上传的文件大小
		InputStream inputStream;
		OutputStream outputStream;
		URL url = new URL(down_url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		httpURLConnection.setConnectTimeout(TIMEOUT);
		httpURLConnection.setReadTimeout(TIMEOUT);
		// 获取下载文件的size
		totalSize = httpURLConnection.getContentLength();
		if (httpURLConnection.getResponseCode() == 404) {
			throw new Exception("fail!");
		}
		inputStream = httpURLConnection.getInputStream();
		outputStream = new FileOutputStream(file, false);// 文件存在则覆盖掉
		byte buffer[] = new byte[1024];
		int readsize = 0;
		while ((readsize = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, readsize);
			downloadCount += readsize;// 时时获取下载到的大小
			/**
			 * 每次增张5%
			 */
			if (updateCount == 0
					|| (downloadCount * 100 / totalSize - down_step) >= updateCount) {
				updateCount += down_step;
				// 改变通知栏
				// notification.setLatestEventInfo(this, "正在下载...", updateCount
				// + "%" + "", pendingIntent);
				contentView.setTextViewText(R.id.notificationPercent,
						updateCount + "%");
				contentView.setProgressBar(R.id.notificationProgress, 100,
						updateCount, false);
				// show_view
				notificationManager.notify(notification_id, notification);
			}
		}
		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
		}
		inputStream.close();
		outputStream.close();
		return downloadCount;
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
