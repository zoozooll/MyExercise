package com.beem.project.btf.ui;

import java.util.ArrayList;

import org.opencv.core.Mat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.beem.project.btf.utils.DimenUtils;
import com.beem.project.btf.utils.UIHelper;
import com.beem.project.btf.utils.UIHelper.UIHelperDialogType;
import com.butterfly.piqs.vvcartoon.CartoonLib;

/**
 * 自定义的ImageView控制，可对图片进行多点触控缩放和拖动
 * @author pjunjun
 */
public class EditImageView extends ImageView {
	/**
	 * 默认状态常量
	 */
	private static final int STATUS_DEFAULT = 0;
	/**
	 * 初始化状态常量
	 */
	private static final int STATUS_INIT = 1;
	/**
	 * 图片放大状态常量
	 */
	private static final int STATUS_ZOOM_OUT = 2;
	/**
	 * 图片缩小状态常量
	 */
	private static final int STATUS_ZOOM_IN = 3;
	/**
	 * 图片拖动状态常量
	 */
	private static final int STATUS_MOVE = 4;
	/**
	 * 图片拖动状态常量
	 */
	private static final int STATUS_EDIT = 5;
	/**
	 * 用于对图片进行移动和缩放变换的矩阵
	 */
	private Matrix matrix = new Matrix();
	/**
	 * 待展示的Bitmap对象
	 */
	private Bitmap sourceBitmap;
	/**
	 * 记录当前操作的状态，可选值为STATUS_INIT、STATUS_ZOOM_OUT、STATUS_ZOOM_IN和STATUS_MOVE
	 */
	private int currentStatus;
	/**
	 * ZoomImageView控件的宽度
	 */
	private int width;
	/**
	 * ZoomImageView控件的高度
	 */
	private int height;
	/**
	 * 记录两指同时放在屏幕上时，中心点的横坐标值
	 */
	private float centerPointX;
	/**
	 * 记录两指同时放在屏幕上时，中心点的纵坐标值
	 */
	private float centerPointY;
	/**
	 * 记录当前图片的宽度，图片被缩放时，这个值会一起变动
	 */
	private float currentBitmapWidth;
	/**
	 * 记录当前图片的高度，图片被缩放时，这个值会一起变动
	 */
	private float currentBitmapHeight;
	/**
	 * 记录上次手指移动时的横坐标
	 */
	private float lastXMove = -1;
	/**
	 * 记录上次手指移动时的纵坐标
	 */
	private float lastYMove = -1;
	/**
	 * 记录手指在横坐标方向上的移动距离
	 */
	private float movedDistanceX;
	/**
	 * 记录手指在纵坐标方向上的移动距离
	 */
	private float movedDistanceY;
	/**
	 * 记录图片在矩阵上的横向偏移值
	 */
	private float totalTranslateX;
	/**
	 * 记录图片在矩阵上的纵向偏移值
	 */
	private float totalTranslateY;
	/**
	 * 记录图片初始坐标X
	 */
	private float OriginalX = 0.0f;
	/**
	 * 记录图片初始坐标Y
	 */
	private float OriginalY = 0.0f;
	/**
	 * 记录图片在矩阵上的总缩放比例
	 */
	private float totalRatio;
	/**
	 * 记录手指移动的距离所造成的缩放比例
	 */
	private float scaledRatio;
	/**
	 * 记录图片初始化时的缩放比例
	 */
	private float initRatio;
	/**
	 * 记录上次两指之间的距离
	 */
	private double lastFingerDis;
	private static final float MaxRatio = 4.0f;
	private int mRadius = 0;
	/**
	 * 涂鸦相关控件
	 */
	private Context mContext;
	private Path path; // 路径
	private Paint paint = null; // 画笔
	// public Bitmap cacheBitmap = null;// 定义一个内存中的图片，该图片将作为缓冲区
	Mat cacheimage = new Mat();
	Canvas cacheCanvas = null;// 定义cacheBitmap上的Canvas对象
	private Bitmap mHairBm;
	private Xfermode xmode = null;
	private SelectType selectType = SelectType.area;
	private SelectType oldselectType = SelectType.area;
	private ArrayList<Integer> pointlist = new ArrayList<Integer>();
	private float preX; // 起始点的x坐标值
	private float preY;// 起始点的y坐标值
	private boolean isDrawPath = false;
	private OnEventChanageListener onEventChanageListener;
	private boolean isProcess = false;//判断是否处理图片中

	private enum SelectType {
		add, clear, area, zoom, move
	}

	private boolean isCartoonMode;
	private boolean disableTouchEvent = false;

