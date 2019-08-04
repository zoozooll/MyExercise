package com.beem.project.btf.ui.views.SingleTouchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.beem.project.btf.R;

/**
 * 单手对图片进行缩放，旋转，平移操作，详情请查看
 * @blog http://blog.csdn.net/xiaanming/article/details/42833893
 * @author xiaanming
 */
public class SingleTouchView extends View {
	private static final String TAG = "SingleTouchView";
	/**
	 * 图片的最大缩放比例
	 */
	public static final float MAX_SCALE = 1.5f;
	/**
	 * 图片的最小缩放比例
	 */
	public static final float MIN_SCALE = 0.3f;
	/**
	 * 控制缩放，旋转图标所在四个点得位置
	 */
	public static final int LEFT_TOP = 0;
	public static final int RIGHT_TOP = 1;
	public static final int RIGHT_BOTTOM = 2;
	public static final int LEFT_BOTTOM = 3;
	/**
	 * 一些默认的常量
	 */
	public static final int DEFAULT_FRAME_PADDING = 8;
	public static final int DEFAULT_FRAME_WIDTH = 1;
	public static final int DEFAULT_FRAME_COLOR = Color.WHITE;
	public static final float DEFAULT_SCALE = 1.0f;
	public static final float DEFAULT_DEGREE = 0;
	public static final int DEFAULT_CONTROL_LOCATION = RIGHT_TOP;
	public static final boolean DEFAULT_EDITABLE = true;
	public static final int DEFAULT_OTHER_DRAWABLE_WIDTH = 50;
	public static final int DEFAULT_OTHER_DRAWABLE_HEIGHT = 50;
	/**
	 * 用于旋转缩放的Bitmap
	 */
	private Bitmap mBitmap;
	/**
	 * SingleTouchView的中心点坐标，相对于其父类布局而言的
	 */
	private PointF mCenterPoint = new PointF();
	/**
	 * View的宽度和高度，随着图片的旋转而变化(不包括控制旋转，缩放图片的宽高)
	 */
	private int mViewWidth, mViewHeight;
	/**
	 * 图片的旋转角度
	 */
	private float mDegree = DEFAULT_DEGREE;
	/**
	 * 图片的缩放比例
	 */
	private float mScale = DEFAULT_SCALE;
	/**
	 * 用于缩放，旋转，平移的矩阵
	 */
	private Matrix matrix = new Matrix();
	/**
	 * SingleTouchView距离父类布局的左间距
	 */
	private int mViewMarginLeft;
	/**
	 * SingleTouchView距离父类布局的上间距
	 */
	private int mViewMarginTop;
	/**
	 * 图片四个点坐标
	 */
	private Point mLTPoint;
	private Point mRTPoint;
	private Point mRBPoint;
	private Point mLBPoint;
	/**
	 * 用于缩放，旋转的控制点的坐标
	 */
	private Point mControlPoint = new Point();
	private Point mControlPoint2 = new Point();
	/**
	 * 用于缩放，旋转的图标
	 */
	private Drawable controlDrawable1;// 用于缩放
	private Drawable controlDrawable2;// 用于旋转
	/**
	 * 缩放，旋转图标的宽和高
	 */
	private int mDrawableWidth1, mDrawableHeight1;// 缩放图标的宽高
	private int mDrawableWidth2, mDrawableHeight2;// 旋转图标的宽高
	/**
	 * 画外围框的Path
	 */
	private Path mPath = new Path();
	/**
	 * 画外围框的画笔
	 */
	private Paint mPaint;
	/**
	 * 初始状态
	 */
	public static final int STATUS_INIT = 0;
	/**
	 * 拖动状态
	 */
	public static final int STATUS_DRAG = 1;
	/**
	 * 旋转状态和放大状态
	 */
	public static final int STATUS_ROTATE_ZOOM = 2;
	/**
	 * 翻转状态
	 */
	public static final int STATUS_FLIP = 3;
	/**
	 * 双手指缩放状态
	 */
	public static final int STATUS_TOUCH_ZOOM = 4;
	/**
	 * 当前所处的状态
	 */
	private int mStatus = STATUS_INIT;
	/**
	 * 外边框与图片之间的间距, 单位是dip
	 */
	private int framePadding = DEFAULT_FRAME_PADDING;
	/**
	 * 外边框颜色
	 */
	private int frameColor = DEFAULT_FRAME_COLOR;
	/**
	 * 外边框线条粗细, 单位是 dip
	 */
	private int frameWidth = DEFAULT_FRAME_WIDTH;
	/**
	 * 是否处于可以缩放，平移，旋转状态
	 */
	private boolean isEditable = DEFAULT_EDITABLE;
	private DisplayMetrics metrics;
	private PointF mPreMovePointF = new PointF();
	private PointF mCurMovePointF = new PointF();
	/**
	 * 图片在旋转时x方向的偏移量
	 */
	private int offsetX;
	/**
	 * 图片在旋转时y方向的偏移量
	 */
	private int offsetY;
	/**
	 * 控制图标所在的位置（比如左上，右上，左下，右下）
	 */
	private int controlLocation1 = DEFAULT_CONTROL_LOCATION;
	private int controlLocation2 = DEFAULT_CONTROL_LOCATION;
	private Matrix drawablematrix = new Matrix();
	private boolean isfirstDraw = true;
	/**
	 * 记录上次两指之间的距离
	 */
	private float startDis;

