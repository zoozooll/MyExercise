package com.tcl.manager.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.tcl.manager.activity.MainActivity;
import com.tcl.manager.application.ManagerApplication;
import com.tcl.manager.score.RunningAppInfo;
import com.tcl.mie.manager.R;

/**
 * 通知栏
 */
public class NotificationSender {
    private static int NOTICE_ID = 1912;
    private final static int UPDATE_NOTIFYID = 1913;
    private final static int CHANGE_NOTIFYID = 1914;
    private final static int RELEASE_MEMORY_ID = 1914;

    /**
     * 正在安装 notification
     */
    public static void installingNotification(Context mContext, String appName, int id) {
        notification(mContext, "\"" + appName + "\"" + mContext.getString(R.string.update), appName, mContext.getString(R.string.update), null, id, 0);
    }

    /**
     * 更新提示 notification
     */
	public static void appsUpdateNotification(Context mContext, int updates,
			Intent notificationIntent) {
		notification(
				mContext,
				String.format(mContext.getString(R.string.update), String.valueOf(updates)),
				mContext.getString(R.string.app_name),
				String.format(mContext.getString(R.string.update),
						String.valueOf(updates)), notificationIntent,
				UPDATE_NOTIFYID, 0);
	}

	public static void killBackgroundApp(Context mContext, String appsName,
			String releaseSize, Intent notificationIntent) {
		notification(mContext, mContext.getString(R.string.stop_apps),
				mContext.getString(R.string.stop_apps), String.format(
						mContext.getString(R.string.release_notification),
						appsName, releaseSize), notificationIntent,
				RELEASE_MEMORY_ID, 0);
	}
	
	public static void killBackgroundApp(Context mContext, 
			int releaseCount, Intent notificationIntent) {
		notification(mContext, mContext.getString(R.string.stop_apps),
				mContext.getString(R.string.backgroup_notification_title, releaseCount), "", notificationIntent,
				RELEASE_MEMORY_ID, 0);
	}

    /**
     * add a notification
     */
    public static void notification(Context context, String tickerText, String contentTitle, String contentText, Intent notificationIntent, int id, int icon) {
        if (icon == 0) {
            icon = R.drawable.ic_launcher;
        }
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        /*Notification notification = new Notification(icon, tickerText, System.currentTimeMillis());
        notification.ledARGB = 0xFF0000;
        notification.ledOnMS = 200;
        notification.ledOffMS = 4000;
        notification.flags = Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        notification.sound = null;
        notification.vibrate = null;*/

        PendingIntent contentIntent = null;
        if (notificationIntent != null) {

            contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        }
        
       /* Notification.Builder sNotifyBuilder = new Notification.Builder(mContext)
		.setContentIntent(contentIntent).setSmallIcon(R.drawable.ic_launcher)  
        .setWhen(System.currentTimeMillis()).setTicker("App Manager Memory")// 设置状态栏的显示的信息  
        .setAutoCancel(true)
        .setSmallIcon(R.drawable.ic_launcher, 0)
        ;
        Notification notification = sNotifyBuilder.build();
        notification.setLatestEventInfo(mContext, contentTitle, contentText, contentIntent);*/
        
     // Adds remote view
 		RemoteViews expandedView = new RemoteViews(context.getPackageName(), 
 		        R.layout.notify_update);
 		expandedView.setTextViewText(R.id.textTitle, contentTitle);
 		expandedView.setTextViewText(R.id.textContent, contentText);
 		expandedView.setTextViewText(R.id.textTime, new SimpleDateFormat("HH:mm").format(new Date()));
 		expandedView.setImageViewResource(R.id.imageIcon, icon);
        
        Notification.Builder builder = new Notification.Builder(context)
		.setAutoCancel(true)
		// .setContentIntent(mPIContent)
		.setContentIntent(contentIntent)
		// .setContentIntent(piUrl)
		// 可能会影响到Ticker的显示
		.setTicker(tickerText)
		.setContent(expandedView)
		.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
		.setTicker("ticker").setPriority(NotificationCompat.PRIORITY_MAX)
		.setSmallIcon(R.drawable.ic_notice_small, 0)
		;
        try {
            nm.notify(NOTICE_ID + id, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }
    
    public static void notificationKillerProcesses(int icon, Map<String, RunningAppInfo> running) {
    	Context context = ManagerApplication.sApplication
				.getApplicationContext();

		Intent resultIntent = new Intent(context, MainActivity.class);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		// Gets a PendingIntent containing the entire back stack
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationManager sNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// Sets an ID for the notification, so it can be updated
		int notifyID = 1234567231;
		
		// Adds remote view
		RemoteViews expandedView = new RemoteViews(context.getPackageName(), 
		        R.layout.notify_clearmemory);
		String title = context.getResources().getString(R.string.backgroup_notification_title, running.size());
		expandedView.setTextViewText(R.id.textTitle, title);
		expandedView.setTextViewText(R.id.textTime, new SimpleDateFormat("HH:mm").format(new Date()));
		expandedView.setImageViewResource(R.id.imageIcon, icon);
		int[] imageViewIds = {R.id.imageApp1, R.id.imageApp2,R.id.imageApp3,R.id.imageApp4,R.id.imageApp5,R.id.imageApp6,
				R.id.imageApp7,R.id.imageApp8};
		/*int i = 0;
		for (String pkgName : running.keySet()) {
			Drawable drawable =  PkgManagerTool.getIcon(context, pkgName);
			expandedView.setImageViewBitmap(imageViewIds[i], AndroidUtil.drawableToBitmap(drawable));
			i ++;
		}*/
		ArrayList<String> pkgs = new ArrayList<String>(running.keySet());
		for (int i = 0, size = imageViewIds.length; i < size; i++) {
			if (i < pkgs.size()) {
				Drawable drawable =  PkgManagerTool.getIcon(context, pkgs.get(i));
				expandedView.setImageViewBitmap(imageViewIds[i], AndroidUtil.drawableToBitmap(drawable));
			} else {
				expandedView.setImageViewBitmap(imageViewIds[i], null);
			}
		}
		
		Notification.Builder sNotifyBuilder = new Notification.Builder(context)
			.setContentIntent(resultPendingIntent)
			.setTicker(context.getString(R.string.stop_apps))
	        .setWhen(System.currentTimeMillis()).setTicker("App Manager Memory")// 设置状态栏的显示的信息  
	        .setAutoCancel(true)  
	        .setContent(expandedView)
	        .setSmallIcon(R.drawable.ic_notice_small, 0)
	        ;
		
		sNotificationManager.notify("notificationKillerProcesses", notifyID,
				sNotifyBuilder.build());
    }

    /**
     * cancel notification
     * 
     * @param mContext
     * @param id
     * @return
     */
    public static void cancelInstallNotification(Context mContext, int id) {
        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NOTICE_ID + id);
    }

    /**
     * cancel 列表更新提醒通知
     * 
     * @param mContext
     * @param id
     * @return
     */
    public static void cancelUpdateNotification(Context mContext) {
        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NOTICE_ID + UPDATE_NOTIFYID);
    }

    /**
     * cancel 列表变化提醒通知
     * 
     * @param mContext
     * @param id
     * @return
     */
    public static void cancelChangeNotification(Context mContext) {
        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(CHANGE_NOTIFYID);
    }

    public static void cancelNotification(Context mContext) {
        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();
    }

}
