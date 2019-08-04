package com.beem.project.btf.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;

/**
 * @ClassName: DragTextViewSub
 * @Description: 拖动TextView第二弹
 * @author: yuedong bao
 * @date: 2015-11-23 下午7:38:04
 */
public class DragTextViewSub extends TextView implements OnGestureListener {
	private boolean isDrawFrame = true;
	private float phase = 1;
	private PointF mCenterPoint = new PointF();
	private PointF mPreMovePointF = new PointF();
	private PointF mCurMovePointF = new PointF();
	private Paint paint = new Paint();
	private boolean canMoveOutParent = false;
	private GestureDetector detector;
	private boolean canMove = true;
	private Drawable tinkleDrawable;
	private boolean isTinking;
	private final int startAlpha = 200;

	// Initial the view.
	public DragTextViewSub(Context context) {
		this(context, null, 0);
	}
	// Initial the view.
	public DragTextViewSub(Context context, AttributeSet attribute) {
		this(context, attribute, 0);
	}
	// Initial the view.
	public DragTextViewSub(Context context, AttributeSet attribute, int style) {
		super(context, attribute, style);
		init();
	}
	private void init() {
		getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						mCenterPoint.set(getWidth() / 2 + getLeft(),
								getHeight() / 2 + getTop());
					}
				});
		detector = new GestureDetector(getContext(), this);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec / 2);
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isCanMove()) {
			int iAction = event.getAction();
			switch (iAction) {
				case MotionEvent.ACTION_DOWN:
					mPreMovePointF.set(event.getX(), event.getY());
					break;
				case MotionEvent.ACTION_MOVE:
					mCurMovePointF.set(event.getX(), event.getY());
					int iDeltx = (int) (mCurMovePointF.x - mPreMovePointF.x);
					int iDelty = (int) (mCurMovePointF.y - mPreMovePointF.y);
					if (iDeltx != 0 || iDelty != 0) {
						mCenterPoint.x += mCurMovePointF.x - mPreMovePointF.x;
						mCenterPoint.y += mCurMovePointF.y - mPreMovePointF.y;
						if (!canMoveOutParent) {
							ViewGroup parent = (ViewGroup) getParent();
							int actualWidth = getWidth();
							int actualHeight = getHeight();
							mCenterPoint.x = (int) Math
									.max(actualWidth / 2
											+ parent.getPaddingLeft(),
											Math.min(
													mCenterPoint.x,
													parent.getWidth()
															- actualWidth
															/ 2
															- parent.getPaddingRight()));
							mCenterPoint.y = (int) Math.max(actualHeight / 2
									+ parent.getPaddingTop(), Math.min(
									mCenterPoint.y,
									parent.getHeight() - actualHeight / 2
											- parent.getPaddingBottom()));
						}
						adjustLayout();
					}
					break;
				case MotionEvent.ACTION_UP:
					break;
				case MotionEvent.ACTION_CANCEL:
					break;
			}
		}
		detector.onTouchEvent(event);
		return true;
	}
	@Override
	protected void onDraw(Canvas canvas) {
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
			phase = (phase + 1) % 360;
			invalidate();
		}
		super.onDraw(canvas);
	}
	public void tinkle() {
		if (isTinking)
			return;
		isTinking = true;
		setBackgroundDrawable(tinkleDrawable);
		tinkleRunnable.alpha = startAlpha;
		postDelayed(tinkleRunnable, 100);
	}

	private class TinkleTask implements Runnable {
		public int alpha = startAlpha;

		@Override
		public void run() {
			if (alpha > 30) {
				tinkleDrawable.setAlpha(alpha);
				setBackgroundDrawable(tinkleDrawable);
				if (alpha < startAlpha - 20) {
					alpha -= 5;
				} else {
					alpha -= 1;
				}
				postDelayed(this, 30);
			} else {
				setBackgroundDrawable(null);
				isTinking = false;
				removeCallbacks(this);
			}
		}
	}

	private TinkleTask tinkleRunnable = new TinkleTask();

	public void tinkleOpen() {
		tinkleDrawable.setAlpha(startAlpha);
		setBackgroundDrawable(tinkleDrawable);
		removeCallbacks(tinkleRunnable);
		isTinking = false;
	}
	public void tinkleClose() {
		tinkle();
	}
	public void tinkleCloseImediately() {
		isTinking = false;
		setBackgroundDrawable(null);
		tinkleRunnable.alpha = 0;
		removeCallbacks(tinkleRunnable);
	}
	public void setTinkleDrawable(int resid) {
		tinkleDrawable = getResources().getDrawable(resid);
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
		int actualWidth = getWidth();
		int actualHeight = getHeight();
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
	public void setFrameDraw(boolean isDrawFrame) {
		if (this.isDrawFrame != isDrawFrame) {
			this.isDrawFrame = isDrawFrame;
			invalidate();
		}
	}
	public boolean isDrawFrameDraw() {
		return isDrawFrame;
	}
	public void setCanMoveOutParent(boolean canMoveOutParent) {
		this.canMoveOutParent = canMoveOutParent;
	}
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		performClick();
		return false;
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		performLongClick();
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}
	public boolean isCanMove() {
		return canMove;
	}
	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}
	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		if (visibility == View.VISIBLE) {
			tinkle();
		}
	}
}
