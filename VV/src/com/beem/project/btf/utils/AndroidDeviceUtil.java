/**
 * 
 */
package com.beem.project.btf.utils;

import android.content.Context;

/**
 * @author Aaron Lee Created at 下午8:15:07 2015-9-9
 */
public class AndroidDeviceUtil {
	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}
	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}
}
