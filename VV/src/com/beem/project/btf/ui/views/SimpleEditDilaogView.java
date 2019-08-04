package com.beem.project.btf.ui.views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beem.project.btf.R;
import com.beem.project.btf.utils.DimenUtils;

public class SimpleEditDilaogView {
	private Context mContext;
	private View mView;
	private LinearLayout editwraper;
	private RelativeLayout EditDilaogWraper;
	private TextView title, textcontent;
	private BtnListener btnListener;
	private LinearLayout btngroup;
	private View line;

	public SimpleEditDilaogView(Context mContext) {
		this.mContext = mContext;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.simple_edit_dilaogview_layout, null);
		editwraper = (LinearLayout) mView.findViewById(R.id.editwraper);
		btngroup = (LinearLayout) mView.findViewById(R.id.btngroup);
		EditDilaogWraper = (RelativeLayout) mView
				.findViewById(R.id.EditDilaogWraper);
		title = (TextView) mView.findViewById(R.id.title);
		line = mView.findViewById(R.id.line);
		textcontent = (TextView) mView.findViewById(R.id.textcontent);
		textcontent.setVisibility(View.GONE);
	}
	// 设置标题
	public void setTitle(String str) {
		title.setText(str);
	}
	// 设置提示内容
	public void setTextContent(String str) {
		textcontent.setText(str);
		textcontent.setVisibility(View.VISIBLE);
		setMargin();
	}
	// 返回布局对象
	public View getmView() {
		return mView;
	}
	// 提供设置对话框内容部分的方法
	public void setContentView(View child) {
		editwraper.addView(child);
	}
	// 设置内容容器的上下margin
	public void setMargin() {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, DimenUtils.dip2px(mContext, 10), 0,
				DimenUtils.dip2px(mContext, 10));
		editwraper.setLayoutParams(lp);
	}
	// 设置内容容器的上margin
	public void setTopMargin() {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, DimenUtils.dip2px(mContext, 10), 0, 0);
		editwraper.setLayoutParams(lp);
	}
	// 设置内容容器的下margin
	public void setBottomMargin() {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 0, 0, DimenUtils.dip2px(mContext, 10));
		editwraper.setLayoutParams(lp);
	}
	// 设置内容容器的padding值
	public void setEditPadding() {
		editwraper.setPadding(0, 0, 0, 0);
	}
	// 设置整个View的padding
	public void setNoPadding() {
		EditDilaogWraper.setPadding(0, 0, 0, 0);
	}
	// 设置标题隐藏
	public void setTitleGone() {
		title.setVisibility(View.GONE);
	}
	// 设置按钮的隐藏
	public void setBtnGone() {
		btngroup.setVisibility(View.GONE);
		line.setVisibility(View.GONE);
	}

	// 对外提供两个按钮的事务处理接口
	public interface BtnListener {
		public void ensure(View contentView);
	}

	public BtnListener getBtnListener() {
		return btnListener;
	}
	public void setBtnListener(BtnListener btnListener) {
		this.btnListener = btnListener;
	}
	/**
	 * 设置左边按钮
	 * @param text
	 * @param listener
	 */
	public void setPositiveButton(String text, final BtnListener listener) {
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
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				if (listener != null) {
					listener.ensure(mView);
				}
			}
		});
		btngroup.addView(button);
	}
	/**
	 * 设置右边按钮
	 * @param text
	 * @param listener
	 */
	public void setNegativeButton(String text, final BtnListener listener) {
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
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				if (listener != null) {
					listener.ensure(mView);
				}
			}
		});
		// 排列添加的按钮
		if (btngroup.getChildCount() > 0) {
			button.setBackgroundResource(R.drawable.dialog_rightbtn_selector);
			Button btn = (Button) btngroup.getChildAt(0);
			btngroup.removeAllViews();
			btn.setBackgroundResource(R.drawable.dialog_leftbtn_selector);
			Log.i("btnwraper", "");
			View line = new View(mContext);
			LinearLayout.LayoutParams pms = new LayoutParams(1,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			line.setLayoutParams(pms);
			line.setBackgroundColor(mContext.getResources().getColor(
					R.color.dialog_line));
			btngroup.addView(btn);
			btngroup.addView(line);
			btngroup.addView(button);
		} else {
			// 如果添加的是第一个按钮使用设置确定按钮
			setPositiveButton(text, listener);
		}
	}
}
