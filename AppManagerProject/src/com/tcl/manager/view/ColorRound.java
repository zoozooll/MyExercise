/**
 * 
 */
package com.tcl.manager.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author zuokang.li
 *
 */
public class ColorRound extends View {
	
	private int bgColor;
	private Paint mPaint = new Paint();
	private RectF mRectF;

	@TargetApi(21)
	public ColorRound(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public ColorRound(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ColorRound(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ColorRound(Context context) {
		super(context);
	}
	
	

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		
	}

	@Override
	public void draw(Canvas canvas) {
		mPaint.reset();
		mPaint.setAntiAlias(true);  
		Drawable d = getBackground();
		if (mRectF == null) {
			mRectF = new RectF(0f, 0.f, getWidth(), getHeight());
		} else {
			mRectF.set(0f, 0.f, getWidth(), getHeight());
		}
		if (d instanceof ColorDrawable) {
			canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG)); 
			bgColor = ((ColorDrawable)d).getColor();
			mPaint.setColor(bgColor);
			canvas.drawArc(mRectF, 0, 360, true, mPaint);
		}
	}
	
	

}
