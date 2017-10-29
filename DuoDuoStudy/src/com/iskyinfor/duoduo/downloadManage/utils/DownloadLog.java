package com.iskyinfor.duoduo.downloadManage.utils;

import java.io.File;

import android.content.Context;
import android.util.Log;

/**
 * 下载组件的日志类
 * 
 * @author pKF29007
 * 
 */
public class DownloadLog {
	public static final String LOGTAG = "Downloader";

	public static boolean isLog = true;

	public static boolean VERBOSE = true;
	public static boolean DEBUG = true;
	public static boolean INFO = true;
	public static boolean WARN = true;
	public static boolean ERROR = true;

	public static void v(String tag, String msg, Throwable th) {
		if (VERBOSE)
			Log.v(tag, msg, th);
	}

	public static void d(String tag, String msg, Throwable th) {
		if (DEBUG)
			Log.d(tag, msg, th);
	}

	public static void i(String tag, String msg, Throwable th) {
		if (INFO)
			Log.i(tag, msg, th);
	}

	public static void w(String tag, String msg, Throwable th) {
		if (WARN)
			Log.w(tag, msg, th);
	}

	public static void e(String tag, String msg, Throwable th) {
		if (ERROR)
			Log.e(tag, msg, th);
	}

	public static void v(String tag, String msg) {
		if (VERBOSE)
			Log.v(tag, msg, null);
	}

	public static void d(String tag, String msg) {
		if (DEBUG)
			Log.d(tag, msg, null);
	}

	public static void i(String tag, String msg) {
		if (INFO)
			Log.i(tag, msg, null);
	}

	public static void w(String tag, String msg) {
		if (WARN)
			Log.w(tag, msg, null);
	}

	public static void e(String tag, String msg) {
		if (ERROR)
			Log.e(tag, msg, null);
	}

	public static void v(String msg) {
		if (VERBOSE)
			Log.v(DownloadLog.LOGTAG, msg, null);
	}

	public static void d(String msg) {
		if (DEBUG)
			Log.d(DownloadLog.LOGTAG, msg, null);
	}

	public static void i(String msg) {
		if (INFO)
			Log.i(DownloadLog.LOGTAG, msg, null);
	}

	public static void w(String msg) {
		if (WARN)
			Log.w(DownloadLog.LOGTAG, msg, null);
	}

	public static void e(String msg) {
		if (ERROR)
			Log.e(DownloadLog.LOGTAG, msg, null);
	}

	/**
	 * 下载组件日志开启的后门标示
	 */
	private static final String LOG_TAG = "log.tag";

	/**
	 * 下载组件开启日志的后门方法
	 * 
	 * @param context
	 */
	public static void checkIsLog(Context context) {
		String file = context.getFilesDir() + File.separator + LOG_TAG;
		if (new File(file).exists()) {
			isLog = true;
			VERBOSE = VERBOSE || isLog;
			DEBUG = DEBUG || isLog;
			INFO = INFO || isLog;
			WARN = WARN || isLog;
			ERROR = ERROR || isLog;
		} else {
			isLog = false;
		}
	}

}
