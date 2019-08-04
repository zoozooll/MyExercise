package com.mogoo.components.ad;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * 获取不同的广告layout
 *
 * @author Administrator
 *
 */
class AdLayoutViewCreator
{

	private static final String tag = "AdLayoutViewCreator";
	/**
	 * 单行，不可滑动
	 */
	public static final int singleNotSlide = 1;
	/**
	 * 单行，可滑动
	 */
	public static final int singleSlide = 2;
	/**
	 * 双行，不可滑动
	 */
	public static final int doubleNotSlide = 4;
	/**
	 * 双行，可滑动
	 */
	public static final int doubleSlide = 5;

	private static class InstanceHolder
	{
		public static final MogooLayoutParent getDoubleNotSlideView(
				Context context)
		{
			Log.e(tag, "getDoubleNotSlideView()");
			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.FILL_PARENT, MogooInfo.adHeight);
			return new DoubleRowNotSlideView(context, layoutParams);
		}

		public static final MogooLayoutParent getDoubleRowSlideLayout(
				Context context)
		{
			MogooInfo.Log(tag, "getDoubleRowSlideLayout()");

			LayoutParams localLayoutParams = new LayoutParams(
					LayoutParams.FILL_PARENT, MogooInfo.adHeight);

			return new DoubleRowSlideLayout(context, localLayoutParams);
		}

		public static final MogooLayoutParent getSingleRowNotSlideLayout(
				Context context)
		{
			MogooInfo.Log(tag, "getSingleRowNotSlideLayout()");

			LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, MogooInfo.adHeight);

			return new SingleRowAdLayout(context, localLayoutParams);
		}

	}

	public static MogooLayoutParent getAdLayoutView(Context context, int type)
	{
		MogooLayoutParent v = null;
		switch (type)
		{
		case singleNotSlide:
			v = InstanceHolder.getSingleRowNotSlideLayout(context);
			break;
		case singleSlide:
			break;
		case doubleNotSlide:
			v = InstanceHolder.getDoubleNotSlideView(context);
			break;
		case doubleSlide:
			v = InstanceHolder.getDoubleRowSlideLayout(context);
			break;

		}
		return v;
	}

}
