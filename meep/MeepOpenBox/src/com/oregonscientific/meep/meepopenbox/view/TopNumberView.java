package com.oregonscientific.meep.meepopenbox.view;

import com.oregonscientific.meep.meepopenbox.R;
import com.oregonscientific.meep.widget.OutlinedTextView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class TopNumberView extends LinearLayout{
	
	public TopNumberView(Context context) {
		this(context, null);
	}
	
	public TopNumberView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public TopNumberView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TopNumber);
		String text = typedArray.getString(R.styleable.TopNumber_number);
		typedArray.recycle();
		//add layout to this view
		View.inflate(getContext(), R.layout.number_button, this);
		OutlinedTextView textView = (OutlinedTextView) findViewById(R.id.text);
		textView.setText(text);
		
	}
	

	
}
