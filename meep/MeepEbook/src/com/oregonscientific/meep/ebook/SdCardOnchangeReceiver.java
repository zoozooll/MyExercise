package com.oregonscientific.meep.ebook;

import android.app.DownloadManager;
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
		String action = intent.getAction();

		if (DataSourceManager == null) {
			/*2013-7-16 -Amy- add brodcastReceiver to update ebook
			 * if ("com.meepstore.action.DOWNLOAD_COMPLETE".equals(action)) {
				Log.e("cdf", " broadcast receive = = ==  "+intent.getAction());
				DataSourceManager = new DataSourceManager(context);
			}*/
			DataSourceManager = new DataSourceManager(context);
		}
		DataSourceManager.getAllItems(true);
	}
}
