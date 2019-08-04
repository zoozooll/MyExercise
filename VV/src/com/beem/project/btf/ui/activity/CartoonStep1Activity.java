package com.beem.project.btf.ui.activity;

import java.io.IOException;
import java.util.Arrays;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import com.beem.project.btf.R;
import com.beem.project.btf.ui.MagnifyView;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.views.FaceView;
import com.beem.project.btf.ui.views.FaceView.OnEventListener;
import com.beem.project.btf.ui.views.FaceView.FaceViewType;
import com.beem.project.btf.utils.DimenUtils;
import com.beem.project.btf.utils.PictureUtil;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.beem.project.btf.utils.UIHelper;
import com.butterfly.piqs.vvcartoon.CartoonLib;

import de.greenrobot.event.EventBus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/** 五官定位步骤 */
public class CartoonStep1Activity extends Activity {
	private static final String TAG = "CartoonStep1Activity";
	// 图片显示区域
	private int mImageShowWidth = 0;
	private int mImageShowHeight = 0;
	private Context mContext;
	private String mBmPath;
	private static final String IMAGEPATH = "BitmapPath";
	private MagnifyView mMagnView;// 放大镜
	private ImageView people_imageview;
	private TextView mSelectMan, mSelectWoman;
	private ImageView mCartoonGifTipView;// gif图片
	private Bitmap mBm;
	private int facestate = CartoonLib.FAILURE;
	public Mat cameraimg = new Mat();
	/**
	 * 双眼以及嘴的位置坐标 
	 * index:0 ==> 0表示定位失败，1表示成功
	 * index:1 ==> 左眼x坐标;
	 * index:2 ==> 左眼y坐标;
	 * index:3 ==> 右眼x坐标;
	 * index:4 ==> 右眼y坐标;
	 * index:5 ==> 嘴x坐标;
	 * index:6 ==> 嘴x坐标;
	 *  */
	private static int[] mFacePoint = new int[8];
	private int mBmWidth = 0;
	private int mBmHeight = 0;
	private RelativeLayout people_imageview_wraper;
	private FaceView mLeftEye, mRightEye, mMouth;

