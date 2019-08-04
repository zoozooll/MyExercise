package com.tcl.manager.timeflying;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.sax.StartElementListener;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Calendar;

import com.tcl.framework.log.NLog;
import com.tcl.framework.util.RandomUtil;

/**
 * When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent 
 * and then starts the IntentService {@code SampleSchedulingService} to do some work.
 */
public class AlarmReceiverForLogupload extends BroadcastReceiver {
    // The app's AlarmManager, which provides access to the system alarm services.
    private static AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private static PendingIntent alarmIntent;
  
    @Override
    public void onReceive(Context context, Intent intent) {   
        Intent service = new Intent(context, SchedulingService.class);
        service.putExtra(SchedulingService.REPEAT_TAG, SchedulingService.TAG_REPEAT_UPLOAD);
//        NLog.i("aaron", "REPEAT onReceive");
        context.startService(service);
    }

    public static void setAlarm(Context context) {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiverForLogupload.class);
        alarmIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
        // Set the alarm's trigger time to 8:30 a.m.
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.add(Calendar.HOUR_OF_DAY, 1);
//  
//        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,  
//        		 calendar.getTimeInMillis(), 1000 * 60 * 10, alarmIntent);
        Calendar c = Calendar.getInstance();
        long now = System.currentTimeMillis();
        c.set(Calendar.HOUR_OF_DAY, RandomUtil.randInt(11));
        long triggerAtMillis = c.getTimeInMillis();
        if (triggerAtMillis < now) {
        	triggerAtMillis = now + 60 * 3;
        }
        alarmMgr.set(AlarmManager.RTC, triggerAtMillis, alarmIntent);
    }
    
    public static void setNextAlarm(Context context) {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiverForLogupload.class);
        alarmIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
        // Set the alarm's trigger time to 8:30 a.m.
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.add(Calendar.HOUR_OF_DAY, 1);
//  
//        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,  
//        		 calendar.getTimeInMillis(), 1000 * 60 * 10, alarmIntent);
        Calendar c = Calendar.getInstance();
        long now = System.currentTimeMillis();
        c.set(Calendar.HOUR_OF_DAY, RandomUtil.randInt(11));
        c.add(Calendar.DAY_OF_YEAR, 1);
        long triggerAtMillis = c.getTimeInMillis();
        if (triggerAtMillis < now) {
        	triggerAtMillis = now + 60 * 3;
        }
        alarmMgr.set(AlarmManager.RTC, triggerAtMillis, alarmIntent);
    }

    /**
     * Cancels the alarm.
     * @param context
     */
    // BEGIN_INCLUDE(cancel_alarm)
    public static void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }
        
        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the 
        // alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
    // END_INCLUDE(cancel_alarm)
}
