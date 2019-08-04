/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.oregonscientific.meep.home.R;

/**
 * This is a subclass of MenuLargeItem for construct the header item view
 */
public class HeaderMenuItem extends MenuItem2 {
	
	private Drawable mOpenedDrawable;
	private Drawable mClosedDrawable;
	private int mPanelHandleId;
	private View mPanelHandle;
	
	public HeaderMenuItem(Context context) {
		this(context, null);
	}
	
	public HeaderMenuItem(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public HeaderMenuItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		// get value of property from layout xml
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MenuHeader);
		mPanelHandleId = a.getResourceId(R.styleable.MenuHeader_panelHandle, 0);
		mOpenedDrawable = a.getDrawable(R.styleable.MenuHeader_openedDrawable);
		mClosedDrawable = a.getDrawable(R.styleable.MenuHeader_closedDrawable);
		a.recycle();
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		if (mPanelHandleId != 0) {
			mPanelHandle = findViewById(mPanelHandleId);
		}
	}

}
