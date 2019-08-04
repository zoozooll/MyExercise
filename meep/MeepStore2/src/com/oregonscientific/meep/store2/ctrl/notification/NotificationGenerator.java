package com.oregonscientific.meep.store2.ctrl.notification;

import com.oregonscientific.meep.notification.Notification;

public class NotificationGenerator {

	public static Notification getProgressNotification(int progressMax, int progress, boolean progressIndeterminate,String title,String message) {
		Notification.Builder builder = new Notification.Builder()
				.setKind(Notification.KIND_STORE)
				.setContentTitle(title)
				.setContentText(message);
		
		Notification.ProgressBarStyle progressStyle = new Notification.ProgressBarStyle(builder)
				.setProgress(progressMax, progress, progressIndeterminate);
		
		return progressStyle.build();
	}
	
	public static Notification generateNormalNotification(String title,String message)
	{
		Notification.Builder builder = new Notification.Builder()
		.setKind(Notification.KIND_STORE)
		.setContentTitle(title)
		.setContentText(message)
		;
		return builder.build();
	}
	public static Notification generateNormalNotification(String message)
	{
		Notification.Builder builder = new Notification.Builder()
		.setKind(Notification.KIND_STORE)
		.setContentText(message)
		;
		return builder.build();
	}
	
	public static Notification generateNormalNotification(NotificationMessageItem item)
	{
		return generateNormalNotification(item.getMessage());
	}
}
