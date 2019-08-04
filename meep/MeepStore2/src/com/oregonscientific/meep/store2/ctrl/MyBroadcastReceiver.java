package com.oregonscientific.meep.store2.ctrl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.oregonscientific.meep.store2.Constants;
import com.oregonscientific.meep.store2.MainActivity;
import com.oregonscientific.meep.store2.MeepStoreService;
import com.oregonscientific.meep.store2.global.MeepStoreApplication;
import com.oregonscientific.meep.store2.global.MeepStoreLog;

public class MyBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		MeepStoreLog.logcatMessage("package", "package intent :" + intent.getAction());
		MeepStoreApplication app = (MeepStoreApplication) context.getApplicationContext();
		String packageName = null;
		if (intent.getDataString() != null) {
			packageName = intent.getDataString().substring(intent.getDataString().lastIndexOf(':') + 1);
		}
		if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)
				&& intent.getBooleanExtra(Intent.EXTRA_DATA_REMOVED, false)) {
			MeepStoreLog.logcatMessage("package", "package removed");

			// if(app.getAppCtrl()!= null &&
			// app.getAppCtrl().getPakageListener()!= null)
			// app.getAppCtrl().getPakageListener().onpackageRemoved(packageName);
			if (app.getAppCtrl() != null
					&& app.getAppCtrl().getPackageListeners() != null) {
				for (AppInstallationCtrl.PakageListener listener : app.getAppCtrl().getPackageListeners()) {
					listener.onpackageRemoved(packageName);
				}
			}

		} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
			MeepStoreLog.logcatMessage("package", "package installed");

			// if(app.getAppCtrl()!= null&&
			// app.getAppCtrl().getPakageListener()!= null)
			// app.getAppCtrl().getPakageListener().onpackageAdded(packageName);
			if (app.getAppCtrl() != null
					&& app.getAppCtrl().getPackageListeners() != null) {
				for (AppInstallationCtrl.PakageListener listener : app.getAppCtrl().getPackageListeners()) {
					listener.onpackageAdded(packageName);
				}
			}
		} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
			MeepStoreLog.logcatMessage("package", "package replaced");

			// if(app.getAppCtrl()!= null&&
			// app.getAppCtrl().getPakageListener()!= null)
			// app.getAppCtrl().getPakageListener().onpackageReplaced(packageName);
			if (app.getAppCtrl() != null
					&& app.getAppCtrl().getPackageListeners() != null) {
				for (AppInstallationCtrl.PakageListener listener : app.getAppCtrl().getPackageListeners()) {
					listener.onpackageReplaced(packageName);
				}
			}

		} else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			MeepStoreLog.logcatMessage("package", "boot completed, start MeepStore service");
			// TODO Auto-generated method stub
			Intent serviceIntent = new Intent(context, MeepStoreService.class);
			// serviceIntent.setAction("com.oregonscientific.meep.store.service");
			context.startService(serviceIntent);
		} else if (intent.getAction().equals(Intent.ACTION_VIEW)) {
			// TODO Auto-generated method stub
			String name = getPackageNameStoreItem(intent);
			if (name != null) {
				Intent storeActivity = new Intent(context, MainActivity.class);
				storeActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				storeActivity.putExtra(Constants.KEY_PAKCAGE_NAME_SINGLE_ITEM, name);
				context.startActivity(storeActivity);
			}
		}

	}

	public String getPackageNameStoreItem(Intent intent) {
		Uri data = intent.getData();
		if (data != null) {
			String packageName = data.getQueryParameter("id");
			MeepStoreLog.logcatMessage("test", "toString:" + data.toString());
			MeepStoreLog.logcatMessage("test", "toString:" + data.getQueryParameter("id"));
			return packageName;
		}
		return null;
	}

}
