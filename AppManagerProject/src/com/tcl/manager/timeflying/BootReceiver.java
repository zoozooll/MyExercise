package com.tcl.manager.timeflying;

import java.util.Set;

import com.tcl.framework.log.NLog;
import com.tcl.manager.battery.BatteryUsageTask;
import com.tcl.manager.firewall.FirewallManager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;

/**
 * This BroadcastReceiver automatically (re)starts the alarm when the device is
 * rebooted. This receiver is set to be disabled (android:enabled="false") in the
 * application's manifest file. When the user sets the alarm, the receiver is enabled.
 * When the user cancels the alarm, the receiver is disabled, so that rebooting the
 * device will not trigger this receiver.
 */
// BEGIN_INCLUDE(autostart)
public class BootReceiver extends BroadcastReceiver {
    
	private static long lastPluginMillis;
	
	@Override
    public void onReceive(Context context, Intent intent) {
    	String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action))
        {
        	onBootCompleted(context);
        } else if (Intent.ACTION_SHUTDOWN.equals(action)) {
        	//new BatteryUsageTask().execute(context.getApplicationContext(), true);
        } else if (intent.ACTION_BATTERY_OKAY.equals(action)) {
        	// If the battery is charged, it will save current value.
        	//new BatteryUsageTask().execute(context.getApplicationContext(), true);
        	/*NLog.i("TAG", "ACTION_BATTERY_OKAY");
        	AlertDialog dialog = new AlertDialog.Builder(context)
        		.setTitle("Action Battery")
        		.setMessage("OK")
        		.create();
        	dialog.show();*/
        } else if (intent.ACTION_TIME_CHANGED.equals(action)) {
        	//Log.d("ACTION_TIME_CHANGED", "action "+DateFormat.format("yyyy-MM-dd", System.currentTimeMillis()));
//        	new BatteryUsageTask().execute(context.getApplicationContext(), true);
        } else if (intent.ACTION_DATE_CHANGED.equals(action)) {
        	//Log.d("DATE_CHANGED", "action "+DateFormat.format("yyyy-MM-dd", System.currentTimeMillis()));
        } else if (intent.ACTION_POWER_CONNECTED.equals(action)) {
        	long now = SystemClock.elapsedRealtime();
        	if ((now - lastPluginMillis) > 1000 * 60) {
        		new BatteryUsageTask().execute(context.getApplicationContext(), true);
        		lastPluginMillis = now;
        	}
        }
        
    }

	private void onBootCompleted(Context context) {
		Intent service = new Intent(context, SchedulingService.class);
		service.putExtra(SchedulingService.REPEAT_TAG, SchedulingService.TAG_REPEAT_START);
		context.startService(service);
		
		FirewallManager.getSingleInstance().initFirewall();
	}
	

}
//END_INCLUDE(autostart)