	/**
	 * ZoomImageView构造函数，将当前操作状态设为STATUS_INIT。
	 * @param context
	 * @param attrs
	 */
	public EditImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		currentStatus = STATUS_INIT;
		mRadius = DimenUtils.dip2px(mContext, 56);
		initPaint();
	}
	private void initPaint() {
		path = new Path();
		paint = new Paint(Paint.DITHER_FLAG);
		paint.setColor(Color.RED); // 设置默认的画笔颜色
		// 设置画笔风格
		paint.setStyle(Paint.Style.STROKE); // 设置填充方式为描边
		paint.setStrokeJoin(Paint.Join.ROUND); // 设置笔刷的图形样式
		paint.setStrokeCap(Paint.Cap.ROUND); // 设置画笔转弯处的连接风格
		paint.setStrokeWidth(1); // 设置默认笔触的宽度
		paint.setAntiAlias(true); // 使用抗锯齿功能
	}
	/**
	 * 将待展示的图片设置进来。
	 * @param bitmap 待展示的Bitmap对象
	 */
	@Override
	public void setImageBitmap(Bitmap bitmap) {
		//LogUtils.i("ZoomImageView~setImageBitmap", 100);
		sourceBitmap = bitmap;
		invalidate();
		super.setImageBitmap(null);
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (changed) {
			// 分别获取到ZoomImageView的宽度和高度
			width = getWidth();
			height = getHeight();
			//LogUtils.i("---onLayout width=" + width + " height=" + height);
		}
	}
	/**
	 * 根据currentStatus的值来决定对图片进行什么样的绘制操作。
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//LogUtils.i("----currentStatus==" + currentStatus);
		switch (currentStatus) {
			case STATUS_ZOOM_OUT:
			case STATUS_ZOOM_IN:
				zoom(canvas);
				break;
			case STATUS_MOVE:
				//LogUtils.i("STATUS_MOVE" + STATUS_MOVE);
				move(canvas);
				break;
			case STATUS_EDIT:
				if (sourceBitmap != null) {
					canvas.drawBitmap(sourceBitmap, matrix, null);
				}
				//LogUtils.i("---onDraw isDrawPath==" + isDrawPath);
				if (mHairBm != null) {
					Paint bmpPaint = new Paint();
					if (isDrawPath) {
						canvas.drawBitmap(mHairBm, matrix, bmpPaint);
						canvas.drawPath(path, paint);
					} else {
						//LogUtils.i("---onDraw mHairBm ----");
						canvas.drawBitmap(mHairBm, matrix, bmpPaint);
					}
				}
				canvas.save(Canvas.ALL_SAVE_FLAG); // 保存canvas的状态
				canvas.restore(); // 恢复canvas之前保存的状态，防止保存后对canvas执行的操作对后续的绘制有影响
				break;
			case STATUS_INIT:
				initBitmap(canvas);
				if (mHairBm != null) {
					Paint bmpPaint = new Paint();
					canvas.drawBitmap(mHairBm, matrix, bmpPaint);
					//LogUtils.i("---onDraw mHairBm ----");
				}
				break;
			case STATUS_DEFAULT:
			default:
				if (sourceBitmap != null) {
					canvas.drawBitmap(sourceBitmap, matrix, null);
					//LogUtils.i("---onDraw sourceBitmap ----");
				}
				break;
		}
	}
	public void setHairBitmap(Bitmap bm) {
		mHairBm = bm;
		cacheimage = new Mat();
		org.opencv.android.Utils.bitmapToMat(mHairBm, cacheimage, true);
	}
	/**
	 * 对图片进行初始化操作，包括让图片居中，以及当图片大于屏幕宽高时对图片进行压缩。
	 * @param canvas
	 */
	private void initBitmap(Canvas canvas) {
		if (sourceBitmap != null) {
			matrix.reset();
			int bitmapWidth = sourceBitmap.getWidth();
			int bitmapHeight = sourceBitmap.getHeight();
			//LogUtils.i("bitmapWidth-->" + bitmapWidth + " bitmapHeight-->" + bitmapHeight + " width-->" + width
			//					+ " height-->" + height);
			if (bitmapWidth > width || bitmapHeight > height) {
				if (bitmapWidth - width > bitmapHeight - height) {
					// 当图片宽度大于屏幕宽度时，将图片等比例压缩，使它可以完全显示出来
					float ratio = width / (bitmapWidth * 1.0f);
					matrix.postScale(ratio, ratio);
					OriginalY = (height - (bitmapHeight * ratio)) / 2f;
					// 在纵坐标方向上进行偏移，以保证图片居中显示
					matrix.postTranslate(0, OriginalY);
					totalTranslateY = OriginalY;
					totalRatio = initRatio = ratio;
				} else {
					// 当图片高度大于屏幕高度时，将图片等比例压缩，使它可以完全显示出来
					float ratio = height / (bitmapHeight * 1.0f);
					matrix.postScale(ratio, ratio);
					OriginalX = (width - (bitmapWidth * ratio)) / 2f;
					// 在横坐标方向上进行偏移，以保证图片居中显示
					matrix.postTranslate(OriginalX, 0);
					totalTranslateX = OriginalX;
					totalRatio = initRatio = ratio;
				}
				currentBitmapWidth = bitmapWidth * initRatio;
				currentBitmapHeight = bitmapHeight * initRatio;
			} else {
				// 当图片的宽高都小于屏幕宽高时，直接让图片居中显示
				OriginalX = (width - sourceBitmap.getWidth()) / 2f;
				OriginalY = (height - sourceBitmap.getHeight()) / 2f;
				//LogUtils.i("---initBitmap translateX=" + OriginalX + " translateY=" + OriginalY);
				matrix.postTranslate(OriginalX, OriginalY);
				totalTranslateX = OriginalX;
				totalTranslateY = OriginalY;
				totalRatio = initRatio = 1f;
				currentBitmapWidth = bitmapWidth;
				currentBitmapHeight = bitmapHeight;
			}
			canvas.drawBitmap(sourceBitmap, matrix, null);
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//LogUtils.i("---onTouchEvent totalRatio=" + totalRatio + " initRatio=" + initRatio);
		if (disableTouchEvent) {
			//LogUtils.i("---onTouchEvent disableTouchEvent=" + disableTouchEvent);
			return true;
		} else {
			getParent().requestDisallowInterceptTouchEvent(true);
		}
		// 图片缩放、平移事件
		if (selectType == SelectType.zoom || selectType == SelectType.move) {
			ScaleAction(event);
		} else {
			// 图片抠图、添加、移除事件
			currentStatus = STATUS_EDIT;
			SelectAction(event);
			//LogUtils.i("---onTouchEvent SelectAction exit");
		}
		return true;
	}
	/**
	 * 对图片进行抠图、添加、移除操作
	 * @param event
	 */
	private void SelectAction(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		//LogUtils.i("SelectAction~~x=" + x + "~~y=" + y);
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN: {
				pointlist.clear();
				preX = x;
				preY = y;
				int[] point = { (int) preX, (int) preY };
				// path.moveTo(preX, preY); // 将绘图的起始点移到（x,y）坐标点的位置
				//LogUtils.i("--ACTION_DOWN~~preX~~" + preX + "ACTION_DOWN~~preY~~" + preY);
				switch (selectType) {
					case add: {
						// 绘制模式
						//LogUtils.i("ACTION_DOWN~~add");
						int[] addpoint = PointTranslate(point);
						if (isCartoonMode) {
							CartoonLib.setAddpointdownMat(
									cacheimage.getNativeObjAddr(), addpoint);
						} else {
							CartoonLib.MaskAreaPtGrowTouchDown(
									cacheimage.getNativeObjAddr(), addpoint);
						}
						org.opencv.android.Utils.matToBitmap(cacheimage,
								mHairBm, true);
						break;
					}
					case clear: {
						// 擦出模式
						//LogUtils.i("ACTION_DOWN~~clear");
						int[] clearpoint = PointTranslate(point);
						if (isCartoonMode) {
							CartoonLib.setDelpointdownMat(
									cacheimage.getNativeObjAddr(), clearpoint);
						} else {
							CartoonLib.MaskAreaDeleteTouchDown(
									cacheimage.getNativeObjAddr(), clearpoint);
						}
						org.opencv.android.Utils.matToBitmap(cacheimage,
								mHairBm, true);
						onEventChanageListener.onChange(x, y, event,
								totalRatio, totalTranslateX, totalTranslateY,
								false);
						break;
					}
					case area: {
						// 圈选模式
						/* 记录点,首位记录点的个数 */
						//LogUtils.i("ACTION_DOWN~~area");
						path.moveTo(x, y); // 将绘图的起始点移到（x,y）坐标点的位置
						pointlist.add(0);
						pointlist.add(point[0]);
						pointlist.add(point[1]);
						onEventChanageListener.onChange(x, y, event,
								totalRatio, totalTranslateX, totalTranslateY,
								true);
						break;
					}
					default:
						break;
				}
				//invalidate();
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				//LogUtils.i("ACTION_MOVE");
				int[] point = { (int) x, (int) y };
				//LogUtils.i("ACTION_MOVE~~x~~" + x + "ACTION_MOVE~~y~~" + y);
				// 调用jni库
				switch (selectType) {
					case add: {
						// 绘制模式
						//LogUtils.i("ACTION_MOVE~~add");
						float dx = Math.abs(x - preX);
						float dy = Math.abs(y - preY);
						double distance = Math.sqrt(dx * dx + dy * dy);
						if (distance < 5) { // 移动距离过小时，不画点
							break;
						}
						int[] addpoint = PointTranslate(point);
						if (isCartoonMode) {
							CartoonLib.setAddpointmoveMat(
									cacheimage.getNativeObjAddr(), addpoint);
						} else {
							CartoonLib.MaskAreaPtGrowTouchMove(
									cacheimage.getNativeObjAddr(), addpoint);
						}
						org.opencv.android.Utils.matToBitmap(cacheimage,
								mHairBm, true);
						break;
					}
					case clear: {
						// 擦出模式
						//LogUtils.i("ACTION_MOVE~~clear");
						int[] clearpoint = PointTranslate(point);
						if (isCartoonMode) {
							CartoonLib.setDelpointmoveMat(
									cacheimage.getNativeObjAddr(), clearpoint);
						} else {
							CartoonLib.MaskAreaDeleteTouchMove(
									cacheimage.getNativeObjAddr(), clearpoint);
						}
						org.opencv.android.Utils.matToBitmap(cacheimage,
								mHairBm, true);
						onEventChanageListener.onChange(x, y, event,
								totalRatio, totalTranslateX, totalTranslateY,
								false);
						break;
					}
					case area: {
						// 圈选模式
						//LogUtils.i("ACTION_MOVE~~area");
						float dx = Math.abs(x - preX);
						float dy = Math.abs(y - preY);
						if (dx >= 5 || dy >= 5) { // 判断是否在允许的范围内
							path.quadTo(preX, preY, (x + preX) / 2,
									(y + preY) / 2);
							preX = x;
							preY = y;
						}
						isDrawPath = true;
						//LogUtils.i("--000-area isDrawPath==" + isDrawPath);
						pointlist.add(point[0]);
						pointlist.add(point[1]);
						onEventChanageListener.onChange(x, y, event,
								totalRatio, totalTranslateX, totalTranslateY,
								true);
						break;
					}
					default:
						break;
				}
				invalidate();
				break;
			}
			case MotionEvent.ACTION_UP: {
				//LogUtils.i("ACTION_UP");
				int[] point = { (int) x, (int) y };
				// //LogUtils.i("ACTION_UP~~preX~~" + preX + "ACTION_UP~~preY~~" + preY);
				switch (selectType) {
					case add: {
						// 绘制模式
						//LogUtils.i("ACTION_UP~~add");
						final int[] addpoint = PointTranslate(point);
						//此步骤耗时，在子线程里操作
						if (!isProcess) {
							isProcess = true;
							new AsyncTask<Void, Integer, Void>() {
								@Override
								protected void onPreExecute() {
									UIHelper.showDialogForLoading(mContext,
											UIHelperDialogType.Simple, false);
								};
								@Override
								protected Void doInBackground(Void... params) {
									// TODO Auto-generated method stub
									if (isCartoonMode) {
										CartoonLib.setAddpointupMat(
												cacheimage.getNativeObjAddr(),
												addpoint);
									} else {
										CartoonLib.MaskAreaPtGrowTouchUp(
												cacheimage.getNativeObjAddr(),
												addpoint);
									}
									return null;
								}
								@Override
								protected void onPostExecute(Void result) {
									org.opencv.android.Utils.matToBitmap(
											cacheimage, mHairBm, true);
									invalidate();
									UIHelper.hideDialogForLoading();
									isProcess = false;
								};
							}.execute();
						}
						break;
					}
					case clear: {
						// 擦出模式
						//LogUtils.i("ACTION_UP~~clear");
						int[] clearpoint = PointTranslate(point);
						if (isCartoonMode) {
							CartoonLib.setDelpointupMat(
									cacheimage.getNativeObjAddr(), clearpoint);
						} else {
							CartoonLib.MaskAreaDeleteTouchUp(
									cacheimage.getNativeObjAddr(), clearpoint);
						}
						org.opencv.android.Utils.matToBitmap(cacheimage,
								mHairBm, true);
						onEventChanageListener.onChange(x, y, event,
								totalRatio, totalTranslateX, totalTranslateY,
								false);
						break;
					}
					case area: {
						// 圈选模式
						pointlist.add(point[0]);
						pointlist.add(point[1]);
						pointlist.set(0, pointlist.size() - 1);
						//LogUtils.i("~~pointlist.size()~~" + pointlist.get(0));
						int[] movepointlist = MovePoints(pointlist);
						// 将pointlist进行移位和缩
						if (isCartoonMode) {
							CartoonLib.setAddContourMat(
									cacheimage.getNativeObjAddr(),
									movepointlist);
						} else {
							CartoonLib.MaskAreaLineConnect(
									cacheimage.getNativeObjAddr(),
									movepointlist);
						}
						org.opencv.android.Utils.matToBitmap(cacheimage,
								mHairBm, true);
						pointlist.clear();
						// 清除已绘制的路径
						path.reset();
						isDrawPath = false;
						onEventChanageListener.onChange(x, y, event,
								totalRatio, totalTranslateX, totalTranslateY,
								true);
						//LogUtils.i("~~111area  isDrawPath==" + isDrawPath);
						break;
					}
					default:
						break;
				}
				invalidate();
				break;
			}
			// 有手指离开屏幕，但屏幕还有触点（手指）
			case MotionEvent.ACTION_POINTER_UP:
				break;
			// 当屏幕上已经有触点（手指），再有一个手指压下屏幕
			case MotionEvent.ACTION_POINTER_DOWN: {
				//LogUtils.i("---onTouchEvent ACTION_POINTER_DOWN enter-----");
				/*
				 * this.setVisibility(View.GONE); this.setClickable(false);
				 */
				onEventChanageListener.onChange(x, y, event, totalRatio,
						totalTranslateX, totalTranslateY, false);
				centerPointBetweenFingers(event);
				lastFingerDis = distanceBetweenFingers(event);
				oldselectType = selectType;
				selectType = SelectType.zoom;
				isDrawPath = false;
				//LogUtils.i("---onTouchEvent ACTION_POINTER_DOWN lastFingerDis-----" + lastFingerDis);
				invalidate();
				break;
			}
		}
	}
	// 图片缩放、平移事件
	private void ScaleAction(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		//LogUtils.i("---ScaleAction x=" + x + "---y=" + y);
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				if (event.getPointerCount() == 2) {
					// 当有两个手指按在屏幕上时，计算两指之间的距离
					oldselectType = selectType;
					selectType = SelectType.zoom;
					centerPointBetweenFingers(event);
					lastFingerDis = distanceBetweenFingers(event);
					//LogUtils.i("---ACTION_POINTER_DOWN lastFingerDis=" + lastFingerDis);
				}
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_MOVE:
				if (selectType == SelectType.move) {
					// 只有单指按在屏幕上移动时，为拖动状态
					//LogUtils.i("---ACTION_MOVE xMove=" + x + "---yMove=" + y);
					if (lastXMove == -1 && lastYMove == -1) {
						lastXMove = x;
						lastYMove = y;
					}
					currentStatus = STATUS_MOVE;
					//LogUtils.i("---ACTION_MOVE lastXMove=" + lastXMove + "---lastYMove=" + lastYMove);
					movedDistanceX = x - lastXMove;
					movedDistanceY = y - lastYMove;
					// 进行边界检查，不允许将图片拖出边界
					if (totalTranslateX + movedDistanceX > 0) {
						movedDistanceX = 0;
					} else if (width - (totalTranslateX + movedDistanceX) > currentBitmapWidth) {
						movedDistanceX = 0;
					}
					if (totalTranslateY + movedDistanceY > 0) {
						movedDistanceY = 0;
					} else if (height - (totalTranslateY + movedDistanceY) > currentBitmapHeight) {
						movedDistanceY = 0;
					}
					// 调用onDraw()方法绘制图片
					if (movedDistanceX != 0 || movedDistanceY != 0) {
						invalidate();
						lastXMove = x;
						lastYMove = y;
					}
				} else if (selectType == SelectType.zoom
						&& event.getPointerCount() == 2) {
					// 有两个手指按在屏幕上移动时，为缩放状态
					// centerPointBetweenFingers(event);
					double fingerDis = distanceBetweenFingers(event);
					//LogUtils.i("---ACTION_MOVE fingerDis=" + fingerDis);
					if (fingerDis == lastFingerDis || lastFingerDis <= 0
							|| fingerDis <= 0) { // 相等或者小于0时，不做处理
						break;
					} else if (fingerDis > lastFingerDis) {
						currentStatus = STATUS_ZOOM_OUT;
					} else {
						currentStatus = STATUS_ZOOM_IN;
					}
					// 进行缩放倍数检查，最大只允许将图片放大4倍，最小可以缩小到初始化时的0.3倍
					if ((currentStatus == STATUS_ZOOM_OUT && totalRatio < MaxRatio
							* initRatio)
							|| (currentStatus == STATUS_ZOOM_IN && totalRatio > initRatio)) {
						scaledRatio = (float) (fingerDis / lastFingerDis);
						totalRatio = totalRatio * scaledRatio;
						//LogUtils.i("---scaledRatio=" + scaledRatio + "--totalRatio=" + totalRatio);
						if (totalRatio > MaxRatio * initRatio) {
							totalRatio = MaxRatio * initRatio;
						} else if (totalRatio < initRatio) {
							totalRatio = initRatio;// minRatio;
							//LogUtils.i("---ACTION_MOVE totalRatio=" + totalRatio);
						}
						// 调用onDraw()方法绘制图片
						//LogUtils.i("---ACTION_MOVE totalRatio111=" + totalRatio);
						invalidate();
						lastFingerDis = fingerDis;
					}
				}
				break;
			case MotionEvent.ACTION_POINTER_UP:
				if (event.getPointerCount() == 2) {
					// 手指离开屏幕时将临时值还原
					lastXMove = -1;
					lastYMove = -1;
					//LogUtils.i("---ACTION_POINTER_UP totalRatio=" + totalRatio);
				}
				break;
			case MotionEvent.ACTION_UP:
				// 手指离开屏幕时将临时值还原
				lastXMove = -1;
				lastYMove = -1;
				if (selectType == SelectType.zoom) {
					selectType = oldselectType;
				}
				//LogUtils.i("---ACTION_UP totalRatio=" + totalRatio + ", selectType=" + selectType);
				break;
			default:
				break;
		}
	}
	/**
	 * 对图片进行缩放处理。
	 * @param canvas
	 */
	private void zoom(Canvas canvas) {
		if (sourceBitmap != null) {
			matrix.reset();
			// 将图片按总缩放比例进行缩放
			matrix.postScale(totalRatio, totalRatio);
			float scaledWidth = sourceBitmap.getWidth() * totalRatio;
			float scaledHeight = sourceBitmap.getHeight() * totalRatio;
			float translateX = 0f;
			float translateY = 0f;
			// 如果当前图片宽度小于屏幕宽度，则按屏幕中心的横坐标进行水平缩放。否则按两指的中心点的横坐标进行水平缩放
			if (currentBitmapWidth < width) {
				translateX = (width - scaledWidth) / 2f;
			} else {
				translateX = totalTranslateX * scaledRatio + centerPointX
						* (1 - scaledRatio);
				//LogUtils.i("---translateX=" + translateX + "--scaledWidth=" + scaledWidth);
				// 进行边界检查，保证图片缩放后在水平方向上不会偏移出屏幕
				if (translateX > 0) {
					translateX = 0;
				} else if (width - translateX > scaledWidth) {
					translateX = width - scaledWidth;
				}
			}
			// 如果当前图片高度小于屏幕高度，则按屏幕中心的纵坐标进行垂直缩放。否则按两指的中心点的纵坐标进行垂直缩放
			if (currentBitmapHeight < height) {
				translateY = (height - scaledHeight) / 2f;
			} else {
				translateY = totalTranslateY * scaledRatio + centerPointY
						* (1 - scaledRatio);
				// 进行边界检查，保证图片缩放后在垂直方向上不会偏移出屏幕
				if (translateY > 0) {
					translateY = 0;
				} else if (height - translateY > scaledHeight) {
					translateY = height - scaledHeight;
				}
			}
			//LogUtils.i("---translateX=" + translateX + "--translateY=" + translateY);
			//LogUtils.i("---scaledWidth=" + scaledWidth + "--scaledHeight=" + scaledHeight);
			// 缩放后对图片进行偏移，以保证缩放后中心点位置不变,
			matrix.postTranslate(translateX, translateY);
			totalTranslateX = translateX;
			totalTranslateY = translateY;
			currentBitmapWidth = scaledWidth;
			currentBitmapHeight = scaledHeight;
			canvas.drawBitmap(sourceBitmap, matrix, null);
			if (mHairBm != null) {
				// 对蒙版进行matrix缩放
				canvas.drawBitmap(mHairBm, matrix, null);
			}
		}
	}
	/**
	 * 对图片进行平移处理
	 * @param canvas
	 */
	private void move(Canvas canvas) {
		if (sourceBitmap != null) {
			matrix.reset();
			// 根据手指移动的距离计算出总偏移值
			totalTranslateX = totalTranslateX + movedDistanceX;
			totalTranslateY = totalTranslateY + movedDistanceY;
			//LogUtils.i("totalTranslateX-->" + totalTranslateX + " totalTranslateY-->" + totalTranslateX);
			//LogUtils.i("totalRatio-->" + totalRatio);
			// 先按照已有的缩放比例对图片进行缩放
			// matrix.postScale(totalRatio, totalRatio, centerPointX, centerPointY);
			matrix.postScale(totalRatio, totalRatio);
			// 再根据移动距离进行偏移
			matrix.postTranslate(totalTranslateX, totalTranslateY);
			Log.i("ZoomImageView", "canvas~~" + canvas + "sourceBitmap~~"
					+ sourceBitmap + "matrix~~" + matrix);
			if (sourceBitmap != null) {
				canvas.drawBitmap(sourceBitmap, matrix, null);
			}
			if (mHairBm != null) {
				// 对蒙版进行matrix缩放
				// mHairBm = Bitmap.createBitmap(mHairBm, 0, 0, width, height, matrix, true);
				canvas.drawBitmap(mHairBm, matrix, null);
			}
		}
	}
	/**
	 * ArrayList<Integer> pointlist进行移位
	 */
	private int[] MovePoints(ArrayList<Integer> pointlist) {
		int[] points = new int[pointlist.size()];
		points[0] = pointlist.get(0);
		// 进行坐标变换
		for (int i = 1; i < points.length; i++) {
			points[i] = pointlist.get(i);
			//LogUtils.i("---MovePoints~--points[i]=" + points[i] + ", i=" + i + ", totalTranslateX=" + totalTranslateX
			//					+ ", totalRatio=" + totalRatio);
			if (i % 2 == 1) {
				points[i] = (int) ((points[i] - totalTranslateX) / totalRatio);
				//LogUtils.i("---MovePoints~~00points[i]=" + points[i] + ", i=" + i);
			} else {
				points[i] = (int) ((points[i] - totalTranslateY) / totalRatio);
			}
			//LogUtils.i("---MovePoints~~points[i]=" + points[i]);
		}
		return points;
	}
	/**
	 * int[]point进行坐标变换
	 */
	private int[] PointTranslate(int[] point) {
		// 进行坐标变换
		int length = point.length;
		int[] pointTrans = new int[length];
		for (int i = 0; i < length; i++) {
			if (i % 2 == 0) { // x坐标
				pointTrans[i] = (int) ((point[i] - totalTranslateX /*+ OriginalX*/) / totalRatio);
			} else { // y坐标
				pointTrans[i] = (int) ((point[i] - totalTranslateY/* + OriginalY*/) / totalRatio);
			}
			//LogUtils.i("---PointTranslate~~point[i]=" + point[i] + ",~pointTrans[i]=" + pointTrans[i] + ", i=" + i);
		}
		return pointTrans;
	}
	/**
	 * 获取按下的点位置的图像 mPosX，mPosY：按下的点坐标 取图片区域为，以(mPosX,mPosY)为中心，radius为半径的圆形区域图像
	 */
	public Bitmap getMoveScaleBmp(float mPosX, float mPosY) {
		Bitmap bm1 = null;
		//LogUtils.i("--getMoveScaleBm mPosX=" + mPosX + ", mPosY=" + mPosY);
		//将按下的点的坐标转化成原图上的坐标
		int origX = (int) ((mPosX - totalTranslateX) / totalRatio);
		int origY = (int) ((mPosY - totalTranslateY) / totalRatio);
		int origRadius = (int) (mRadius / totalRatio);
		int startX = origX - origRadius;
		int startY = origY - origRadius;
		int diameter = 2 * origRadius;
		/*
		 * 计算需显示图片区域的左上角的坐标
		 */
		if (startX < 0) {
			startX = 0;
		} else if (startX > width - diameter) {
			startX = width - diameter;
		}
		if (startY < 0) {
			startY = 0;
		} else if (startY > height - diameter) {
			startY = height - diameter;
		}
		//LogUtils.i("----getMoveScaleBm startX=" + startX + ", startY=" + startY);
		if (mHairBm != null && mHairBm.isRecycled() == false) {
			try {
				bm1 = Bitmap.createBitmap(mHairBm, startX, startY, diameter,
						diameter, null, true);
				//LogUtils.i("--getMoveScaleBm bm1 Width()=" + bm1.getWidth() + ", Height()=" + bm1.getHeight());
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
				//LogUtils.i("getMoveScaleBm, OutOfMemoryError");
			} catch (Exception e) {
				e.printStackTrace();
				//LogUtils.i("getMoveScaleBm, Exception");
			}
		}
		return bm1;
	}
	public void UnDo() {
		//LogUtils.i("undo click ObjAddr=" + cacheimage.getNativeObjAddr());
		if (isCartoonMode) {
			CartoonLib.UndoMat(cacheimage.getNativeObjAddr());
		} else {
			CartoonLib.LastStation(cacheimage.getNativeObjAddr());
		}
		org.opencv.android.Utils.matToBitmap(cacheimage, mHairBm, true);
		invalidate();
	}
	public void ReDo() {
		//LogUtils.i("redo click ObjAddr=" + cacheimage.getNativeObjAddr());
		if (isCartoonMode) {
			CartoonLib.RedoMat(cacheimage.getNativeObjAddr());
		} else {
			CartoonLib.NextStation(cacheimage.getNativeObjAddr());
		}
		org.opencv.android.Utils.matToBitmap(cacheimage, mHairBm, true);
		invalidate();
	}
	public void clear() {
		// 擦除模式
		if (paint != null) {
			// xmode = null;
			selectType = SelectType.clear;
			xmode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
			paint.setXfermode(xmode);
			paint.setStrokeWidth(24); // 设置笔触的宽度
		}
	}
	public void area() {
		// 圈选模式
		if (paint != null) {
			xmode = null;
			selectType = SelectType.area;
			paint.setXfermode(xmode);
			paint.setColor(Color.RED);
			paint.setStrokeWidth(10); // 设置默认笔触的宽度为5像素
		}
	}
	public void addDraw() {
		// 绘制模式
		if (paint != null) {
			xmode = null;
			selectType = SelectType.add;
			paint.setXfermode(xmode);
			paint.setColor(Color.RED);
			paint.setStrokeWidth(24); // 设置默认笔触的宽度为1像素
		}
	}
	public void moveDraw() {
		// 移动模式
		if (paint != null) {
			xmode = null;
			selectType = SelectType.move;
			paint.setXfermode(xmode);
			paint.setColor(Color.RED);
			paint.setStrokeWidth(24); // 设置默认笔触的宽度为1像素
		}
	}
	public void setCartoonMode(boolean isCartoonMode) {
		// 移动模式
		this.isCartoonMode = isCartoonMode;
	}
	/**
	 * 计算两个手指之间的距离。
	 * @param event
	 * @return 两个手指之间的距离
	 */
	private double distanceBetweenFingers(MotionEvent event) {
		float disX = Math.abs(event.getX(0) - event.getX(1));
		float disY = Math.abs(event.getY(0) - event.getY(1));
		//LogUtils.i("event.getX(0)~~" + event.getX(0) + "event.getX(1)~~" + event.getX(1));
		//LogUtils.i("event.getY(0)~~" + event.getY(0) + "event.getY(1)~~" + event.getY(1));
		return Math.sqrt(disX * disX + disY * disY);
	}
	/**
	 * 计算两个手指之间中心点的坐标。
	 * @param event
	 */
	private void centerPointBetweenFingers(MotionEvent event) {
		float xPoint0 = event.getX(0);
		float yPoint0 = event.getY(0);
		float xPoint1 = event.getX(1);
		float yPoint1 = event.getY(1);
		centerPointX = (xPoint0 + xPoint1) / 2;
		centerPointY = (yPoint0 + yPoint1) / 2;
	}
	/**
	 * 设置事件回调接口
	 * @param onEventChanageListener
	 */
	public void setOnEventChanageListener(
			OnEventChanageListener onChanageListener) {
		this.onEventChanageListener = onChanageListener;
	}

	/**
	 * 监听编辑框文本变化
	 */
	public interface OnEventChanageListener {
		/**
		 * 当eventAction变化时
		 * @param editTextStr 最后的text
		 */
		public void onChange(float x, float y, MotionEvent event,
				float totalRatio, float fx, float fy, boolean isAreaMode);
	}

	public void setHairVisibility(int gone) {
		// TODO Auto-generated method stub
		switch (gone) {
			case View.GONE:
				currentStatus = STATUS_DEFAULT;
				break;
			case View.VISIBLE:
				currentStatus = STATUS_INIT;
				break;
			default:
				break;
		}
		invalidate();
	}
	public void setTouchEventdisable(boolean disable) {
		disableTouchEvent = disable;
	}
}
