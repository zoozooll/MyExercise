package com.oregonscientific.meep.widget;

import com.oregonscientific.meep.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class DialogButton extends RelativeLayout {

	public DialogButton(Context context) {
		this(context, null);
	}
	
	public DialogButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public DialogButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		View v = View.inflate(getContext(), R.layout.dialog_button, null);
		addView(v);
	}
	
	/**
	 * Sets the string to be displayed in the button. 
	 * 
	 * @param text the button label to be displayed
	 */
	public void setText(String text) {
		StrokeTextView textView = (StrokeTextView) findViewById(R.id.text);
		if (textView != null) {
			textView.setText(text);
		}
	}
}
