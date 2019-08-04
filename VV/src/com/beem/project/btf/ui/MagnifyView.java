package com.beem.project.btf.ui;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.views.FaceView.FaceViewType;
import com.beem.project.btf.utils.DimenUtils;
import com.beem.project.btf.utils.PictureUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MagnifyView extends View {
	private String TAG = "MagnifyView";
	private Context mContext;
	/*
	 * 存放要放大的原始图片
	 */
	private Bitmap mSourceBitmap; // 图片资源
	/*
	 * 存放要放大的蒙版图片
	 */
	private Bitmap mSourceBitmap2 = null;
	private int mMagnRadius = 0; // 放大镜的半径
	private float mPointRadius = 1; //橡皮擦的半徑
	/*
	 * 放大镜的左上角坐标(mMagnDisplayX, mMagnDisplayY)
	 */
	private int mMagnDisplayX = 0;
	// 矩形边长
	private int mDiameter = 0;
	/*
	 * 屏幕的高度和宽度
	 */
	private int mScreenWidth = 0;
	private int mScreenHeight = 0;
	// 是否显示特定的指示标志
	private boolean mIsDisplayIndicator = false;
	private Bitmap mIndicatorBm;
	//	public int mLayoutWidth = 0;
	//	public int mLayoutHeight = 0;
	private int mIndicatorImgId = 0;
	private Rect mSrcRect;
	private Paint paint, paint2, textpaint;
	private boolean isAreaMode = false;
	private FaceViewType viewtype;

	public MagnifyView(Context context) {
		super(context);
		mContext = context;
	}
	public MagnifyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (changed) {
			// 分别获取到ZoomImageView的宽度和高度
			mScreenWidth = getWidth();
			mScreenHeight = getHeight();
			Log.i(TAG, "---onLayout mScreenWidth=" + mScreenWidth
					+ " mScreenHeight=" + mScreenHeight);
		}
	}
	public void setSourceBitmap(Bitmap bm) {
		mSourceBitmap = bm;
		mMagnRadius = DimenUtils.dip2px(mContext, 56);
		//Log.d(TAG, "setSourceBitmap, Width = " + mSourceBitmap.getWidth() + ", Height()=" + mSourceBitmap.getHeight());
		mDiameter = 2 * mMagnRadius;
		paint = new Paint();
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(4);
	}
	public void setMagnifyMode(boolean isAreaMode) {
		if (isAreaMode) {//area mode
			paint2 = new Paint();
			paint2.setStyle(Style.FILL);
			paint2.setColor(Color.WHITE);
			paint2.setStrokeWidth(10);
		} else { //clear mode
			paint2 = new Paint();
			paint2.setStyle(Style.STROKE);
			paint2.setColor(Color.WHITE);
			paint2.setStrokeWidth(4);
		}
		this.isAreaMode = isAreaMode;
	}
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//Log.d(TAG, "---onDraw, mMagnDisplayX = " + mMagnDisplayX);
		Rect dstRect = new Rect(mMagnDisplayX, 0, mMagnDisplayX + mDiameter,
				mDiameter);
		if (mSourceBitmap != null) {
			canvas.drawBitmap(mSourceBitmap, mSrcRect, dstRect, null);
		}
		if (mSourceBitmap2 != null) {
			canvas.drawBitmap(mSourceBitmap2, null, dstRect, null);
		}
		canvas.drawRect(dstRect, paint);
		if (isAreaMode) {
			if (paint2 != null) {
				canvas.drawCircle(mMagnDisplayX + mMagnRadius, mMagnRadius, 5,
						paint2);
			}
		} else {
			if (paint2 != null) {
				canvas.drawCircle(mMagnDisplayX + mMagnRadius, mMagnRadius,
						mPointRadius, paint2);
			}
		}
		if (mIsDisplayIndicator) {
			if (mIndicatorBm != null && mIndicatorBm.isRecycled() == false) {
				int width = mIndicatorBm.getWidth();
				int height = mIndicatorBm.getHeight();
				int indicatorX = mMagnDisplayX + mMagnRadius - width / 2;
				int indicatorY = mMagnRadius - height / 2;
				canvas.drawBitmap(mIndicatorBm, indicatorX, indicatorY, null);
				if (viewtype != null) {
					String viewname = viewtype.getName();
					textpaint = new Paint();
					textpaint.setStyle(Style.FILL);
					textpaint.setColor(Color.WHITE);
					textpaint.setTextSize(DimenUtils.sp2px(mContext, 15));
					textpaint.setTextAlign(Paint.Align.CENTER);
					canvas.drawText(viewname, mMagnDisplayX + mMagnRadius,
							indicatorY + height + 20, textpaint);
				}
			}
		} else {
			if (mIndicatorBm != null && mIndicatorBm.isRecycled() == false) {
				mIndicatorBm.recycle();
				mIndicatorBm = null;
			}
		}
	}
	public void setHairBitmap(Bitmap bm2) {
		// 将按下的点的坐标转化成原图上的坐标
		if (bm2 != null) {
			// 释放之前申请的内存
			if (mSourceBitmap2 != null && !mSourceBitmap2.isRecycled()) {
				mSourceBitmap2.recycle();
				mSourceBitmap2 = null;
				Log.d(TAG, "pjj recycle mSourceBitmap2");
			}
			mSourceBitmap2 = bm2;
		}
	}
	public void onMagTouchEvent(float mPosX, float mPosY) {
		this.onMagTouchEvent(mPosX, mPosY, 1, 0, 0);
	}
	public void onMagTouchEvent(float mPosX, float mPosY, float ratio,
			float dx, float dy) {
		//Log.d(TAG, "onMagTouchEvent, mPosX = " + mPosX + ", mPosY = " + mPosY);
		mPointRadius = 12 * ratio;
		int origX = (int) ((mPosX - dx) / ratio);
		int origY = (int) ((mPosY - dy) / ratio);
		int origRadius = 0;
		if (this.viewtype != null) {
			//如果是五官定位，则固定2倍放大比率
			origRadius = (mMagnRadius / 2);
		} else {
			origRadius = (int) (mMagnRadius / ratio);
		}
		// 在屏幕的左上角显示时，变换到右上角
		if (origX <= mDiameter && origY <= mDiameter) {
			mMagnDisplayX = mScreenWidth - mDiameter;
		} else if (origX >= mScreenWidth - mDiameter && origY <= mDiameter) {// 在屏幕的右上角显示，变换到左上角
			mMagnDisplayX = 0;
		}
		int startX = origX - origRadius;
		int startY = origY - origRadius;
		int diameter = 2 * origRadius;
		/*
		 * 计算需显示图片区域的左上角的坐标
		 */
		if (startX < 0) {
			startX = 0;
		} else if (startX > mScreenWidth - diameter) {
			startX = mScreenWidth - diameter;
		}
		if (startY < 0) {
			startY = 0;
		} else if (startY > mScreenHeight - diameter) {
			startY = mScreenHeight - diameter;
		}
		//Log.d(TAG, "startX = " + startX + ", startY = " + startY);
		mSrcRect = new Rect(startX, startY, startX + diameter, startY
				+ diameter);
		invalidate();
	}
	public void setIndicatorImgId(int imgId, boolean display) {
		mIsDisplayIndicator = display;
		if (mIndicatorImgId != imgId) {
			mIndicatorImgId = imgId;
			if (mIsDisplayIndicator && mIndicatorImgId != 0) {
				mIndicatorBm = BitmapFactory.decodeResource(getResources(),
						mIndicatorImgId);
				mIndicatorBm = Bitmap
						.createScaledBitmap(mIndicatorBm,
								mIndicatorBm.getWidth(),
								mIndicatorBm.getHeight(), true);
			}
		}
	}
	public void setIndicatorType(FaceViewType viewtype) {
		mIsDisplayIndicator = true;
		this.viewtype = viewtype;
		switch (this.viewtype) {
			case LeftEye: {
				mIndicatorBm = BitmapFactory.decodeResource(
						this.getResources(), R.drawable.cartoon_left_eye);
				break;
			}
			case RightEye: {
				mIndicatorBm = BitmapFactory.decodeResource(
						this.getResources(), R.drawable.cartoon_right_eye);
				break;
			}
			case Mouth: {
				mIndicatorBm = BitmapFactory.decodeResource(
						this.getResources(), R.drawable.cartoon_mouth);
				break;
			}
		}
		mIndicatorBm = PictureUtil.ScaleBitmap(2.0f, mIndicatorBm);
	}
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		//		LogUtils.i("---onDetachedFromWindow---, ");
		if (mSourceBitmap != null && !mSourceBitmap.isRecycled()) {
			mSourceBitmap.recycle();
			mSourceBitmap = null;
		}
		if (mSourceBitmap2 != null && !mSourceBitmap2.isRecycled()) {
			mSourceBitmap2.recycle();
			mSourceBitmap2 = null;
		}
		if (paint != null) {
			paint.clearShadowLayer();
			paint.reset();
			paint = null;
		}
		if (paint2 != null) {
			paint2.clearShadowLayer();
			paint2.reset();
			paint2 = null;
		}
	}
}
