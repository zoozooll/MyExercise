package com.beem.project.btf.bbs.view;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.ListView;

/**
 * @author yuedong bao
 * @func listview内置ViewPager特殊定制处理
 */
public class CustomListViewForViewPager extends ListView {
	private GestureDetector mGestureDetector;

	public CustomListViewForViewPager(Context context) {
		super(context);
	}
	public CustomListViewForViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(new YScrollDetector());
		setFadingEdgeLength(0);
		setCacheColorHint(R.color.transparent);
		setScrollingCacheEnabled(false);
	}
	public CustomListViewForViewPager(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev)
				&& mGestureDetector.onTouchEvent(ev);
	}

	class YScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (distanceY != 0 && distanceX != 0) {
			}
			if (Math.abs(distanceY) >= Math.abs(distanceX)) {
				return true;
			}
			return false;
		}
	}
}
