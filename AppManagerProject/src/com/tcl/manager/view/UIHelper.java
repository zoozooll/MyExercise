package com.tcl.manager.view;

import android.content.Context;
import android.view.View;

/**
 * @Description: UI帮助类
 * @author wenchao.zhang
 * @date 2014年12月30日 下午9:00:28
 * @copyright TCL-MIE
 */

public class UIHelper {
	/**
	 * 显示默认对话框
	 * 
	 * @param context
	 * @param msg
	 *            提示文本
	 * @param confirmTips
	 *            确认提交文本
	 * @param leftButton
	 *            左边按钮
	 * @param rightButton
	 * @param leftListener
	 * @param rightListener
	 */
	public static void showCustomDialog(Context context, String msg,
			String confirmTips, String leftButton, String rightButton,
			View.OnClickListener leftListener,
			View.OnClickListener rightListener) {
		showCustomDialog(context, null, msg, confirmTips,leftButton, rightButton, leftListener, rightListener);

	}
	/**
	 * 弹出对话框
	 * @param context
	 * @param msg
	 * @param leftListener
	 * @param rightListener
	 */
	public static void showCustomDialog(Context context, String msg,String confirmTips,
			View.OnClickListener leftListener,
			View.OnClickListener rightListener) {
		showCustomDialog(context, null, msg, confirmTips, null, null, leftListener,
				rightListener);

	}
	
	public static void showCustomDialog(Context context,String title, String msg,String confirmTips,
			View.OnClickListener leftListener,
			View.OnClickListener rightListener) {
		showCustomDialog(context, title, msg, confirmTips, null, null, leftListener,
				rightListener);

	}
	
	public static void showCustomDialog(Context context, String title, String msg,
			String confirmTips, String leftButton, String rightButton,
			View.OnClickListener leftListener,
			View.OnClickListener rightListener) {
		try {
			CustomDialog dialog = new CustomDialog(context, title, msg, confirmTips,
					leftButton, rightButton, leftListener, rightListener);
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
