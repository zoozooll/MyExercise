/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * A class containing utility methods related to Network
 * 
 * @author Stanley Lam
 */
public class NetworkUtils {
	
	private static final String TAG = "NetworkUtils";
	
	/**
	 * The URL to test whether connection is available
	 */
	private static final String CONNECTION_URL = "http://www.google.com";
	
	/**
	 * Determines whether or not the device can connect to a network
	 * 
	 * @param context The context of the caller
	 * @return Returns true if the device is connected to a network, false otherwise
	 */
	public static boolean hasConnection(Context context) {
		// Quick return if the request cannot be processed
		if (context == null) {
			return false;
		}
		
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm != null ? cm.getActiveNetworkInfo() : null;
		if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
			Log.e(TAG, "No connection found");
			return false;
		}
		return true;
	}
	
	/**
	 * Determines whether or not the device is connected to the Internet 
	 * 
	 * @param context The context of the caller
	 * @return {@code true} if the device is connected to the Internet, {@code false} otherwise
	 */
	public static boolean hasInternetConnection(Context context) {
		boolean result = false;
		if (hasConnection(context)) {
			FutureTask<Boolean> task = new FutureTask<Boolean>(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					HttpURLConnection urlc = (HttpURLConnection) (new URL(CONNECTION_URL).openConnection());
		            urlc.setRequestProperty("User-Agent", "Android/MEEP");
		            urlc.setRequestProperty("Connection", "close");
		            urlc.setConnectTimeout(1500); 
		            urlc.connect();
					return (urlc.getResponseCode() == 200);
				}
				
			});
			ExecutorService service = Executors.newCachedThreadPool();
			service.submit(task);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = task.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot determine whether Internet connection is available because " + ex);
			}
		}
		return result;
	}

}
