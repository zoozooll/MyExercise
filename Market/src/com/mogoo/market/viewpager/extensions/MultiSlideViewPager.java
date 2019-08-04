package com.mogoo.market.viewpager.extensions;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MultiSlideViewPager extends ViewPager {

	private View mSlideView;
	private int mSlideViewIndexOfPager = -1;
	
	public MultiSlideViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MultiSlideViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		try {
			if (getCurrentItem() == mSlideViewIndexOfPager
					&& mSlideView != null) {
				int x = (int) event.getX();
				int y = (int) event.getY();
				int[] location = new int[2];
				int width = mSlideView.getWidth();
				int height = mSlideView.getHeight();
				mSlideView.getLocationOnScreen(location);

				if (mSlideView != null && location[0] < x
						&& x < location[0] + width && location[1] < y
						&& y < location[1] + height) {
					return false;
				}
			}
			return super.onInterceptTouchEvent(event);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
			// android自身bug，在屏幕上进行多点快速滑动的时候会抛出这个异常.
			// ignore it.
		} catch (ArrayIndexOutOfBoundsException ex) {
			// android自身bug，在屏幕上进行多点快速滑动的时候会抛出这个异常.
			// ignore it.
			ex.printStackTrace();
			return false;
		}
	}
	
	public void setSlideView(int slideViewIndexOfPager, View slidView) {
		this.mSlideViewIndexOfPager  = slideViewIndexOfPager;
		this.mSlideView = slidView;
	}
}
