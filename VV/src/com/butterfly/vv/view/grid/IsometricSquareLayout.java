package com.butterfly.vv.view.grid;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * @author le yang 固定宽高比为4:3的相对布局
 */
public class IsometricSquareLayout extends RelativeLayout {
	public IsometricSquareLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	public IsometricSquareLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public IsometricSquareLayout(Context context) {
		super(context);
	}
	@SuppressWarnings("unused")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// For simple implementation, or internal size is always 0.
		// We depend on the container to specify the layout size of
		// our view. We can't really know what it is since we will be
		// adding and removing different arbitrary views and do not
		// want the layout to change as this happens.
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
				getDefaultSize(0, heightMeasureSpec));
		// Children are just made to fill our space.
		int childWidthSize = getMeasuredWidth();
		int childHeightSize = getMeasuredHeight();
		// if(childWidthSize > childHeightSize) {
		// childWidthSize = childHeightSize;
		// }else {
		// childHeightSize = childWidthSize;
		// }
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,
				MeasureSpec.EXACTLY);
		heightMeasureSpec = widthMeasureSpec * 3 / 4;
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
