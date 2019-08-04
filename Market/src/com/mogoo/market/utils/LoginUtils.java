package com.mogoo.market.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class LoginUtils {
	/**
	 * 转向登录界面
	 * 
	 * @author 张永辉
	 * @date 2011-12-14
	 * @param requestCode
	 */
	public static void gotoLogin(Activity activity, int requestCode) {
		Intent intent = new Intent("android.intent.action.LOGIN_DIALOG");
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 转向登录界面
	 * 
	 * @author 张永辉
	 * @date 2011-12-14
	 */
	public static void gotoLogin(Context context) {
		Intent intent = new Intent("android.intent.action.LOGIN_DIALOG");
		context.startActivity(intent);
	}
}
