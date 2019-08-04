/**
 * 
 */
package com.idt.bw.view;

import com.idt.bw.activity.R;

import android.R.color;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author aaronli
 *
 */
public class BMILevelView extends View {
	
	private float[] levelValues  = {18.5f, 25f, 30f};
	private float[] bordePercents = { 0.23421588594704684317718940936864f,
			0.53767820773930753564154786150713f,
			0.76782077393075356415478615071283f };
	private float arrowToF;
	private float bmiValue;
	private Bitmap bmibarImage;
	private int barWidth, barHeight;
	private Paint p;
	private Path path = new Path();
	

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public BMILevelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		bmibarImage = BitmapFactory.decodeResource(getResources(), R.drawable.main_bmi_bar);
		p = new Paint();
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(dip2px(3));
		p.setARGB(255, 255, 255, 255);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public BMILevelView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * @param context
	 */
	public BMILevelView(Context context) {
		this(context, null);
	}

	/* (non-Javadoc)
	 * @see android.view.View#onLayout(boolean, int, int, int, int)
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	/* (non-Javadoc)
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
	}

	/* (non-Javadoc)
	 * @see android.view.View#onSizeChanged(int, int, int, int)
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int imageWidth = bmibarImage.getWidth();
		int imageHeight = bmibarImage.getHeight();
		barWidth = w;
		barHeight = imageHeight * barWidth / imageWidth;
	}

	/* (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawBitmap(bmibarImage, new Rect(0 ,0, bmibarImage.getWidth(), bmibarImage.getHeight()), 
				new Rect(0, 0, barWidth, barHeight), null);
		
		int padding = dip2px(8);
		float lineBase = barHeight + dip2px(20);
		path.reset();
		path.moveTo(padding, lineBase);
		float lineToArrow = (getWidth() - padding * 2) * arrowToF + padding ;
		if (lineToArrow > dip2px(10))
			path.lineTo(lineToArrow - dip2px(10), lineBase);
		path.lineTo(lineToArrow, lineBase - dip2px(10));
		path.lineTo(lineToArrow+ dip2px(10),  lineBase);
		if (lineToArrow < getWidth() - padding - dip2px(10))
			path.lineTo(getWidth() - padding,  lineBase);
		canvas.drawPath(path, p);
	}

	/**
	 * @param levelValues the levelValues to set
	 */
	public void setLevelValues(float[] levelValues) {
		this.levelValues = levelValues;
	}
	
	/**
	 * @param bmiValue the bmiValue to set
	 */
	public void setBmiValue(float bmiValue) {
		this.bmiValue = bmiValue;
		countArrowPointF();
		invalidate();
	}

	public int dip2px(float dipValue){ 
		final float scale = getResources().getDisplayMetrics().density; 
		return (int) (dipValue * scale); 

	}	
	
	public  float px2dip( float pxValue){ 
		final float scale = getResources().getDisplayMetrics().density; 

		return pxValue / scale;
	}
	
	private void countArrowPointF() {
		int level = 0;
		for (int size = levelValues.length; level < size; level++) {
			if(bmiValue < levelValues[level]) {
				break;
			}
		}
		float basePrecent, nextPrecent, baseValue, nextValue;
		if (level == 0) {
			basePrecent = 0;
			nextPrecent = bordePercents [level];
			baseValue = 0 ;
			nextValue = levelValues [level];
		} else if (level == bordePercents.length) {
			basePrecent = bordePercents [level - 1];
			nextPrecent = 1f;
			baseValue = levelValues [level - 1];
			nextValue = 100f;
		} else {
			basePrecent = bordePercents [level - 1];
			nextPrecent = bordePercents [level];
			baseValue = levelValues [level - 1];
			nextValue = levelValues [level];
		}
		arrowToF = basePrecent + (bmiValue - baseValue) * (nextPrecent - basePrecent)/(nextValue - baseValue);
	}

}
