package com.beem.project.btf.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * 增加布局大小改变的监听
 */
public class ResizeLayout extends LinearLayout {
	private OnResizeListener mListener;

	public interface OnResizeListener {
		void OnResize(int w, int h, int oldw, int oldh);
	}

	public void setOnResizeListener(OnResizeListener l) {
		mListener = l;
	}
	public ResizeLayout(Context context) {
		super(context);
	}
	public ResizeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (mListener != null) {
			mListener.OnResize(w, h, oldw, oldh);
		}
		Log.i("ResizeLayout", "w= " + w + " h = " + h + " oldw = " + oldw
				+ " oldh = " + oldh);
	}
}
