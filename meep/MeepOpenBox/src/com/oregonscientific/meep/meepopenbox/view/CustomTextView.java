package com.oregonscientific.meep.meepopenbox.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.oregonscientific.meep.meepopenbox.R;

/**
 * Text View enabling customized font to be used
 * @author Charles
 *
 */
public class CustomTextView extends TextView {
	
	public CustomTextView(
			Context context,
			AttributeSet attributeSet,
			int defaultStyle) {
		super(context, attributeSet, defaultStyle);
		init(attributeSet);
	}
	
	public CustomTextView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		init(attributeSet);
	}
	
	public CustomTextView(Context context) {
		super(context);
	}
	
	public void init(AttributeSet attributeSet) {
		if (!isInEditMode()) {
			TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.CustomTextView);
			final int indexCount = typedArray.getIndexCount();
			
			for (int i = 0; i < indexCount; ++i) {
				int index = typedArray.getIndex(i);
				switch (index) {
					case R.styleable.CustomTextView_font_directory:
						Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), typedArray.getString(index));
						setTypeface(typeFace);
						break;
					default:
						break;
				}
			}
			typedArray.recycle();
		}
	}
	
}