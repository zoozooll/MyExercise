package com.butterfly.vv.vv.utils;

import android.util.Log;

public class Debug {
	private static final boolean DEBUGENABLE = true;
	private static Debug debug;

	public static Debug getDebugInstance() {
		if (debug == null) {
			debug = new Debug();
		}
		return debug;
	}
	public void log(String msg) {
		if (DEBUGENABLE) {
			Log.i("VV", msg);
		}
	}
}
