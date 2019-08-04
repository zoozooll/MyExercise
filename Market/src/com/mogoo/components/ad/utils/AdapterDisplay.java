package com.mogoo.components.ad.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

public class AdapterDisplay
{

	static int displayWidht = 320;
	static int displayheight = 480;

	private static int getAdViewHeight(int paramInt)
	{
		int height = paramInt;
		if (height <= 240)
			return 36;
		if (height <= 320)
			return 40;
		if (height <= 480)
			return 50;
		if (height <= 960)
			return 74;
		return 60;
	}

	/**
	 * 获取单行展示区域的默认高度
	 * @param paramContext
	 * @return
	 */
	public static int getSingleRowAdHeight(Context paramContext)
	{
		int screenHeight = getHeight(paramContext);
		return getAdViewHeight(screenHeight);
	}

	/**
	 * 获取双行展示区域的默认高度
	 * 
	 * @param context
	 *            Context
	 * @param disRow
	 *            两行之间的距离
	 * @return
	 */
	public static int getDoubleRowAdHeight(Context context, int disRow)
	{
		int height = 60;
		int screenHeight = getHeight(context);
		if (screenHeight <= 320)
		{
			height = 40 + disRow + 40;
		} else if (screenHeight <= 480)
		{
			height = 82 + disRow + 82;
		} else if (screenHeight <= 854)
		{
			height = 103 + disRow + 103;
		} else if (screenHeight <= 960)
		{
			height = 153 + disRow + 153;
		} else
		{
			height = 163 + disRow + 163;
		}
		return height;
	}

	/**
	 * 获取设备的实际分辨率：宽度
	 * 
	 * @param paramContext
	 * @return
	 */
	public static int getWidth(Context paramContext)
	{
		Activity localActivity = (Activity) paramContext;
		if (localActivity != null)
		{
			DisplayMetrics localDisplayMetrics = new DisplayMetrics();
			localActivity.getWindowManager().getDefaultDisplay()
					.getMetrics(localDisplayMetrics);
			displayWidht = localDisplayMetrics.widthPixels;
			Log.d("AdapterDisplay", "++++++displayWidht=" + displayWidht);
		}
		return displayWidht;

	}

	/**
	 * 获取设备的实际分辨率：高度（包括标题栏高度在内）
	 * 
	 * @param paramContext
	 * @return
	 */
	public static int getHeight(Context paramContext)
	{
		Activity localActivity = (Activity) paramContext;
		if (localActivity != null)
		{
			DisplayMetrics localDisplayMetrics = new DisplayMetrics();
			localActivity.getWindowManager().getDefaultDisplay()
					.getMetrics(localDisplayMetrics);
			displayheight = localDisplayMetrics.heightPixels;
			Log.d("AdapterDisplay", "++++++displayheight=" + displayheight);
		}
		return displayheight;
	}

}
