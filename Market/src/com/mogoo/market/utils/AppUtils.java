package com.mogoo.market.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;

/**
 * @author dengliren
 * @date:2012-1-9
 * @description
 */
public class AppUtils {

	/**
	 * 判断sd卡是否存在
	 * */

	public static boolean isSdcardExist() {
		return Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 判断是否重复下载
	 * */

	public static boolean isRepeatDown() {

		return false;
	}

	/**
	 * 获取SD卡的路径
	 * */
	public static String getSDKpath() {
		String sdcardStr = "";
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {// 判断sdcard是否存在
			sdcardStr = Environment.getExternalStorageDirectory() + "";// SDCARDL路径
		} else {
			// sdcard不存在
		}
		return sdcardStr;
	}
	
	/**
	 * 返回sdcard是否存在，存在返回true
	 * @return sdcard is ready
	 */
	public static boolean isSDCardReady() {
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			return true;
		else return false;
	}
	
	/**
	 * add by fdl
	 * @param context
	 * @return
	 */
	public static PackageInfo getPackageInfo(Context context) {
		PackageInfo p = null;
		try {
			p = context.getPackageManager().getPackageInfo(context.getPackageName(),
					PackageManager.GET_INSTRUMENTATION);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return p;
	}

}
