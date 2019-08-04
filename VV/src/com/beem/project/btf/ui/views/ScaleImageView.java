package com.beem.project.btf.ui.views;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ScaleImageView extends ImageView {
	private static final String TAG = "ScaleImageView";

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ScaleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	public ScaleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public ScaleImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	/*@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.8f);
				PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.8f);
				ObjectAnimator.ofPropertyValuesHolder(this, pvhY, pvhZ).setDuration(20).start();
				return true;
			}
			case MotionEvent.ACTION_UP: {
				PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 0.8f, 1.0f);
				PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 0.8f, 1.0f);
				ObjectAnimator.ofPropertyValuesHolder(this, pvhY, pvhZ).setDuration(20).start();
				return true;
			}
		}
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		performClick();
		return false;
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}*/
	@Override
	protected void dispatchSetPressed(boolean pressed) {
		if (pressed) {
			PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX",
					1f, 0.8f);
			PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY",
					1f, 0.8f);
			ObjectAnimator.ofPropertyValuesHolder(this, pvhY, pvhZ)
					.setDuration(20).start();
		} else {
			PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX",
					0.8f, 1.0f);
			PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY",
					0.8f, 1.0f);
			ObjectAnimator.ofPropertyValuesHolder(this, pvhY, pvhZ)
					.setDuration(20).start();
		}
	}
}
