/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.oregonscientific.meep.util.SystemUtils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * The control object to control the build environment
 */
public class Build {
	
	private static final String TAG = "Build";
	
	private static final String FILE_VERSION = "meepver.txt";
	private static final String FILE_SERIAL = "sn.txt";
	
	/** The private directory */
	private static final String PRIVATE_DIRECTORY = "/mnt/private";

	/**
	 * For debugging 
	 */
	public static enum Environment {
		PRODUCTION,
		SANDBOX;
		
		public static Environment fromInteger(int value) {
			return value == 1 ? SANDBOX : PRODUCTION;
		}
	};
	
	public static Environment environment = Environment.PRODUCTION;
	
	/** Returns true if we are running a debug build */
	public static boolean DEBUG = false;
	
	/** Version strings */
    public static class VERSION {
    	
    	/**
         * The user-visible version string.  E.g., "1.0" or "3.4b5".
         */
    	public static final String NAME = getString("ro.meep.version.name");
    	
    	/**
    	 * The user-visible SDK revision number.
    	 */
    	public static final String CODE = getString("ro.meep.version.code");
    }
    
    /** A hardware serial number, if available.  Alphanumeric only, case-insensitive. */ 
    public static final String SERIAL = readFile(new File(PRIVATE_DIRECTORY, FILE_SERIAL));
    
    /** The hardware MAC address */
    public static final String MAC_ADDRESS = getString("ro.hardware.wifi.mac");
    
    /**
     * Updates system properties 
     */
    public static void update(Context context) {
    	// TODO: updates Android system properties
    	
    	try {
    		String value = readFile(new File(PRIVATE_DIRECTORY, FILE_SERIAL));
    		SystemProperties.set("ro.serialno", value);
    		
    		PackageInfo info = context.getPackageManager().getPackageInfo(SystemUtils.PACKAGE_HOME, PackageManager.GET_CONFIGURATIONS);
    		SystemProperties.set("ro.meep.version.name", info.versionName);
    		SystemProperties.set("ro.meep.version.code", Integer.toString(info.versionCode));
        	
        	value = getMacAddress(context);
        	SystemProperties.set("ro.hardware.wifi.mac", value);
    	} catch (Exception ex) {
    		Log.e(TAG, "Cannot update system properties because " + ex);
    	}
    }
	
	private static String readFile(File file) {
		String line = null;
		// Read text from file
		StringBuilder text = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));

			while ((line = br.readLine()) != null) {
				text.append(line);
				text.append("\n");
			}
		} catch (Exception e) {
			// Ignored
			text.delete(0, text.length());
		}

		return text.toString().trim();
	}
	
	/**
	 * Returns the hardware MAC address of the Wifi network
	 * 
	 * @param context the context that the application runs in
	 * @return the hardware MAC address of the Wifi radio or an empty string if no Wifi radio is found
	 */
	private static String getMacAddress(Context context) {
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wm.getConnectionInfo();
		return info == null ? "" : info.getMacAddress();
	}
	
	private static String getString(String property) {
		return SystemProperties.get(property);
	}
	
	private static long getLong(String property) {
        try {
            return Long.parseLong(SystemProperties.get(property));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
	
}
