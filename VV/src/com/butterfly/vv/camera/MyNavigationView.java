package com.butterfly.vv.camera;

import com.beem.project.btf.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyNavigationView extends LinearLayout {
	private String TAG = "MyNavigationView";
	private Context mContext;
	private Button btn_left;
	private Button btn_right;
	private TextView titleText;
	private String strBtnLeft;
	private String strBtnRight;
	private String strTitle;
	private int left_drawable;
	private int right_drawable;
	public boolean mIsChoiceMode = false;
	private int mViewHeight;
	private int mTitleTextTopGap;
	private int mBtnTextTopGap;

	public MyNavigationView(Context context) {
		super(context);
		mContext = context;
		initContent();
	}
	public MyNavigationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initAttributes(attrs);
		initContent();
	}
	private void initAttributes(AttributeSet attributeSet) {
		if (null != attributeSet) {
			final int attrIds[] = new int[] { R.attr.btn_leftText,
					R.attr.btn_rightText, R.attr.tv_title,
					R.attr.left_drawable, R.attr.right_drawable };
			Context context = getContext();
			TypedArray array = context.obtainStyledAttributes(attributeSet,
					attrIds);
			CharSequence t1 = array.getText(0);
			CharSequence t2 = array.getText(1);
			CharSequence t3 = array.getText(2);
			left_drawable = array.getResourceId(3, 0);
			right_drawable = array.getResourceId(4, 0);
			array.recycle();
			if (null != t1) {
				strBtnLeft = t1.toString();
			}
			if (null != t2) {
				strBtnRight = t2.toString();
			}
			if (null != t3) {
				strTitle = t3.toString();
			}
		}
	}
	private void initContent() {
		/*
		 * mViewHeight = getHeight(); ViewGroup.LayoutParams params = getLayoutParams(); if (params
		 * != null) { mViewHeight = params.height; }
		 */
		mViewHeight = (int) mContext.getResources().getDimension(
				R.dimen.common_title_bar_height);
		mTitleTextTopGap = Utils.DipToPx(10, mContext);
		mBtnTextTopGap = Utils.DipToPx(12, mContext);
		setOrientation(HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);
		// 设置背景
		setBackgroundResource(R.color.new_title_bg);
		Context context = getContext();
		btn_left = new Button(context);
		btn_right = new Button(context);
		updateBtnContent();
		addView(btn_left);
		titleText = new TextView(context);
		updateTitleContent();
		addView(titleText);
		addView(btn_right);
	}
	public void setChoiceMode(boolean mode, String titleStr) {
		float btnTextSize = mViewHeight - mBtnTextTopGap * 2;
		float titleTextSize = mViewHeight - mTitleTextTopGap * 2;
		mIsChoiceMode = mode;
		if (mode) {
			titleText.setText(titleStr);
			titleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
			titleText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		} else {
			titleText.setText(titleStr);
			titleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
			titleText.setGravity(Gravity.CENTER);
		}
	}
	public boolean getChoiceMode() {
		return mIsChoiceMode;
	}
	public void setBtnLeftListener(OnClickListener listener) {
		btn_left.setOnClickListener(listener);
	}
	public void setBtnRightListener(OnClickListener listener) {
		btn_right.setOnClickListener(listener);
	}
	public Button getBtn_left() {
		return btn_left;
	}
	public Button getBtn_right() {
		return btn_right;
	}
	public TextView getTitleText() {
		return titleText;
	}
	public String getStrBtnLeft() {
		return strBtnLeft;
	}
	public void setStrBtnLeft(String strBtnLeft) {
		this.strBtnLeft = strBtnLeft;
		updateBtnContent();
	}
	public String getStrBtnRight() {
		return strBtnRight;
	}
	public void setStrBtnRight(String strBtnRight) {
		this.strBtnRight = strBtnRight;
		updateBtnContent();
	}
	public String getStrTitle() {
		return strTitle;
	}
	public void setStrTitle(String strTitle) {
		this.strTitle = strTitle;
		if (titleText != null) {
			titleText.setText(strTitle);
		}
	}
	public int getLeft_drawable() {
		return left_drawable;
	}
	public void setLeft_drawable(int left_drawable) {
		this.left_drawable = left_drawable;
		updateBtnContent();
	}
	public int getRight_drawable() {
		return right_drawable;
	}
	public void setRight_drawable(int right_drawable) {
		this.right_drawable = right_drawable;
		updateBtnContent();
	}
	private void updateTitleContent() {
		float titleTextSize = mViewHeight - mTitleTextTopGap * 2;
		Log.d(TAG, "updateTitleContent, titleTextSize = " + titleTextSize);
		LayoutParams centerParam = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		centerParam.weight = 1;
		titleText.setLayoutParams(centerParam);
		titleText.setTextColor(Color.WHITE);
		titleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
		if (null != strTitle) {
			titleText.setText(strTitle);
		}
		titleText.setGravity(Gravity.CENTER);
		btn_left.setVisibility(View.VISIBLE);
	}
	@SuppressWarnings("deprecation")
	private void updateBtnContent() {
		// int btnLWidth = Utils.DipToPx(32, mContext);
		// int btnLHeight = Utils.DipToPx(32, mContext);
		// int btnRWidth = Utils.DipToPx(30, mContext);
		// int btnRHeight = Utils.DipToPx(26, mContext);
		int marginLeftRight = Utils.DipToPx(10, mContext);
		float btnTextSize = mViewHeight - mBtnTextTopGap * 2;
		if (left_drawable != 0) {
			btn_left.setBackgroundResource(left_drawable);
		} else {
			btn_left.setBackgroundDrawable(mContext.getResources().getDrawable(
					R.color.new_title_bg));
		}
		btn_left.setTextColor(Color.WHITE);// 字体颜色
		if (null != strBtnLeft) {
			LayoutParams btnLeftParams = new LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			btnLeftParams.setMargins(marginLeftRight, 0, 0, 0);
			btn_left.setLayoutParams(btnLeftParams);
			btn_left.setText(strBtnLeft);
			btn_left.setPadding(6, 0, 6, 0);
			btn_left.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
			btn_left.setVisibility(View.VISIBLE);
		} else {
			// Log.v(TAG,"===initContent=22===="+left_drawable);
			LayoutParams btnLeftParams = new LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			btnLeftParams.setMargins(marginLeftRight, 0, 0, 0);
			btn_left.setPadding(0, 0, 0, 0);
			btn_left.setText(null);
			btn_left.setLayoutParams(btnLeftParams);
		}
		btn_left.setVisibility(View.VISIBLE);
		btn_right.setTextColor(Color.WHITE);// 字体颜色
		if (right_drawable != 0) {
			btn_right.setBackgroundResource(right_drawable);
		} else {
			btn_right.setBackgroundDrawable(mContext.getResources()
					.getDrawable(R.color.new_title_bg));
		}
		if (null != strBtnRight) {
			LayoutParams btnRightParams = new LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			btnRightParams.setMargins(0, 0, marginLeftRight, 0);
			btnRightParams.gravity = Gravity.CENTER_VERTICAL;
			btn_right.setGravity(Gravity.CENTER_VERTICAL);
			btn_right.setLayoutParams(btnRightParams);
			btn_right.setPadding(0, 0, 0, 0);
			btn_right.setText(strBtnRight);
			btn_right.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
			btn_right.setVisibility(View.VISIBLE);
		} else {
			LayoutParams btnRightParams = new LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			btnRightParams.setMargins(0, 0, marginLeftRight, 0);
			btn_right.setPadding(0, 0, 0, 0);
			btn_right.setText(null);
			btn_right.setLayoutParams(btnRightParams);
		}
	}
}
