/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oregonscientific.meep.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.oregonscientific.meep.R;

/**
 * This class adds a stroke to the generic TextView allowing the text to stand out better against
 * the background (ie. in the AllApps button).
 */
public class StrokedTextView extends TextView {
    private final Canvas mCanvas = new Canvas();
    private final Paint mPaint = new Paint();
    private Bitmap mCache;
    private boolean mUpdateCachedBitmap;
    private int mStrokeColor;
    private float mStrokeWidth;
    private int mTextColor;
    private Typeface mTypeface;

    public StrokedTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public StrokedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public StrokedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StrokedTextView, defStyle, 0);
        mStrokeColor = a.getColor(R.styleable.StrokedTextView_textStrokeColor, 0xFF000000);
        mStrokeWidth = a.getFloat(R.styleable.StrokedTextView_textStrokeWidth, 0.0f);
        mTextColor = this.getCurrentTextColor();
        a.recycle();
        
        
        
        a = context.obtainStyledAttributes(attrs, R.styleable.Text);
        String font = a.getString(R.styleable.Text_font);
		if (font != null) {
			mTypeface = Typeface.createFromAsset(context.getAssets(), font);
		}
        a.recycle();
        
        mUpdateCachedBitmap = true;

        // Setup the text paint
        mPaint.set(getPaint());
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        
        // Setup paddings
        setPadding(
        		getPaddingLeft() + (int) (mStrokeWidth * 2f), 
        		getPaddingTop(), 
        		getPaddingRight() + (int) mStrokeWidth, 
        		getPaddingBottom() + (int) (mStrokeWidth * 2f));
        
		// Currently, if the TextView is centered horizontally and the view is
		// rotated, text cannot be rendered properly. To prevent that, we
		// disable horizontal centering
        int gravity = getGravity();
        if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.CENTER_HORIZONTAL) {
        	gravity &= ~Gravity.CENTER_HORIZONTAL;
            setGravity(gravity);
        }
    }
    
    public void setStrokeColor(int color) {
    	mStrokeColor = color;
    	invalidate();
    }
    
    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        mUpdateCachedBitmap = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            mUpdateCachedBitmap = true;
            mCache = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        } else {
            mCache = null;
        }
    }
    
	protected void onDraw(Canvas canvas) {
		if (mCache != null) {
			if (mUpdateCachedBitmap) {
				final int w = getMeasuredWidth();
				final int h = getMeasuredHeight();
				final String text = getText() == null ? "" : getText().toString();
				final Rect textBounds = new Rect();
				final TextPaint textPaint = getPaint();
				final String displayText = TextUtils.ellipsize(
						text.subSequence(0, text.length()), 
						textPaint, 
						w,
						getEllipsize()).toString();
				final int textWidth = (int) textPaint.measureText(displayText);
				textPaint.getTextBounds("x", 0, 1, textBounds);
				
				// Clear the old cached image
				mCanvas.setBitmap(mCache);
				mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

				// Draw the drawable
				final int drawableLeft = getPaddingLeft();
				final int drawableTop = getPaddingTop();
				final Drawable[] drawables = getCompoundDrawables();
				for (int i = 0; i < drawables.length; ++i) {
					if (drawables[i] != null) {
						drawables[i].setBounds(
										drawableLeft,
										drawableTop,
										drawableLeft + drawables[i].getIntrinsicWidth(),
										drawableTop + drawables[i].getIntrinsicHeight());
						drawables[i].draw(mCanvas);
					}
				}

				int left, bottom;
				switch (mPaint.getTextAlign()) {
				case RIGHT:
					left = w - getPaddingRight() - textWidth;
					bottom = (h + textBounds.height()) / 2;
				case CENTER:
					left = (int) (getWidth() * 0.5f);
					bottom = (int) (getHeight() * 0.5f - ((mPaint.descent() + mPaint.ascent()) / 2));
				default:
					left = getPaddingLeft();
					bottom = (h + textBounds.height()) / 2;
					break;
				}

				// Draw the outline of the text
				mPaint.setStrokeWidth(mStrokeWidth);
				mPaint.setColor(mStrokeColor);
				mPaint.setTextSize(getTextSize());
				if (mTypeface != null) {
					mPaint.setTypeface(mTypeface);
				}
				mCanvas.drawText(displayText, left, bottom, mPaint);

				// Draw the text itself
				mPaint.setStrokeWidth(0);
				mPaint.setColor(mTextColor);
				mCanvas.drawText(displayText, left, bottom, mPaint);

				mUpdateCachedBitmap = false;
			}
			canvas.drawBitmap(mCache, 0, 0, mPaint);
		} else {
			super.onDraw(canvas);
		}
	}
}