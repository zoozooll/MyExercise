package com.oregonscientific.meep.meepopenbox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

/**
 * Linear Layout supports Text View checking
 * @author Charles
 *
 */
public class CheckableLinearLayout extends LinearLayout implements Checkable {
	
	private CheckedTextView mCheckedTextView;
	
	/**
	 * Constructor.
	 * @param context The context to operate in.
	 * @param attributeSet The attributes defined in XML for this element.
	 */
	public CheckableLinearLayout(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.widget.Checkable#isChecked()
	 */
	@Override
	public boolean isChecked() {
		return mCheckedTextView != null ? mCheckedTextView.isChecked() : false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mCheckedTextView = (CheckedTextView) findViewById(android.R.id.text1);
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.widget.Checkable#setChecked(boolean)
	 */
	@Override
	public void setChecked(boolean checked) {
		if (mCheckedTextView != null) {
			mCheckedTextView.setChecked(checked);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.widget.Checkable#toggle()
	 */
	@Override
	public void toggle() {
		if (mCheckedTextView != null) {
			mCheckedTextView.toggle();
		}
	}
}