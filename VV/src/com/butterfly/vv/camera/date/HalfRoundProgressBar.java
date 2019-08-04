package com.butterfly.vv.camera.date;

import com.beem.project.btf.R;
import com.butterfly.vv.camera.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义半圆形SeekBar
 **/
public class HalfRoundProgressBar extends View {
	private static final String TAG = "HalfRoundProgressBar";
	public static final int BAR_WIDTH_IN_DIP = 20;
	public static final int VIEW_HEIGHT_GAP_INDIP = 4;
	/** The context 上下文 */
	private Context mContext;
	/** The listener to listen for changes SeekBar监听事件 */
	private OnSeekChangeListener mListener;
	/** The color of the progress ring 画笔圆形颜色 */
	private Paint arcColor;
	/** the color of the inside circle. Acts as background color */
	private Paint innerRingColor;
	/** The progress circle ring background */
	private Paint outerRingColor;
	/** The marker line paint */
	private Paint markerLinePaint;
	/** The angle of progress */
	private int sweepAngle = -30; // 初始化滑动图标位置，尽可能靠近弧度的开始位置。
	/** The start angle (12 O'clock 开始角度 is 270 */
	private int startAngle = 0; // 3 O'clock开始角度 is 0
	/** The width of the progress ring */
	private int barWidth = 0;
	/** The width of the view */
	private int width;
	/** The height of the view */
	private int height;
	/** The maximum progress amount 最大进度 100 */
	private int maxProgress = 100;
	/** The current progress 进度 */
	private int progress;
	// /** The progress percent 进度百分比 */
	// private int progressPercent;
	/** The radius of the inner circle 内半径 */
	private float innerRadius;
	/** The radius of the outer circle 外半径 */
	private float outerRadius;
	/** The circle's center X coordinate 圆心x坐标 */
	private float cx;
	/** The circle's center Y coordinate 圆心y坐标 */
	private float cy;
	/** The X coordinate for the top left corner of the marking drawable */
	private float dx;
	/** The Y coordinate for the top left corner of the marking drawable */
	private float dy;
	/** The X coordinate for 12 O'Clock X开始点 */
	// private float startPointX;
	/** The Y coordinate for 12 O'Clock Y开始点 */
	// private float startPointY;
	/**
	 * The X coordinate for the current position of the marker, pre adjustment to center
	 */
	private float markPointX;
	/**
	 * The Y coordinate for the current position of the marker, pre adjustment to center
	 */
	private float markPointY;
	/**
	 * The adjustment factor. This adds an adjustment of the specified size to both sides of the
	 * progress bar, allowing touch events to be processed more user friendlily (yes, I know that's
	 * not a word)
	 */
	private float adjustmentFactor = 0;
	/** The progress mark when the view isn't being progress modified */
	private Bitmap progressMark;
	/** The progress mark when the view is being progress modified. */
	private Bitmap progressMarkPressed;
	/** The flag to see if view is pressed 是否按下 */
	private boolean IS_PRESSED = false;
	/** The rectangle containing our circles and arcs. */
	private RectF rectOut = new RectF();
	private RectF rectInner = new RectF();
	// 下面的这些参数是为了能画出半圆型的角度大于180度，具体度数可根据View的空间来定
	private int startAngle1 = 300;
	private int startAngle2 = 330;
	private int mUpDegress1 = 60;
	private int mUpDegress2 = 30;
	private int mMax_angle1 = 300;
	private int mMax_angle2 = 240;
	private final int MAX_ANGLE = 180; // 有效度数是180度半圆形，两边多出来的度数作装饰用
	private int mMarkImageHeight = 0;

