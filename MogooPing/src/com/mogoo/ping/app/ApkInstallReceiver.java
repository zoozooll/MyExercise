/**
 * 
 */
package com.mogoo.ping.app;

import com.mogoo.ping.utils.UsedDataManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews.ActionException;

/**
 * @author Aaron Lee
 * TODO
 * @Date 下午7:18:11  2012-10-24
 */
public class ApkInstallReceiver extends BroadcastReceiver {

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
		if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
			
		} else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
			String packageName = intent.getDataString();
			Log.d("ApkInstallReceiver", "ACTION_PACKAGE_REMOVED "+packageName);
			UsedDataManager.remoteUninstallPackages(context, packageName);
		}
	}

}
