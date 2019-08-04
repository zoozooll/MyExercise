package com.beem.project.btf.ui.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.views.CartoonShareActivity;
import com.beem.project.btf.ui.views.CartoonShareActivity.RestartActivityType;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.PictureUtil;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.beem.project.btf.utils.UIHelper;
import com.beem.project.btf.utils.UIHelper.UIHelperDialogType;
import com.butterfly.piqs.vvcartoon.CartoonLib;
import com.butterfly.vv.cartoon.MyPointEvaluator;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.TypeEvaluator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nineoldandroids.view.ViewHelper;

import de.greenrobot.event.EventBus;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/** 绘制步骤 */
public class CartoonStep3Activity extends Activity {
	private static final String TAG = "CartoonStep3Activity";
	private Mat drawing;
	private static Bitmap showbm;
	private ImageView people_imageview;
	private TextView process_hint;
	private ImageButton mCartoonOk;
	private ImageButton mCartoonDelete;
	private ImageView brushview;
	//图片显示区域
	private int mImageShowWidth = 0;
	private int mImageShowHeight = 0;
	private int mBmWidth = 0;
	private int mBmHeight = 0;
	//points 永远在同步后台正在画的最新的部位的线条；
	private Point[] points;
	//currentpoints 同步画笔当前正在画的线条；
	private Point[] currentpoints;
	private ValueAnimator valueAnimator;
	private AnimationDrawable frameanim;
	/** 开始画器官有关的线条（后台） */
	private final int BRUSH_START = 1;
	private final int BRUSH_END = 2;
	private final int BRUSH_PRE = 3;
	/** 画笔路径线条改变 */
	private final int BRUSH_DRAWING = 4;
	private final int BRUSH_ERROR_NOHAIR = 0x11;
	private boolean isfinish = false;
	private int[] mCartoonPoint = new int[51];
	private Integer[] facepoint;
	private String mBmPath;
	private Timer timer;
	private Queue<BrushLine> lineQueue = new LinkedBlockingQueue<BrushLine>();
	private Point lastHeadPoints;
	// 画图的状态；
	private int state;
	// 画笔动画循环次数
	private int repeatCount;
//	private Canvas debugCanvas;
//	private Paint debugPaint;
//	private Bitmap debugBitmap;
	private TimerTask titask = new TimerTask() {
		@Override
		public void run() {
			getdata();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cartoonstep3_layout);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mImageShowWidth = metric.widthPixels; // 屏幕宽度（像素）
		mImageShowHeight = metric.heightPixels
				- getResources().getDimensionPixelSize(
						R.dimen.time_save_btm_bar_height); // 屏幕高度（像素）
		people_imageview = (ImageView) findViewById(R.id.people_imageview);
		process_hint = (TextView) findViewById(R.id.process_hint);
		mCartoonOk = (ImageButton) findViewById(R.id.cartoon_ok);
		mCartoonDelete = (ImageButton) findViewById(R.id.cartoon_delete);
		mCartoonOk.setVisibility(View.GONE);
		mCartoonDelete.setVisibility(View.GONE);
		EventBus.getDefault().register(this);
		mCartoonDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mCartoonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					people_imageview.setImageBitmap(showbm);
					mBmPath = PictureUtil.saveToTemp(
							CartoonStep3Activity.this, showbm);
					//Log.i(TAG, "~mBmPath~1"+mBmPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				File tempCameraFile = new File(PictureUtil.getCartoonTempImage().getPath());
				if (tempCameraFile.exists()) {
					tempCameraFile.delete();
				}
				CartoonShareActivity.launch(CartoonStep3Activity.this, mBmPath,
						RestartActivityType.CartoonCamera.toString());
				//结束前面的页面
				EventBus.getDefault().post(
						new EventBusData(EventAction.FinishActivity, null));
			}
		});
		brushview = (ImageView) findViewById(R.id.brushview);
		frameanim = (AnimationDrawable) brushview.getBackground();
		facepoint = SharedPrefsUtil.getValue(CartoonStep3Activity.this,
				"facepoint");
