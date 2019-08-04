/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

/**
 * A class containing utility methods related to the Android system.
 */
public class SystemUtils {
	
	public static final String PACKAGE_HOME = "com.oregonscientific.meep.home";
	public static final String PACKAGE_CONFIGURATION = "com.oregonscientific.meep.meepopenbox";
	
	/** hide */
	private static final String FILE_INFO = ".info";
	private static final long FILE_IO_DELAY = 200;
	
	/** Volume and headphone handling */
	private static final String FILE_H2W = "sys/class/switch/h2w/state";
	
	/**
	 * The headset that state that indicates a headset is plugged in
	 */
	public static final int HEADSET_STATE_PLUGGED = 1;
	
	/**
	 * The headset that state that indicates a headset is unplugged in
	 */
	public static final int HEADSET_STATE_UNPLUGGED = 0;
	
	private SystemUtils() {
	};

	@TargetApi(11)
	public static void enableStrictMode() {
		if (SystemUtils.hasGingerbread()) {
			StrictMode.ThreadPolicy.Builder threadPolicyBuilder = 
				new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog();
			StrictMode.VmPolicy.Builder vmPolicyBuilder = 
				new StrictMode.VmPolicy.Builder().detectAll().penaltyLog();

			if (SystemUtils.hasHoneycomb()) {
				threadPolicyBuilder.penaltyFlashScreen();
			}
			
			StrictMode.setThreadPolicy(threadPolicyBuilder.build());
			StrictMode.setVmPolicy(vmPolicyBuilder.build());
		}
	}

	public static boolean hasFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed
		// behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}
	
	/**
	 * Returns whether or not the system is configured
	 * 
	 * @param context The context invoking this method
	 * @return {@code true} if the system is configured, {@code false} otherwise
	 */
	public static boolean isSystemConfigured(Context context) {
		boolean result = false;
		try {
			Context ctx = context.createPackageContext(PACKAGE_CONFIGURATION, Context.CONTEXT_IGNORE_SECURITY);
			File f = new File(ctx.getFilesDir().getPath(), FILE_INFO);
			result = f.exists();
		} catch (Exception ex) {
			// Ignored
		}
		return result;
	}
	
	public static void setSystemConfigured(Context context, boolean configured) {
		try {
			Context ctx = context.createPackageContext(PACKAGE_CONFIGURATION, Context.CONTEXT_IGNORE_SECURITY);
			File f = new File(ctx.getFilesDir().getPath(), FILE_INFO);
			if (!configured && f.exists()) {
				f.delete();
			} else if (configured) {
				f.createNewFile();
				Thread.sleep(FILE_IO_DELAY);
			}
		} catch (Exception ex) {
			// Ignored
		}
	}
	
	/**
	 * Returns whether or not the system is restored to factory default.  
	 * 
	 * @param context The context invoking this method
	 * @return {@code true} if the system is restored to factory default, {@code false} otherwise
	 */
	public static boolean isSystemRestored(Context context) {
		boolean result = false;
		try {
			Context ctx = context.createPackageContext(PACKAGE_HOME, Context.CONTEXT_IGNORE_SECURITY);
			File f = new File(ctx.getFilesDir().getPath(), FILE_INFO);
			result = !f.exists();
		} catch (Exception ex) {
			// Ignored
		}
		return result;
	}
	
	public static void setSystemRestored(Context context, boolean restored) {
		try {
			Context ctx = context.createPackageContext(PACKAGE_HOME, Context.CONTEXT_IGNORE_SECURITY);
			File f = new File(ctx.getFilesDir().getPath(), FILE_INFO);
			if (!restored && f.exists()) {
				f.delete();
			} else if (restored) {
				f.createNewFile();
				Thread.sleep(FILE_IO_DELAY);
			}
		} catch (Exception ex) {
			// Ignored
		}
	}
	
	/**
	 * Determines whether or not a headset is plugged in to the device 
	 * 
	 * @return {@code true} if an headset is plugged in, {@code false} otherwise
	 */
	public static boolean isHeadsetPlugged() {
		boolean result = false;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(FILE_H2W));
			int state = Integer.valueOf(reader.readLine());
			result = state == HEADSET_STATE_PLUGGED;
		} catch (Exception ex) {
			// Ignore
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// Cannot close reader, ignore
			}
		}
		return result;
	}
}
