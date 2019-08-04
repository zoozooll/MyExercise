package com.beem.project.btf.ui.views.SingleTouchView;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author le yang 只计算某些子view的宽高，并包裹
 */
public class StaticWrapLayout extends FrameLayout {
	public StaticWrapLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	public StaticWrapLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public StaticWrapLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		/**
		 * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
		 */
		Log.i("StaticWrapLayout", "~~onMeasure~~");
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		// 计算出所有的childView的宽和高
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		int width = 0;
		int height = 0;
		int cCount = getChildCount();
		int cWidth = 0;
		int cHeight = 0;
		ArrayList<Integer> widthlist = new ArrayList<Integer>();
		ArrayList<Integer> heightlist = new ArrayList<Integer>();
		for (int i = 0; i < cCount; i++) {
			View childView = getChildAt(i);
			cWidth = childView.getMeasuredWidth();
			cHeight = childView.getMeasuredHeight();
			if (childView.getVisibility() != View.GONE) {
				// 隐藏的不计算在内
				if (i != 1 && i != cCount - 1) {
					// 可编辑图片和文字不计算在内
					widthlist.add(cWidth);
					heightlist.add(cHeight);
				}
			}
		}
		Collections.sort(widthlist);
		Collections.sort(heightlist);
		if (widthlist.size() > 0 && heightlist.size() > 0) {
			// 拿到最大的宽高
			width = widthlist.get(widthlist.size() - 1);
			height = heightlist.get(heightlist.size() - 1);
			widthlist.clear();
			heightlist.clear();
			widthlist = null;
			heightlist = null;
		}
		/**
		 * 如果是wrap_content设置为我们计算的值 否则：直接设置为父容器计算的值
		 */
		setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth
				: width, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight
				: height);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//LogUtils.i("onDraw");
	}
}
