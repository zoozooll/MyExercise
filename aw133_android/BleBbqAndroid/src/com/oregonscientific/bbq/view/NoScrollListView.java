package com.oregonscientific.bbq.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class NoScrollListView extends ListView
{ 
    /**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public NoScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 */
	public NoScrollListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override 
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
    { 
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, 0) ); 
 
        // here I assume that height's being calculated for one-child only, seen it in ListView's source which is actually a bad idea 
        int childHeight = getMeasuredHeight() - (getListPaddingTop() + getListPaddingBottom() +  getVerticalFadingEdgeLength() * 2); 
 
        int fullHeight = getListPaddingTop() + getListPaddingBottom() + childHeight*(getCount()); 
 
        setMeasuredDimension(getMeasuredWidth(), fullHeight); 
    } 
} 