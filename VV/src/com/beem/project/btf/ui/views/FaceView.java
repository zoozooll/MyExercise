package com.beem.project.btf.ui.views;

import com.beem.project.btf.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 可拖动view,具有拖动边缘限制
 * @author le yang
 */
public class FaceView extends View {
	private static final String TAG = "FaceView";
	/*宽高限定,即可移动范围*/
	private int maxwidth, maxheight;
	/*view里显示的Bitmap*/
	private Bitmap mBitmap = null;
	/*Bitmap宽高*/
	private int mBmWidth, mBmHeight;
	/*view的宽高,包含btimap+padding*/
	private int viewWidth, viewHeight;
	private Rect rect = new Rect();
	//记录中心点位置
	private Point cPoint = new Point();
	private int lastx, lasty;
	/*图标类型*/
	private FaceViewType viewtype = FaceViewType.LeftEye;
	private OnEventListener onEventListener;
	private boolean ismoved = false;

	public enum FaceViewType {
		LeftEye("左眼"), RightEye("右眼"), Mouth("嘴");
		private String description;

		private FaceViewType(String str) {
			this.description = str;
		}
		public String getName() {
			return description;
		}
	}

	public FaceView(Context context, AttributeSet attrs, FaceViewType viewtype) {
		super(context, attrs);
		this.viewtype = viewtype;
		// TODO Auto-generated constructor stub
		initBtimap();
	}
	public FaceView(Context context, FaceViewType viewtype, int maxwidth,
			int maxheight) {
		super(context);
		this.viewtype = viewtype;
		this.maxwidth = maxwidth;
		this.maxheight = maxheight;
		// TODO Auto-generated constructor stub
		initBtimap();
	}
	private void initBtimap() {
		switch (viewtype) {
			case LeftEye: {
				mBitmap = BitmapFactory.decodeResource(this.getResources(),
						R.drawable.cartoon_left_eye);
				mBmWidth = mBitmap.getWidth();
				mBmHeight = mBitmap.getHeight();
				break;
			}
			case RightEye: {
				mBitmap = BitmapFactory.decodeResource(this.getResources(),
						R.drawable.cartoon_right_eye);
				mBmWidth = mBitmap.getWidth();
				mBmHeight = mBitmap.getHeight();
				break;
			}
			case Mouth: {
				mBitmap = BitmapFactory.decodeResource(this.getResources(),
						R.drawable.cartoon_mouth);
				mBmWidth = mBitmap.getWidth();
				mBmHeight = mBitmap.getHeight();
				break;
			}
		}
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		rect.left = getPaddingLeft();
		rect.right = viewWidth - getPaddingRight();
		rect.top = getPaddingTop();
		rect.bottom = viewHeight - getPaddingBottom();
		//canvas.drawColor(Color.BLUE);
		canvas.drawBitmap(mBitmap, null, rect, null);
	}
	/** 重写onMeasure方法使view包裹bitmap */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		/*设置宽度*/
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);
		if (specMode == MeasureSpec.EXACTLY) {
			viewWidth = specSize;
		} else {
			// 由图片决定的宽  
			viewWidth = getPaddingLeft() + getPaddingRight() + mBmWidth;
		}
		/*设置高度*/
		specMode = MeasureSpec.getMode(heightMeasureSpec);
		specSize = MeasureSpec.getSize(heightMeasureSpec);
		if (specMode == MeasureSpec.EXACTLY) {
			viewHeight = specSize;
		} else {
			viewHeight = getPaddingTop() + getPaddingBottom() + mBmHeight;
		}
		setMeasuredDimension(viewWidth, viewHeight);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				lastx = (int) event.getRawX();
				lasty = (int) event.getRawY();
				if (onEventListener != null) {
					onEventListener.onChange(cPoint.x, cPoint.y, event,
							viewtype);
				}
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				ismoved = true;
				int dx = (int) event.getRawX() - lastx;
				int dy = (int) event.getRawY() - lasty;
				lastx = (int) event.getRawX();
				lasty = (int) event.getRawY();
				int left = getLeft() + dx;
				int top = getTop() + dy;
				int right = getRight() + dx;
				int bottom = getBottom() + dy;
				//边缘检测
				if (left < 0) {
					left = 0;
					right = viewWidth;
				}
				if (right >= maxwidth) {
					right = maxwidth;
					left = right - viewWidth;
				}
				if (top < 0) {
					top = 0;
					bottom = viewHeight;
				}
				if (bottom > maxheight) {
					bottom = maxheight;
					top = maxheight - viewHeight;
				}
				layout(left, top, right, bottom);
				cPoint.x = left + viewWidth / 2;
				cPoint.y = top + viewHeight / 2;
				if (onEventListener != null) {
					onEventListener.onChange(cPoint.x, cPoint.y, event,
							viewtype);
				}
				break;
			}
			case MotionEvent.ACTION_UP: {
				if (onEventListener != null) {
					onEventListener.onChange(0, 0, event, viewtype);
				}
				break;
			}
		}
		return true;
	}

	/*放大镜交互接口*/
	public interface OnEventListener {
		public void onChange(float x, float y, MotionEvent event,
				FaceViewType viewtype);
	}

	public void setOnEventChanageListener(OnEventListener onChanageListener) {
		this.onEventListener = onChanageListener;
	}
	/*设定view可拖动范围*/
	public void setViewRange(int maxwidth, int maxheight) {
		this.maxwidth = maxwidth;
		this.maxheight = maxheight;
	}
	/*设置view的位置*/
	public void setViewPosition(int x, int y) {
		cPoint.x = x;
		cPoint.y = y;
		layout(cPoint.x - viewWidth / 2, cPoint.y - viewHeight / 2, cPoint.x
				+ viewWidth / 2, cPoint.y + viewHeight / 2);
	}
	//获取view的中心点
	public Point getCenterPoint() {
		return cPoint;
	}
	public boolean ismoved() {
		return ismoved;
	}
}
