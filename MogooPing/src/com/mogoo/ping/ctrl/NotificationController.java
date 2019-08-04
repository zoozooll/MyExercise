package com.mogoo.ping.ctrl;

import com.mogoo.ping.MainActivity;
import com.mogoo.ping.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class NotificationController {

	private Context mContext;
	//定义NotificationManager
    private NotificationManager mNotificationManager;
    
    private static NotificationController instance;
    
	private NotificationController(Context context) {
		mContext = context.getApplicationContext();
    	mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	public static NotificationController getInstance(Context context) {
		if (instance == null) {
			instance = new NotificationController(context);
		}
		return instance;
	}

	public void showNotification(int resText, int resIcon) {
		
        //定义通知栏展现的内容信息
        CharSequence tickerText = mContext.getText(resText);
        long when = System.currentTimeMillis();
        Notification notification = new Notification(resIcon, tickerText, when);
        notification.contentView = new RemoteViews(mContext.getPackageName(), R.layout.nofity_icon_show1);
        //定义下拉通知栏时要展现的内容信息
        //CharSequence contentTitle = "我的通知栏标展开标题";
        //CharSequence contentText = "我的通知栏展开详细内容";
        Intent notificationIntent = new Intent(mContext, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,notificationIntent, 0);
        //notification.setLatestEventInfo(mContext, contentTitle, contentText,contentIntent);
        notification.contentIntent = contentIntent;
        notification.flags = Notification.FLAG_NO_CLEAR; 
        //用mNotificationManager的notify方法通知用户生成标题栏消息通知
        mNotificationManager.notify(1, notification);
	}
	
	public void cancelNotification(int id) {
		 mNotificationManager.cancel(id); // 关闭一个通知 
	}
	
	private void setNotiType(int iconId, String text) {

	       /*
	        * 创建新的Intent，作为单击Notification留言条时， 会运行的Activity
	        */
	       Intent notifyIntent = new Intent(mContext, MainActivity.class);

	       notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

	       /* 创建PendingIntent作为设置递延运行的Activity */
	       PendingIntent appIntent = PendingIntent.getActivity(mContext, 0, notifyIntent,0);

	       /* 创建Notication，并设置相关参数 */
	       Notification myNoti = new Notification();

	       // 在状态栏不能被清除
	       myNoti.flags = Notification.FLAG_NO_CLEAR;

	       /* 设置statusbar显示的icon */
	       myNoti.icon = iconId;

	       /* 设置statusbar显示的文字信息 */
	       myNoti.tickerText = text;

	       /* 设置notification发生时同时发出默认声音 */

	       // myNoti.defaults =Notification.DEFAULT_SOUND;

	       /* 设置Notification留言条的参数 */
	       myNoti.setLatestEventInfo(mContext, text, text,appIntent);

	       /* 送出Notification*/
	       mNotificationManager.notify(0, myNoti);

	    } 
}
