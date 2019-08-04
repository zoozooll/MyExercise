/**
 * 
 */
package com.butterfly.vv.view.timeflyView;

import org.lucasr.twowayview.TwoWayView;

import com.butterfly.vv.view.BlockHolder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author Aaron Lee Created at 上午9:30:44 2015-10-21
 */
public class HolderTwowayView extends TwoWayView {
	private BlockHolder slideHolder;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public HolderTwowayView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	/**
	 * @param context
	 * @param attrs
	 */
	public HolderTwowayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	/**
	 * @param context
	 */
	public HolderTwowayView(Context context) {
		super(context);
		init(context);
	}
	private void init(Context context) {
	}
	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		if (!isEnabled()) {
			return false;
		}
		// Give everything to the gesture detector
		final int action = event.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				onDown(event);
				break;
			case MotionEvent.ACTION_UP:
				onUp(event);
				break;
			case MotionEvent.ACTION_CANCEL:
				onCancel(event);
				break;
			default:
				break;
		}
		return super.onTouchEvent(event);
	}
	public boolean onDown(final MotionEvent e) {
		if (slideHolder != null) {
			slideHolder.setBlocked(false);
		}
		return true;
	}
	/**
	 * Called when a touch event's action is MotionEvent.ACTION_UP.
	 */
	void onUp(MotionEvent e) {
		if (slideHolder != null) {
			slideHolder.setBlocked(true);
		}
	}
	/**
	 * Called when a touch event's action is MotionEvent.ACTION_CANCEL.
	 */
	void onCancel(MotionEvent e) {
		onUp(e);
	}
	public void setHolder(BlockHolder slideHolder) {
		this.slideHolder = slideHolder;
	}
}