	/**
	 * Instantiates a new circular seek bar.
	 * @param context the context
	 * @param attrs the attrs
	 * @param defStyle the def style
	 */
	public HalfRoundProgressBar(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initView();
	}
	/**
	 * Instantiates a new circular seek bar.
	 * @param context the context
	 * @param attrs the attrs
	 */
	public HalfRoundProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}
	/**
	 * Instantiates a new circular seek bar.
	 * @param context the context
	 */
	public HalfRoundProgressBar(Context context) {
		super(context);
		mContext = context;
		initView();
	}
	/**
	 * Inits the view
	 */
	public void initDrawable() {
		mListener = new OnSeekChangeListener() {
			@Override
			public void onProgressChange(HalfRoundProgressBar view,
					int newProgress) {
			}
		};
		arcColor = new Paint();
		innerRingColor = new Paint();
		outerRingColor = new Paint();
		arcColor.setColor(Color.parseColor("#ffd8d8d8")); // Set default
		innerRingColor.setColor(Color.parseColor("#fffafdff")); // Set default
																// background
																// color to
		outerRingColor.setColor(Color.parseColor("#fff04d5a"));// Set default
																// background
																// color to
		// Gray
		arcColor.setAntiAlias(true);
		innerRingColor.setAntiAlias(true);
		outerRingColor.setAntiAlias(true);
		arcColor.setStrokeWidth(5); // 5
		innerRingColor.setStrokeWidth(5); // 5
		outerRingColor.setStrokeWidth(5); // 5
		arcColor.setStyle(Paint.Style.FILL);
		markerLinePaint = new Paint();
		markerLinePaint.setColor(Color.parseColor("#ffd8d8d8"));
		markerLinePaint.setAntiAlias(true);
		markerLinePaint.setStrokeWidth(2);
		progressMark = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.xc_round_seekbar_mark_n);
		progressMarkPressed = BitmapFactory.decodeResource(
				mContext.getResources(), R.drawable.xc_round_seekbar_mark_h);
		int size1 = progressMark.getWidth();
		int size2 = progressMarkPressed.getWidth();
		mMarkImageHeight = (size1 > size2) ? size1 : size2;
	}
	public void initView() {
		initDrawable();
	}
	/*
	 * (non-Javadoc)
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		float leftOut, rightOut, topOut, bottomOut;
		float leftInner, rightInner, topInner, bottomInner;
		barWidth = Utils.DipToPx(BAR_WIDTH_IN_DIP, mContext);
		adjustmentFactor = barWidth * 3;
		// width = getWidth(); // Get View Width
		// height = getHeight();// Get View Height
		int paddingLeftEight = (int) mContext.getResources().getDimension(
				R.dimen.round_seekbar_padding_left_right);
		int paddingTopBtm = (int) mContext.getResources().getDimension(
				R.dimen.round_seekbar_padding_top_bottom);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		width = dm.widthPixels;
		cx = width / 2; // Center X for circle
		cy = paddingTopBtm; // height / 2; // Center Y for circle
		outerRadius = width / 2 - paddingLeftEight; // size / 2; // Radius of
													// the outer circle
		height = (int) (outerRadius + paddingTopBtm * 2)
				+ Utils.DipToPx(VIEW_HEIGHT_GAP_INDIP, mContext);
		innerRadius = outerRadius - barWidth; // Radius of the inner circle
		// double upDegreesTemp =
		// Math.toDegrees(Math.asin(paddingTopBtm/innerRadius));
		// mUpDegress = (int) Math.round(upDegreesTemp);
		// startAngle2 = 360 - mUpDegress;
		// mMax_angle2 = 180 + mUpDegress*2;
		leftOut = cx - outerRadius; // Calculate left bound of our rect
		rightOut = cx + outerRadius;// Calculate right bound of our rect
		topOut = cy - outerRadius; // Calculate top bound of our rect
		bottomOut = cy + outerRadius;// Calculate bottom bound of our rect
		leftInner = cx - innerRadius; // Calculate left bound of our rect
		rightInner = cx + innerRadius;// Calculate right bound of our rect
		topInner = cy - innerRadius; // Calculate top bound of our rect
		bottomInner = cy + innerRadius;// Calculate bottom bound of our rect
		rectOut.set(leftOut, topOut, rightOut, bottomOut); // assign size to
															// rect
		rectInner.set(leftInner, topInner, rightInner, bottomInner); // assign
																		// size
																		// to
																		// rect
	}
	/*
	 * (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawArc(rectOut, startAngle2, mMax_angle2, true, outerRingColor);
		if (sweepAngle > 0) {
			canvas.drawArc(rectOut, startAngle2, 30, true, arcColor);
		}
		if (sweepAngle >= 180) {
			canvas.drawArc(rectOut, startAngle, 210, true, arcColor);
		} else if (sweepAngle > 0) {
			canvas.drawArc(rectOut, startAngle, sweepAngle, true, arcColor);
		}
		canvas.drawArc(rectInner, startAngle1, mMax_angle1, true,
				innerRingColor);
		drawMarkerLine(canvas);
		drawMarkerAtProgress(canvas);
		super.onDraw(canvas);
	}
	/**
	 * Draw marker line onto the given canvas.
	 * @param canvas the canvas
	 */
	public void drawMarkerLine(Canvas canvas) {
		float r1 = innerRadius - Utils.DipToPx(22, mContext);
		float r2 = innerRadius - Utils.DipToPx(4, mContext);
		int x1, y1, x2, y2;
		int xTemp, yTemp;
		double radian;
		int[] markerDegree = { 0, 20, 40, 60, 80, 100, 120, 140, 160, 180 };
		int[] markerPoint = new int[40];
		for (int i = 0; i < markerDegree.length; i++) {
			int degree = markerDegree[i];
			if (0 <= degree && degree < 90) {
				radian = Math.toRadians(degree);
				yTemp = (int) (Math.sin(radian) * r1);
				y1 = (int) (cy + yTemp);
				yTemp = (int) (Math.sin(radian) * r2);
				y2 = (int) (cy + yTemp);
				xTemp = (int) (Math.cos(radian) * r1);
				x1 = (int) (cx + xTemp);
				xTemp = (int) (Math.cos(radian) * r2);
				x2 = (int) (cx + xTemp);
				markerPoint[i * 4] = x1;
				markerPoint[i * 4 + 1] = y1;
				markerPoint[i * 4 + 2] = x2;
				markerPoint[i * 4 + 3] = y2;
			} else if (90 < degree && degree <= 180) {
				radian = Math.toRadians((180 - degree));
				yTemp = (int) (Math.sin(radian) * r1);
				y1 = (int) (cy + yTemp);
				yTemp = (int) (Math.sin(radian) * r2);
				y2 = (int) (cy + yTemp);
				xTemp = (int) (Math.cos(radian) * r1);
				x1 = (int) (cx - xTemp);
				xTemp = (int) (Math.cos(radian) * r2);
				x2 = (int) (cx - xTemp);
				markerPoint[i * 4] = x1;
				markerPoint[i * 4 + 1] = y1;
				markerPoint[i * 4 + 2] = x2;
				markerPoint[i * 4 + 3] = y2;
			}
		}
		for (int i = 0; i < markerDegree.length; i++) {
			float startX, startY, stopX, stopY;
			startX = markerPoint[i * 4];
			startY = markerPoint[i * 4 + 1];
			stopX = markerPoint[i * 4 + 2];
			stopY = markerPoint[i * 4 + 3];
			canvas.drawLine(startX, startY, stopX, stopY, markerLinePaint);
		}
	}
	/**
	 * Draw marker at the current progress point onto the given canvas.
	 * @param canvas the canvas
	 */
	public void drawMarkerAtProgress(Canvas canvas) {
		setMarkPoint(sweepAngle);
		dx = getXFromAngle();
		dy = getYFromAngle();
		// Log.d(TAG, "drawMarkerAtProgress, markPointX = " + markPointX +
		// ", dx = " + dx);
		// Log.d(TAG, "drawMarkerAtProgress, markPointY = " + markPointY +
		// ", dy = " + dy);
		if (IS_PRESSED) {
			canvas.drawBitmap(progressMarkPressed, dx, dy, null);
		} else {
			canvas.drawBitmap(progressMark, dx, dy, null);
		}
	}
	/**
	 * Gets the X coordinate of the arc's end arm's point of intersection with the circle
	 * @return the X coordinate
	 */
	public float getXFromAngle() {
		int size1 = progressMark.getWidth();
		int size2 = progressMarkPressed.getWidth();
		int adjust = (size1 > size2) ? size1 : size2;
		float x = markPointX - (adjust / 2);
		return x;
	}
	/**
	 * Gets the Y coordinate of the arc's end arm's point of intersection with the circle
	 * @return the Y coordinate
	 */
	public float getYFromAngle() {
		int size1 = progressMark.getHeight();
		int size2 = progressMarkPressed.getHeight();
		int adjust = (size1 > size2) ? size1 : size2;
		float y = markPointY - (adjust / 2);
		return y;
	}
	/**
	 * Get the angle.
	 * @return the angle
	 */
	public int getAngle() {
		return sweepAngle;
	}
	/**
	 * Set the angle.
	 * @param angle the new angle
	 */
	private void setAngle(int angle) {
		this.sweepAngle = angle;
	}
	/**
	 * Sets the seek bar change listener.
	 * @param listener the new seek bar change listener
	 */
	public void setSeekBarChangeListener(OnSeekChangeListener listener) {
		mListener = listener;
	}
	/**
	 * Gets the seek bar change listener.
	 * @return the seek bar change listener
	 */
	public OnSeekChangeListener getSeekBarChangeListener() {
		return mListener;
	}
	/**
	 * Gets the bar width.
	 * @return the bar width
	 */
	public int getBarWidth() {
		return barWidth;
	}
	/**
	 * Sets the bar width.
	 * @param barWidth the new bar width
	 */
	public void setBarWidth(int barWidth) {
		this.barWidth = barWidth;
	}

	/**
	 * The listener interface for receiving onSeekChange events. The class that is interested in
	 * processing a onSeekChange event implements this interface, and the object created with that
	 * class is registered with a component using the component's
	 * <code>setSeekBarChangeListener(OnSeekChangeListener)<code> method. When
	 * the onSeekChange event occurs, that object's appropriate
	 * method is invoked.
	 * @see OnSeekChangeEvent
	 */
	public interface OnSeekChangeListener {
		/**
		 * On progress change.
		 * @param view the view
		 * @param newProgress the new progress
		 */
		public void onProgressChange(HalfRoundProgressBar view, int newProgress);
	}

	/**
	 * Gets the max progress.
	 * @return the max progress
	 */
	public int getMaxProgress() {
		return maxProgress;
	}
	/**
	 * Sets the max progress.
	 * @param maxProgress the new max progress
	 */
	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
	}
	/**
	 * Gets the progress.
	 * @return the progress
	 */
	public int getProgress() {
		return progress;
	}
	/**
	 * Sets the progress.
	 * @param progress the new progress
	 */
	public void setProgress(int progress) {
		if (this.progress != progress) {
			this.progress = progress;
			int newAngle = (progress * MAX_ANGLE) / this.maxProgress;
			this.setAngle(newAngle);
			invalidate();
		}
	}
	private void updateProgress() {
		int tempProgress = 0;
		if (sweepAngle > 0) {
			tempProgress = (sweepAngle * maxProgress) / MAX_ANGLE;
		} else {
			tempProgress = 0;
		}
		this.progress = tempProgress;
		mListener.onProgressChange(this, progress);
	}
	/**
	 * Gets the progress percent.
	 * @return the progress percent
	 */
	/*
	 * public int getProgressPercent() { return progressPercent; }
	 */
	/**
	 * Sets the progress percent.
	 * @param progressPercent the new progress percent
	 */
	/*
	 * public void setProgressPercent(int progressPercent) { this.progressPercent = progressPercent;
	 * }
	 */
	/**
	 * Sets the ring background color.
	 * @param color the new ring background color
	 */
	public void setRingBackgroundColor(int color) {
		outerRingColor.setColor(color);
	}
	/**
	 * Sets the back ground color.
	 * @param color the new back ground color
	 */
	public void setBackGroundColor(int color) {
		innerRingColor.setColor(color);
	}
	/**
	 * Sets the progress color.
	 * @param color the new progress color
	 */
	public void setProgressColor(int color) {
		arcColor.setColor(color);
	}
	/*
	 * (non-Javadoc)
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		boolean up = false;
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				moved(x, y, up);
				break;
			case MotionEvent.ACTION_MOVE:
				moved(x, y, up);
				break;
			case MotionEvent.ACTION_UP:
				up = true;
				moved(x, y, up);
				break;
		}
		return true;
	}
	/**
	 * Moved.
	 * @param x the x
	 * @param y the y
	 * @param up the up
	 */
	private void moved(float x, float y, boolean up) {
		float distance = (float) Math.sqrt(Math.pow((x - cx), 2)
				+ Math.pow((y - cy), 2));
		if (distance < outerRadius + adjustmentFactor
				&& distance > innerRadius - adjustmentFactor && !up) {
			IS_PRESSED = true;
			double degrees, xTemp, yTemp;
			if (y <= cy) {
				if (x > cx) {
					xTemp = x - cx;
					yTemp = cy - y;
					double temp = Math.toDegrees(Math.atan(yTemp / xTemp));
					degrees = -temp;
					// setMarkPoint(degrees, 1);
				} else {
					xTemp = cx - x;
					yTemp = cy - y;
					double temp = Math.toDegrees(Math.atan(yTemp / xTemp));
					degrees = 180 + temp;
					// setMarkPoint(degrees, 4);
					// degrees = degrees + 180 + mUpDegress;
				}
			} else if (x > cx) {
				xTemp = x - cx;
				yTemp = y - cy;
				degrees = Math.toDegrees(Math.atan(yTemp / xTemp));
				// setMarkPoint(degrees, 2);
			} else if (x == cx) {
				// setMarkPoint(90, 2);
				degrees = 90;
			} else {
				xTemp = cx - x;
				yTemp = y - cy;
				degrees = Math.toDegrees(Math.atan(xTemp / yTemp));
				// setMarkPoint(degrees, 3);
				degrees = degrees + 90;
			}
			setAngle((int) Math.round(degrees));
			updateProgress();
			invalidate();
		} else {
			IS_PRESSED = false;
			invalidate();
		}
	}
	/*
	 * 获取弧度中心点的坐标用于画滑动图标
	 */
	/*
	 * private void setMarkPoint(double originalDegree, int location) { double radian = 0; float
	 * centerPointRadius = innerRadius + barWidth/2; double tempY, tempX; switch(location) { case 1:
	 * //第一象限 radian = Math.toRadians(originalDegree); tempY = Math.sin(radian) * centerPointRadius;
	 * tempX = Math.cos(radian) * centerPointRadius; markPointX = cx + (int)tempX; markPointY = cy -
	 * (int)tempY; break; case 2: //第二象限 radian = Math.toRadians(originalDegree); tempY =
	 * Math.sin(radian) * centerPointRadius; tempX = Math.cos(radian) * centerPointRadius;
	 * markPointX = cx + (int)tempX; markPointY = cy + (int)tempY; break; case 3: //第三象限 radian =
	 * Math.toRadians(originalDegree); tempX = Math.sin(radian) * centerPointRadius; tempY =
	 * Math.cos(radian) * centerPointRadius; markPointX = cx - (int)tempX; markPointY = cy +
	 * (int)tempY; break; case 4: //第四象限 radian = Math.toRadians(originalDegree); tempY =
	 * Math.sin(radian) * centerPointRadius; tempX = Math.cos(radian) * centerPointRadius;
	 * markPointX = cx - (int)tempX; markPointY = cy - (int)tempY; break; default: break; } }
	 */
	private void setMarkPoint(double degree) {
		double radian = 0;
		double tempY, tempX;
		float centerPointRadius = innerRadius + barWidth / 2;
		if (degree < 0) {
			double temp = Math.abs(degree);
			radian = Math.toRadians(temp);
			tempY = Math.sin(radian) * centerPointRadius;
			tempX = Math.cos(radian) * centerPointRadius;
			markPointX = cx + (int) tempX;
			markPointY = cy - (int) tempY;
			if (markPointY < mMarkImageHeight / 2) {
				markPointY = mMarkImageHeight / 2;
				// 勾股定理求markPointX
				double temp1 = Math.pow(centerPointRadius, 2)
						- Math.pow((cy - markPointY), 2);
				double temp2 = Math.sqrt(temp1);
				markPointX = (float) (cx + temp2);
			}
		} else if (degree > 180) {
			double temp = degree - 180;
			radian = Math.toRadians(temp);
			tempY = Math.sin(radian) * centerPointRadius;
			tempX = Math.cos(radian) * centerPointRadius;
			markPointX = cx - (int) tempX;
			markPointY = cy - (int) tempY;
			if (markPointY < mMarkImageHeight / 2) {
				markPointY = mMarkImageHeight / 2;
				// 勾股定理求markPointX ，使滑动图标显示全，不至于显示一半或到外面去
				double temp1 = Math.pow(centerPointRadius, 2)
						- Math.pow((cy - markPointY), 2);
				double temp2 = Math.sqrt(temp1);
				markPointX = (float) (cx - temp2);
			}
		} else if (degree == 0) {
			markPointX = cx + centerPointRadius;
			markPointY = cy;
		} else if (degree == 90) {
			markPointX = cx;
			markPointY = cy + centerPointRadius;
		} else if (degree == 180) {
			markPointX = cx - centerPointRadius;
			markPointY = cy;
		} else if (0 < degree && degree < 90) {
			radian = Math.toRadians(degree);
			tempY = Math.sin(radian) * centerPointRadius;
			tempX = Math.cos(radian) * centerPointRadius;
			markPointX = cx + (int) tempX;
			markPointY = cy + (int) tempY;
		} else if (90 < degree && degree < 180) {
			degree -= 90;
			radian = Math.toRadians(degree);
			tempX = Math.sin(radian) * centerPointRadius;
			tempY = Math.cos(radian) * centerPointRadius;
			markPointX = cx - (int) tempX;
			markPointY = cy + (int) tempY;
		}
		Log.d(TAG, "setMarkPoint, degree = " + degree);
		Log.d(TAG, "setMarkPoint, markPointX = " + markPointX);
		Log.d(TAG, "setMarkPoint, markPointY = " + markPointY);
	}
	/**
	 * Gets the adjustment factor.
	 * @return the adjustment factor
	 */
	public float getAdjustmentFactor() {
		return adjustmentFactor;
	}
	/**
	 * Sets the adjustment factor.
	 * @param adjustmentFactor the new adjustment factor
	 */
	public void setAdjustmentFactor(float adjustmentFactor) {
		this.adjustmentFactor = adjustmentFactor;
	}
}
