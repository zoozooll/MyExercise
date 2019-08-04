package com.oregonscientific.meep.view;

import com.osgd.meep.library.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewConfiguration;
import android.widget.GridView;

/**
 * 
 * @author aaronli
 *
 */
public class MyGridView extends GridView {

	private static final String TAG = "MyGridView";

	private int mWidth;
	private int mHeight;
	private int mPaddingLeft;
	private int mPaddingTop;
	private int mPaddingRight;
	private int mPaddingBottom;

	private int backgroundWidth;
	private int backgroundHeight;
	private Bitmap background;
	private Rect backgroundDst;
	private Paint mPaint;

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.MyGridView);
		background = BitmapFactory.decodeResource(context
				.getApplicationContext().getResources(), array.getResourceId(
				R.styleable.MyGridView_line_background, -1));
		if (background != null) {
			backgroundWidth = background.getWidth();
			backgroundHeight = background.getHeight();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.AbsListView#onLayout(boolean, int, int, int, int)
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		mWidth = getWidth();
		mHeight = getHeight();
		mPaddingLeft = getPaddingLeft();
		mPaddingTop = getPaddingTop();
		mPaddingRight = getPaddingRight();
		mPaddingBottom = getPaddingBottom();
		backgroundDst = new Rect();
		mPaint = new Paint();
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		int count = getChildCount();
		// Log.d(TAG, "child count "+count);
		// int top = count>0 ? getChildAt(0).getTop() : 0;
		// int backgroundWidth = background.getWidth();
		// int backgroundHeight = background.getHeight();
		// int width = getWidth();
		// int height = getHeight();
		int lineCount = (count + getNumColumns() - 1) / getNumColumns();
		for (int i = 0; i < lineCount; i++) {
			// Log.d(TAG, "begin drawLinesBackground");
			int bottom = count > 0 ? getChildAt(i * 5).getBottom() : 0;
			// Log.d(TAG, "bottom "+bottom);
			if (background != null) {
				drawLinesBackground(canvas, bottom - backgroundHeight + 5);
			}
		}

		super.dispatchDraw(canvas);
	}

	private void drawLinesBackground(Canvas canvas, int y) {
		// for (int x = mPaddingLeft; x < mWidth - mPaddingRight; x +=
		// backgroundWidth){
		// Log.d(TAG, "draw "+x+","+y);
		backgroundDst.left = mPaddingLeft - 40;
		backgroundDst.top = y;
		backgroundDst.right = mWidth - mPaddingRight;
		backgroundDst.bottom = y + backgroundHeight + 5;
		canvas.drawBitmap(background, null, backgroundDst, mPaint);
		// }
	}
}
