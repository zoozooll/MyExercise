package com.beem.project.btf.ui.activity;

import java.io.IOException;
import java.util.ArrayList;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.ui.EditImageView;
import com.beem.project.btf.ui.EditImageView.OnEventChanageListener;
import com.beem.project.btf.ui.MagnifyView;
import com.beem.project.btf.ui.activity.base.VVBaseActivity;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.guide.IntroduceActivity;
import com.beem.project.btf.utils.PictureUtil;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.beem.project.btf.utils.UIHelper;
import com.butterfly.piqs.vvcartoon.CartoonLib;

import de.greenrobot.event.EventBus;

public class TimeCameraSaveActivity extends VVBaseActivity {
	private EditImageView mShowImage;
	private Bitmap mBm;// 用于接受传过来的图片
	private MagnifyView mMagnView;// 放大镜
	//private int mDisplayScaleLayout = 0; // 放大镜 0 不显示， 1显示
	private ImageButton mCartoonOk;
	private ImageButton mCartoonDelete;
	private ImageButton mCancelButton;
	private ImageButton mRestoreButton;
	private Button mCartoonAdd, mCartoonEarse, mCartoonArea, mCartoonMove;
	private Context mContext;
	private int mBmWidth = 0;
	private int mBmHeight = 0;
	private String mBmPath;
	private static final String TAG = "TimeCameraSaveActivity";
	private static Mat cameraimg = new Mat();
	private ArrayList<View> mCartoonViews = new ArrayList<View>();
	//图片显示区域
	private int mImageShowWidth = 0;
	private int mImageShowHeight = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.time_camera_save);
		EventBus.getDefault().register(this);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mImageShowWidth = metric.widthPixels; // 屏幕宽度（像素）
		mImageShowHeight = metric.heightPixels
				- getResources().getDimensionPixelSize(
						R.dimen.time_save_btm_bar_height); // 屏幕高度（像素）
		mContext = this;
		Intent intent1 = getIntent();
		mBmPath = intent1.getStringExtra("BitmapPath");
		mShowImage = (EditImageView) findViewById(R.id.cartoon_image_show);
		mCartoonOk = (ImageButton) findViewById(R.id.cartoon_ok);
		mCartoonDelete = (ImageButton) findViewById(R.id.cartoon_delete);
		mCancelButton = (ImageButton) findViewById(R.id.undobtn);
		mRestoreButton = (ImageButton) findViewById(R.id.redobtn);
		mCartoonArea = (Button) findViewById(R.id.cartoon_area);
		mCartoonArea.setTag(0);
		mCartoonAdd = (Button) findViewById(R.id.cartoon_add);
		mCartoonAdd.setTag(1);
		mCartoonEarse = (Button) findViewById(R.id.cartoon_earse);
		mCartoonEarse.setTag(2);
		mCartoonMove = (Button) findViewById(R.id.cartoon_move);
		mCartoonMove.setTag(3);
		mCartoonViews.add(mCartoonArea);
		mCartoonViews.add(mCartoonAdd);
		mCartoonViews.add(mCartoonEarse);
		mCartoonViews.add(mCartoonMove);
		mMagnView = (MagnifyView) findViewById(R.id.cartoon_magn_view);
		mCartoonOk.setOnClickListener(mBtnClickListener);
		mCartoonDelete.setOnClickListener(mBtnClickListener);
		mCancelButton.setOnClickListener(mBtnClickListener);
		mRestoreButton.setOnClickListener(mBtnClickListener);
		mCartoonAdd.setOnClickListener(mBtnClickListener);
		mCartoonEarse.setOnClickListener(mBtnClickListener);
		mCartoonArea.setOnClickListener(mBtnClickListener);
		mCartoonMove.setOnClickListener(mBtnClickListener);
		findViewById(R.id.introduce_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(TimeCameraSaveActivity.this,
								IntroduceActivity.class);
						intent.putExtra("which", 0);
						startActivity(intent);
					}
				});
		new AsyncTask<Void, Integer, Void>() {
			@Override
			protected void onPreExecute() {
				UIHelper.showDialogForLoading(mContext, "读取图片中....", false);
			};
			@Override
			protected Void doInBackground(Void... params) {
				//mBm = PictureUtil.revitionImage(mBmPath, mImageShowWidth, mImageShowHeight);
				try {
					PictureUtil.CompressTempBitmap(mBmPath, PictureUtil
							.getCartoonTempImage().getPath());
					mBm = PictureUtil.decodeUriAsBitmap(PictureUtil
							.getCartoonTempImage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				initBitmap(mBm);
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				if (mBm != null) {
					try {
						// 把图片数据放到放大镜里
						mMagnView.setSourceBitmap(mBm);
						mBmWidth = mBm.getWidth();
						mBmHeight = mBm.getHeight();
						Log.i(TAG, "onCreate, bmWidth = " + mBmWidth
								+ ", bmHeight = " + mBmHeight);
						// 显示相机获取到的图片
						mShowImage.setImageBitmap(mBm);
						// 创建一块同等大小的人物蒙版
						Bitmap hairBm = Bitmap.createBitmap(mBmWidth,
								mBmHeight, Config.ARGB_8888);
						// 把头发蒙版数据放到mHsView
						mShowImage.setHairBitmap(hairBm);
						// 设置放大镜
						Utils.bitmapToMat(mBm, cameraimg, true);
						int setBstate = CartoonLib.getSourceMat(cameraimg
								.getNativeObjAddr());
						// 设置默认选中项——圈选模式
						mCartoonAdd.setSelected(true);
						mShowImage.setCartoonMode(false);
						mShowImage.addDraw();
						mShowImage
								.setOnEventChanageListener(mTouchEventChangeListener);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				UIHelper.hideDialogForLoading();
				if (!SharedPrefsUtil.getValue(mContext, this.getClass()
						.getName(), false)) {
					Intent intent = new Intent(TimeCameraSaveActivity.this,
							IntroduceActivity.class);
					intent.putExtra("which", 0);
					startActivity(intent);
					SharedPrefsUtil.putValue(mContext, this.getClass()
							.getName(), true);
				}
			};
		}.execute();
	}
	private void initBitmap(Bitmap bitmap) {
		if (bitmap != null) {
			Matrix matrix = new Matrix();
			matrix.reset();
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			if (width > mImageShowWidth || height > mImageShowHeight) {
				if (width - mImageShowWidth > height - mImageShowHeight) {
					// 当图片宽度大于屏幕宽度时，将图片等比例压缩，使它可以完全显示出来
					float ratio = mImageShowWidth / (width * 1.0f);
					matrix.postScale(ratio, ratio);
				} else {
					// 当图片高度大于屏幕高度时，将图片等比例压缩，使它可以完全显示出来
					float ratio = mImageShowHeight / (height * 1.0f);
					matrix.postScale(ratio, ratio);
				}
				mBm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
						true);
			} else {
				if (mImageShowWidth - width > mImageShowHeight - height) {
					// 当屏幕高度大于图片高度时，将图片等比例缩放，使它可以完全显示出来
					float ratio = mImageShowHeight / (height * 1.0f);
					matrix.postScale(ratio, ratio);
				} else {
					// 当屏幕宽度大于图片宽度时，将图片等比例缩放，使它可以完全显示出来
					float ratio = mImageShowWidth / (width * 1.0f);
					matrix.postScale(ratio, ratio);
				}
				mBm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
						true);
			}
		}
	}

	OnClickListener mBtnClickListener = new OnClickListener() {
		@Override
		public void onClick(View paramView) {
			// TODO Auto-generated method stub
			if (paramView == mCartoonOk) {
				// 保存并跳转
				int size[] = new int[2];
				CartoonLib.reurnNewMatWidthHeight(size);
				if (size[0] <= 0 || size[1] <= 0) {
					Toast.makeText(mContext, "请先抠图", Toast.LENGTH_SHORT).show();
				} else {
					Bitmap bm = Bitmap.createBitmap(size[0], size[1],
							Config.ARGB_8888);
					Mat mat = new Mat();
					Utils.bitmapToMat(bm, mat, true);
					CartoonLib.getNewSrcMat(mat.getNativeObjAddr());
					Utils.matToBitmap(mat, bm, true);
					TimeCameraEditorActivity.setCartoonBm(bm);
					Intent intent1 = new Intent(TimeCameraSaveActivity.this,
							TimeCameraEditorActivity.class);
					startActivity(intent1);
				}
			} else if (paramView == mCartoonDelete) {
				// 返回拍照
				TimeCameraSaveActivity.this.finish();
			} else if (paramView == mCartoonAdd) {
				Log.d(TAG, "~~mCartoonAdd~~");
				mShowImage.addDraw();
				setCartoonViewSelected((Integer) mCartoonAdd.getTag());
			} else if (paramView == mCartoonEarse) {
				Log.d(TAG, "~~mCartoonEarse~~");
				mShowImage.clear();
				setCartoonViewSelected((Integer) mCartoonEarse.getTag());
			} else if (paramView == mCartoonArea) {
				Log.d(TAG, "~~mCartoonArea~~");
				mShowImage.area();
				// 圈选
				setCartoonViewSelected((Integer) mCartoonArea.getTag());
			} else if (paramView == mCartoonMove) {
				mShowImage.moveDraw();
				setCartoonViewSelected((Integer) mCartoonMove.getTag());
			} else if (paramView == mCancelButton) {
				// 取消
				mShowImage.UnDo();
				Log.i(TAG, "~~UnDo~~");
			} else if (paramView == mRestoreButton) {
				// 恢复
				mShowImage.ReDo();
				Log.i(TAG, "~~ReDo~~");
			}
		}
	};

	/**
	 * 功能键设置选中效果方法
	 */
	private void setCartoonViewSelected(int index) {
		for (int i = 0; i < mCartoonViews.size(); i++) {
			if (i == index) {
				mCartoonViews.get(i).setSelected(true);
			} else {
				mCartoonViews.get(i).setSelected(false);
			}
		}
	}

	OnEventChanageListener mTouchEventChangeListener = new OnEventChanageListener() {
		@Override
		public void onChange(final float x, final float y, MotionEvent event,
				final float totalRatio, final float dx, final float dy,
				final boolean isAreaMode) {
			// TODO Auto-generated method stub
			switch (event.getActionMasked()) {
				case MotionEvent.ACTION_DOWN:
					mMagnView.setVisibility(View.VISIBLE);
					mMagnView.setIndicatorImgId(0, false);
					mMagnView.setMagnifyMode(isAreaMode);
					break;
				case MotionEvent.ACTION_MOVE:
					Bitmap ScaleBm = mShowImage.getMoveScaleBmp(x, y);
					mMagnView.setHairBitmap(ScaleBm);
					mMagnView.onMagTouchEvent(x, y, totalRatio, dx, dy);
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_DOWN:
				default:
					// //LogUtils.i("--View.GONE x=" + x + ", y = " + y);
					mMagnView.setVisibility(View.GONE);
					break;
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		Log.d(TAG, "onDestroy");
		// 释放资源
		if (mBm != null && mBm.isRecycled() == false) {
			mBm.recycle();
			mBm = null;
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
	public void registerVVBroadCastReceivers() {
		// TODO Auto-generated method stub
	}
}
