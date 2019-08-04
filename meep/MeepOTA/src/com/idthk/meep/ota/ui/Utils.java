package com.idthk.meep.ota.ui;

import android.util.Log;

public class Utils {

	public static boolean DEBUG = false;
	public static void printLogcatDebugMessage(String text) {
		if (DEBUG)
			Log.d("MeepOTA", text);
	}
	public static void printLogcatDebugMessage(String tag,String text) {
		if (DEBUG)
			Log.d(tag, text);
	}
}
