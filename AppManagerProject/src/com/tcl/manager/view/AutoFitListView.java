package com.tcl.manager.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
/**
 * 
 * @author wenchao.zhang
 *
 */
public class AutoFitListView extends ListView {

	public AutoFitListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public AutoFitListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public AutoFitListView(Context context) {
		super(context);
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
