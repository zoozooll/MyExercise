package com.butterfly.vv.vv.utils;

import com.beem.project.btf.receiver.AlarmReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class TimeflyNotifiesManager {
	
	
	public static void setTimeflyNotifies(Context c) {
		Intent intent = new Intent(c, AlarmReceiver.class);
		intent.putExtra(Intent.EXTRA_ALARM_COUNT, 1	);
		PendingIntent sender = PendingIntent.getBroadcast(c, 0, intent, 0);
		AlarmManager alarm = (AlarmManager) c
				.getSystemService(Context.ALARM_SERVICE);
		alarm.set(AlarmManager.RTC, System.currentTimeMillis() + 5000L, sender);
	}
}
