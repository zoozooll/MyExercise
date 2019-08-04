package com.beem.project.btf.bbs.view;

import com.beem.project.btf.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * @author sunday 2013-12-04 带删除按钮的edittext
 */
public class ClearEditText2 extends EditText {
	private final static String TAG = "CutomerEditText";
	private Drawable imgleft;
	private Drawable imgright;
	private Context mContext;
	private IsemptyListener isemptyListener;

	public ClearEditText2(Context context) {
		super(context);
		mContext = context;
		init();
	}
	public ClearEditText2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}
	public ClearEditText2(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}
	private void init() {
		imgright = mContext.getResources().getDrawable(
				R.drawable.delete_icon_normal2);
		imgleft = mContext.getResources().getDrawable(
				R.drawable.friend_find_normal);
		setCompoundDrawablePadding(10);
		addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				setDrawable();
			}
		});
		setDrawable();
	}
	// 设置删除图片
	private void setDrawable() {
		if (length() < 1) {
			// 左下右上
			setCompoundDrawablesWithIntrinsicBounds(imgleft, null, null, null);
			if (isemptyListener != null) {
				isemptyListener.isempty(false);
			}
		} else {
			setCompoundDrawablesWithIntrinsicBounds(imgleft, null, imgright,
					null);
			if (isemptyListener != null) {
				isemptyListener.isempty(true);
			}
		}
	}
	// 处理删除事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (imgright != null && event.getAction() == MotionEvent.ACTION_UP) {
			/*
			 * int eventX = (int) event.getRawX(); int eventY = (int) event.getRawY(); Log.e(TAG,
			 * "eventX = " + eventX + "; eventY = " + eventY); Rect rect = new Rect();
			 * getGlobalVisibleRect(rect); //重设矩形区域 rect.left = rect.right -
			 * imgAble.getIntrinsicWidth(); if (rect.contains(eventX, eventY)) setText("");
			 */
			// 上面注释掉的代码在弹出框中失效改用下面的计算方法
			/**
			 * 当手指抬起的位置在clean的图标的区域 我们将此视为进行清除操作 getWidth():得到控件的宽度
			 * event.getX():抬起时的坐标(改坐标是相对于控件本身而言的) getTotalPaddingRight():clean的图标左边缘至控件右边缘的距离
			 * getPaddingRight():clean的图标右边缘至控件右边缘的距离 于是: getWidth() - getTotalPaddingRight()表示:
			 * 控件左边到clean的图标左边缘的区域 getWidth() - getPaddingRight()表示: 控件左边到clean的图标右边缘的区域
			 * 所以这两者之间的区域刚好是clean的图标的区域
			 */
			boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
					&& (event.getX() < ((getWidth() - getPaddingRight())));
			if (touchable) {
				setText("");
			}
		}
		return super.onTouchEvent(event);
	}
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	// 处理外界随着输入框字数变化处理事物的接口
	public interface IsemptyListener {
		public void isempty(boolean staus);
	}

	public IsemptyListener getIsemptyListener() {
		return isemptyListener;
	}
	public void setIsemptyListener(IsemptyListener isemptyListener) {
		this.isemptyListener = isemptyListener;
	}
}
