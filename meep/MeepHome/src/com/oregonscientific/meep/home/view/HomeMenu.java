/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.oregonscientific.meep.home.R;

/**
 * The menu is the root view of menu in {@link HomeActivity}. This menu displays the list
 * of application packages available on MEEP
 */
public class HomeMenu extends LinearLayout{
			
	public HomeMenu(Context context) {
		super(context);
		initLayout(); 
	}
	
	public HomeMenu(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		initLayout(); 
	}
	
	public void onFinishInflate() {
		super.onFinishInflate();

		View leftBottomView = findViewById(R.id.left_menu_bottom_bg);
		if (leftBottomView != null) {
			leftBottomView.bringToFront();
		}
		
		View rightBottomView = findViewById(R.id.right_menu_bottom_bg);
		if (rightBottomView != null) {
			rightBottomView.bringToFront();
		}
	}

	/**
	 * Initialize the layout property
	 */
	private void initLayout() {
		View.inflate(getContext(), R.layout.home_menu, this);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
}
