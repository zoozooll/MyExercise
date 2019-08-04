package com.mogoo.market.widget;

import com.mogoo.market.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class TextProgressBar extends ProgressBar {
	private String text;
	private Paint mPaint;
	private int mTextSize;
	private int mTextColor;

	public TextProgressBar(Context context) {
		super(context);
		initText();
	}

	public TextProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextProgressBar, 0, 0);
        mTextSize = a.getDimensionPixelSize(R.styleable.TextProgressBar_customTextSize, 14);
        mTextColor = a.getColor(R.styleable.TextProgressBar_customTextColor, 0xFFFFFF);
        a.recycle();
		initText();
	}

	public TextProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextProgressBar, 0, 0);
        mTextSize = a.getDimensionPixelSize(R.styleable.TextProgressBar_customTextSize, 14);
        mTextColor = a.getColor(R.styleable.TextProgressBar_customTextColor, 0xFFFFFF);
        a.recycle();
		initText();
	}

	@Override
	public void setProgress(int progress) {
		setText(progress);
		super.setProgress(progress);

	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Rect rect = new Rect();
		this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
		int x = (getWidth() / 2) - rect.centerX();
		int y = (getHeight() / 2) - rect.centerY();
		canvas.drawText(this.text, x, y, this.mPaint);
	}

	// 初始化，画笔
	private void initText() {
		this.mPaint = new Paint();
		this.mPaint.setAntiAlias(true);
		this.mPaint.setTextSize(mTextSize);
		this.mPaint.setColor(mTextColor);

	}

	// 设置文字内容
	private void setText(int progress) {
		int i = (int) ((progress * 1.0f / this.getMax()) * 100);
		this.text = String.valueOf(i) + "%";
	}
}