//		Log.i(TAG, Arrays.toString(facepoint));
		new AsyncTask<Void, Integer, Void>() {
			
			@Override
			protected void onPreExecute() {
				UIHelper.showDialogForLoading(CartoonStep3Activity.this,
						UIHelperDialogType.Simple, false);
			};
			@Override
			protected Void doInBackground(Void... params) {
				showbm = PictureUtil.decodeUriAsBitmap(PictureUtil
						.getCartoonTempImage());
				showbm = PictureUtil.scaleBitmap(showbm, mImageShowWidth,
						mImageShowHeight);
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				UIHelper.hideDialogForLoading();
				// Debug image
//				debugBitmap = Bitmap.createBitmap(showbm.getWidth(), showbm.getHeight(), Bitmap.Config.ARGB_8888);
//				debugCanvas = new Canvas(debugBitmap);
//				debugPaint = new Paint();
//				debugPaint.setColor(0xffff0000);
//				debugPaint.setStrokeWidth(5);
//				debugCanvas.drawBitmap(showbm, 0, 0, debugPaint);
				if (showbm != null) {
					showbm = PictureUtil.scaleBitmap(showbm, mImageShowWidth,
							mImageShowHeight);
					mBmWidth = showbm.getWidth();
					mBmHeight = showbm.getHeight();
					people_imageview.setImageBitmap(showbm);
					//启动绘图线程,在jni里执行
					Bitmap hairBm = Bitmap.createBitmap(mBmWidth, mBmHeight,
							Config.ARGB_8888);
					drawing = new Mat();
					Utils.bitmapToMat(hairBm, drawing, true);
					CartoonLib.setHairOKMat(drawing.getNativeObjAddr());
					isfinish = false;
				}
				timer = new Timer();
				timer.schedule(titask, 100, 100);
			};
		}.execute();
		initpoints();
		initanim();
		
	}
	//画笔移动动画
	private void initanim() {
		valueAnimator = new ValueAnimator();
		valueAnimator.setObjectValues(currentpoints);
		valueAnimator.setEvaluator(new MyPointEvaluator());
		valueAnimator.setInterpolator(new LinearInterpolator());
		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Point point = (Point) animation.getAnimatedValue();
//				Log.d(TAG, "onAnimationUpdate " + point);
				ViewHelper.setX(brushview, point.x - brushview.getWidth() / 2);
				ViewHelper.setY(brushview, point.y - brushview.getHeight());
			}
		});
		valueAnimator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
