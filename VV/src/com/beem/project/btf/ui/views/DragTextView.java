package com.beem.project.btf.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * @author le yang
 * @category 此textview控件解决触摸事件和单击事件共存的问题，同时添加流动的虚线边框
 */
public class DragTextView extends TextView {
	private static final String TAG = "DragTextView";
	private boolean isMove = false;
	private boolean isDrawFrame = true;
	private float phase = 1;
	private PointF mCenterPoint = new PointF();
	private boolean isfirstDraw = true;
	private PointF mPreMovePointF = new PointF();
	private PointF mCurMovePointF = new PointF();
	private Paint paint = new Paint();

	// Initial the view.
	public DragTextView(Context context) {
		super(context);
	}
	// Initial the view.
	public DragTextView(Context context, AttributeSet attribute) {
		this(context, attribute, 0);
		isMove = false;
	}
	// Initial the view.
	public DragTextView(Context context, AttributeSet attribute, int style) {
		super(context, attribute, style);
		isMove = false;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.i(TAG, "~~onMeasure~~");
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		if (changed) {
			adjustLayout();
		}
	}
	// On touch Event.
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int iAction = event.getAction();
		switch (iAction) {
			case MotionEvent.ACTION_DOWN:
				// Log.e(TAG, "ACTION_DOWN");
				isMove = false;
				mPreMovePointF.set(event.getX(), event.getY());
				break;
			case MotionEvent.ACTION_MOVE:
				// Log.e(TAG, "ACTION_MOVE");
				mCurMovePointF.set(event.getX(), event.getY());
				int iDeltx = (int) (mCurMovePointF.x - mPreMovePointF.x);
				int iDelty = (int) (mCurMovePointF.y - mPreMovePointF.y);
				Log.i(TAG, "~~iDeltx~~" + iDeltx + "~~iDelty~~" + iDelty);
				if (iDeltx != 0 || iDelty != 0) {
					mCenterPoint.x += mCurMovePointF.x - mPreMovePointF.x;
					mCenterPoint.y += mCurMovePointF.y - mPreMovePointF.y;
					adjustLayout();
				}
				if (Math.abs(iDeltx) > 10 || Math.abs(iDelty) > 10) {
					isMove = true;
				}
				break;
			case MotionEvent.ACTION_UP:
				// Log.e(TAG, "ACTION_UP");
				if (isMove) {
					// 触摸事件执行
				} else {
					// 执行单击事件
					performClick();
				}
				isMove = false;
				break;
			case MotionEvent.ACTION_CANCEL:
				// Log.e(TAG, "ACTION_CANCEL");
				break;
		}
		return true;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// //LogUtils.i("~onDraw~");
		if (isDrawFrame) {
			// 将边框设为黑色
			paint.setColor(android.graphics.Color.RED);
			PathEffect effects = new DashPathEffect(new float[] { 10, 10, 10,
					10 }, phase);
			paint.setPathEffect(effects);
			paint.setStrokeWidth(4);
			paint.setStyle(Paint.Style.STROKE);// 设置填满
			canvas.drawRect(0, 0, this.getMeasuredWidth(),
					this.getMeasuredHeight(), paint);// 长方形
			// 改变phase值,形成动画效果
			if (phase < Integer.MAX_VALUE) {
				phase += 1;
			} else if (phase == Integer.MAX_VALUE) {
				phase = -1;
			}
			if (phase > 0) {
				invalidate();
			}
		}
		super.onDraw(canvas);
	}
	/**
	 * 调整View的大小，位置
	 */
	public void adjustLayout() {
		int newMarginLeft = getMargins()[0];
		int newMarginTop = getMargins()[1];
		int newMarginRight = getMargins()[2];
		int newMarginBottom = getMargins()[3];
		layout(newMarginLeft, newMarginTop, newMarginRight, newMarginBottom);
	}
	private int[] getMargins() {
		int actualWidth = getMeasuredWidth();
		int actualHeight = getMeasuredHeight();
		if (isfirstDraw) {
			isfirstDraw = false;
			mCenterPoint.set(actualWidth / 2, actualHeight / 2);
		}
		int newMarginLeft = (int) (mCenterPoint.x - actualWidth / 2);
		int newMarginTop = (int) (mCenterPoint.y - actualHeight / 2);
		return new int[] { newMarginLeft, newMarginTop,
				newMarginLeft + actualWidth, newMarginTop + actualHeight };
	}
	public int[] getViewPostion() {
		int newMarginLeft = getMargins()[0];
		int newMarginTop = getMargins()[1];
		return new int[] { newMarginLeft, newMarginTop };
	}
	public void setCenterPoint(int x, int y) {
		mCenterPoint.set(x, y);
	}
	public void setFrameDraw(boolean isDrawFrame) {
		if (this.isDrawFrame != isDrawFrame) {
			this.isDrawFrame = isDrawFrame;
			invalidate();
		}
	}
	public boolean isDrawFrameDraw() {
		return isDrawFrame;
	}
}
