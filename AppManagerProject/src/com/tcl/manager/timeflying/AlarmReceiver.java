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

/**
 * When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent 
 * and then starts the IntentService {@code SampleSchedulingService} to do some work.
 */
public class AlarmReceiver extends BroadcastReceiver {
    // The app's AlarmManager, which provides access to the system alarm services.
    private static AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private static PendingIntent alarmIntent;
  
    @Override
    public void onReceive(Context context, Intent intent) {   
        Intent service = new Intent(context, SchedulingService.class);
        service.putExtra(SchedulingService.REPEAT_TAG, SchedulingService.TAG_REPEAT_LOOP);
//        NLog.i("aaron", "REPEAT onReceive");
        context.startService(service);
    }

    // BEGIN_INCLUDE(set_alarm)
    /**
     *  When the
     * alarm fires, the app broadcasts an Intent to this WakefulBroadcastReceiver.
     * @param context
     */
    public static void setAlarmRepeat(Context context) {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // Set the alarm's trigger time to 8:30 a.m.
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.add(Calendar.HOUR_OF_DAY, 1);
//  
        alarmMgr.setInexactRepeating(AlarmManager.RTC,  
        		 calendar.getTimeInMillis(), 1000 * 60 * 60, alarmIntent);
        
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
