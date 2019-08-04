package com.beem.project.btf.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beem.project.btf.R;

/**
 * 应用程序UI工具包：封装数据加载等待框
 * @author AA
 * @Date 2014-11-23
 */
public class UIHelper {
	/** 加载数据对话框 */
	private static Dialog mLoadingDialog;
	private static UIHelperDialogType currenttype = UIHelperDialogType.Normal;

	public enum UIHelperDialogType {
		Normal, Simple
	}

	/**
	 * 显示加载对话框
	 * @param context 上下文
	 * @param msg 对话框显示内容
	 * @param cancelable 对话框是否可以取消
	 */
	private static void showDialogForLoading(Context context, String msg,
			UIHelperDialogType type, boolean cancelable) {
		currenttype = type;
		View view = null;
		switch (currenttype) {
			case Normal: {
				view = LayoutInflater.from(context).inflate(
						R.layout.layout_loading_dialog, null);
				TextView loadingText = (TextView) view
						.findViewById(R.id.id_tv_loading_dialog_text);
				loadingText.setText(msg);
				break;
			}
			case Simple: {
				view = LayoutInflater.from(context).inflate(
						R.layout.layout_loading_simple_dialog, null);
				break;
			}
		}
		if (mLoadingDialog != null) {
			//	throw new IllegalArgumentException("the mLoadingDialog must be dissmiss first! ");
			hideDialogForLoading();
		}
		if (currenttype == UIHelperDialogType.Normal) {
			mLoadingDialog = new Dialog(context, R.style.loading_dialog_style);
		} else {
			mLoadingDialog = new Dialog(context,
					R.style.loading_dialog_simple_style);
		}
		mLoadingDialog.setCancelable(cancelable);
		mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mLoadingDialog.show();
	}
	public static void showDialogForLoading(Context context,
			UIHelperDialogType type, boolean cancelable) {
		showDialogForLoading(context, "", type, cancelable);
	}
	public static void showDialogForLoading(Context context, String msg,
			boolean cancelable) {
		showDialogForLoading(context, msg, UIHelperDialogType.Normal,
				cancelable);
	}
	/**
	 * 关闭加载对话框
	 */
	public static void hideDialogForLoading() {
		if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
			mLoadingDialog.dismiss();
			mLoadingDialog = null;
		}
	}
}
