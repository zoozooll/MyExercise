/**
 * 
 */
package com.mogoo.ping.app;

import com.mogoo.ping.R;
import com.mogoo.ping.ctrl.NotificationController;
import com.mogoo.ping.utils.UsedDataManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author Aaron Lee
 * TODO
 * @Date ����10:34:27  2012-10-9
 */
public class BootReceiver extends BroadcastReceiver {

	public BootReceiver() {
		super();
		
	}

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			NotificationController controller = NotificationController.getInstance(context);
			controller.showNotification(R.string.app_name, R.drawable.ic_launcher);
		} else if (intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
			UsedDataManager.saveCurrentUsedApkitems(context);
		}
	}

}
