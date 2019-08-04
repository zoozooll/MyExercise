/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.oregonscientific.meep.R;

public class OutlinedTextView extends LinearLayout {
	
	private RelativeLayout mContainer;
	private StrokeTextView mOutline;
	private StrokeTextView mText;

	public OutlinedTextView(Context context) {
		this(context, null);
	}
	
	public OutlinedTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public OutlinedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mContainer = new RelativeLayout(context);
		mContainer.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		int[] attrsArray = new int[] {
				android.R.attr.gravity, // 0
		};

		TypedArray b = context.obtainStyledAttributes(attrs, attrsArray);
		int gravity = b.getInt(0,  Gravity.CENTER);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (gravity == Gravity.LEFT) {
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		}
		else {
			params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		}
		b.recycle();
		mText = new StrokeTextView(context);
		mText.setLayoutParams(params);
		
		params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		if (gravity == Gravity.LEFT) {
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		}
		else {
			params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		}
		mOutline = new StrokeTextView(context);
		mOutline.setLayoutParams(params);
		
		setOrientation(LinearLayout.HORIZONTAL);
		mContainer.addView(mOutline);
		mContainer.addView(mText);
		addView(mContainer);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Text);
		
		setTextWidth(a.getFloat(R.styleable.Text_width, StrokeTextView.DEFAULT_WIDTH));
		setOutlineWidth(a.getFloat(R.styleable.Text_outlineWidth, StrokeTextView.DEFAULT_WIDTH));
		setMaxLines(a.getInteger(R.styleable.Text_maxLines, 0));
		setEllipsize(a.getInteger(R.styleable.Text_ellipsize, 0));
		setText(a.getString(R.styleable.Text_text));
		setTextSize(a.getFloat(R.styleable.Text_textSize, StrokeTextView.DEFAULT_SIZE));
		setTextColor(a.getColor(R.styleable.Text_textColor, StrokeTextView.DEFAULT_COLOR));
		setOutlineColor(a.getColor(R.styleable.Text_outlineColor, StrokeTextView.DEFAULT_COLOR));
		setTextScaleX(a.getFloat(R.styleable.Text_scaleX, StrokeTextView.DEFAULT_SCALEX));
		setMiter(a.getFloat(R.styleable.Text_milter, StrokeTextView.DEFAULT_MITER));
		setJoin(a.getInteger(R.styleable.Text_joinStyle, 0));
		
		String font = a.getString(R.styleable.Text_font);
		if (font != null) {
			Typeface typeface = Typeface.createFromAsset(context.getAssets(), font);
			mOutline.setTypeface(typeface);
			mText.setTypeface(typeface);
		}
		
		a.recycle();
	}
	
	private void setEllipsize(int where) {
		TextUtils.TruncateAt location = null;
		switch (where) {
		case 1:
			location = TextUtils.TruncateAt.START;
			break;
		case 2:
			location = TextUtils.TruncateAt.MIDDLE;
			break;
		case 3: 
			location = TextUtils.TruncateAt.END;
			break;
		case 4:
			location = TextUtils.TruncateAt.MARQUEE;
			break;
		default:
			location = null;
			break;
		}
		setEllipsize(location);
	}
	
	/**
	 * Causes words in the text that are longer than the view is wide to be ellipsized instead of broken in the middle. 
	 * Use {@code null} to turn off ellipsizing.
	 * 
	 * @param where Indicates where in the text should ellipsize occur.
	 */
	public void setEllipsize(TextUtils.TruncateAt where) {
		if (mOutline != null) {
			mOutline.setEllipsize(where);
		}
		if (mText != null) {
			mText.setEllipsize(where);
		}
	}
	
	/**
	 * Sets the text to be displayed
	 * 
	 * @param text the text to be displayed
	 */
	public void setText(String text) {
		if (mOutline != null) {
			mOutline.setText(text);
		}
		if (mText != null) {
			mText.setText(text);
		}
		requestLayout();
	}
	
	/**
	 * Return the text the TextView is displaying.
	 */
	public CharSequence getText() {
		return mText.getText();
	}
	
	/**
	 * Set the default text size to the given value, interpreted as "scaled pixel" units. This size is adjusted based on the current density and user font size preference.
	 * @param size The scaled pixel size.
	 */
	public void setTextSize(float size) {
		if (mOutline != null) {
			mOutline.setTextSize(size);
		}
		if (mText != null) {
			mText.setTextSize(size);
		}
		requestLayout();
	}
	
	/**
	 * Sets the outline's stroke miter value. This is used to control the
	 * behavior of miter joins when the joins angle is sharp. This value must be
	 * >= 0.
	 * 
	 * @param miter set the miter limit on the text and outline
	 */
	public void setMiter(float miter) {
		if (mOutline != null) {
			mOutline.setMiter(miter);
		}
		if (mText != null) {
			mText.setMiter(miter);
		}
		requestLayout();
	}
	
	/**
	 * Sets the stroke join of the text
	 * 
	 * @param join the new join for the text
	 */
	public void setJoin(int join) {
		if (mOutline != null) {
			mOutline.setJoin(join);
		}
		if (mText != null) {
			mText.setJoin(join);
		}
		requestLayout();
	}
	
	/**
	 * Makes the OutlinedTextView at most this many lines tall
	 * 
	 * @param maxLines the maximum number of lines
	 */
	public void setMaxLines(int maxlines) {
		if (maxlines > 0) {
			if (mOutline != null) {
				mOutline.setSingleLine(maxlines == 1);
				mOutline.setMaxLines(maxlines);
			}
			if (mText != null) {
				mText.setSingleLine(maxlines == 1);
				mText.setMaxLines(maxlines);
			}
			requestLayout();
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
		if (mOutline != null) {
			mOutline.setTextScaleX(scaleX);
		}
		if (mText != null) {
			mText.setTextScaleX(scaleX);
		}
		requestLayout();
	}
	
	/**
	 * Set the width for the outline. Pass 0 to stroke in hairline mode.
	 * Hairlines always draws a single pixel independent of the canva's matrix.
	 * 
	 * @param width
	 *            set the outline's stroke width
	 */
	public void setOutlineWidth(float width) {
		if (mOutline != null) {
			mOutline.setTextWidth(width);
		}
		requestLayout();
	}
	
	/**
	 * Sets the text color for all the states (normal, selected, focused) to be this color.
	 * 
	 * @param color the new color for the text
	 */
	public void setTextColor(int color) {
		if (mText != null) {
			mText.setTextColor(color);
		}
	}
	
	/**
	 * Sets the text color.
	 * 
	 * @param colors the new color for the text
	 */
	public void setTextColor(ColorStateList colors) {
		if (mText != null) {
			mText.setTextColor(colors);
		}
	}
	
	/**
	 * Sets the outline text color for all the states (normal, selected, focused) to be this color.
	 * 
	 * @param color the new color for the outline text
	 */
	public void setOutlineColor(int color) {
		if (mOutline != null) {
			mOutline.setTextColor(color);
		}
	}
	
	/**
	 * Sets the outline text color
	 * 
	 * @param colors the new color for the outline text
	 */
	public void setOutlineColor(ColorStateList colors) {
		if (mOutline != null) {
			mOutline.setTextColor(colors);
		}
	}
	
	/**
	 * Set the width for the text. Pass 0 to stroke in hairline mode. Hairlines
	 * always draws a single pixel independent of the canva's matrix.
	 * 
	 * @param width
	 *            set the text's stroke width
	 */
	public void setTextWidth(float width) {
		if (mText != null) {
			mText.setTextWidth(width);
		}
		requestLayout();
	}
	
}
