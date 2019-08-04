package com.beem.project.btf.ui.activity;

import java.util.ArrayList;
import java.util.Arrays;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.EditImageView;
import com.beem.project.btf.ui.EditImageView.OnEventChanageListener;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.guide.IntroduceActivity;
import com.beem.project.btf.ui.MagnifyView;
import com.beem.project.btf.utils.PictureUtil;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.beem.project.btf.utils.UIHelper;
import com.butterfly.piqs.vvcartoon.CartoonLib;

import de.greenrobot.event.EventBus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

/** 头发选区步骤 */
public class CartoonStep2Activity extends Activity {
	private int mImageShowWidth = 0;
	private int mImageShowHeight = 0;
	private Context mContext;
	private String mBmPath;
	private EditImageView mShowImage;
	private ImageButton mCartoonOk;
	private ImageButton mCartoonDelete;
	private ImageButton mCancelButton;
	private ImageButton mRestoreButton;
	private Button mCartoonAdd, mCartoonEarse, mCartoonMove;
	private Bitmap mBm;// 用于接受传过来的图片
	private MagnifyView mMagnView;// 放大镜
	private ArrayList<View> mCartoonViews = new ArrayList<View>();
	private static final String TAG = "CartoonStep2Activity";
	private int mBmWidth = 0;
	private int mBmHeight = 0;
	private Bitmap mHairBm;
	private int sexflag;
	private int[] facepoints;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.cartoonstep2_layout);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mImageShowWidth = metric.widthPixels; // 屏幕宽度（像素）
		mImageShowHeight = metric.heightPixels
				- getResources().getDimensionPixelSize(
						R.dimen.time_save_btm_bar_height); // 屏幕高度（像素）
		mContext = this;
		mShowImage = (EditImageView) findViewById(R.id.cartoon_image_show);
		mCartoonOk = (ImageButton) findViewById(R.id.cartoon_ok);
		mCartoonDelete = (ImageButton) findViewById(R.id.cartoon_delete);
		mCancelButton = (ImageButton) findViewById(R.id.undobtn);
		mRestoreButton = (ImageButton) findViewById(R.id.redobtn);
		mCartoonAdd = (Button) findViewById(R.id.cartoon_add);
		mCartoonAdd.setTag(0);
		mCartoonEarse = (Button) findViewById(R.id.cartoon_earse);
		mCartoonEarse.setTag(1);
		mCartoonMove = (Button) findViewById(R.id.cartoon_move);
		mCartoonMove.setTag(2);
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
		mCartoonMove.setOnClickListener(mBtnClickListener);
		EventBus.getDefault().register(this);
		findViewById(R.id.introduce_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(CartoonStep2Activity.this,
								IntroduceActivity.class);
						intent.putExtra("which", 1);
						startActivity(intent);
					}
				});
		Intent intent = getIntent();
		sexflag = intent.getIntExtra("sexstate", CartoonLib.VV_SEX_MALE);
		facepoints = intent.getIntArrayExtra("hairstate");
		new AsyncTask<Void, Integer, Void>() {
			@Override
			protected void onPreExecute() {
				UIHelper.showDialogForLoading(mContext, "绘制头发选区...", false);
			};
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				mBm = PictureUtil.decodeUriAsBitmap(PictureUtil
						.getCartoonTempImage());
				mBm = PictureUtil.scaleBitmap(mBm, mImageShowWidth,
						mImageShowHeight);
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				if (mBm != null) {
					mMagnView.setSourceBitmap(mBm);
					mBmWidth = mBm.getWidth();
					mBmHeight = mBm.getHeight();
					mShowImage.setImageBitmap(mBm);
					mShowImage.setCartoonMode(true);
					mCartoonAdd.setSelected(true);
					mShowImage.addDraw();
					mShowImage
							.setOnEventChanageListener(mTouchEventChangeListener);
					mHairBm = Bitmap.createBitmap(mBmWidth, mBmHeight,
							Config.ARGB_8888);
					Mat hairmask = new Mat();
					Utils.bitmapToMat(mHairBm, hairmask, true);
					// 调用jni库设置性别
					int sexstate = CartoonLib.setSex(sexflag);
					// 初始化一块头发蒙版出来
					Log.i(TAG, "facepoints~" + Arrays.toString(facepoints));
					int hairstate = CartoonLib.setPointsMat(facepoints,
							hairmask.getNativeObjAddr());
					Utils.matToBitmap(hairmask, mHairBm, true);
					mShowImage.setHairBitmap(mHairBm);
				}
				// 以上工作处理完才关掉对话框
				UIHelper.hideDialogForLoading();
			};
		}.execute();
		/*InnerGuideHelper.showCartoonCameraGuide(this);*/
		if (!SharedPrefsUtil.getValue(mContext, this.getClass().getName(), false)) {
			Intent introdece = new Intent(this, IntroduceActivity.class);
			introdece.putExtra("which", 1);
			startActivity(introdece);
			SharedPrefsUtil.putValue(mContext, this.getClass().getName(), true);
		}
	}
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
					//LogUtils.i("--View.GONE x=" + x + ", y = " + y);
					mMagnView.setVisibility(View.GONE);
					break;
			}
		}
	};
	OnClickListener mBtnClickListener = new OnClickListener() {
		@Override
		public void onClick(View paramView) {
			// TODO Auto-generated method stub
			if (paramView == mCartoonOk) {
				// 调用jni库，结束头发涂抹
				Intent intent = new Intent(CartoonStep2Activity.this,
						CartoonStep3Activity.class);
				startActivity(intent);
			} else if (paramView == mCartoonDelete) {
				CartoonStep2Activity.this.finish();
			} else if (paramView == mCartoonAdd) {
				Log.d(TAG, "~~mCartoonAdd~~");
				mShowImage.addDraw();
				setCartoonViewSelected((Integer) mCartoonAdd.getTag());
			} else if (paramView == mCartoonEarse) {
				Log.d(TAG, "~~mCartoonEarse~~");
				mShowImage.clear();
				setCartoonViewSelected((Integer) mCartoonEarse.getTag());
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
