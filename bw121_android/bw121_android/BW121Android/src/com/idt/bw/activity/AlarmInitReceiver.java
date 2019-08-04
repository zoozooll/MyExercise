/**
 * 
 */
package com.idt.bw.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author aaronli
 *
 */
public class AlarmInitReceiver extends BroadcastReceiver {

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent intent2 = new Intent();
        intent2.setAction("com.idt.bw.activity.AlarmService");
        context.startService(intent2);

	}

}
