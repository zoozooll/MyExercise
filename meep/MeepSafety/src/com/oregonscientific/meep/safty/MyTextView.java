package com.oregonscientific.meep.safty;

import java.util.ArrayList;
import java.util.WeakHashMap;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Pair;
import android.widget.TextView;

import com.oregonscientific.meep.safty.R;

public class MyTextView extends TextView {

	private WeakHashMap<String, Pair<Canvas, Bitmap>> canvasStore;

	private float strokeWidth;
	private Integer strokeColor;
	private Join strokeJoin;
	private float strokeMiter;

	private int[] lockedCompoundPadding;
	private boolean frozen = false;

	public MyTextView(Context context) {
		this(context, null);
	}

	public MyTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		if (this.isInEditMode())
			return;
		if (canvasStore == null) {
			canvasStore = new WeakHashMap<String, Pair<Canvas, Bitmap>>();
		}
		final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextViewPlus);
		final String customFont = a.getString(R.styleable.TextViewPlus_customFont);

		// Build a custom typeface-cache here!
		this.setTypeface(Typeface.createFromAsset(context.getAssets(), customFont));

		if (a.hasValue(R.styleable.TextViewPlus_strokeColor)) {
			float strokeWidth = a.getFloat(R.styleable.TextViewPlus_strokeWidth, 1);
			int strokeColor = a.getColor(R.styleable.TextViewPlus_strokeColor, 0xff000000);
			float strokeMiter = a.getFloat(R.styleable.TextViewPlus_strokeMiter, 10);
			Join strokeJoin = null;
			switch (a.getInt(R.styleable.TextViewPlus_strokeJoinStyle, 0)) {
			case (0):
				strokeJoin = Join.MITER;
				break;
			case (1):
				strokeJoin = Join.BEVEL;
				break;
			case (2):
				strokeJoin = Join.ROUND;
				break;
			}
			this.setStroke(strokeWidth, strokeColor, strokeJoin, strokeMiter);
		}
	}

	public void setStroke(float width, int color, Join join, float miter) {
		strokeWidth = width;
		strokeColor = color;
		strokeJoin = join;
		strokeMiter = miter;
	}

	public void setStroke(float width, int color) {
		setStroke(width, color, Join.ROUND, 10);
	}



	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (this.isInEditMode())
			return;
		freeze();
		Drawable restoreBackground = this.getBackground();
		Drawable[] restoreDrawables = this.getCompoundDrawables();
		int restoreColor = this.getCurrentTextColor();

		this.setCompoundDrawables(null, null, null, null);
		
		this.setTextColor(restoreColor);

		if (strokeColor != null) {
			TextPaint paint = this.getPaint();
			paint.setFakeBoldText(true);
			paint.setStyle(Style.STROKE);
			paint.setStrokeJoin(strokeJoin);
			paint.setStrokeMiter(strokeMiter);
			this.setTextColor(strokeColor);
			paint.setStrokeWidth(strokeWidth);
			super.onDraw(canvas);
			paint.setStyle(Style.FILL);
			this.setTextColor(restoreColor);
		}

		if (restoreDrawables != null) {
			this.setCompoundDrawablesWithIntrinsicBounds(restoreDrawables[0], restoreDrawables[1], restoreDrawables[2], restoreDrawables[3]);
		}
		this.setBackgroundDrawable(restoreBackground);
		this.setTextColor(restoreColor);

		unfreeze();
	}

	// Keep these things locked while onDraw in processing
	public void freeze() {
		lockedCompoundPadding = new int[] { getCompoundPaddingLeft(),
				getCompoundPaddingRight(), getCompoundPaddingTop(),
				getCompoundPaddingBottom() };
		frozen = true;
	}

	public void unfreeze() {
		frozen = false;
	}

	@Override
	public void requestLayout() {
		if (!frozen)
			super.requestLayout();
	}

	@Override
	public void postInvalidate() {
		if (!frozen)
			super.postInvalidate();
	}

	@Override
	public void postInvalidate(int left, int top, int right, int bottom) {
		if (!frozen)
			super.postInvalidate(left, top, right, bottom);
	}

	@Override
	public void invalidate() {
		if (!frozen)
			super.invalidate();
	}

	@Override
	public void invalidate(Rect rect) {
		if (!frozen)
			super.invalidate(rect);
	}

	@Override
	public void invalidate(int l, int t, int r, int b) {
		if (!frozen)
			super.invalidate(l, t, r, b);
	}

	@Override
	public int getCompoundPaddingLeft() {
		return !frozen ? super.getCompoundPaddingLeft()
				: lockedCompoundPadding[0];
	}

	@Override
	public int getCompoundPaddingRight() {
		return !frozen ? super.getCompoundPaddingRight()
				: lockedCompoundPadding[1];
	}

	@Override
	public int getCompoundPaddingTop() {
		return !frozen ? super.getCompoundPaddingTop()
				: lockedCompoundPadding[2];
	}

	@Override
	public int getCompoundPaddingBottom() {
		return !frozen ? super.getCompoundPaddingBottom()
				: lockedCompoundPadding[3];
	}

}
