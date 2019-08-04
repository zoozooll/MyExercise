package com.beem.project.btf.bbs.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;

public class CutomerMyEditText extends EditText implements
		OnFocusChangeListener {
	String hint;

	public CutomerMyEditText(Context context) {
		super(context);
		init();
	}
	public CutomerMyEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	public CutomerMyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public void init() {
		setOnFocusChangeListener(this);
		if (TextUtils.isEmpty(getText().toString().trim())) {
		} else {
			setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
		}
	}
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			toggleSoftInput(true);
		} else {
		}
	}
	// 显隐软键盘
	public void toggleSoftInput(boolean isShow) {
		InputMethodManager imm = (InputMethodManager) getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (isShow) {
			// Toast.makeText(getContext(), "显示键盘", Toast.LENGTH_SHORT).show();
			imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
			imm.showSoftInputFromInputMethod(getWindowToken(), 0);
		} else {
			// Toast.makeText(getContext(), "隐藏键盘", Toast.LENGTH_SHORT).show();
			imm.hideSoftInputFromInputMethod(getWindowToken(), 0);
		}
	}
}
