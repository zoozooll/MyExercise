package com.mogoo.market.utils;

import com.mogoo.log.MyLogger;

/**
 * 日志工具类
 * 
 * @author 张永辉
 * @date 2011-10-18
 */
public class LogUtils {

	public static boolean DEBUG = false;
	public static boolean INFO = false;
	public static boolean WARN = false;
	public static boolean ERROR = false;

	private static final String MODULE_NAME = "MogooMarket";

	public static void debug(Class cs, String msg) {
		if (DEBUG) {
			MyLogger.debug(cs, MODULE_NAME, msg);
		}
	}

	public static void info(Class cs, String msg) {
		if (INFO) {
			MyLogger.info(cs, MODULE_NAME, msg);
		}
	}

	public static void warn(Class cs, String msg) {
		if (WARN) {
			MyLogger.warn(cs, MODULE_NAME, msg);
		}
	}

	public static void error(Class cs, String msg) {
		if (ERROR) {
			MyLogger.error(cs, MODULE_NAME, msg);
		}
	}

	public static void error(Class cs, String msg, Throwable t) {
		if (ERROR) {
			MyLogger.error(cs, MODULE_NAME, msg, t);
		}
	}
}
