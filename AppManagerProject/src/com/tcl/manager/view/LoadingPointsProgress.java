/**
 * 
 */
package com.tcl.manager.view;

import com.tcl.framework.log.NLog;
import com.tcl.framework.util.DeviceManager;
import com.tcl.manager.util.DisplayUtils;
import com.tcl.mie.manager.R;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author zuokang.li
 *
 */
public class LoadingPointsProgress extends View {
	
	private Paint mPaint;
	
	private ValueAnimator mAnimator;
	
	private int drawCount;
	
	private static final String POINT_TEXT = "...";

	@TargetApi(21)
	public LoadingPointsProgress(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initialize(context, attrs, defStyleAttr, defStyleRes);
	}

	public LoadingPointsProgress(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context, attrs, defStyleAttr, 0);
	}

	public LoadingPointsProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context, attrs, 0, 0);
	}

	public LoadingPointsProgress(Context context) {
		super(context);
		initialize(context, null, 0, 0);
	}
	
	private void initialize(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		
		mPaint = new Paint();
		mAnimator = ValueAnimator.ofInt(0, 4);
		mAnimator.setDuration(1000);
		mAnimator.setRepeatCount(ValueAnimator.INFINITE);
		mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Object o = animation.getAnimatedValue();
//				Log.d("aaron", "getAnimatedValue "+ o);
				drawCount = (Integer) o;
				invalidate();
				
			}
		});
	}

	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		if (visibility == View.VISIBLE) {
			mAnimator.start();
		} else {
			mAnimator.end();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mPaint.reset();
		mPaint.setColor(getResources().getColor(R.color.white));
		mPaint.setTextSize(DisplayUtils.sp2px(getContext(), 18.f));
//		NLog.d("aaron", "onDraw drawCount " + drawCount);
		canvas.drawText(POINT_TEXT, 0, drawCount, 1, getHeight() - 5, mPaint);
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int desiredWidth = (int) DisplayUtils.sp2px(getContext(), 28.f) + 2;
	    int desiredHeight = (int) DisplayUtils.sp2px(getContext(), 28.f) + 2;

	    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);

	    int width;
	    int height;

	    //Measure Width
	    if (widthMode == MeasureSpec.EXACTLY) {
	        //Must be this size
	        width = widthSize;
	    } else if (widthMode == MeasureSpec.AT_MOST) {
	        //Can't be bigger than...
	        width = Math.min(desiredWidth, widthSize);
	    } else {
	        //Be whatever you want
	        width = desiredWidth;
	    }

	    //Measure Height
	    if (heightMode == MeasureSpec.EXACTLY) {
	        //Must be this size
	        height = heightSize;
	    } else if (heightMode == MeasureSpec.AT_MOST) {
	        //Can't be bigger than...
	        height = Math.min(desiredHeight, heightSize);
	    } else {
	        //Be whatever you want
	        height = desiredHeight;
	    }

	    //MUST CALL THIS
	    setMeasuredDimension(width, height);
	}
	
	

}
