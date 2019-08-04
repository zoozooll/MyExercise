/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home.view;

import com.oregonscientific.meep.home.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * A view for showing a set of circle as indicator for display the sequence of pages
 * @author joyaether
 *
 */
public class CircleIndicator extends LinearLayout{
	
	private int count;
	private int currentIndex = 0;

	public CircleIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public CircleIndicator(Context context) {
		super(context);
		initLayout();
	}
	
	@Override
	public void onFinishInflate() {
		super.onFinishInflate();
		initLayout();
	}
	
	/**
	 * create the view with count
	 */
	private void initLayout() {
		removeAllViews();
		int count = getCount();
		for (int i = 0; i< count; i++) {
			Button button = new Button(getContext());
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = 1;
			layoutParams.rightMargin = 1;
			button.setBackgroundResource(R.drawable.rounded_cell);
			button.setTag(i+"");
			button.setWidth(15);
			button.setTextColor(Color.BLACK);
			button.setHeight(15);
			addView(button, layoutParams);
		}	
		updateButtons();
	}
	
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
		initLayout();
	}


	/**
	 * @return the currentIndex
	 */
	public int getCurrentIndex() {
		return currentIndex;
	}


	/**
	 * @param currentIndex the currentIndex to set
	 */
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
		updateButtons();
	}
	
	/**
	 * move to next circle indicator
	 */
	public void next() {
		//can not move to next index with last circle indicator
		if (getCurrentIndex() == getCount() - 1) {
			return;
		}
		setCurrentIndex(getCurrentIndex() + 1);
		updateButtons();	
	}
	
	/**
	 * move to previous circle indicator
	 */
	public void previous() {
		if (getCurrentIndex() == 0) {
			return;
		}
		setCurrentIndex(getCurrentIndex() - 1);
		updateButtons();
	}
	
	/**
	 * change the button view with the state when change page
	 */
	public void updateButtons() {
		showCurrentIndex();
		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);
			if (i != getCurrentIndex() && v instanceof Button) {
				if (v != null) {
					resetButton((Button) v);
				}
			}
		}
	}
	
	/**
	 * set the button current index number with circle indicator opaque and larger than others 
	 */
	public void showCurrentIndex() {
		Button button = (Button) findViewWithTag(getCurrentIndex()+"");
		if (button == null) {
			return;
		}
		button.setWidth(20);
		button.setHeight(20);
		button.setAlpha(1);
//		invalidate();
	}
	
	/**
	 * reset the default property with button 
	 * @param button the button of need to reset
	 */
	public void resetButton(Button button) {
		button.setWidth(15);
		button.setAlpha(0.5f);
		button.setHeight(15);
	}
	
	

}