	public SingleTouchView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public SingleTouchView(Context context) {
		this(context, null);
	}
	public SingleTouchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		obtainStyledAttributes(attrs);
		init();
	}
	/**
	 * 获取自定义属性
	 * @param attrs
	 */
	private void obtainStyledAttributes(AttributeSet attrs) {
		metrics = getContext().getResources().getDisplayMetrics();
		framePadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, DEFAULT_FRAME_PADDING, metrics);
		frameWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, DEFAULT_FRAME_WIDTH, metrics);
		TypedArray mTypedArray = getContext().obtainStyledAttributes(attrs,
				R.styleable.SingleTouchView);
		Drawable srcDrawble = mTypedArray
				.getDrawable(R.styleable.SingleTouchView_image_src);
		if (srcDrawble instanceof BitmapDrawable) {
			BitmapDrawable bd = (BitmapDrawable) srcDrawble;
			this.mBitmap = bd.getBitmap();
		}
		framePadding = mTypedArray.getDimensionPixelSize(
				R.styleable.SingleTouchView_framePadding, framePadding);
		frameWidth = mTypedArray.getDimensionPixelSize(
				R.styleable.SingleTouchView_frameWidth, frameWidth);
		frameColor = mTypedArray.getColor(
				R.styleable.SingleTouchView_frameColor, DEFAULT_FRAME_COLOR);
		mScale = mTypedArray.getFloat(R.styleable.SingleTouchView_scale,
				DEFAULT_SCALE);
		mDegree = mTypedArray.getFloat(R.styleable.SingleTouchView_degree,
				DEFAULT_DEGREE);
		controlDrawable1 = mTypedArray
				.getDrawable(R.styleable.SingleTouchView_controlDrawable1);
		controlDrawable2 = mTypedArray
				.getDrawable(R.styleable.SingleTouchView_controlDrawable2);
		controlLocation1 = mTypedArray.getInt(
				R.styleable.SingleTouchView_controlLocation1,
				DEFAULT_CONTROL_LOCATION);
		controlLocation2 = mTypedArray.getInt(
				R.styleable.SingleTouchView_controlLocation2,
				DEFAULT_CONTROL_LOCATION);
		isEditable = mTypedArray.getBoolean(
				R.styleable.SingleTouchView_editable, DEFAULT_EDITABLE);
		mTypedArray.recycle();
	}
	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(frameColor);
		mPaint.setStrokeWidth(frameWidth);
		mPaint.setStyle(Style.STROKE);
		if (controlDrawable1 == null) {
			controlDrawable1 = getContext().getResources().getDrawable(
					R.drawable.scale_icon);
		}
		if (controlDrawable2 == null) {
			controlDrawable2 = getContext().getResources().getDrawable(
					R.drawable.scale_icon);
		}
		mDrawableWidth1 = controlDrawable1.getIntrinsicWidth();
		mDrawableHeight1 = controlDrawable1.getIntrinsicHeight();
		mDrawableWidth2 = controlDrawable2.getIntrinsicWidth();
		mDrawableHeight2 = controlDrawable2.getIntrinsicHeight();
		transformDraw();
	}
	/*
	 * @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { int
	 * widthMode = MeasureSpec.getMode(widthMeasureSpec); int widthSize =
	 * MeasureSpec.getSize(widthMeasureSpec); int heightMode =
	 * MeasureSpec.getMode(heightMeasureSpec); int heightSize =
	 * MeasureSpec.getSize(heightMeasureSpec); int width = 0; int height = 0; if (widthMode ==
	 * MeasureSpec.EXACTLY) { width = widthSize; } else { width = mViewWidth +
	 * gethalfDrawableSize()[0] * 2; } if (heightMode == MeasureSpec.EXACTLY) { height = heightSize;
	 * } else { height = mViewHeight + gethalfDrawableSize()[1] * 2; } //LogUtils.i("~~widthMode=" +
	 * widthMode + ", width=" + width); //setMeasuredDimension(width, height); }
	 */
	/**
	 * 调整View的大小，位置
	 */
	public void adjustLayout() {
		int newMarginLeft = getMargins()[0];
		int newMarginTop = getMargins()[1];
		int newMarginRight = getMargins()[2];
		int newMarginBottom = getMargins()[3];
		layout(newMarginLeft, newMarginTop, newMarginRight, newMarginBottom);
		/*
		 * //LogUtils.i("~~adjustLayout newMarginLeft=" + newMarginLeft
		 * +", newMarginTop="+newMarginTop + ", newMarginRight=" +newMarginRight +
		 * ", newMarginBottom="+newMarginBottom);
		 */
	}
	private int[] getMargins() {
		// //LogUtils.i("~~getMargins~~");
		int actualWidth = mViewWidth + gethalfDrawableSize()[0] * 2;
		int actualHeight = mViewHeight + gethalfDrawableSize()[1] * 2;
		// //LogUtils.i("~~getMargins-actualWidth~~" + actualWidth + "~~getMargins-actualHeight~~" +
		// actualHeight);
		if (isfirstDraw) {
			isfirstDraw = false;
			mCenterPoint.set(actualWidth / 2, actualHeight / 2);
		}
		int newMarginLeft = (int) (mCenterPoint.x - actualWidth / 2);
		int newMarginTop = (int) (mCenterPoint.y - actualHeight / 2);
		// //LogUtils.i("~~getMargins-newMarginLeft~~" + newMarginLeft + "~~getMargins-newMarginTop~~"
		// + newMarginTop);
		if (mViewMarginLeft != newMarginLeft || mViewMarginTop != newMarginTop) {
			mViewMarginLeft = newMarginLeft;
			mViewMarginTop = newMarginTop;
		}
		return new int[] { newMarginLeft, newMarginTop,
				newMarginLeft + actualWidth, newMarginTop + actualHeight };
	}
	/**
	 * 设置旋转图
	 * @param bitmap
	 */
	public void setImageBitamp(Bitmap bitmap, boolean isChangeScale) {
		this.mBitmap = bitmap;
		transformDraw(isChangeScale);
	}
	/**
	 * 设置旋转图
	 * @param drawable
	 */
	public void setImageDrawable(Drawable drawable) {
		this.mBitmap = drawable2Bitmap(drawable);
		transformDraw();
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (left == 0 && top == 0) {
			// 解决控件第一次布局变化不刷新的问题
		}
		// invalidate();
	}
	/**
	 * 从Drawable中获取Bitmap对象
	 * @param drawable
	 * @return
	 */
	private Bitmap drawable2Bitmap(Drawable drawable) {
		try {
			if (drawable == null) {
				return null;
			}
			if (drawable instanceof BitmapDrawable) {
				return ((BitmapDrawable) drawable).getBitmap();
			}
			int intrinsicWidth = drawable.getIntrinsicWidth();
			int intrinsicHeight = drawable.getIntrinsicHeight();
			Bitmap bitmap = Bitmap.createBitmap(
					intrinsicWidth <= 0 ? DEFAULT_OTHER_DRAWABLE_WIDTH
							: intrinsicWidth,
					intrinsicHeight <= 0 ? DEFAULT_OTHER_DRAWABLE_HEIGHT
							: intrinsicHeight, Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
			return bitmap;
		} catch (OutOfMemoryError e) {
			return null;
		}
	}
	private Drawable MatrixDrawable(Drawable drawable, Matrix matrix) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap oldbmp = drawable2Bitmap(drawable); // drawable 转换成 bitmap
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
				matrix, true);
		// 建立新的 bitmap ，其内容是对原 bitmap 的缩放后的图
		return new BitmapDrawable(newbmp); // 把 bitmap 转换成 drawable 并返回
	}
	/**
	 * 根据id设置旋转图
	 * @param resId
	 */
	public void setImageResource(int resId) {
		Drawable drawable = getContext().getResources().getDrawable(resId);
		setImageDrawable(drawable);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mBitmap == null)
			return;
		// 每次draw之前调整View的位置和大小
		adjustLayout();
		// //LogUtils.i("---onDraw enter");
		canvas.drawBitmap(mBitmap, matrix, null);
		// 处于可编辑状态才画边框和控制图标
		if (isEditable) {
			mPath.reset();
			mPath.moveTo(mLTPoint.x, mLTPoint.y);
			mPath.lineTo(mRTPoint.x, mRTPoint.y);
			mPath.lineTo(mRBPoint.x, mRBPoint.y);
			mPath.lineTo(mLBPoint.x, mLBPoint.y);
			mPath.lineTo(mLTPoint.x, mLTPoint.y);
			mPath.lineTo(mRTPoint.x, mRTPoint.y);
			canvas.drawPath(mPath, mPaint);
			// //LogUtils.i("~~onDraw~~ mRTPoint.x=" +mRTPoint.x + ", mRTPoint.y=" + mRTPoint.y);
			// //LogUtils.i("~~onDraw~~ mRBPoint.x=" +mRBPoint.x + ", mRBPoint.y=" + mRBPoint.y);
			// 画图标1
			Drawable drawable1 = MatrixDrawable(controlDrawable1,
					drawablematrix);
			drawable1.setBounds(mControlPoint.x - mDrawableWidth1 / 2,
					mControlPoint.y - mDrawableHeight1 / 2, mControlPoint.x
							+ mDrawableWidth1 / 2, mControlPoint.y
							+ mDrawableHeight1 / 2);
			drawable1.draw(canvas);
			// //LogUtils.i("~~onDraw~~ mControlPoint.x=" +mControlPoint.x + ", mControlPoint.y=" +
			// mControlPoint.y);
			// 画图标2
			Drawable drawable2 = MatrixDrawable(controlDrawable2,
					drawablematrix);
			drawable2.setBounds(mControlPoint2.x - mDrawableWidth2 / 2,
					mControlPoint2.y - mDrawableHeight2 / 2, mControlPoint2.x
							+ mDrawableWidth2 / 2, mControlPoint2.y
							+ mDrawableHeight2 / 2);
			drawable2.draw(canvas);
			// //LogUtils.i("---onDraw drawable");
		}
	}
	/**
	 * 设置Matrix, 强制刷新
	 */
	public void transformDraw() {
		transformDraw(true);
	}
	public void transformDraw(boolean ischangeScale) {
		//LogUtils.i("~~transformDraw~~");
		int bitmapWidth = 0;
		int bitmapHeight = 0;
		// 设置缩放比例
		if (ischangeScale) {
			bitmapWidth = (int) (mBitmap.getWidth() * mScale);
			bitmapHeight = (int) (mBitmap.getHeight() * mScale);
			matrix.setScale(mScale, mScale);
		} else {
			bitmapWidth = mBitmap.getWidth();
			bitmapHeight = mBitmap.getHeight();
			matrix.setScale(1.0f, 1.0f);
		}
		computeRect(-framePadding, -framePadding, bitmapWidth + framePadding,
				bitmapHeight + framePadding, mDegree);
		drawablematrix.setScale(1.0f, 1.0f);
		// 绕着图片中心进行旋转
		matrix.postRotate(mDegree % 360, bitmapWidth / 2, bitmapHeight / 2);
		drawablematrix.postRotate(mDegree % 360, bitmapWidth / 2,
				bitmapHeight / 2);
		// 设置画该图片的起始点
		matrix.postTranslate(offsetX + gethalfDrawableSize()[0], offsetY
				+ gethalfDrawableSize()[1]);
		drawablematrix.postTranslate(offsetX + gethalfDrawableSize()[0],
				offsetY + gethalfDrawableSize()[1]);
		// 每次draw之前调整View的位置和大小
		// adjustLayout();
		invalidate();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isEditable) {
			return super.onTouchEvent(event);
		}
		PointF midPoint = null; // 中心点
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				mPreMovePointF.set(event.getX() + mViewMarginLeft, event.getY()
						+ mViewMarginTop);
				mStatus = JudgeStatus(event.getX(), event.getY());
				if (mStatus == STATUS_FLIP) {
					mBitmap = convertBmp(mBitmap);
					transformDraw();
				}
				break;
			case MotionEvent.ACTION_UP:
				mStatus = STATUS_INIT;
				break;
			case MotionEvent.ACTION_MOVE:
				mCurMovePointF.set(event.getX() + mViewMarginLeft, event.getY()
						+ mViewMarginTop);
				if (mStatus == STATUS_ROTATE_ZOOM) {
					float scale = 1f;
					int halfBitmapWidth = mBitmap.getWidth() / 2;
					int halfBitmapHeight = mBitmap.getHeight() / 2;
					// 图片某个点到图片中心的距离
					float bitmapToCenterDistance = FloatMath
							.sqrt(halfBitmapWidth * halfBitmapWidth
									+ halfBitmapHeight * halfBitmapHeight);
					// 移动的点到图片中心的距离
					float moveToCenterDistance = distance4PointF(mCenterPoint,
							mCurMovePointF);
					// 计算缩放比例
					scale = moveToCenterDistance / bitmapToCenterDistance;
					// 缩放比例的界限判断
					if (scale <= MIN_SCALE) {
						scale = MIN_SCALE;
					} else if (scale >= MAX_SCALE) {
						scale = MAX_SCALE;
					}
					// 角度
					double a = distance4PointF(mCenterPoint, mPreMovePointF);
					double b = distance4PointF(mPreMovePointF, mCurMovePointF);
					double c = distance4PointF(mCenterPoint, mCurMovePointF);
					double cosb = (a * a + c * c - b * b) / (2 * a * c);
					if (cosb >= 1) {
						cosb = 1f;
					}
					double radian = Math.acos(cosb);
					float newDegree = (float) radianToDegree(radian);
					// center -> proMove的向量， 我们使用PointF来实现
					PointF centerToProMove = new PointF(
							(mPreMovePointF.x - mCenterPoint.x),
							(mPreMovePointF.y - mCenterPoint.y));
					// center -> curMove 的向量
					PointF centerToCurMove = new PointF(
							(mCurMovePointF.x - mCenterPoint.x),
							(mCurMovePointF.y - mCenterPoint.y));
					// 向量叉乘结果, 如果结果为负数， 表示为逆时针， 结果为正数表示顺时针
					float result = centerToProMove.x * centerToCurMove.y
							- centerToProMove.y * centerToCurMove.x;
					if (result < 0) {
						newDegree = -newDegree;
					}
					mDegree = mDegree + newDegree;
					mScale = scale;
					transformDraw();
				} else if (mStatus == STATUS_DRAG) {
					// 修改中心点
					mCenterPoint.x += mCurMovePointF.x - mPreMovePointF.x;
					mCenterPoint.y += mCurMovePointF.y - mPreMovePointF.y;
					adjustLayout();
				} else if (mStatus == STATUS_TOUCH_ZOOM) {
					float endDis = distanceBetweenFingers(event); // 结束距离
					//LogUtils.i("---jj onTouch MOVE, ZOOM endDis=" + endDis);
					if (endDis != startDis && endDis > 10f) {
						float scale = endDis / startDis; // 放大倍数
						mScale = mScale * scale;
						if (mScale <= MIN_SCALE) {
							mScale = MIN_SCALE;
						} else if (mScale >= MAX_SCALE) {
							mScale = MAX_SCALE;
						}
						startDis = endDis;
						// mScale = totalRatio;
						//LogUtils.i("---jj onTouch MOVE, ZOOM scale=" + scale);
						transformDraw();
					}
				}
				mPreMovePointF.set(mCurMovePointF);
				break;
			// 有手指离开屏幕，但屏幕还有触点（手指）
			case MotionEvent.ACTION_POINTER_UP:
				mStatus = STATUS_DRAG;
				break;
			// 当屏幕上已经有触点（手指），再有一个手指压下屏幕，切换到缩放模式
			case MotionEvent.ACTION_POINTER_DOWN:
				//LogUtils.i("---onTouchEvent ACTION_POINTER_DOWN enter-----");
				mStatus = STATUS_TOUCH_ZOOM;
				startDis = distanceBetweenFingers(event);
				if (startDis > 10f) {
					midPoint = mid(event);
				}
				//LogUtils.i("---jj onTouch MOVE, ZOOM startDis=" + startDis + ", mid.x=" + midPoint.x + ",mid.y="
				//						+ midPoint.y);
				break;
		}
		return true;
	}
	/**
	 * 获取四个点和View的大小
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @param degree
	 */
	private void computeRect(int left, int top, int right, int bottom,
			float degree) {
		//LogUtils.i("~~computeRect~~");
		Point lt = new Point(left, top);
		Point rt = new Point(right, top);
		Point rb = new Point(right, bottom);
		Point lb = new Point(left, bottom);
		Point cp = new Point((left + right) / 2, (top + bottom) / 2);
		mLTPoint = obtainRoationPoint(cp, lt, degree);
		mRTPoint = obtainRoationPoint(cp, rt, degree);
		mRBPoint = obtainRoationPoint(cp, rb, degree);
		mLBPoint = obtainRoationPoint(cp, lb, degree);
		// 计算X坐标最大的值和最小的值
		int maxCoordinateX = getMaxValue(mLTPoint.x, mRTPoint.x, mRBPoint.x,
				mLBPoint.x);
		int minCoordinateX = getMinValue(mLTPoint.x, mRTPoint.x, mRBPoint.x,
				mLBPoint.x);
		mViewWidth = maxCoordinateX - minCoordinateX;
		// 计算Y坐标最大的值和最小的值
		int maxCoordinateY = getMaxValue(mLTPoint.y, mRTPoint.y, mRBPoint.y,
				mLBPoint.y);
		int minCoordinateY = getMinValue(mLTPoint.y, mRTPoint.y, mRBPoint.y,
				mLBPoint.y);
		mViewHeight = maxCoordinateY - minCoordinateY;
		// View中心点的坐标
		Point viewCenterPoint = new Point(
				(maxCoordinateX + minCoordinateX) / 2,
				(maxCoordinateY + minCoordinateY) / 2);
		offsetX = mViewWidth / 2 - viewCenterPoint.x;
		offsetY = mViewHeight / 2 - viewCenterPoint.y;
		addJustPos();
	}
	public int[] gethalfDrawableSize() {
		int halfDrawableWidth, halfDrawableHeight;
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(controlLocation1);
		list.add(controlLocation2);
		if (list.contains(LEFT_TOP) && list.contains(RIGHT_TOP)
				|| (list.contains(LEFT_BOTTOM) && list.contains(RIGHT_BOTTOM))) {
			// 水平同侧
			halfDrawableWidth = (mDrawableWidth1 + mDrawableWidth2) / 2;
			halfDrawableHeight = Math.max(mDrawableHeight1, mDrawableHeight2) / 2;
		} else if (list.contains(LEFT_TOP) && list.contains(LEFT_BOTTOM)
				|| (list.contains(RIGHT_TOP) && list.contains(RIGHT_BOTTOM))) {
			// 垂直同侧
			halfDrawableWidth = Math.max(mDrawableWidth1, mDrawableWidth2) / 2;
			halfDrawableHeight = (mDrawableHeight1 + mDrawableHeight2) / 2;
		} else {
			// 斜对角
			halfDrawableWidth = (mDrawableWidth1 + mDrawableWidth2) / 2;
			halfDrawableHeight = (mDrawableHeight1 + mDrawableHeight2) / 2;
		}
		return new int[] { halfDrawableWidth, halfDrawableHeight };
	}
	private void addJustPos() {
		// //LogUtils.i("~~addJustPos~~");
		int halfDrawableWidth = gethalfDrawableSize()[0], halfDrawableHeight = gethalfDrawableSize()[1];
		// 将Bitmap的四个点的X的坐标移动offsetX + halfDrawableWidth
		mLTPoint.x += (offsetX + halfDrawableWidth);
		mRTPoint.x += (offsetX + halfDrawableWidth);
		mRBPoint.x += (offsetX + halfDrawableWidth);
		mLBPoint.x += (offsetX + halfDrawableWidth);
		// 将Bitmap的四个点的Y坐标移动offsetY + halfDrawableHeight
		mLTPoint.y += (offsetY + halfDrawableHeight);
		mRTPoint.y += (offsetY + halfDrawableHeight);
		mRBPoint.y += (offsetY + halfDrawableHeight);
		mLBPoint.y += (offsetY + halfDrawableHeight);
		mControlPoint = LocationToPoint(controlLocation1);
		mControlPoint2 = LocationToPoint(controlLocation2);
		////LogUtils.i("~~addJustPos~~ mControlPoint.x=" + mControlPoint.x + ", mControlPoint.y=" + mControlPoint.y);
	}
	/**
	 * 图像水平翻转
	 */
	private Bitmap convertBmp(Bitmap bmp) {
		int w = bmp.getWidth();
		int h = bmp.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(-1, 1); // 镜像垂直翻转
		Bitmap convertBmp = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);
		return convertBmp;
	}
	/**
	 * 根据位置判断控制图标处于那个点
	 * @return
	 */
	private Point LocationToPoint(int location) {
		switch (location) {
			case LEFT_TOP:
				return mLTPoint;
			case RIGHT_TOP:
				return mRTPoint;
			case RIGHT_BOTTOM:
				return mRBPoint;
			case LEFT_BOTTOM:
				return mLBPoint;
		}
		return mLTPoint;
	}
	/**
	 * 获取变长参数最大的值
	 * @param array
	 * @return
	 */
	public int getMaxValue(Integer... array) {
		List<Integer> list = Arrays.asList(array);
		Collections.sort(list);
		return list.get(list.size() - 1);
	}
	/**
	 * 获取变长参数最小的值
	 * @param array
	 * @return
	 */
	public int getMinValue(Integer... array) {
		List<Integer> list = Arrays.asList(array);
		Collections.sort(list);
		return list.get(0);
	}
	/**
	 * 获取旋转某个角度之后的点
	 * @param viewCenter
	 * @param source
	 * @param degree
	 * @return
	 */
	private Point obtainRoationPoint(Point center, Point source, float degree) {
		// 两者之间的距离
		Point disPoint = new Point();
		disPoint.x = source.x - center.x;
		disPoint.y = source.y - center.y;
		// 没旋转之前的弧度
		double originRadian = 0;
		// 没旋转之前的角度
		double originDegree = 0;
		// 旋转之后的角度
		double resultDegree = 0;
		// 旋转之后的弧度
		double resultRadian = 0;
		// 经过旋转之后点的坐标
		Point resultPoint = new Point();
		double distance = Math.sqrt(disPoint.x * disPoint.x + disPoint.y
				* disPoint.y);
		if (disPoint.x == 0 && disPoint.y == 0) {
			return center;
			// 第一象限
		} else if (disPoint.x >= 0 && disPoint.y >= 0) {
			// 计算与x正方向的夹角
			originRadian = Math.asin(disPoint.y / distance);
			// 第二象限
		} else if (disPoint.x < 0 && disPoint.y >= 0) {
			// 计算与x正方向的夹角
			originRadian = Math.asin(Math.abs(disPoint.x) / distance);
			originRadian = originRadian + Math.PI / 2;
			// 第三象限
		} else if (disPoint.x < 0 && disPoint.y < 0) {
			// 计算与x正方向的夹角
			originRadian = Math.asin(Math.abs(disPoint.y) / distance);
			originRadian = originRadian + Math.PI;
		} else if (disPoint.x >= 0 && disPoint.y < 0) {
			// 计算与x正方向的夹角
			originRadian = Math.asin(disPoint.x / distance);
			originRadian = originRadian + Math.PI * 3 / 2;
		}
		// 弧度换算成角度
		originDegree = radianToDegree(originRadian);
		resultDegree = originDegree + degree;
		// 角度转弧度
		resultRadian = degreeToRadian(resultDegree);
		resultPoint.x = (int) Math.round(distance * Math.cos(resultRadian));
		resultPoint.y = (int) Math.round(distance * Math.sin(resultRadian));
		resultPoint.x += center.x;
		resultPoint.y += center.y;
		return resultPoint;
	}
	/**
	 * 弧度换算成角度
	 * @return
	 */
	public static double radianToDegree(double radian) {
		return radian * 180 / Math.PI;
	}
	/**
	 * 角度换算成弧度
	 * @param degree
	 * @return
	 */
	public static double degreeToRadian(double degree) {
		return degree * Math.PI / 180;
	}
	/**
	 * 根据点击的位置判断是否点中控制旋转，缩放的图片， 初略的计算
	 * @param x
	 * @param y
	 * @return
	 */
	private int JudgeStatus(float x, float y) {
		PointF touchPoint = new PointF(x, y);
		PointF controlPointF = new PointF(mControlPoint);
		PointF controlPointF2 = new PointF(mControlPoint2);
		// 点击的点到控制旋转，缩放点的距离
		float distanceToControl = distance4PointF(touchPoint, controlPointF);
		// 如果两者之间的距离小于 控制图标的宽度，高度的最小值，则认为点中了控制图标
		if (distanceToControl < Math.min(mDrawableWidth1 / 2,
				mDrawableHeight1 / 2)) {
			return STATUS_ROTATE_ZOOM;
		}
		distanceToControl = distance4PointF(touchPoint, controlPointF2);
		// 如果两者之间的距离小于 控制图标的宽度，高度的最小值，则认为点中了控制图标
		if (distanceToControl < Math.min(mDrawableWidth2 / 2,
				mDrawableHeight2 / 2)) {
			return STATUS_FLIP;
		}
		return STATUS_DRAG;
	}
	public float getImageDegree() {
		return mDegree;
	}
	/**
	 * 设置图片旋转角度
	 * @param degree
	 */
	public void setImageDegree(float degree) {
		if (this.mDegree != degree) {
			this.mDegree = degree;
			transformDraw();
		}
	}
	public float getImageScale() {
		return mScale;
	}
	/**
	 * 设置图片缩放比例
	 * @param scale
	 */
	public void setImageScale(float scale) {
		if (this.mScale != scale) {
			this.mScale = scale;
			transformDraw();
		}
	}
	public Drawable getControlDrawable() {
		return controlDrawable1;
	}
	/**
	 * 设置控制图标
	 * @param drawable
	 */
	public void setControlDrawable1(Drawable drawable) {
		this.controlDrawable1 = drawable;
		mDrawableWidth1 = drawable.getIntrinsicWidth();
		mDrawableHeight1 = drawable.getIntrinsicHeight();
		transformDraw();
	}
	public void setControlDrawable2(Drawable drawable) {
		this.controlDrawable2 = drawable;
		mDrawableWidth2 = drawable.getIntrinsicWidth();
		mDrawableHeight2 = drawable.getIntrinsicHeight();
		transformDraw();
	}
	public int getFramePadding() {
		return framePadding;
	}
	public void setFramePadding(int framePadding) {
		if (this.framePadding == framePadding)
			return;
		this.framePadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, framePadding, metrics);
		transformDraw();
	}
	public int getFrameColor() {
		return frameColor;
	}
	public void setFrameColor(int frameColor) {
		if (this.frameColor == frameColor)
			return;
		this.frameColor = frameColor;
		mPaint.setColor(frameColor);
		invalidate();
	}
	public int getFrameWidth() {
		return frameWidth;
	}
	public void setFrameWidth(int frameWidth) {
		if (this.frameWidth == frameWidth)
			return;
		this.frameWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, frameWidth, metrics);
		mPaint.setStrokeWidth(frameWidth);
		invalidate();
	}
	/**
	 * 设置控制图标的位置, 设置的值只能选择LEFT_TOP ，RIGHT_TOP， RIGHT_BOTTOM，LEFT_BOTTOM
	 * @param controlLocation
	 */
	public void setControlLocation1(int location) {
		if (this.controlLocation1 == location)
			return;
		this.controlLocation1 = location;
		transformDraw();
	}
	public void setControlLocation2(int location) {
		if (this.controlLocation2 == location)
			return;
		this.controlLocation2 = location;
		transformDraw();
	}
	public int getControlLocation1() {
		return controlLocation1;
	}
	public int getControlLocation2() {
		return controlLocation2;
	}
	public PointF getCenterPoint() {
		return mCenterPoint;
	}
	/**
	 * 设置图片中心点位置，相对于父布局而言
	 * @param mCenterPoint
	 */
	public void setCenterPoint(PointF mCenterPoint) {
		this.mCenterPoint = mCenterPoint;
		adjustLayout();
	}
	public boolean isEditable() {
		return isEditable;
	}
	/**
	 * 设置是否处于可缩放，平移，旋转状态
	 * @param isEditable
	 */
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
		invalidate();
	}
	/**
	 * 两个点之间的距离
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	private float distance4PointF(PointF pf1, PointF pf2) {
		float disX = pf2.x - pf1.x;
		float disY = pf2.y - pf1.y;
		return FloatMath.sqrt(disX * disX + disY * disY);
	}
	/**
	 * 计算两点之间的中间点
	 * @param event
	 * @return
	 */
	private static PointF mid(MotionEvent event) {
		float midX = (event.getX(1) + event.getX(0)) / 2;
		float midY = (event.getY(1) + event.getY(0)) / 2;
		return new PointF(midX, midY);
	}
	/**
	 * 计算两个手指之间的距离。
	 * @param event
	 * @return 两个手指之间的距离
	 */
	private static float distanceBetweenFingers(MotionEvent event) {
		float disX = Math.abs(event.getX(0) - event.getX(1));
		float disY = Math.abs(event.getY(0) - event.getY(1));
		return FloatMath.sqrt(disX * disX + disY * disY);
	}

	@SuppressWarnings("serial")
	public static class NotSupportedException extends RuntimeException {
		private static final long serialVersionUID = 1674773263868453754L;

		public NotSupportedException() {
			super();
		}
		public NotSupportedException(String detailMessage) {
			super(detailMessage);
		}
	}

	public int[] getViewPostion() {
		return new int[] {
				mViewMarginLeft + gethalfDrawableSize()[0] + framePadding,
				mViewMarginTop + gethalfDrawableSize()[1] + framePadding };
	}
	public int getViewWidth() {
		return mBitmap.getWidth();
	}
	public int getViewHeight() {
		return mBitmap.getHeight();
	}
	public Bitmap getBitmap() {
		Bitmap newbitmap = Bitmap.createBitmap(mBitmap, 0, 0,
				mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
		return newbitmap;
	}
}
