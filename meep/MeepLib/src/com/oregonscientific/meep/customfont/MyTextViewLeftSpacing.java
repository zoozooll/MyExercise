package com.oregonscientific.meep.customfont;

import java.util.WeakHashMap;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.util.Pair;
import android.widget.TextView;

public class MyTextViewLeftSpacing extends MyTextView {

	public MyTextViewLeftSpacing(Context context) {
		super(context);
	}
	public MyTextViewLeftSpacing(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}
	public MyTextViewLeftSpacing(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private CharSequence originalText = "";

	@Override
	public void setText(CharSequence text, BufferType type) {
		originalText = text;
		applyLeftSpacing();

	}

	@Override
	public CharSequence getText() {
		return originalText;
	}

	private void applyLeftSpacing() {
		StringBuilder builder = new StringBuilder();
		if (originalText.length() != 0) {
			builder.append(" ");
			for (int i = 0; i < originalText.length(); i++) {
				builder.append(originalText.charAt(i));
				if (i + 1 < originalText.length()) {
					builder.append(" ");
				}
			}
			SpannableString finalText = new SpannableString(builder.toString());
			if (builder.toString().length() > 2) {
				for (int i = 2; i < builder.toString().length(); i += 2) {
					finalText.setSpan(new ScaleXSpan(( 1f) / 10), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
			super.setText(finalText, BufferType.SPANNABLE);
		}
	}
}