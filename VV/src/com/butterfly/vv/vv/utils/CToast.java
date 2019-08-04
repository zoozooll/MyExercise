package com.butterfly.vv.vv.utils;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.widget.Toast;

public class CToast {
	private static Toast mToast;

	public static void showToast(Context context, String text, int duration) {
		if (null != mToast) {
			if (Build.VERSION.SDK_INT <= 14) {
				mToast.cancel();
			}
			mToast.setText(text);
		} else {
			mToast = Toast.makeText(context, text, duration);
		}
		mToast.show();
	}
	public static void showToast(Context context, int strId, int duration) {
		showToast(context, context.getString(strId), duration);
	}
}
