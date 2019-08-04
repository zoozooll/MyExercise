/**
 * 
 */
package com.oregonscientific.meep.customfont;

import java.util.WeakHashMap;

import com.osgd.meep.library.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Pair;
import android.widget.TextView;

/**
 * The custom textview shows in snake view.
 * @author aaronli at Jul17 2013
 *
 */
public class SnakeTextView extends TextView {
	
	private WeakHashMap<String, Pair<Canvas, Bitmap>> canvasStore;
	private float strokeWidth;
	private Integer strokeColor;
	private Join strokeJoin;
	private float strokeMiter;


	public SnakeTextView(Context context) {
		super(context);
	}

	public SnakeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SnakeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (this.isInEditMode())
			return;
		if (canvasStore == null) {
			canvasStore = new WeakHashMap<String, Pair<Canvas, Bitmap>>();
		}
		final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextViewPlus);
		if (a.hasValue(R.styleable.TextViewPlus_customFont)) {
			final String customFont = a.getString(R.styleable.TextViewPlus_customFont);
			
			// Build a custom typeface-cache here!
			this.setTypeface(Typeface.createFromAsset(context.getAssets(), customFont));
		}
		
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
			setStroke(strokeWidth, strokeColor, strokeJoin, strokeMiter);
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
		if (this.isInEditMode())
			return;
		//freeze();
		Drawable restoreBackground = this.getBackground();
		Drawable[] restoreDrawables = this.getCompoundDrawables();
		int restoreColor = this.getCurrentTextColor();

		//this.setCompoundDrawables(null, null, null, null);
		
		this.setTextColor(restoreColor);

		if (strokeColor != null) {
			TextPaint paint = this.getPaint();
			paint.setStyle(Style.STROKE);
			paint.setStrokeJoin(strokeJoin);
			paint.setStrokeMiter(strokeMiter);
			this.setTextColor(strokeColor);
			paint.setStrokeWidth(strokeWidth);
			super.onDraw(canvas);
			paint.setStyle(Style.FILL);
			this.setTextColor(restoreColor);
			super.onDraw(canvas);
		}

		if (restoreDrawables != null) {
			this.setCompoundDrawablesWithIntrinsicBounds(restoreDrawables[0], restoreDrawables[1], restoreDrawables[2], restoreDrawables[3]);
		}
	}
}
