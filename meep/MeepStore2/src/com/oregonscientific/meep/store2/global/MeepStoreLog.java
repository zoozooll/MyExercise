package com.oregonscientific.meep.store2.global;

import android.util.Log;

public class MeepStoreLog {

	private static boolean isDebug = false;
	public static String IAP_PURCHASE_ACTIVITY = "IAPPurchaseActivity";
	public static String IAP_REST_REQUEST = "IAPRestRequest";
	public static String IAP_SERVICE= "IAPService";

	public static void logcatMessage(String tag, String message) {
		if (isDebug) {
			Log.d(tag, message);
		}

	}

	public static void LogMsg(String message) {
		if (isDebug) {
			Log.d("MeepStoreLog", message);
		}

	}

	public void LogErr(String message) {
		Log.e("MeepStoreLog", message);
	}

}
