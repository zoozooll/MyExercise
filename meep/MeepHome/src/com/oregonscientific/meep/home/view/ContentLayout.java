package com.oregonscientific.meep.home.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.oregonscientific.meep.home.R;
import com.oregonscientific.meep.home.view.FadingScrollView.OnScrollChangedListener;

/**
 * A view can provide set the background padding
 */
public class ContentLayout extends RelativeLayout {
	
	private int mLeftPadding, mTopPadding, mRightPadding, mBottomPadding;
	
	private Bitmap mBackground;
	private RectF mRect = new RectF();
	
	public ContentLayout(Context context) {
		this(context, null);
	}

	@SuppressWarnings("deprecation")
	public ContentLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ContentLayout);
		mLeftPadding = a.getInt(R.styleable.ContentLayout_backgroundPaddingLeft, 0);
		mTopPadding = a.getInt(R.styleable.ContentLayout_backgroundPaddingTop, 0);
		mRightPadding = a.getInt(R.styleable.ContentLayout_backgroundPaddingRight, 0);
		mBottomPadding = a.getInt(R.styleable.ContentLayout_backgroundPaddingBottom, 0);
		a.recycle();
		
		if (getBackground() != null) {
			mBackground = ((BitmapDrawable) getBackground()).getBitmap();
			// clear the bitmap and draw it on onDraw()
			setBackgroundDrawable(null);
		}
		setWillNotDraw(false);
	}
	
	@Override
	public void onFinishInflate() {
		super.onFinishInflate();
		
		final Handler handler = new Handler();
		final View topFadingEdge = findViewById(R.id.scrollview_fading_up);
		for (int i = 0; i < this.getChildCount(); i++) {
			View v = getChildAt(i);
			if (v instanceof FadingScrollView) {
				((FadingScrollView) v).setOnScrollChangedListener(new OnScrollChangedListener() {

					@Override
					public void onScrollChanged(ScrollView scrollView, int l, int t, int oldl, int oldt) {
						if (topFadingEdge != null) {
							final int visibility = t == scrollView.getTop() ? View.INVISIBLE : View.VISIBLE;
							int currentVisibility = topFadingEdge.getVisibility();
							if (currentVisibility != visibility) {
								handler.post(new Runnable() {

									@Override
									public void run() {
										topFadingEdge.setVisibility(visibility);
									}
									
								});
							}
						}	
					}
					
				});
			}
		}
	}
	
	/**
	 * Set the background with bitmap that will use at onDraw()
	 * @param bitmap
	 */
	@SuppressWarnings("deprecation")
	public void setBackgroundBitmap(Bitmap bitmap) {
		if (bitmap != null) {
			mBackground = bitmap;
			setBackgroundDrawable(null);
			
			invalidate();
		}
	}
	
	/**
	 * Sets the background paddings, in pixels.
	 * 
	 * @param left the left padding size
	 * @param top the top padding size
	 * @param right the right padding size
	 * @param bottom the bottom padding size
	 */
	public void setBackgroundPaddings(int left, int top, int right, int bottom) {
		mLeftPadding = left;
		mTopPadding = top;
		mRightPadding = right;
		mBottomPadding = bottom;
		
		invalidate();
	}
	
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (mBackground != null) {
			// Draw background
			mRect.set(mLeftPadding, mTopPadding, getWidth() - mRightPadding, getHeight() - mBottomPadding);
			canvas.drawBitmap(mBackground, null, mRect, null);
		}
	}
	
}
