/**
 * 
 */
package com.idt.bw.view;

import android.content.Context;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ScrollView;

/**
 * @author aaronli
 *
 */
public class CustomScrollView extends ScrollView {

	private boolean forceInterceptTouchEnable;

	

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public CustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 */
	public CustomScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.widget.ScrollView#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//Log.d("onTouchEvent	", "MotionEvent");
		return super.onTouchEvent(ev);
	}

	/* (non-Javadoc)
	 * @see android.widget.ScrollView#requestDisallowInterceptTouchEvent(boolean)
	 */
	@Override
	public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
		// TODO Auto-generated method stub
		super.requestDisallowInterceptTouchEvent(disallowIntercept);
	}

	/* (non-Javadoc)
	 * @see android.widget.ScrollView#onInitializeAccessibilityEvent(android.view.accessibility.AccessibilityEvent)
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		/*if (forceInterceptTouchEnable)
			return false;
		else {
			//return super.onInterceptTouchEvent(ev);
			return false;
		}*/
		return false;
	}
	 
	/**
	 * @param forceInterceptTouchEnable the forceInterceptTouchEnable to set
	 */
	public void setForceInterceptTouchEnable(boolean forceInterceptTouchEnable) {
		this.forceInterceptTouchEnable = forceInterceptTouchEnable;
	}

}
