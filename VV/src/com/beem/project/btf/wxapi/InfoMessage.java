package com.beem.project.btf.wxapi;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Build.VERSION;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @ClassName: InfoMessage
 * @Description:提示数据控件Toast的工具类
 * @author 方鹏程
 * @date 2013-9-17
 */
public class InfoMessage {
	private static Handler handler = new Handler(Looper.getMainLooper());
	private static Toast toast = null;
	private static Object synObj = new Object();
	private static int sysVersion = Integer.parseInt(VERSION.SDK);

	/**
	 * 显示提示消息
	 * @param act 显示的页面显示提示的activity
	 * @param msg 提示消息
	 */
	public static void showMessage(Context act, final String msg) {
		showMessage(act, msg, Toast.LENGTH_SHORT);
	}
	/**
	 * 显示提示消息
	 * @param act 显示的页面显示提示的activity
	 * @param msg 提示消息
	 */
	public static void showMessage(Context act, final int msg) {
		showMessage(act, act.getResources().getString(msg), Toast.LENGTH_SHORT);
	}
	/**
	 * 显示提示消息
	 * @param act 显示提示的activity
	 * @param msg 提示消息
	 * @param len 时间
	 */
	public static void showMessage(final Context act, final String msg,
			final int len) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				synchronized (synObj) {
					if (toast != null) {
						// 4.0不用cancel
						if (sysVersion <= 14) {
							toast.cancel();
						}
						toast.setText(msg);
						toast.setDuration(len);
					} else {
						toast = Toast.makeText(act, msg, len);
						toast.setGravity(Gravity.BOTTOM, 0, 0);
					}
					toast.show();
				}
			}
		});
	}
}
