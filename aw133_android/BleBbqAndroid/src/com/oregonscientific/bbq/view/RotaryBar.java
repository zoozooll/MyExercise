/**
 * 
 */
package com.oregonscientific.bbq.view;

import com.oregonscientific.bbq.bean.BBQDataSet.DonenessLevel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.NetworkInfo.DetailedState;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author aaronli
 *
 */
public class RotaryBar extends View {
	
	private RectF area;
	private Paint paint;
	// round center point
	private int x0;
	private int y0;
	private boolean touchable;
	private static final float FIRSTANGLE = -90;
	// the value means 100F
	protected static final float VALUE_MIN_ANGLE = -20;
//	the value means 375F
	protected static final float VALUE_MAX_ANGLE = -360 + 20;
	
	
	/*protected float rareAngle = 1;
	protected float mediumrareAngle = -130;
	protected float mediumAngle = -30;
	protected float mediumwellAngle = -30;
	protected float welldoneAngle = -30;
//	private float overcookAngle = -30;*/
	
	/*protected final float[] angles = new float[5];;
	protected final int[] colors = new int[6];;
	protected int firstAnglesIndex = -1;*/
	protected float predonenessAngle;
	protected int endColor;
	protected SparseArray<RotaryPair> dataArray = new SparseArray<RotaryBar.RotaryPair>();
	private float preTouchX;
	private float preTouchY;
	protected double angle;
	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public RotaryBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//setLongClickable(true);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public RotaryBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * @param context
	 */
	public RotaryBar(Context context) {
		this(context, null);
	}
	
	/* (non-Javadoc)
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (!touchable) {
			return false;
		}
		if (dataArray.size() == 0) {
			return super.onTouchEvent(event);
		}
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			preTouchX =  event.getX();
			preTouchY = event.getY();
			return true;
		case MotionEvent.ACTION_MOVE:
			if (preTouchX < 0 || preTouchY < 0) {
				preTouchX =  event.getX();
				preTouchY = event.getY();
				return true;
			}
			final float touchX = event.getX();
			final float touchY = event.getY();
			angle = getAngleBetweenTwoPointsWithRound(preTouchX, preTouchY, touchX, touchY);
			predonenessAngle += angle;
			if (predonenessAngle > VALUE_MIN_ANGLE) {
				predonenessAngle = VALUE_MIN_ANGLE;
			} else if (totalDonenessAngle() < VALUE_MAX_ANGLE) {
				predonenessAngle = VALUE_MAX_ANGLE - totalNotRareDonenessAngle();
			}
			invalidate();
			preTouchX =  touchX;
			preTouchY = touchY;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			preTouchX = -1;
			preTouchY = -1;
			break;

		default:
			break;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (dataArray.size() == 0 ) {
			return;
		}
		if (area == null) {
			final int viewWidth = getWidth();
			final int viewHeight = getHeight();
			final int minWH = Math.min(viewWidth, viewHeight);
			area = new RectF(((viewWidth - minWH) >> 1) + 50, 50, ((viewWidth - minWH - 50) >> 1) +minWH - 25, minWH - 50);
			x0 = viewWidth >> 1;
			y0 = viewHeight >> 1;
		}
		if (paint == null) {
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(50);
		}
		float startFlag = FIRSTANGLE;
		drawAngleColor(canvas, startFlag, predonenessAngle, dataArray.valueAt(0).color);
		
		startFlag += predonenessAngle;
		// modified by aaronli at Mar 10; For the arc was cut in Nexus 5
		for (int i = 1, size = dataArray.size(); i < size; i ++) {
			RotaryPair pair = dataArray.valueAt(i);
			if (pair.angle < 0) {
				drawAngleColor(canvas, startFlag, pair.angle - 1, pair.color);
				startFlag += pair.angle;
			}
		}
		/*if (angles[0] < 0) {
			drawAngleColor(canvas, startFlag, angles[0], colors[0]);
			startFlag += angles[0];
		}
		if (angles[1] < 0) {
			drawAngleColor(canvas, startFlag, angles[1], colors[1]);
			startFlag += angles[1];
		}
		if (angles[2] < 0) {
			
			drawAngleColor(canvas, startFlag, angles[2], colors[2]);
			startFlag += angles[2];
		}
		
		if (angles[3] < 0) {
			drawAngleColor(canvas, startFlag, angles[3], colors[3]);
			startFlag += angles[3];
		}
		if (angles[4] < 0) {
			drawAngleColor(canvas, startFlag, angles[4], colors[4]);
			startFlag += angles[4];
		}*/
		
		drawAngleColor(canvas, startFlag + 1, -360 - 90 - startFlag - 1, endColor);
		
	}
	
	private void drawAngleColor(Canvas canvas, float startAngle, float sweepAngle, int color) {
		paint.setColor(color);
		canvas.drawArc(area, startAngle, sweepAngle, false, paint);
	}
	
	/**
	 * 
	 * @param x1
	 * @param y1
	 * @return
	 */
	private double getPointAngleFromCenter(float x1, float y1) {
		float dx = (x0 - x1);
		float dy = (y0 - y1);
		double angle = Math.atan2(dy, dx);
		if (angle > 0) {
			angle -= Math.PI * 2;
		}
		return radiansToAngles(angle);
	}
	
	private double radiansToAngles(double radians) {
		return radians * 180/Math.PI;
	}
	
	private double getAngleBetweenTwoPointsWithRound(float x1, float y1, float x2, float y2) {
		final double angle1 = getPointAngleFromCenter(x1, y1);
		final double angle2 = getPointAngleFromCenter(x2, y2);
		double deltaAngle = angle2 - angle1;
		if (deltaAngle < -180 || deltaAngle > 180) {
		
			deltaAngle = 360 - deltaAngle;
		}
		return deltaAngle;
	}
	
	private float totalDonenessAngle() {
		/*float flag = 0;
		for (float f : angles) {
			flag += f;
		}*/
		return totalNotRareDonenessAngle() + predonenessAngle;
	}
	
	private float totalNotRareDonenessAngle() {
		float flag = 0;
		for (int i = 1, size = dataArray.size(); i < size ; i++) {
			flag += dataArray.valueAt(i).angle;
		}
		return flag;
		
	}

	/**
	 * @return the touchable
	 */
	public boolean isTouchable() {
		return touchable;
	}

	/**
	 * @param touchable the touchable to set
	 */
	public void setTouchable(boolean touchable) {
		this.touchable = touchable;
	}
	
	static class RotaryPair {
		DonenessLevel level;
		int color;
		float angle;
	}

}
