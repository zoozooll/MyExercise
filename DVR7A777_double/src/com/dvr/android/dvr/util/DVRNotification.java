package com.dvr.android.dvr.util;

import com.dvr.android.dvr.DVRActivity;
import com.dvr.android.dvr.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 *  DVR通知类的封装
 */
public class DVRNotification {
	
	private final int NOTIF_BACKGROUND_ID    = 1001;
	private final int NOTIF_SDCARD_NOFREE_ID = NOTIF_BACKGROUND_ID + 1;
	private final int NOTIF_EXCEPTION_ID     = NOTIF_SDCARD_NOFREE_ID + 1;
  
	private Context mContext;
	
	private static DVRNotification sInstance;
	
	private NotificationManager notifManager;
	
	private Intent notifIntent;
	
	private Notification backgroundNotification, sdcardNotification;
	
	private DVRNotification(Context context) {
		mContext = context;
		
		if (notifManager == null) {
			notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		if (notifIntent == null) {
			notifIntent = new Intent(context.getApplicationContext(), DVRActivity.class);
        }
		backgroundNotification = getNotification(R.string.background_notification,
        		Notification.FLAG_ONGOING_EVENT, R.string.background_notification_title,
        		R.string.background_notification);
		sdcardNotification = getNotification(R.string.background_sdcard_nofree,
	        		Notification.FLAG_ONGOING_EVENT, R.string.background_sdcard_nofree_title,
	        		R.string.background_sdcard_nofree);
	}
	
	public static synchronized DVRNotification getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new DVRNotification(context);
		}
		return sInstance;
	}
	
	public void notifBackground() {
    	cancelNotification();
        notifManager.notify(NOTIF_BACKGROUND_ID, backgroundNotification);
    }
	
	public void nofiySDCardNofree() {
    	cancelNotification();
    	notifManager.notify(NOTIF_SDCARD_NOFREE_ID, sdcardNotification);
    }

/*	
  private void showExceptionNotification() {
	cancelNotification();
	Notification notification = getNotification(R.string.background_exception_title,
    		Notification.FLAG_ONGOING_EVENT, R.string.background_exception_title,
    		R.string.background_exception);
    mNotificationManager.notify(NOTIF_EXCEPTION_ID, notification);
}
*/
	
	private Notification getNotification(int tickerTextResId, int flag, int titleResId, int msgResId) {
    	Notification notification = new Notification(R.drawable.icon, mContext.getString(tickerTextResId),
                System.currentTimeMillis());
        notification.flags = flag;
        PendingIntent contentIntent = PendingIntent.getActivity(mContext.getApplicationContext(),
                R.string.app_name, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(mContext.getApplicationContext(),
        		mContext.getString(titleResId), mContext.getString(msgResId), contentIntent);
        return notification;
    }
	
	public void cancelNotification() {
        notifManager.cancel(NOTIF_BACKGROUND_ID);
        notifManager.cancel(NOTIF_SDCARD_NOFREE_ID);
        notifManager.cancel(NOTIF_EXCEPTION_ID);
    }
}
