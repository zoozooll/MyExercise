package com.iskyinfor.duoduo.ui;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PageView extends LinearLayout {
	
	private TextView textView;

	public PageView(Context context) {
		super(context);
		
		
	}
	public PageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		int pageSize = 10;
		if (pageSize >= 10) {
			for (int i = 1; i < 10; i++) {
				textView = new TextView(context);
				textView.setText(Html.fromHtml("<u>" + i + "</u>"));
				addView(textView ,new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
		                LayoutParams.WRAP_CONTENT));
			}
			
		}
	}
}
