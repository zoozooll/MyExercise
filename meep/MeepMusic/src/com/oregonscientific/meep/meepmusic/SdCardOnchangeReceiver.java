package com.oregonscientific.meep.meepmusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author aaronli
 *
 */
public class SdCardOnchangeReceiver extends BroadcastReceiver {
	
	private static DataSourceManager DataSourceManager;

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		//Log.d(this.getClass().getSimpleName(), context+" broadcast receive "+intent.getAction());
		if (DataSourceManager == null) {
			DataSourceManager = new DataSourceManager(context);
		}
		DataSourceManager.getAllItems(true);
	}

}
