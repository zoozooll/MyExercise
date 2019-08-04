/**
 * Copyright (C) 2013 IDT International Ltd.
 * 
 */
package com.oregonscientific.meep.home.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import com.oregonscientific.meep.home.R;


/**
 * This is the handle for opening and closing the {@link DrawerPanel}
 */
public class DrawerPanelHandle extends RelativeLayout {

	private Drawable mOpenedDrawable;
	private Drawable mClosedDrawable;
	private View mPanelHandle;
	
	public DrawerPanelHandle(Context context) {
		this(context, null);
	}
	
	public DrawerPanelHandle(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public DrawerPanelHandle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MenuHeader);
		mOpenedDrawable = a.getDrawable(R.styleable.MenuHeader_openedDrawable);
		mClosedDrawable = a.getDrawable(R.styleable.MenuHeader_closedDrawable);
		a.recycle();
		
		View.inflate(getContext(), R.layout.drawer_panel_handle, this);
		mPanelHandle = findViewById(R.id.rightPanelHandle);
	}

}
