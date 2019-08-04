package com.mogoo.components.ad.utils;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MogooAnimation
{
	/**
	 * 从左往右进入
	 */
	public static final int Left2Right = 1;
	/**
	 * 从右往左进入
	 */
	public static final int Right2Left = 2;
	/**
	 * 从下至上进入
	 */
	public static final int Bottom2Top = 3;

	/**
	 * 根据传入的参数返回动画文件
	 * @param context
	 * @param swith
	 * @return
	 */
	public static final Animation getAnimation(Context context, int swith)
	{
		Animation a = null;
		switch (swith)
		{
		case Left2Right:
			a = Left2Right(context);
			break;
		case Right2Left:
			a = Right2Left(context);
			break;
		case Bottom2Top:
			a = Bottom2Top(context);
			break;
		}
		return a;
	}

	/**
	 * 从左往右进入
	 * 
	 * @param context
	 * @return
	 */
	public static final Animation Left2Right(Context context)
	{
		Animation a = AnimationUtils.makeInAnimation(context, true);
		return a;
	}

	/**
	 * 从右往左进入
	 * 
	 * @param context
	 * @return
	 */
	public static final Animation Right2Left(Context context)
	{
		Animation a = AnimationUtils.makeInAnimation(context, false);
		return a;
	}

	/**
	 * 从下至上进入
	 * 
	 * @param context
	 * @return
	 */
	public static final Animation Bottom2Top(Context context)
	{
		Animation a = AnimationUtils.makeInChildBottomAnimation(context);
		return a;
	}
}
