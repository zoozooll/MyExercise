package com.mogoo.components.ad;

import android.content.Context;

/**
 * 分两行两列显示的view， 不可滑动
 * 
 * @author Administrator
 * 
 */
class DoubleRowNotSlideView extends MogooLayoutParent
{
	private static final String tag = "DoubleNotSlideView";
	private DoubleRowView doubleRowView;

	public DoubleRowNotSlideView(Context context, LayoutParams layoutParams)
	{
		super(context, layoutParams);
		doubleRowView = new DoubleRowView(context);
		this.addView(doubleRowView);
	}

	public void updateUi()
	{
		doubleRowView.setAdOnClickListener(mListener);
		doubleRowView.setAdData(AdDataCache.adPositionItemList);
		invalidate();
	}

}
