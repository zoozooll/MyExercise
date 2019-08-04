package com.beem.project.btf.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.BBSCustomerDialog;
import com.beem.project.btf.bbs.view.PreviewPoseView;
import com.beem.project.btf.bbs.view.TimeCameraPagerAdapter;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.ui.activity.CartoonStep1Activity;
import com.beem.project.btf.ui.activity.InnerGuideHelper;
import com.beem.project.btf.ui.activity.TimeCameraSaveActivity;
import com.beem.project.btf.ui.activity.base.VVBaseActivity;
import com.beem.project.btf.ui.camera.CameraController;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.TimeCameraImageInfo;
import com.beem.project.btf.ui.views.SimpleDilaogView;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.DimenUtils;
import com.beem.project.btf.utils.PictureUtil;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.beem.project.btf.utils.ThreadUtils;
import com.beem.project.btf.utils.TimeCameraMaterialUtil;
import com.beem.project.btf.utils.UIHelper;
import com.butterfly.piqs.vvcartoon.CartoonLib;
import com.butterfly.vv.camera.GalleryActivity;
import com.butterfly.vv.vv.utils.CToast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

public class CartoonCameraActivity extends VVBaseActivity {
	private static String TAG = "CartoonCameraActivity";
	private Camera camera = null; // 相机对象
	private SurfaceView mSv;
	private SurfaceHolder mHolder;
	private Button mTakePhoto;
	private Button mChangeCamera, PreviewPose_btn;
	private ImageView face_icon;
	private ViewPager face_vp;
	private RelativeLayout time_take_photo_layout;
	private ImageButton timecamera_back;
	private Button timecamera_takephoto;
	private TextView timecamera_album, timecamera_note;
	private int mCameraPosition = Camera.CameraInfo.CAMERA_FACING_FRONT;// 默认后置相机
	private int mBmWidth = 0;
	private int mBmHeight = 0;
	private int mLayoutWidth = 0;
	private int mLayoutHeight = 0;
	private float mHeightScale = 0;
	private float mWidthScale = 0;
	private ViewGroup take_photo_layout;
	private Context mContext;
	private int cameraType = CameraType.CARTOON.ordinal();
	private int cameraCount = 0;
	private static final int HIDECHANGEBUTTON = 1;
	private String mBmPath;// 图片路径
	private TimeCameraPagerAdapter pagerAdapter;
	private boolean isexception = false;
	private TimeCameraImageInfo currentImageInfo = new TimeCameraImageInfo();
	private List<TimeCameraImageInfo> imageInfos = new ArrayList<TimeCameraImageInfo>();
	private ArrayList<ImageView> bigPicIvList = new ArrayList<ImageView>();
	private static final int RESULT_LOAD_IMAGE = 4;
	private static final int RESULT_LOAD_GALLERY = 5;
	private boolean isViewpager_action = false;
	private boolean isTakePhoto = false;
	private DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.transparent)
			.showImageOnFail(R.drawable.transparent).cacheInMemory(true)
			.cacheOnDisk(true).build();

	public enum CameraType {
		// 卡通相机 时光
		CARTOON, TIME;
	}

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == HIDECHANGEBUTTON) {
				mChangeCamera.setVisibility(View.GONE);
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.cartoon_camera_main);
		mContext = this;
		EventBus.getDefault().register(this);
		new AsyncTask<Void, Integer, Integer>() {
			@Override
			protected Integer doInBackground(Void... params) {
				// TODO Auto-generated method stub
				//初始化漫画库
				return CartoonLib.InitLib(
						BBSUtils.getTestCartoonDir(
								BeemApplication.getContext(), "cartoon", true)
								.getAbsolutePath(), CartoonCameraActivity.this
								.getFilesDir().getAbsolutePath());
			}
		}.execute();
		Intent intent = getIntent();
		if (intent.hasExtra("cameraType")) {
			// 默认不传这个参数
			this.cameraType = intent.getIntExtra("cameraType",
					CameraType.CARTOON.ordinal());
		}
		mSv = (SurfaceView) findViewById(R.id.surfaceView1); // 获取SurfaceView组件，用于显示相机预览
		mHolder = mSv.getHolder();
		// 设置该surface不需要自己维护缓冲区
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// 获取拍照按钮
		take_photo_layout = (ViewGroup) findViewById(R.id.cartoon_take_photo_layout);
		time_take_photo_layout = (RelativeLayout) findViewById(R.id.time_take_photo_layout);
		take_photo_layout.setVisibility(View.GONE);
		time_take_photo_layout.setVisibility(View.GONE);
		face_icon = (ImageView) findViewById(R.id.face_icon);
		face_vp = (ViewPager) findViewById(R.id.face_vp);
		timecamera_note = (TextView) findViewById(R.id.timecamera_note);
		timecamera_note.setVisibility(View.GONE);
		// 切换手绘相机和时光相机布局
		PreviewPose_btn = (Button) findViewById(R.id.PreviewPose_btn);
		switchLayout(cameraType);
		mChangeCamera = (Button) findViewById(R.id.change_camera);
		mChangeCamera.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 切换前后摄像头
				if (mCameraPosition == Camera.CameraInfo.CAMERA_FACING_BACK) {
					stopPreview();
					mCameraPosition = Camera.CameraInfo.CAMERA_FACING_FRONT;
					// readyToPreview();
					OpenCamera(mCameraPosition);
				} else {
					stopPreview();
					mCameraPosition = Camera.CameraInfo.CAMERA_FACING_BACK;
					// readyToPreview();
					OpenCamera(mCameraPosition);
				}
			}
		});
		if (cameraType == CameraType.TIME.ordinal()) {
			InnerGuideHelper.showTimeflycameraGuide(this);
		}
	}
	public static void launch(Context ctx, int type) {
		Intent intent = new Intent(ctx, CartoonCameraActivity.class);
		intent.putExtra("cameraType", type);
		ctx.startActivity(intent);
	}
	/**
	 * 布局切换
	 */
	private void switchLayout(int cameraType) {
		if (cameraType == CameraType.CARTOON.ordinal()) {
			// 如果是手绘相机
			take_photo_layout.setVisibility(View.VISIBLE);
			PreviewPose_btn.setVisibility(View.GONE);
			mTakePhoto = (Button) findViewById(R.id.take_photo);
			timecamera_note.setVisibility(View.VISIBLE);
			timecamera_note.setText("亲~请正脸拍照效果会更好~");
			mTakePhoto.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (camera != null && !isTakePhoto) {
						isTakePhoto = true;
						try {
							camera.takePicture(null, null, takePhotoCB);
						} catch (Exception e) {
							// TODO: handle exception
							stopPreview();
						}
					}
				}
			});
			findViewById(R.id.cartooncamera_album).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							// 跳转到相册库
							/*Intent i = new Intent(Intent.ACTION_PICK,
									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							startActivityForResult(i, RESULT_LOAD_IMAGE);*/
						}
					});
		} else {
			// 如果是时光相机
			time_take_photo_layout.setVisibility(View.VISIBLE);
			PreviewPose_btn.setVisibility(View.VISIBLE);
			PreviewPose_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					final BBSCustomerDialog blurDlg = BBSCustomerDialog
							.newInstance(mContext, R.style.blurdialog);
					String[] urls = new String[3];
					if (Integer.parseInt(currentImageInfo.getLaycount()) < 3) {
						urls[0] = currentImageInfo.getLaypath1();
						urls[1] = currentImageInfo.getLaypath2();
					} else {
						urls[0] = currentImageInfo.getLaypath1();
						urls[1] = currentImageInfo.getLaypath2();
						urls[2] = currentImageInfo.getLaypath3();
					}
					PreviewPoseView pview = new PreviewPoseView(mContext, urls);
					// 设置标题
					pview.setCloseListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							blurDlg.dismiss();
						}
					});
					blurDlg.setContentView(pview.getView());
					blurDlg.show();
				}
			});
			face_icon.setVisibility(View.GONE);
			timecamera_note.setVisibility(View.GONE);
			face_vp.setVisibility(View.VISIBLE);
			timecamera_back = (ImageButton) findViewById(R.id.timecamera_back);
			timecamera_back.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finishActivity();
				}
			});
			timecamera_takephoto = (Button) findViewById(R.id.timecamera_takephoto);
			timecamera_album = (TextView) findViewById(R.id.timecamera_album);
			timecamera_album.setVisibility(View.VISIBLE);
			timecamera_album.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// 跳转到相册库
					Intent intent = new Intent(CartoonCameraActivity.this,
							GalleryActivity.class);
					intent.putExtra(GalleryActivity.GALLERY_CHOOSE_MAX, 1);
					intent.putExtra(GalleryActivity.GALLERY_FROM_CAMERA, true);
					intent.putExtra(GalleryActivity.GALLERY_CROP, false);
					intent.putExtra(GalleryActivity.GALLERY_PICKABLE, true);
					startActivityForResult(intent, Constants.PICKPHOTO);
				}
			});
			timecamera_takephoto.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (camera != null && !isTakePhoto) {
						isTakePhoto = true;
						try {
							camera.takePicture(null, null, takePhotoCB);
						} catch (Exception e) {
							stopPreview();
						}
					}
				}
			});
			// 加载人物动作图片
			new AsyncTask<Void, Integer, List<TimeCameraImageInfo>>() {
				@Override
				protected List<TimeCameraImageInfo> doInBackground(
						Void... params) {
					// TODO Auto-generated method stub
					// 初始化素材数据库表
					TimeCameraMaterialUtil.initMaterialDB();
					// 取出数据库中所有的元素
					imageInfos = TimeCameraImageInfo.queryDownload();
					return imageInfos;
				}
				@Override
				protected void onPostExecute(List<TimeCameraImageInfo> result) {
					if (result != null && result.size() > 0) {
						int index = 0;
						// 读取用户上次动作习惯
						String cid = SharedPrefsUtil.getValue(mContext,
								SettingKey.TimeCameraMaterial_id, imageInfos
										.get(0).getId());
						// 封装imageview列表
						for (int i = 0; i < result.size(); i++) {
							ImageView picImageView = new ImageView(mContext);
							bigPicIvList.add(picImageView);
							if (result.get(i).getId().equals(cid)) {
								// 根据获取的素材id获取当前的素材实体
								index = i;
								currentImageInfo = imageInfos.get(index);
								//Log.i(TAG, "currentImageInfo~~" + currentImageInfo.toString());
							}
						}
						// 预加载当前页的图片
						ImageLoader.getInstance().displayImage(
								currentImageInfo.getPose(),
								bigPicIvList.get(index), defaultOptions);
						pagerAdapter = new TimeCameraPagerAdapter(bigPicIvList,
								mContext);
						face_vp.setAdapter(pagerAdapter);
						// 滑到当前页
						face_vp.setCurrentItem(index, true);
					}
				};
			}.execute();
			face_vp.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}
				@Override
				public void onPageScrollStateChanged(int arg0) {
					switch (arg0) {
						case ViewPager.SCROLL_STATE_DRAGGING: {
							isViewpager_action = false;
							break;
						}
						case ViewPager.SCROLL_STATE_SETTLING: {
							isViewpager_action = true;
							break;
						}
						case ViewPager.SCROLL_STATE_IDLE: {
							if (face_vp.getCurrentItem() == pagerAdapter
									.getCount() - 1 && !isViewpager_action) {
								Toast.makeText(mContext, "最后一张",
										Toast.LENGTH_SHORT).show();
							}
							isViewpager_action = true;
							break;
						}
					}
				}
				@Override
				public void onPageSelected(int arg0) {
					// 切换时记录当前素材实体
					currentImageInfo = imageInfos.get(arg0);
					//Log.i(TAG, "currentImageInfo~~id" + currentImageInfo.getId());
					// 加载当前素材人物动作图片
					ImageLoader.getInstance().displayImage(
							currentImageInfo.getPose(), bigPicIvList.get(arg0));
					// 记录当前所使用的图片id和groupid，写入到偏好设置里
					SharedPrefsUtil.putValue(mContext,
							SettingKey.TimeCameraMaterial_id,
							currentImageInfo.getId());
					SharedPrefsUtil.putValue(mContext,
							SettingKey.TimeCameraMaterial_groupid,
							currentImageInfo.getGroupid());
				}
			});
		}
	}

	/**
	 * 拍照完成后的回调方法
	 */
	final PictureCallback takePhotoCB = new PictureCallback() {
		@Override
		public void onPictureTaken(final byte[] data, Camera camera) {
			new AsyncTask<Void, Integer, String>() {
				@Override
				protected void onPreExecute() {
					UIHelper.showDialogForLoading(mContext, "图片正在保存....", true);
				};
				@Override
				protected String doInBackground(Void... params) {
					// TODO Auto-generated method stub
					Bitmap bm = null;
					try {
						bm = BitmapFactory
								.decodeByteArray(data, 0, data.length);
						/** 保存原始图片 */
						// mBmPath = PictureUtil.saveToSDCard(mContext, bm);
						/** 旋转处理 */
						bm = PictureUtil.rotaingImageView(90.0f, bm);
						/** 翻转处理 */
						if (mCameraPosition == Camera.CameraInfo.CAMERA_FACING_FRONT) {
							bm = PictureUtil.convertYBmp(bm);
						}
						/** 缩放处理 */
						bm = ScaleBitmap(bm);
						/** 剪裁 */
						bm = ImageCrop(bm);
						/** 再次保存处理过的图片 */
						int[] p = getViewWH();
						mBmPath = PictureUtil.saveToTemp(mContext, bm);
					} catch (OutOfMemoryError e) {
						e.printStackTrace();
						Log.d(TAG, "takePhotoCB, OutOfMemoryError");
					} catch (Exception e) {
						e.printStackTrace();
						Log.d(TAG, "takePhotoCB, createBitmap exception");
					}
					return mBmPath;
				}
				@Override
				protected void onPostExecute(String result) {
					UIHelper.hideDialogForLoading();
					if (result != null && !result.isEmpty()) {
						Log.i(TAG, "~~图片路径~~" + result);
						if (cameraType == CameraType.CARTOON.ordinal()) {
							// 如果是手绘相机
							CartoonStep1Activity.launch(mContext, mBmPath);
						} else {
							// 如果是时光相机进来的
							Intent intent = new Intent(
									CartoonCameraActivity.this,
									TimeCameraSaveActivity.class);
							intent.putExtra("BitmapPath", result);
							startActivity(intent);
						}
					} else {
						Toast.makeText(mContext, "保存失败，请重新拍照",
								Toast.LENGTH_SHORT).show();
						stopPreview();
						readyToPreview();
					}
				};
			}.execute();
		}
	};

	/**
	 * 根据相机摄像头个数情况，选择打开某个相机
	 */
	private void setCameraPreview() {
		cameraCount = Camera.getNumberOfCameras();// 得到摄像头的个数
		Log.d(TAG, "1~~摄像头个数cameraCount~~" + cameraCount);
		if (cameraCount == 0) {
			// 没有检测到相机
			Toast.makeText(mContext, "您的手机不支持相机拍照", Toast.LENGTH_SHORT).show();
			finish();
		} else if (cameraCount == 1) {
			// 只有后置相机则打开后置相机
			mCameraPosition = Camera.CameraInfo.CAMERA_FACING_BACK;
			OpenCamera(mCameraPosition);
			myHandler.sendEmptyMessage(HIDECHANGEBUTTON);
		} else if (cameraCount == 2) {
			// 含有前后相机
			if (cameraType == CameraType.CARTOON.ordinal()) {
				mCameraPosition = Camera.CameraInfo.CAMERA_FACING_FRONT;
			} else {
				// 如果是时光相机则默认打开后置相机
				mCameraPosition = Camera.CameraInfo.CAMERA_FACING_BACK;
			}
			OpenCamera(mCameraPosition);
		}
	}
	/**
	 * 打开相机，并设置参数开始预览
	 */
	private void OpenCamera(int cameraId) {
		try {
			if (camera == null) {
				camera = Camera.open(cameraId);// 打开当前选中的摄像头
			}
			camera.setPreviewDisplay(mHolder); // 设置用于显示预览的SurfaceView
			// 设置相机参数
			setCameraParameters();
			/*
			 * int degree = getCameraDisplayOrientation(CartoonCameraActivity.this, cameraId);
			 */
			// 设置预览方向
			camera.setDisplayOrientation(90);
			camera.startPreview(); // 开始预览
			// camera.autoFocus(mAFCallBack); // 设置自动对焦
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 用于设置相机参数,优选算法需要修改
	 */
	private void setCameraParameters() {
		if (camera != null) {
			CameraController controller = new CameraController(camera);
			controller.setCameraParameters(this);
		}
	}
	/**
	 * 把全屏的图片剪裁掉下方遮掉的部分
	 */
	private Bitmap ImageCrop(Bitmap bitmap) {
		int w = bitmap.getWidth(); // 得到图片的宽，高
		int h = bitmap.getHeight();
		int Croph = 0;
		if (h > getViewWH()[1]) {
			Croph = getViewWH()[1];
		} else {
			Croph = h;
		}
		Log.d(TAG,
				"要裁掉的高度"
						+ DimenUtils.px2dip(CartoonCameraActivity.this,
								take_photo_layout.getHeight()));
		Log.d(TAG, "裁剪前宽w:" + w + "高h:" + h + "裁剪后的高Croph:" + Croph);
		return Bitmap.createBitmap(bitmap, 0, 0, w, Croph, null, false);
	}
	/**
	 * 获取预览显示区域
	 */
	private int[] getViewWH() {
		Point p = DimenUtils.getScreenMetrics(this);
		int croph = 0;
		if (cameraType == CameraType.CARTOON.ordinal()) {
			croph = p.y - take_photo_layout.getHeight();
		} else {
			croph = p.y - time_take_photo_layout.getHeight();
		}
		int[] viewWH = { p.x, croph };
		return viewWH;
	}
	/**
	 * 获取图像预览方向
	 */
	public static int getCameraDisplayOrientation(Activity activity,
			int cameraId) {
		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		Log.d(TAG, "~~rotation~~" + rotation);
		int degrees = 0;
		switch (rotation) {
			case Surface.ROTATION_0:
				degrees = 0;
				break;
			case Surface.ROTATION_90:
				degrees = 90;
				break;
			case Surface.ROTATION_180:
				degrees = 180;
				break;
			case Surface.ROTATION_270:
				degrees = 270;
				break;
		}
		Log.d(TAG, "info.facint = " + info.facing + ", info.orientation = "
				+ info.orientation + ",degrees=" + degrees);
		int result = 0;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			// 前置摄像头角度处理
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			// 后置摄像头角度处理
			result = (info.orientation - degrees + 360) % 360;
		}
		Log.d(TAG, "getCameraDisplayOrientation, result = " + result);
		return result;
	}

	/**
	 * 自动对焦回调方法
	 */
	AutoFocusCallback mAFCallBack = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			// TODO Auto-generated method stub
			if (success) {
				Toast.makeText(CartoonCameraActivity.this, "自动对焦中...",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	public float[] getMatrixScale(Bitmap bm1) {
		float[] scales = new float[2];
		mLayoutWidth = mSv.getMeasuredWidth();
		mLayoutHeight = mSv.getMeasuredHeight();
		if (bm1 != null) {
			mBmWidth = bm1.getWidth();
			mBmHeight = bm1.getHeight();
			mWidthScale = (float) mLayoutWidth / (float) mBmWidth;
			scales[0] = mWidthScale;
			mHeightScale = (float) mLayoutHeight / (float) mBmHeight;
			scales[1] = mHeightScale;
		}
		Log.d(TAG, "getMatrixScale, mLayoutWidth = " + mLayoutWidth
				+ ", mLayoutHeight = " + mLayoutHeight);
		Log.d(TAG, "getMatrixScale, mBmWidth = " + mBmWidth + ", mBmHeight = "
				+ mBmHeight);
		Log.d(TAG, "getMatrixScale, mWidthScale = " + mWidthScale
				+ ", mHeightScale = " + mHeightScale);
		return scales;
	}
	// /////////////////////////////#########图像处理方法start#########/////////////////////////////////
	/**
	 * 图片缩放处理
	 */
	private Bitmap ScaleBitmap(Bitmap bmp) {
		float[] scales = getMatrixScale(bmp);
		return PictureUtil.ScaleBitmap(scales, bmp);
	}
	// /////////////////////////////#######图像处理方法end#########/////////////////////////////////
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "onResume()");
		isTakePhoto = false;
		stopPreview();
		readyToPreview();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		stopPreview();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finishActivity();
	}
	/** 开始预览 */
	private void readyToPreview() {
		ThreadUtils.executeTask(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(300);
					if (camera == null) {
						setCameraPreview();
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					stopPreview();
				}
			}
		});
	}
	private void stopPreview() {
		try {
			if (camera != null) {
				camera.stopPreview(); // 停止预览
				camera.release(); // 释放资源
				camera = null;
				isexception = false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			isexception = true;
			ExceptionDialog();
		}
	}
	private void ExceptionDialog() {
		final BBSCustomerDialog blurDlg = BBSCustomerDialog.newInstance(
				mContext, R.style.blurdialog);
		SimpleDilaogView simpleDilaogView = new SimpleDilaogView(mContext);
		simpleDilaogView.setTitle("没有检测到相机，或是权限被禁用，请检查相机是否可用并开启权限！");
		simpleDilaogView.setPositiveButton("知道了", new OnClickListener() {
			@Override
			public void onClick(View v) {
				blurDlg.dismiss();
				finish();
			}
		});
		blurDlg.setContentView(simpleDilaogView.getmView());
		blurDlg.show();
	}
	private void finishActivity() {
		stopPreview();
		if (!isexception) {
			finish();
		}
	}
	public void onEventMainThread(final EventBusData data) {
		if (data.getAction() == EventAction.FinishActivity) {
			finishActivity();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		String picturePath = null;
		/*Log.i(TAG, "~~picturePath1~~" + picturePath + "~~data~~" + data + "~requestCode~" + requestCode
				+ "~resultCode~" + resultCode);*/
		//		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
		//			//Log.i(TAG, "~~picturePath2~~" + picturePath);
		//			Uri selectedImage = data.getData();
		//			String[] filePathColumn = { MediaColumns.DATA };
		//			Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		//			cursor.moveToFirst();
		//			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		//			picturePath = cursor.getString(columnIndex);
		//			cursor.close();
		//			/*if (cameraType == CameraType.TIME.ordinal()) {
		//				Intent intent = new Intent(CartoonCameraActivity.this, TimeCameraSaveActivity.class);
		//				intent.putExtra("BitmapPath", picturePath);
		//				startActivity(intent);
		//			}else{
		//				Intent intent = new Intent(CartoonCameraActivity.this, CartoonSaveActivity.class);
		//				intent.putExtra("BitmapPath", picturePath);
		//				startActivity(intent);
		//			}*/
		//		} else if (requestCode == RESULT_LOAD_GALLERY && resultCode == RESULT_OK && data != null) {
		//			ArrayList<String> result = data.getStringArrayListExtra(CameraActivity.SELECTED_RESULT_KEY);
		//			//Log.i(TAG, "~~picturePath3~~" + picturePath);
		//			if (result != null && !result.isEmpty()) {
		//				picturePath = result.get(0);
		//				//Log.i(TAG, "~~picturePath4~~" + picturePath);
		//			} else {
		//				Toast.makeText(mContext, "没有返回数据", Toast.LENGTH_SHORT).show();
		//			}
		//			if (picturePath != null && cameraType == CameraType.TIME.ordinal()) {
		//				Intent intent = new Intent(CartoonCameraActivity.this, TimeCameraSaveActivity.class);
		//				intent.putExtra("BitmapPath", picturePath);
		//				startActivity(intent);
		//			} else if (picturePath != null && cameraType == CameraType.CARTOON.ordinal()) {
		//				Intent intent = new Intent(CartoonCameraActivity.this, CartoonSaveActivity.class);
		//				intent.putExtra("BitmapPath", picturePath);
		//				startActivity(intent);
		//			}
		//		} else 
		if (requestCode == Constants.PICKPHOTO) {
			if (resultCode == RESULT_OK) {
				Uri resultData = data.getData();
				if (resultData != null) {
					picturePath = resultData.getPath();
				} else {
					picturePath = data
							.getStringExtra(GalleryActivity.GALLERY_RESULT_DATA);
				}
				if (picturePath != null
						&& cameraType == CameraType.TIME.ordinal()) {
					Intent intent = new Intent(CartoonCameraActivity.this,
							TimeCameraSaveActivity.class);
					intent.putExtra("BitmapPath", picturePath);
					startActivity(intent);
				} else if (picturePath != null
						&& cameraType == CameraType.CARTOON.ordinal()) {
					Intent intent = new Intent(CartoonCameraActivity.this,
							CartoonStep1Activity.class);
					intent.putExtra("BitmapPath", picturePath);
					startActivity(intent);
				}
			} else {
				CToast.showToast(this, R.string.pick_photo_failed,
						Toast.LENGTH_SHORT);
			}
		}
	}
	@Override
	public void registerVVBroadCastReceivers() {
	}
}
