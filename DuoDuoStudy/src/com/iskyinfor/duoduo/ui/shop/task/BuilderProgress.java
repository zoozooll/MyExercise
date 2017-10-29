package com.iskyinfor.duoduo.ui.shop.task;

import android.app.ProgressDialog;
import android.content.Context;

public class BuilderProgress {
	public static ProgressDialog 		mDialog;
	
	public static void loadProgress(String title , Context context) {
		mDialog = new ProgressDialog(context);
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.setMessage(title);
		mDialog.setIndeterminate(false);
		mDialog.setCancelable(true);
		mDialog.show();
	}
}
