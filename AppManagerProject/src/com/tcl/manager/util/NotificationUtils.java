/**
 * 
 */
package com.tcl.manager.util;

import com.tcl.manager.activity.MainActivity;
import com.tcl.manager.application.ManagerApplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * @author zuokang.li
 *
 */
public class NotificationUtils {
	
	private static NotificationManager sNotificationManager;
	private static NotificationCompat.Builder sNotifyBuilder;
	
	public static void showNotify(int iconRes, CharSequence title,
			CharSequence content) {
		Context context = ManagerApplication.sApplication
				.getApplicationContext();

		Intent resultIntent = new Intent(context, MainActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		// Gets a PendingIntent containing the entire back stack
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);

		sNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// Sets an ID for the notification, so it can be updated
		int notifyID = 1;
		sNotifyBuilder = new NotificationCompat.Builder(context)
				.setContentTitle(title).setContentText(content)
				.setSmallIcon(iconRes).setContentIntent(resultPendingIntent);

		sNotificationManager.notify("App Manager Notification tag", notifyID,
				sNotifyBuilder.build());

	}
	
	
}
