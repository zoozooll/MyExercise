/**
 * 控制和写入toast的样式
 */
package com.mogoo.ping.ctrl;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Aaron Lee
 * @Date 上午11:34:44  2012-10-23
 */
public class ToastController {
	
	private static Toast sToast;
	
	public static void makeToast(Context context, CharSequence text) {
		if (sToast == null) {
			sToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			sToast.setGravity(Gravity.BOTTOM, 0, 0);
		} else {
			//sToast.cancel();
			sToast.setText(text);
		}
		sToast.show();
	}
	
	public static void makeToast(Context context, int textId) {
		if (sToast == null) {
			sToast = Toast.makeText(context, textId, Toast.LENGTH_SHORT);
			sToast.setGravity(Gravity.BOTTOM, 0, 0);
		} else {
			sToast.setText(textId);
			//sToast.cancel();
		}
		sToast.show();
	}
	
	public static void clearToast() {
		sToast = null;
	}
}
