package com.beem.project.btf.bbs.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class CustomerViewPager extends ViewPager implements
		GestureDetector.OnGestureListener {
	private boolean fatherScrolling;
	private boolean myScrolling;
	private float xDist, yDist;
	private float mTouchSlop;

	public CustomerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN: {
				xDist = ev.getRawX();
				yDist = ev.getRawY();
				myScrolling = false;
				fatherScrolling = false;
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				if (fatherScrolling) {
					return false;
				}
				if (myScrolling) {
					return super.dispatchTouchEvent(ev);
				}
				float dx = Math.abs(ev.getRawX() - xDist);
				float dy = Math.abs(ev.getRawY() - yDist);
				if (dx > dy && dx > mTouchSlop) {
					//					//LogUtils.i("myScrolling.");
					myScrolling = true;
					getParent().requestDisallowInterceptTouchEvent(true);
				} else if (dx < dy && dy > mTouchSlop) {
					//					//LogUtils.i("fatherScrolling.");
					fatherScrolling = true;
					getParent().requestDisallowInterceptTouchEvent(false);
					return false;
				} else {
					return false;
				}
				break;
			}
			case MotionEvent.ACTION_UP: {
				getParent().requestDisallowInterceptTouchEvent(false);
				break;
			}
			default:
				break;
		}
		return super.dispatchTouchEvent(ev);
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
}
