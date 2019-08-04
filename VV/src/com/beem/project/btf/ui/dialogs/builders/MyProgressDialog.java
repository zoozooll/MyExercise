package com.beem.project.btf.ui.dialogs.builders;

import android.app.ProgressDialog;
import android.content.Context;

public class MyProgressDialog extends ProgressDialog {
	private static MyProgressDialog instance;

	public static synchronized MyProgressDialog newInstance(Context context) {
		if (instance == null) {
			instance = new MyProgressDialog(context);
		}
		return instance;
	}
	private MyProgressDialog(Context context) {
		super(context);
		setMessage("任务执行中，请等待...");
		setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}
	@Override
	public void dismiss() {
		super.dismiss();
		instance = null;
	}
}
