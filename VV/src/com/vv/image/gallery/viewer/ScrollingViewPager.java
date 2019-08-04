package com.vv.image.gallery.viewer;

import com.butterfly.vv.view.BlockHolder;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * 自定义viewPager
 * @author shuangshuang.li
 */
public class ScrollingViewPager extends ViewPager implements BlockHolder {
	private OnPageChangeListener listener;
	private boolean isCanScroll = true;

	public ScrollingViewPager(Context context) {
		super(context);
	}
	public ScrollingViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		super.setOnPageChangeListener(mOnPageChangeListener);
	}
	public void setScanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}
	@Override
	public void scrollTo(int x, int y) {
		if (isCanScroll) {
			super.scrollTo(x, y);
		}
	}
	@Override
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		this.listener = listener;
	}

	private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageScrollStateChanged(int arg0) {
			if (isCanScroll && listener != null) {
				listener.onPageScrollStateChanged(arg0);
			}
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if (isCanScroll && listener != null) {
				listener.onPageScrolled(arg0, arg1, arg2);
			}
		}
		@Override
		public void onPageSelected(int arg0) {
			if (isCanScroll && listener != null) {
				listener.onPageSelected(arg0);
			}
		}
	};

	@Override
	public void setBlocked(boolean enable) {
		isCanScroll = enable;
	}
}
