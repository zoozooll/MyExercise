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
	//����NotificationManager
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
		
        //����֪ͨ��չ�ֵ�������Ϣ
        CharSequence tickerText = mContext.getText(resText);
        long when = System.currentTimeMillis();
        Notification notification = new Notification(resIcon, tickerText, when);
        notification.contentView = new RemoteViews(mContext.getPackageName(), R.layout.nofity_icon_show1);
        //��������֪ͨ��ʱҪչ�ֵ�������Ϣ
        //CharSequence contentTitle = "�ҵ�֪ͨ����չ������";
        //CharSequence contentText = "�ҵ�֪ͨ��չ����ϸ����";
        Intent notificationIntent = new Intent(mContext, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,notificationIntent, 0);
        //notification.setLatestEventInfo(mContext, contentTitle, contentText,contentIntent);
        notification.contentIntent = contentIntent;
        notification.flags = Notification.FLAG_NO_CLEAR; 
        //��mNotificationManager��notify����֪ͨ�û����ɱ�������Ϣ֪ͨ
        mNotificationManager.notify(1, notification);
	}
	
	public void cancelNotification(int id) {
		 mNotificationManager.cancel(id); // �ر�һ��֪ͨ 
	}
	
	private void setNotiType(int iconId, String text) {

	       /*
	        * �����µ�Intent����Ϊ����Notification������ʱ�� �����е�Activity
	        */
	       Intent notifyIntent = new Intent(mContext, MainActivity.class);

	       notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

	       /* ����PendingIntent��Ϊ���õ������е�Activity */
	       PendingIntent appIntent = PendingIntent.getActivity(mContext, 0, notifyIntent,0);

	       /* ����Notication����������ز��� */
	       Notification myNoti = new Notification();

	       // ��״̬�����ܱ����
	       myNoti.flags = Notification.FLAG_NO_CLEAR;

	       /* ����statusbar��ʾ��icon */
	       myNoti.icon = iconId;

	       /* ����statusbar��ʾ��������Ϣ */
	       myNoti.tickerText = text;

	       /* ����notification����ʱͬʱ����Ĭ������ */

	       // myNoti.defaults =Notification.DEFAULT_SOUND;

	       /* ����Notification�������Ĳ��� */
	       myNoti.setLatestEventInfo(mContext, text, text,appIntent);

	       /* �ͳ�Notification*/
	       mNotificationManager.notify(0, myNoti);

	    } 
}
