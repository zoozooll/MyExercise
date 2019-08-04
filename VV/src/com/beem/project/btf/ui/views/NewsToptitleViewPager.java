package com.beem.project.btf.ui.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/** 控制viewpager是否可以手动滑动 */
public class NewsToptitleViewPager extends ViewPager {
	private boolean isCanScroll = true;

	public NewsToptitleViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public NewsToptitleViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public void setCanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if (isCanScroll) {
			return super.onTouchEvent(arg0);
		} else {
			return false;
		}
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if (isCanScroll) {
			return super.onInterceptTouchEvent(arg0);
		} else {
			return false;
		}
	}
}
