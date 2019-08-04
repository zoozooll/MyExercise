package com.idt.bw.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.app.Notification;
import com.idt.bw.activity.MainActivity;
import android.app.Service;

public class CallAlarmNotification extends BroadcastReceiver {
	NotificationManager mNotificationManager;
	@Override
	public void onReceive(Context context, Intent intentin) {
		// TODO Auto-generated method stub
		/*
		int NotifyLoop = intentin.getIntExtra("NotifyLoop", 0);
		String NotifyTime =  intentin.getStringExtra("NotifyTime");
		
		mNotificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
		//setNotification("please weigh again","weigh reminder","weigh reminder notification",R.drawable.arrow_l);
		Notification notification=new Notification(R.drawable.arrow_l, "please weigh again", System.currentTimeMillis());
		Intent intent= new Intent(context, ChooseUserActivity.class);
		//intent.setClass(this, ChooseUserActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		notification.setLatestEventInfo(context, "weigh reminder", "weigh reminder notification", pendingIntent);
		mNotificationManager.notify(123, notification);
		*/
		String userName =  intentin.getStringExtra("UserName");
		
		Date datenow = new Date(System.currentTimeMillis());	
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String dateString = formatter.format(datenow);
		mNotificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
		Notification m_Notification = new Notification();
		Intent m_Intent= new Intent(context, ChooseUserActivity.class);
		PendingIntent m_PendingIntent = PendingIntent.getActivity(context, 0, m_Intent, 0);
		m_Notification.icon = R.drawable.producttour_page_orange;
		m_Notification.tickerText = String.format(context.getResources().getString(R.string.notify_timeon), userName);;
		m_Notification.defaults = Notification.DEFAULT_SOUND;
		m_Notification.setLatestEventInfo(context, context.getResources().getString(R.string.notify_title), m_Notification.tickerText, m_PendingIntent);
		mNotificationManager.notify(123, m_Notification);
		
		Intent intent = new Intent();
        intent.setAction("com.idt.bw.activity.AlarmService");
        context.startService(intent);

	}	
	
}
