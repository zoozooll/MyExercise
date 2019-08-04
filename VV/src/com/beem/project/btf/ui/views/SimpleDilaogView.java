package com.beem.project.btf.ui.views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.beem.project.btf.R;

/**
 * 此类可以设置一个或多个按钮
 */
public class SimpleDilaogView {
	private Context mContext;
	private View mView;
	private LinearLayout btnwraper;
	private TextView title;
	private ImageView titleIcon;
	private View.OnClickListener listener;
	private String text;

	public SimpleDilaogView(Context mContext) {
		this.mContext = mContext;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.simpledilaogview_layout, null);
		btnwraper = (LinearLayout) mView.findViewById(R.id.btnwraper);
		title = (TextView) mView.findViewById(R.id.title);
		titleIcon = (ImageView) mView.findViewById(R.id.titleIcon);
	}
	/**
	 * 设置左边按钮
	 * @param text
	 * @param listener
	 */
	public void setPositiveButton(String text,
			final View.OnClickListener listener) {
		this.listener = listener;
		this.text = text;
		Button button = new Button(mContext);
		LinearLayout.LayoutParams params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		params.weight = 1.0f;
		button.setLayoutParams(params);
		button.setBackgroundResource(R.drawable.dialog_onebtn_selector);
		button.setText(text);
		button.setTextColor(mContext.getResources().getColor(
				R.color.dialog_text));
		button.setTextSize(20);
		button.setOnClickListener(listener);
		btnwraper.addView(button);
	}
	/**
	 * 设置右边按钮
	 * @param text
	 * @param listener
	 */
	public void setNegativeButton(String text,
			final View.OnClickListener listener) {
		Button button = new Button(mContext);
		LinearLayout.LayoutParams params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		params.weight = 1.0f;
		button.setLayoutParams(params);
		button.setText(text);
		button.setTextColor(mContext.getResources().getColor(
				R.color.dialog_text));
		button.setTextSize(20);
		button.setOnClickListener(listener);
		// 排列添加的按钮
		if (btnwraper.getChildCount() > 0) {
			button.setBackgroundResource(R.drawable.dialog_rightbtn_selector);
			Button btn = (Button) btnwraper.getChildAt(0);
			btnwraper.removeAllViews();
			btn.setBackgroundResource(R.drawable.dialog_leftbtn_selector);
			Log.i("btnwraper", "");
			View line = new View(mContext);
			LinearLayout.LayoutParams pms = new LayoutParams(1,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			line.setLayoutParams(pms);
			line.setBackgroundColor(mContext.getResources().getColor(
					R.color.dialog_line));
			btnwraper.addView(btn);
			btnwraper.addView(line);
			btnwraper.addView(button);
		} else {
			// 如果添加的是第一个按钮使用设置确定按钮
			setPositiveButton(text, listener);
		}
	}
	// 设置标题内容
	public void setTitle(String str) {
		title.setText(str);
	}
	// 设置标题图标
	public void setTitleIcon(int resId) {
		titleIcon.setImageResource(resId);
	}
	public View getmView() {
		return mView;
	}
}
