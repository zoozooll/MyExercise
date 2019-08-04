package com.beem.project.btf.ui.views;

import com.beem.project.btf.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

public class NewsToptitleImageView extends ImageView implements
		OnGestureListener {
	private static final String TAG = "NewsToptitleImageView";
	private float startDis;// 开始距离
	private PointF midPoint;// 中间点
	private float oldRotation = 0;
	private float rotation = 0;
	private PointF startPoint = new PointF();
	private Matrix matrix = new Matrix();
	private Matrix currentMatrix = new Matrix();
	private boolean is_Editable = true;
	private WindowManager wm;
	private ImageView imageView;
	private GestureDetector detector;

	/**
	 * 模式 NONE：无 DRAG：拖拽. ZOOM:缩放
	 * @author zhangjia
	 */
	private enum MODE {
		NONE, DRAG, ZOOM
	};

	private MODE mode = MODE.NONE;// 默认模式

	/** 控制图片的可编辑性 **/
	public void setis_Editable(boolean is_Editable) {
		this.is_Editable = is_Editable;
	}
	public boolean getis_Editable() {
		return this.is_Editable;
	}
	/** 构造方法 **/
	public NewsToptitleImageView(Context context) {
		super(context);
		wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		detector = new GestureDetector(context, this);
	}
	public NewsToptitleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		detector = new GestureDetector(context, this);
	}
	/**
	 * 计算两点之间的距离
	 * @param event
	 * @return
	 */
	public static float distance(MotionEvent event) {
		float dx = event.getX(1) - event.getX(0);
		float dy = event.getY(1) - event.getY(0);
		return FloatMath.sqrt(dx * dx + dy * dy);
	}
	/**
	 * 计算两点之间的中间点
	 * @param event
	 * @return
	 */
	public static PointF mid(MotionEvent event) {
		float midX = (event.getX(1) + event.getX(0)) / 2;
		float midY = (event.getY(1) + event.getY(0)) / 2;
		return new PointF(midX, midY);
	}
	private float rotation(MotionEvent event) {
		double delta_x = (event.getX(0) - event.getX(1));
		double delta_y = (event.getY(0) - event.getY(1));
		double radians = Math.atan2(delta_y, delta_x);
		return (float) Math.toDegrees(radians);
	}
	//创建一个悬浮在最顶层的边框
	private void addFrame() {
		WindowManager.LayoutParams wlp = new WindowManager.LayoutParams();
		wlp.flags = WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		wlp.format = PixelFormat.RGBA_8888;
		wlp.gravity = Gravity.LEFT | Gravity.TOP;
		wlp.width = getWidth();
		wlp.height = getHeight();
		int[] xy = new int[2];
		getLocationInWindow(xy);
		wlp.x = xy[0];
		wlp.y = xy[1] - getStatusHeight(getContext());
		imageView = new ImageView(getContext());
		imageView.setBackgroundResource(R.drawable.frameline);
		imageView.setLayoutParams(wlp);
		if (imageView != null && imageView.getParent() == null) {
			wm.addView(imageView, wlp);
		}
	}
	private void removeFrame() {
		if (imageView != null && imageView.getParent() != null) {
			wm.removeView(imageView);
		}
	}
	/***
	 * touch 事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/** 处理单点、多点触摸 **/
		if (is_Editable == true) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:// 手指压下屏幕
					mode = MODE.DRAG;
					currentMatrix.set(this.getImageMatrix());// 记录ImageView当前的移动位置
					matrix.set(currentMatrix);
					startPoint.set(event.getX(), event.getY());
					addFrame();
					break;
				case MotionEvent.ACTION_POINTER_DOWN:// 当屏幕上还有触点（手指），再有一个手指压下屏幕
					mode = MODE.ZOOM;
					oldRotation = rotation(event);
					startDis = distance(event);
					if (startDis > 10f) {
						midPoint = mid(event);
						currentMatrix.set(this.getImageMatrix());// 记录ImageView当前的缩放倍数
					}
					break;
				case MotionEvent.ACTION_MOVE:// 手指在屏幕移动，该 事件会不断地触发
					getParent().requestDisallowInterceptTouchEvent(true);
					if (mode == MODE.DRAG) {
						float dx = event.getX() - startPoint.x;// 得到在x轴的移动距离
						float dy = event.getY() - startPoint.y;// 得到在y轴的移动距离
						matrix.set(currentMatrix);// 在没有进行移动之前的位置基础上进行移动
						matrix.postTranslate(dx, dy);
					} else if (mode == MODE.ZOOM) {// 缩放与旋转
						float endDis = distance(event);// 结束距离
						rotation = (rotation(event) - oldRotation);
						if (endDis > 10f) {
							float scale = endDis / startDis;// 得到缩放倍数
							matrix.set(currentMatrix);
							matrix.postScale(scale, scale, midPoint.x,
									midPoint.y);
							matrix.postRotate(rotation, midPoint.x, midPoint.y);
						}
					}
					break;
				case MotionEvent.ACTION_UP:// 手指离开屏
					mode = MODE.NONE;
					removeFrame();
					break;
				case MotionEvent.ACTION_CANCEL: {
					mode = MODE.NONE;
					removeFrame();
					break;
				}
				case MotionEvent.ACTION_POINTER_UP:// 有手指离开屏幕,但屏幕还有触点（手指）
					mode = MODE.NONE;
					break;
			}
			detector.onTouchEvent(event);
			this.setImageMatrix(matrix);
		}
		return true;
	}
	//重置matrix
	public void reset() {
		matrix.reset();
		this.setImageMatrix(matrix);
	}
	//设置图片居中
	public void setCenter(float sacle, float dx, float dy) {
		matrix.setScale(sacle, sacle);
		matrix.postTranslate(dx, dy);
		matrix.postRotate(0);
		this.setImageMatrix(matrix);
	}
	/**
	 * 获取状态栏的高度
	 * @param context
	 * @return
	 */
	private int getStatusHeight(Context context) {
		int statusHeight = 0;
		Rect localRect = new Rect();
		((Activity) context).getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass
						.getField("status_bar_height").get(localObject)
						.toString());
				statusHeight = context.getResources().getDimensionPixelSize(i5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
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
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
}
