/**
 * 
 */
package com.oregonscientific.meep.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author aaronli
 *
 */
public class ApplicationOnchangeReceiver extends BroadcastReceiver {
	
	private static AppManager sAppManager;

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		//Log.d(this.getClass().getSimpleName(), context+" broadcast receive "+intent.getAction());
		if (sAppManager == null) {
			sAppManager = new AppManager(context);
		}
		sAppManager.getAllItems(true);
	}

}
