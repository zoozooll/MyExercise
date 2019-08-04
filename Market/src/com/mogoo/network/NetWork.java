package com.mogoo.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetWork {
	/**
	 * 功能：判断当前是否有可用网络
	 * 
	 * @return
	 */
	public static boolean isAvailable(Context context) {
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

		if (networkInfo != null)// 注意，这个判断一定要的哦，要不然会出错
		{
			return networkInfo.isAvailable();
		}

		return false;
	}

	
	/*8
	 * 功能：判断当前网络是否已经链接
	 * 
	 * @return
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

		if (networkInfo != null)// 注意，这个判断一定要的哦，要不然会出错
		{
			return networkInfo.isAvailable();
		}

		return false;
	}
	 
	 
	
	/**
	 * 功能：获取当前网络类型
	 * 
	 * @return "mobile" 表示移动网络 "wifi"表示wifi网络 null 表示网络不可用
	 */
	public static String getNetworkTypeName(Context context) 
	{
		ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

		if (networkInfo != null)// 注意，这个判断一定要的哦，要不然会出错
		{
			return networkInfo.getTypeName().toLowerCase();
		}

		return null;
	}
	
	/**
	 * 功能：获取当前网络类型
	 * 
	 * @return "mobile" 表示移动网络 "wifi"表示wifi网络 null 表示网络不可用
	 */
	public static int getNetworkType(Context context) 
	{
		ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

		if (networkInfo != null)// 注意，这个判断一定要的哦，要不然会出错
		{
			return networkInfo.getType();
		}

		return -1;
	}
	
	
	// **************************获取当前网络制式*******************************//
	public static int getPhoneType(Context context) 
	{
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getPhoneType();
	}
}