//				Log.i(TAG, "valueAnimator_end");
//				if (isfinish) {
//					Utils.matToBitmap(drawing, showbm, true);
//					people_imageview.setImageBitmap(showbm);
//					people_imageview.setImageBitmap(showbm);
//				}
				/*if (!isfinish) {
					currentpoints = points;
					mHandler.obtainMessage(BRUSH_START).sendToTarget();
				}*/
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				super.onAnimationRepeat(animation);
				if (lineQueue.size() > 0) {
					valueAnimator.cancel();
					BrushLine line = lineQueue.poll();
					currentpoints = line.point;
					showbm = PictureUtil.getBitmapFormFile(line.bmpPath, 1);
					people_imageview.setImageBitmap(showbm);
					PictureUtil.deleteTempFile(line.bmpPath);
					valueAnimator.setObjectValues(currentpoints);
					valueAnimator.setDuration(currentpoints.length * 100);
					valueAnimator.start();
				} else if (CartoonLib.isDrawing(state) || CartoonLib.isPrepaer(state)) {
					repeatCount ++;
				} else {
					valueAnimator.cancel();
					mHandler.obtainMessage(BRUSH_END).sendToTarget();
					
				}
			}

			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				repeatCount = 0;
			}
		});
	}
	//循环动画设置
	private void setFristAnim() {
		valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
		valueAnimator.setRepeatMode(ValueAnimator.RESTART);
	}
	private void initpoints() {
//		points.clear();
		/*points = new Point[30];
		int offsetx = Math.abs(facepoint[3] - facepoint[1]) / 10;
		int offsety = Math.abs(facepoint[4] - facepoint[2]);
		for (int i = 0; i < 30; i++) {
			//封装笔画路径
			Point point = new Point();
			if (i < 10) {
				point.x = facepoint[1] + offsetx * i;
				point.y = facepoint[2];
			} else if (i >= 10 && i < 20) {
				point.x = facepoint[3] - offsetx * (i - 10);
				point.y = facepoint[2] + offsety;
			} else if (i >= 20 && i < 30) {
				point.x = facepoint[1] + offsetx * (i - 20);
				point.y = facepoint[2] + offsety * 2;
			}
			points[i] = point;
		}*/
		int dx = (facepoint[3] - facepoint[1]) >> 1;
		int dy = (facepoint[6] - facepoint[2]) >> 1;
		points = new Point[9];
		points[0] = new Point(facepoint[1] - dx, facepoint[2]);
		points[1] = new Point(facepoint[1] + dx, facepoint[2] - dy);
		points[2] = new Point(facepoint[3] + dx, facepoint[2]);
		points[3] = new Point(facepoint[1] + dx, facepoint[2] + dy);
		points[4] = new Point(facepoint[1] - dx, facepoint[6]);
		points[5] = new Point(facepoint[1] + dx, facepoint[6] + dy);
		points[6] = new Point(facepoint[3] + dx, facepoint[6]);
		points[7] = new Point(facepoint[1] + dx, facepoint[2] + dy);
		points[8] = new Point(facepoint[1] - dx, facepoint[2]);
	
		currentpoints = points;
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case BRUSH_PRE: {
					//Log.i(TAG, "~BRUSH_PRE~");
					process_hint.setText("初始化模型数据中...");
					if (brushview.getVisibility() == View.GONE) {
						brushview.setVisibility(View.VISIBLE);
					}
					if (!valueAnimator.isRunning()) {
						setFristAnim();
						frameanim.start();
						valueAnimator.setDuration(currentpoints.length * 900);
						valueAnimator.start();
					}
					break;
				}
				case BRUSH_START: {
					//Log.i(TAG, "~BRUSH_START~");
					if (!isfinish) {
						process_hint.setText("正在绘制...");
					}
					//开启画笔动画
					if (lineQueue.size() == 0 && repeatCount > 0) {
						// 队列中没有线条数据，且循环已经超过一次，
						// 说明画笔画的是当前的部位，且已经超过一个循环
						// 此时开始切换画笔动画的部位
						valueAnimator.cancel();
						currentpoints = points;
						valueAnimator.setObjectValues(currentpoints);
						valueAnimator.setDuration(currentpoints.length * 100);
						valueAnimator.start();
						Utils.matToBitmap(drawing, showbm, true);
						people_imageview.setImageBitmap(showbm);
					} else {
						// 如果不符合，需要将当前正在画的部位线条放入到队列中，画笔继续处理；
						Bitmap m = Bitmap.createBitmap(showbm.getWidth(), showbm.getHeight(), showbm.getConfig());
						Utils.matToBitmap(drawing, m, true);
						String path;
						try {
							path = PictureUtil.saveToTemp(CartoonStep3Activity.this, m);
							BrushLine line = new BrushLine();
							line.bmpPath = path;
							line.point = points;
							lineQueue.offer(line);
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							m.recycle();
						}
					}
					break;
				}
				case BRUSH_DRAWING: {
					if (!isfinish) {
						process_hint.setText("正在绘制...");
					}
					showBrushingAnimation();
				}
					break;
				case BRUSH_END: {
					// 绘制结束
					Log.i(TAG, "~BRUSH_END~");
					frameanim.stop();
//					valueAnimator.cancel();
					brushview.setVisibility(View.GONE);
					Utils.matToBitmap(drawing, showbm, true);
					people_imageview.setImageBitmap(showbm);
					process_hint.setText("绘制成功");
					mCartoonOk.setVisibility(View.VISIBLE);
					mCartoonDelete.setVisibility(View.VISIBLE);
					isfinish = true;
					/*try {
						PictureUtil.saveToSDCard(CartoonStep3Activity.this,
								showbm);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					/*try {
						PictureUtil.saveToDICM(CartoonStep3Activity.this, debugBitmap);
					} catch (IOException e) {
						e.printStackTrace();
					}*/
					break;
				}
				case BRUSH_ERROR_NOHAIR: {
					if (timer != null) {
						timer.cancel();
						timer = null;
					}
					frameanim.stop();
					valueAnimator.cancel();
					brushview.setVisibility(View.GONE);
					com.butterfly.vv.vv.utils.CToast.showToast(
							CartoonStep3Activity.this, "选择头发区域有误",
							android.widget.Toast.LENGTH_SHORT);
					finish();
				}
					break;
			}
		}
	};

	private void getdata() {
		int state = CartoonLib.getcurrentActionMat(drawing.getNativeObjAddr(),
				mCartoonPoint);
		int[] dataPoints = new int[mCartoonPoint.length];
		System.arraycopy(mCartoonPoint, 0, dataPoints, 0, mCartoonPoint.length);
		/*if (showbm != null) {
				Log.i(TAG, "~com.amlogic.osdoverlay.rows()~" + drawing.rows() + "~drawing.cols()~" + drawing.cols() + "~mBmWidth~"
						+ mBmWidth + "~mBmHeight~" + mBmHeight);
		}*/
//		Log.d(TAG, "getdata "+state + Arrays.toString(dataPoints));
		// Debug points
		
//		for (Point p  : currentpoints) {
//		}
		if (state == CartoonLib.FINISH) {
			//Log.i(TAG, "FINISH");
			// 发送绘制完成的消息通知
			// 新的方案，未能立刻停止动画，因为画笔未画完
			lastHeadPoints = null;
			if (timer != null) {
				timer.cancel();
				timer = null;
			}
//			mHandler.obtainMessage(BRUSH_END).sendToTarget();
		} else if (CartoonLib.isPrepaer(state)) {
			// 数据准备状态
			//Log.i(TAG, "PREPAER");
			lastHeadPoints = null;
			mHandler.obtainMessage(BRUSH_PRE).sendToTarget();
		} else if (CartoonLib.CARTOON_ERROR_NO_HAIR == state) {
			lastHeadPoints = null;
			mHandler.sendEmptyMessage(BRUSH_ERROR_NOHAIR);
		} else {
			/*points.clear();
			for (int i = 1; i < mCartoonPoint[0]; i += 2) {
				//封装笔画路径
				Point point = new Point();
				point.x = mCartoonPoint[i];
				point.y = mCartoonPoint[i + 1];
				points.add(point);
			}
			mHandler.obtainMessage(BRUSH_START).sendToTarget();*/
			int size = dataPoints[0];
			if (size > 1) {
				Point[] p = new Point[(size - 1) / 2];
				for (int i = 1; i < size; i += 2 ) {
					p[i / 2] = new Point(dataPoints[i], dataPoints[i + 1]);
//					debugCanvas.drawPoint(dataPoints[i], dataPoints[i + 1], debugPaint);
				}
				if (lastHeadPoints == null
						|| Math.abs(lastHeadPoints.x - p[0].x) > 100
						|| Math.abs(lastHeadPoints.y - p[0].y) > 100) {
					// 判断，如果当前的线的第一个点于上一个部位线条的第一个点的x或者y距离超过100
					// 则判断为画的部位发生了变化;
					points = p;
					mHandler.obtainMessage(BRUSH_START).sendToTarget();
				}
				lastHeadPoints = p[0] ;
			}
		}
		this.state= state;
	}
	
	private void showBrushingAnimation() {
		if (!valueAnimator.isRunning()) {
			valueAnimator.start();
		}
		valueAnimator.setDuration(currentpoints.length * 100);
		if (valueAnimator.getRepeatCount() != 0) {
			valueAnimator.setRepeatCount(0);
			
		}
	}
	
	@Override
	public void onBackPressed() {
		if (isfinish) {
			mHandler.removeCallbacksAndMessages(null);
			super.onBackPressed();
		}
	}
	/**
	 * 响应数据更新
	 */
	public void onEventMainThread(final EventBusData data) {
		if (data.getAction() == EventAction.FinishActivity) {
			finish();
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (showbm != null && !showbm.isRecycled()) {
			showbm.recycle();
		}
		EventBus.getDefault().unregister(this);
	}
	
	private class BrushLine {
		int state;
		Point[] point;
		String bmpPath;
		
	}
}
