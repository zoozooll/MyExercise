/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * A subclass of ScrollView and define a interface - OnScrollChangedListener for 
 * others class to implement callback when ScrollView changed
 */
public class FadingScrollView extends ScrollView {

	private OnScrollChangedListener mListener;
	
	/**
	 * a interface when the scroll the view
	 */
	public interface OnScrollChangedListener {
		public void onScrollChanged(ScrollView scrollView, int l, int t, int oldl, int oldt);
	}
	
	public FadingScrollView(Context context) {
		this(context, null);
	}
	
	public FadingScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public FadingScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setFadingEdgeLength(0);
		setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
		setLayerType(LAYER_TYPE_HARDWARE, null);
		setSmoothScrollingEnabled(true);
	}
	
	@Override
	public void onScrollChanged(int l, int t, int oldl, int oldt) {
		if (mListener != null) {
			mListener.onScrollChanged(this, l, t, oldl, oldt);
		}

		super.onScrollChanged(l, t, oldl, oldt);
	}

	/**
	 * Register a callback to be invoked when this view is scrolled
	 * 
	 * @param onScrollChangedListener the callback that will be invoked
	 */
	public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
		mListener = onScrollChangedListener;
	}
	

}
