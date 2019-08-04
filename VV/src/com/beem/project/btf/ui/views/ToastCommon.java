package com.beem.project.btf.ui.views;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.R;

public class ToastCommon {
	private static ToastCommon toastCommom;
	private Toast toast;

	private ToastCommon() {
	}
	public static ToastCommon createToastConfig() {
		if (toastCommom == null) {
			toastCommom = new ToastCommon();
		}
		return toastCommom;
	}
	/**
	 * 显示Toast
	 * @param context
	 * @param root
	 * @param tvString
	 */
	public void ToastShow(Context context, ViewGroup root, String name,
			String time) {
		View layout = LayoutInflater.from(context).inflate(
				R.layout.toast_timefly, null);
		TextView tv_content1 = (TextView) layout.findViewById(R.id.tvw_mailerTo);
		TextView tv_content2 = (TextView) layout.findViewById(R.id.tvw_mailerContent);
//		Spanned str1 = Html.fromHtml("亲爱的" + "<font color=#fd5151>" + name
//				+ "</font>" + ",");
//		Spanned str2 = Html.fromHtml("在" + "<font color=#fd5151>" + time
//				+ "</font>" + "后再给你发送一组时光");
//		//    	tv_content1.setText("亲爱的"+name+",");
//		//    	tv_content2.setText("在"+time+"后再给你发送一组时光");
		tv_content1.setText(name);
		tv_content2.setText(context.getString(R.string.mailer_content_sf, time));
		toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}
}
