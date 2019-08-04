package com.mogoo.market.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mogoo.market.R;

/**
 * @author csq 标题栏：高度需要自己指定
 */
public class TitleBar extends LinearLayout {
	public static final int TITLE_LAYOUT = R.id.titleBarLayout;
	public static final int MIDDLE_TEXT = R.id.textView;
	public static final int LEFT_BUTTON = R.id.leftBtn;
	public static final int RIGHT_BUTTON = R.id.rightBtn;

	public TextView midTextView;
	public Button leftBtn, rightBtn;
	public RelativeLayout mainLayout;

	public TitleBar(Context context) {
		super(context);
		View view = LayoutInflater.from(context).inflate(R.layout.title_bar,
				this, true);

		mainLayout = (RelativeLayout) view.findViewById(TITLE_LAYOUT);

		midTextView = (TextView) view.findViewById(MIDDLE_TEXT);
		leftBtn = (Button) view.findViewById(LEFT_BUTTON); // 左边图片按钮,如果没有文字，需要将text设置为空，再改变背景
		rightBtn = (Button) view.findViewById(RIGHT_BUTTON);
	}

	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = LayoutInflater.from(context).inflate(R.layout.title_bar,
				this, true);

		mainLayout = (RelativeLayout) view.findViewById(TITLE_LAYOUT);

		midTextView = (TextView) view.findViewById(MIDDLE_TEXT);
		leftBtn = (Button) view.findViewById(LEFT_BUTTON); // 左边图片按钮,如果没有文字，需要将text设置为空，再改变背景
		rightBtn = (Button) view.findViewById(RIGHT_BUTTON);
	}

	public void setMidText(CharSequence text) {
		midTextView.setText(text);
	}

	public void setLeftBtnText(CharSequence text) {
		leftBtn.setText(text);
	}

	public void setRightBtnText(CharSequence text) {
		rightBtn.setText(text);
	}

	public void setMidText(int resid) {
		midTextView.setText(resid);
	}

	public void setLeftBtnText(int resid) {
		leftBtn.setText(resid);
	}

	public void setRightBtnText(int resid) {
		rightBtn.setText(resid);
	}

	public void setBackground(Drawable drawable) {
		mainLayout.setBackgroundDrawable(drawable);
	}

	public void setBackground(int resId) {
		mainLayout.setBackgroundResource(resId);
	}

	public void setLeftButtonBackground(Drawable drawable) {
		leftBtn.setBackgroundDrawable(drawable);
	}

	public void setLeftButtonBackground(int resId) {
		leftBtn.setBackgroundResource(resId);
	}

	public void setRightButtonBackground(Drawable drawable) {
		rightBtn.setBackgroundDrawable(drawable);
	}

	public void setRightButtonBackground(int resId) {
		rightBtn.setBackgroundResource(resId);
	}

}
