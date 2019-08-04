/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.oregonscientific.meep.R;
import com.oregonscientific.meep.permission.PermissionManager;

/**
 * Contains helper method to retrieve localized messages to be displayed
 */
public class DialogMessage {
	
	public static final String TAG = DialogMessage.class.getSimpleName();
	
	/**
	 * Returns the message from resource that indicates a package cannot be launched
	 *  
	 * @param context the context to retrieve message
	 * @param intent the intent to launch the package
	 * @param reason the resource id to retrieve the message
	 * @return the localized message
	 */
	public static String getLaunchPackageFailedMessage(Context context, Intent intent, int reason) {
		// Quick return if the request cannot be processed
		if (context == null) {
			return null;
		}
		
		// Retrieves information on the component specified
		PackageManager packageManager = context.getPackageManager();
		ApplicationInfo info = null;
		try {
			ComponentName component = intent.getComponent();
			String packageName = component.getPackageName();
			info = packageManager.getApplicationInfo(packageName, 0);
		} catch (Exception e) {
			Log.e(TAG, "No component is specified...");
		}
		
		String applicationLabel = (String) (info == null ? "" : packageManager.getApplicationLabel(info));
		applicationLabel = applicationLabel == null ? "" : applicationLabel;
		
		String message = context.getString(reason);
		try {
			message = message == null ? "" : String.format(message, applicationLabel, applicationLabel);
		} catch (Exception ex) {
			Log.e(TAG, "Cannot format message because " + ex);
		}
		return message;
	}

	/**
	 * Returns the message from resource that indicates permission of a package
	 * is violated
	 * 
	 * @param context
	 *            the context to retrieve message
	 * @param component
	 *            the component that violated permission
	 * @param reason
	 *            the reason for violating permission. Must be one of
	 *            {@link com.oregonscientific.meep.permission.PermissionManager#FLAG_PERMISSION_DENIED}
	 *            or
	 *            {@link com.oregonscientific.meep.permission.PermissionManager#FLAG_PERMISSION_TIMEOUT}
	 * @return a localized message
	 */
	public static String getPackagePermissionViolatedMessage(Context context, ComponentName component, int reason) {
		return getPackagePermissionViolatedMessage(context, component == null ? "" : component.getPackageName(), reason);
	}
	
	/**
	 * Returns the message from resource that indicates permission of a package is violated
	 * 
	 * @param context
	 *            the context to retrieve message
	 * @param component
	 *            the component that violated permission
	 * @param reason
	 *            the reason for violating permission. Must be one of
	 *            {@link com.oregonscientific.meep.permission.PermissionManager#FLAG_PERMISSION_DENIED}
	 *            or
	 *            {@link com.oregonscientific.meep.permission.PermissionManager#FLAG_PERMISSION_TIMEOUT}
	 * @return a localized message
	 */
	public static String getPackagePermissionViolatedMessage(Context context, String component, int reason) {
		// Quick return if the request cannot be processed
		if (context == null) {
			return null;
		}
		
		// Retrieves information on the component specified
		PackageManager packageManager = context.getPackageManager();
		ApplicationInfo info = null;
		try {
			info = packageManager.getApplicationInfo(component, 0);
		} catch (Exception e) {
			Log.e(TAG, "No component is specified...");
		} 
		
		String applicationLabel = (String) (info == null ? "" : packageManager.getApplicationLabel(info));
		applicationLabel = applicationLabel == null ? "" : applicationLabel;
		
		String message = null;
		switch (reason) {
		case PermissionManager.FLAG_PERMISSION_DENIED:
			message = context.getString(R.string.alert_permission_denied);
			message = message == null ? "" : String.format(message, applicationLabel, applicationLabel);
			break;
		case PermissionManager.FLAG_PERMISSION_TIMEOUT:
			message = context.getString(R.string.alert_permission_timeout);
		}
		return message;
	}

}