	public static void launch(Context context, String path) {
		Intent intent = new Intent(context, CartoonStep1Activity.class);
		intent.putExtra(IMAGEPATH, path);
		context.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cartoonstep1_layout);
		mContext = this;
		EventBus.getDefault().register(this);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mImageShowWidth = metric.widthPixels; // 屏幕宽度（像素）
		mImageShowHeight = metric.heightPixels
				- DimenUtils.dip2px(mContext, 120);// 屏幕高度（像素）
		Intent intent = getIntent();
		mBmPath = intent.getStringExtra(IMAGEPATH);
		people_imageview = (ImageView) findViewById(R.id.people_imageview);
		mMagnView = (MagnifyView) findViewById(R.id.cartoon_magn_view);
		people_imageview_wraper = (RelativeLayout) findViewById(R.id.people_imageview_wraper);
		mSelectMan = (TextView) findViewById(R.id.take_photo_man);
		mSelectWoman = (TextView) findViewById(R.id.take_photo_woman);
		mSelectMan.setEnabled(false);
		mSelectWoman.setEnabled(false);
		mSelectMan.setOnClickListener(mSelectSexBtnListener);
		mSelectWoman.setOnClickListener(mSelectSexBtnListener);
		mCartoonGifTipView = (ImageView) findViewById(R.id.cartoon_gif_tip);
		final AnimationDrawable frameanim = (AnimationDrawable) mCartoonGifTipView
				.getBackground();
		new AsyncTask<Void, Integer, Void>() {
			@Override
			protected void onPreExecute() {
				UIHelper.showDialogForLoading(mContext, "识别脸部特征中....", false);
			};
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				try {
					PictureUtil.CompressTempBitmap(mBmPath, PictureUtil
							.getCartoonTempImage().getPath());
					mBm = PictureUtil.decodeUriAsBitmap(PictureUtil
							.getCartoonTempImage());
					mBm = PictureUtil.scaleBitmap(mBm, mImageShowWidth,
							mImageShowHeight);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 写入三个定位点，调用jni库
				Utils.bitmapToMat(mBm, cameraimg, true);
				CartoonLib.setBitmapMat(cameraimg.getNativeObjAddr(),
						mFacePoint);
				if (mFacePoint[0] > 0) {
					facestate = CartoonLib.SUCESS;
				} else {
					facestate = CartoonLib.FAILURE;
				}
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				if (mBm != null) {
					frameanim.start();
					mMagnView.setSourceBitmap(mBm);
					mBmWidth = mBm.getWidth();
					mBmHeight = mBm.getHeight();
					people_imageview.setImageBitmap(mBm);
					mLeftEye = new FaceView(mContext, FaceViewType.LeftEye,
							mBmWidth, mBmHeight);
					mRightEye = new FaceView(mContext, FaceViewType.RightEye,
							mBmWidth, mBmHeight);
					mMouth = new FaceView(mContext, FaceViewType.Mouth,
							mBmWidth, mBmHeight);
					mLeftEye.setOnEventChanageListener(mTouchEventListener);
					mRightEye.setOnEventChanageListener(mTouchEventListener);
					mMouth.setOnEventChanageListener(mTouchEventListener);
					people_imageview_wraper.addView(mLeftEye);
					people_imageview_wraper.addView(mRightEye);
					people_imageview_wraper.addView(mMouth);
					if (facestate == CartoonLib.SUCESS) {
						// 脸部识别成功标识三个点的位置
						Log.i(TAG, "坐标~~" + Arrays.toString(mFacePoint));
						mSelectMan.setEnabled(true);
						mSelectWoman.setEnabled(true);
						people_imageview_wraper.getViewTreeObserver()
								.addOnGlobalLayoutListener(
										new OnGlobalLayoutListener() {
											@Override
											public void onGlobalLayout() {
												// TODO Auto-generated method
												// stub
												mLeftEye.setViewPosition(
														mFacePoint[1],
														mFacePoint[2]);
												mRightEye.setViewPosition(
														mFacePoint[3],
														mFacePoint[4]);
												mMouth.setViewPosition(
														mFacePoint[5],
														mFacePoint[6]);
											}
										});
					} else {
						// 未检测到人脸
						Toast.makeText(mContext, "未识别到人脸,请重新选择图片或手动定位",
								Toast.LENGTH_LONG).show();
						people_imageview_wraper.getViewTreeObserver()
								.addOnGlobalLayoutListener(
										new OnGlobalLayoutListener() {
											@Override
											public void onGlobalLayout() {
												// TODO Auto-generated method
												// stub
												mLeftEye.setViewPosition(
														mLeftEye.getWidth(),
														mLeftEye.getHeight());
												mRightEye.setViewPosition(
														mLeftEye.getWidth()
																+ mRightEye
																		.getWidth(),
														mRightEye.getHeight());
												mMouth.setViewPosition(
														mLeftEye.getWidth() * 3 / 2,
														mMouth.getHeight()
																+ mLeftEye
																		.getHeight());
											}
										});
						mSelectMan.setEnabled(false);
						mSelectWoman.setEnabled(false);
					}
				}
				// 以上工作处理完才关掉对话框
				UIHelper.hideDialogForLoading();
			};
		}.execute();
	}

	/** 实现放大镜接口 */
	OnEventListener mTouchEventListener = new OnEventListener() {
		@Override
		public void onChange(float x, float y, MotionEvent event,
				FaceViewType viewtype) {
			// TODO Auto-generated method stub
			switch (event.getActionMasked()) {
				case MotionEvent.ACTION_DOWN:
					mMagnView.setVisibility(View.VISIBLE);
					mMagnView.setIndicatorType(viewtype);
					mMagnView.onMagTouchEvent(x, y);
					break;
				case MotionEvent.ACTION_MOVE:
					mMagnView.onMagTouchEvent(x, y);
					break;
				case MotionEvent.ACTION_UP:
					mMagnView.setVisibility(View.INVISIBLE);
					if (mLeftEye.ismoved() && mRightEye.ismoved()
							&& mMouth.ismoved()) {
						mSelectMan.setEnabled(true);
						mSelectWoman.setEnabled(true);
					}
					break;
			}
		}
	};
	/** 性别选择监听 */
	OnClickListener mSelectSexBtnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int sex = CartoonLib.VV_SEX_MALE;
			mFacePoint[0] = mFacePoint.length - 1;
			mFacePoint[1] = mLeftEye.getCenterPoint().x;
			mFacePoint[2] = mLeftEye.getCenterPoint().y;
			mFacePoint[3] = mRightEye.getCenterPoint().x;
			mFacePoint[4] = mRightEye.getCenterPoint().y;
			mFacePoint[5] = mMouth.getCenterPoint().x;
			mFacePoint[6] = mMouth.getCenterPoint().y;
			if (v == mSelectMan) {
				sex = CartoonLib.VV_SEX_MALE;
			} else if (v == mSelectWoman) {
				sex = CartoonLib.VV_SEX_FEMALE;
			}
			SharedPrefsUtil.putValue(mContext, "facepoint", mFacePoint);
			Intent intent = new Intent(CartoonStep1Activity.this,
					CartoonStep2Activity.class);
			intent.putExtra("sexstate", sex);
			intent.putExtra("hairstate", mFacePoint);
			startActivity(intent);
		}
	};

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
		if (mBm != null && !mBm.isRecycled()) {
			mBm.recycle();
		}
		EventBus.getDefault().unregister(this);
	}
}
