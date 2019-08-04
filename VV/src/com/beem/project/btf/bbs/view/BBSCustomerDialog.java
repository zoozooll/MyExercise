package com.beem.project.btf.bbs.view;

import com.beem.project.btf.utils.BBSUtils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

public class BBSCustomerDialog extends Dialog {
	private Context mContext;
	private boolean isCanclable = true;

	public static BBSCustomerDialog newInstance(Context context, int theme) {
		return new BBSCustomerDialog(context, theme);
	}
	private BBSCustomerDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.55f;
		lp.width = (int) (BBSUtils.getScreenWH(mContext)[0] * 0.9f);
		getWindow().setAttributes(lp);
		//getWindow().setSoftInputMode(Input);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		// 设置点击屏幕可关闭
		setCancelable(isCanclable);
		setCanceledOnTouchOutside(isCanclable);
	}
	@Override
	public void dismiss() {
		super.dismiss();
	}
	@Override
	public void setCancelable(boolean flag) {
		super.setCancelable(flag);
		this.isCanclable = flag;
		setCanceledOnTouchOutside(isCanclable);
	}
}
