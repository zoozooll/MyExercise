/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.oregonscientific.meep.R;

public class StrokeTextView extends TextView {
	
	public static final int DEFAULT_MITER = 0;
	public static final int DEFAULT_WIDTH = -1;
	public static final float DEFAULT_SCALEX = 1.0f;
	public static final int DEFAULT_COLOR = 0xff000000;
	public static final float DEFAULT_SIZE = 10.f;
	
	private float mWidth;
	private Join mJoin;
	private float mMiter;
	private float mScaleX;
	
	public StrokeTextView(Context context) {
		this(context, null);
	}
	
	public StrokeTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public StrokeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Text);
		
		String font = a.getString(R.styleable.Text_font);
		if (font != null) {
			Typeface typeface = Typeface.createFromAsset(context.getAssets(), font);
			setTypeface(typeface);
		}
		
		setTextWidth(a.getFloat(R.styleable.Text_width, DEFAULT_WIDTH));
		setTextScaleX(a.getFloat(R.styleable.Text_scaleX, DEFAULT_SCALEX));
		setTextSize(a.getFloat(R.styleable.Text_textSize, StrokeTextView.DEFAULT_SIZE));
		setMiter(a.getFloat(R.styleable.Text_milter, DEFAULT_MITER));
		setJoin(a.getInteger(R.styleable.Text_joinStyle, 0));
		
		a.recycle();
	}
	
	/**
	 * Sets the stroke join of the text
	 * 
	 * @param join the new join for the text
	 */
	public void setJoin(int join) {
		Join j = Join.MITER;
		switch (join) {
		case 0:
			j = Join.MITER;
			break;
		case 1:
			j = Join.BEVEL;
			break;
		case 2:
			j = Join.ROUND;
			break;
		}
		
		if (!j.equals(mJoin)) {
			mJoin = j;
			invalidate();
		}
	}
	
	/**
	 * Set the paint's horizontal scale factor for text. The default value is
	 * 1.0. Values > 1.0 will stretch the text wider. Values < 1.0 will stretch
	 * the text narrower.
	 * 
	 * @param scaleX set the paint's scale in X for drawing/measuring text.
	 */
	public void setTextScaleX(float scaleX) {
		mScaleX = scaleX;
		invalidate();
	}
	
	/**
	 * Set the width for the text. Pass 0 to stroke in hairline mode. Hairlines
	 * always draws a single pixel independent of the canva's matrix.
	 * 
	 * @param width
	 *            set the text's stroke width
	 */
	public void setTextWidth(float width) {
		if (width >= DEFAULT_WIDTH) {
			mWidth = width;
			if (mWidth > 1) {
				Bitmap bm = Bitmap.createBitmap((int) mWidth - 1, 1, Bitmap.Config.ARGB_4444);
				BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
				setCompoundDrawablesWithIntrinsicBounds(bd, bd, bd, bd);
			}
		}
	}
	
	/**
	 * Returns the width of the text
	 * 
	 * @return a float indicating the width of the text
	 */
	public float getTextWidth() {
		return mWidth;
	}
	
	/**
	 * Sets the outline's stroke miter value. This is used to control the
	 * behavior of miter joins when the joins angle is sharp. This value must be
	 * >= 0.
	 * 
	 * @param miter set the miter limit on the text and outline
	 */
	public void setMiter(float miter) {
		if (miter >= 0) {
			mMiter = miter;
			invalidate();
		}
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		TextPaint paint = getPaint();
		if (mWidth != DEFAULT_WIDTH) {
			paint.setFakeBoldText(true);
			paint.setStyle(Style.STROKE);
			paint.setStrokeMiter(mMiter);
			paint.setStrokeJoin(mJoin);
			paint.setStrokeWidth(mWidth);
			paint.setStyle(Style.FILL_AND_STROKE);
		}
		paint.setTextScaleX(mScaleX);
		
		super.onDraw(canvas);
	}

}
