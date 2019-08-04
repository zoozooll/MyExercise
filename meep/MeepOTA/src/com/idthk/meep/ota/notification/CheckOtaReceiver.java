package com.idthk.meep.ota.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;
import com.idthk.meep.ota.ui.Utils;
import com.idthk.meep.ota.utility.OtaUpgradeUtility;

public class CheckOtaReceiver extends BroadcastReceiver {
	// private static final int PERIOD = 30*1000; // 30 seconds
	// private static final int PERIOD = 30*60*1000; // 30 minites
	private static final int PERIOD = 60 * 60 * 1000; // 1 hours
	private static final int INITIAL_DELAY = 2000; // 2 seconds

	private static final String ACTION_CHECK_OTA = "com.idthk.meep.ota.checkota";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(ACTION_CHECK_OTA)) {
			context.startService(new Intent(context, CheckOtaService.class));
		} else {
			// schedule check OTA
			scheduleAlarms(context);

			// report version
			reportVersion(context);
		}
	}

	public void scheduleAlarms(Context ctxt) {
		AlarmManager mgr = (AlarmManager) ctxt.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(ctxt, CheckOtaReceiver.class);
		i.setAction(ACTION_CHECK_OTA);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(ctxt, 0, i, 0);

		mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()
				+ INITIAL_DELAY, PERIOD, pendingIntent);

	}

	public void reportVersion(Context context) {
		//get serial number
		String serial = OtaUpgradeUtility.getSerialNumber();
		//get version information
		ItemVersionReport version = getSelfVerison(context);
		if(version!=null)
		{
			String versionJson = new Gson().toJson(version);
			Utils.printLogcatDebugMessage("version-info:"+versionJson);
			//send rest request
			RestRequestReportVersion rest = new RestRequestReportVersion(context);
			rest.reportVersionToServer(versionJson, serial);
		}
	}

	/**
	 * get version information
	 * @param context
	 * @return version-info object
	 */
	public ItemVersionReport getSelfVerison(Context context) {
		PackageInfo packageInfo = null;
		try {
			PackageManager packageManager = context.getPackageManager();
			packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			String packageName = packageInfo.packageName;
			int versionCode = packageInfo.versionCode;
			String versionName = packageInfo.versionName;
			ItemVersionReport version = new ItemVersionReport(packageName, versionCode, versionName, ItemVersionReport.INSTALL_STATUS_INSTALLED);
			return version;
		} catch (NameNotFoundException e) {
			Log.e("reportVersion", "getversion error:" + e.toString());
			e.printStackTrace();
		}
		return null;
	}

}
