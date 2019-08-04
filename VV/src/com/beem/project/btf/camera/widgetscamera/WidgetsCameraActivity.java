package com.beem.project.btf.camera.widgetscamera;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.BBSCustomerDialog;
import com.beem.project.btf.ui.activity.base.VVBaseActivity;
import com.beem.project.btf.ui.camera.CameraController;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.views.SimpleDilaogView;
import com.beem.project.btf.utils.DimenUtils;
import com.beem.project.btf.utils.PictureUtil;
import com.beem.project.btf.utils.ThreadUtils;
import com.butterfly.vv.camera.CameraActivity;

import de.greenrobot.event.EventBus;

/**
 * Widgets相机activity,初步命名。
 * @author Aaron Lee Created at 上午11:34:41 2015-10-29
 */
public class WidgetsCameraActivity extends VVBaseActivity implements OnClickListener, SurfaceHolder.Callback {
	private static String TAG = "WidgetsCameraActivity";
	private Camera camera = null; // 相机对象
	private SurfaceView preview_camera;
	private SurfaceHolder mHolder;
	private View layout_take_photo;
	private Button mChangeCamera;
	private Button btn_camera_takephoto;
	private ImageButton timecamera_back;
	private TextView timecamera_album;
	private int mCameraPosition = Camera.CameraInfo.CAMERA_FACING_FRONT;// 默认后置相机
	private int mBmWidth = 0;
	private int mBmHeight = 0;
	private int mLayoutWidth = 0;
	private int mLayoutHeight = 0;
	private float mHeightScale = 0;
	private float mWidthScale = 0;
	private int cameraCount = 0;
	private Bitmap previewBmp;
	private boolean isPreviewBmp;
	private PreviewMode mode;
	private static final int HIDECHANGEBUTTON = 1;
	private boolean isexception = false;
	private static final int RESULT_LOAD_IMAGE = 4;
	private static final int RESULT_LOAD_GALLERY = 5;
	private int previewPictureWidth;
	private int previewPictureHeight;
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_widgetscamera);
		EventBus.getDefault().register(this);
		initViews();
	}
	private void initViews() {
		preview_camera = (SurfaceView) findViewById(R.id.preview_camera); // 获取SurfaceView组件，用于显示相机预览
		mHolder = preview_camera.getHolder();
		// 设置该surface不需要自己维护缓冲区
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mHolder.addCallback(this);
		mChangeCamera = (Button) findViewById(R.id.change_camera);
		layout_take_photo = findViewById(R.id.layout_take_photo);
		btn_camera_takephoto = (Button) findViewById(R.id.btn_camera_takephoto);
		timecamera_back = (ImageButton) findViewById(R.id.btn_back);
		timecamera_album = (TextView) findViewById(R.id.timecamera_album);
		// 设置按钮事件；
		mChangeCamera.setOnClickListener(this);
		btn_camera_takephoto.setOnClickListener(this);
		timecamera_back.setOnClickListener(this);
		timecamera_album.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if (v == mChangeCamera) {
			// 切换前后摄像头
			if (mCameraPosition == Camera.CameraInfo.CAMERA_FACING_BACK) {
				stopPreview();
				mCameraPosition = Camera.CameraInfo.CAMERA_FACING_FRONT;
				// readyToPreview();
				openCamera(mCameraPosition);
			} else {
				stopPreview();
				mCameraPosition = Camera.CameraInfo.CAMERA_FACING_BACK;
				// readyToPreview();
				openCamera(mCameraPosition);
			}
		} else if (v == btn_camera_takephoto) {
			try {
				Log.d(TAG, "takePicture");
				camera.takePicture(null, null, takePhotoCB);
			} catch (Exception e) {
				Log.e(TAG, "take photo error", e);
				stopPreview();
			}
		} else if (v == timecamera_back) {
			onBackPressed();
		} else if (timecamera_album == v) {
			// 跳转到相册库
			Intent intent = new Intent("android.intent.action.vv.camera.photo.main");
			intent.putExtra(CameraActivity.CAMERA_GALLERY_TYPE, CameraActivity.GALLERY_TYPE_TIME);
			startActivityForResult(intent, RESULT_LOAD_GALLERY);
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
				};
				@Override
				protected String doInBackground(Void... params) {
					// TODO Auto-generated method stub
					Bitmap bm = null;
					try {
						bm = BitmapFactory.decodeByteArray(data, 0, data.length);
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
						previewBmp = bm;
					} catch (Exception e) {
						e.printStackTrace();
						Log.d(TAG, "takePhotoCB, createBitmap exception");
						return "fail";
					}
					return "success";
				}
				@Override
				protected void onPostExecute(String result) {
				};
			}.execute();
		}
	};

	/**
	 * 根据相机摄像头个数情况，选择打开某个相机
	 */
	private void setCameraPreview() {
		cameraCount = Camera.getNumberOfCameras();// 得到摄像头的个数
		if (cameraCount == 0) {
			// 没有检测到相机
			Toast.makeText(mContext, "您的手机不支持相机拍照", Toast.LENGTH_SHORT).show();
			finish();
		} else if (cameraCount == 1) {
			// 只有后置相机则打开后置相机
			mCameraPosition = Camera.CameraInfo.CAMERA_FACING_BACK;
			myHandler.sendEmptyMessage(HIDECHANGEBUTTON);
		}
		openCamera(mCameraPosition);
	}
	/**
	 * 打开相机，并设置参数开始预览
	 */
	private void openCamera(int cameraId) {
		try {
			Log.d(TAG, "openCamera");
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
	 * 获取图像预览方向
	 */
	public static int getCameraDisplayOrientation(Activity activity, int cameraId) {
		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
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
		Log.d(TAG, "info.facint = " + info.facing + ", info.orientation = " + info.orientation + ",degrees=" + degrees);
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
	public float[] getMatrixScale(Bitmap bm1) {
		float[] scales = new float[2];
		mLayoutWidth = preview_camera.getMeasuredWidth();
		mLayoutHeight = preview_camera.getMeasuredHeight();
		if (bm1 != null) {
			mBmWidth = bm1.getWidth();
			mBmHeight = bm1.getHeight();
			mWidthScale = (float) mLayoutWidth / (float) mBmWidth;
			scales[0] = mWidthScale;
			mHeightScale = (float) mLayoutHeight / (float) mBmHeight;
			scales[1] = mHeightScale;
		}
		return scales;
	}
	/**
	 * 图片缩放处理
	 */
	private Bitmap ScaleBitmap(Bitmap bmp) {
		float[] scales = getMatrixScale(bmp);
		return PictureUtil.ScaleBitmap(scales, bmp);
	}
	private void recyclePreviewBitmap() {
		if (previewBmp != null && !previewBmp.isRecycled()) {
			previewBmp.recycle();
			previewBmp = null;
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		readyToPreview();
	}
	@Override
	protected void onPause() {
		super.onPause();
		stopPreview();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		if (previewBmp != null && !previewBmp.isRecycled()) {
			previewBmp.recycle();
			previewBmp = null;
		}
	}
	@Override
	public void onBackPressed() {
		switch (mode) {
			case PREVIEW_PICTURE:
				recyclePreviewBitmap();
				break;
			case PREVIEW_CAMERA:
				super.onBackPressed();
				break;
			default:
				break;
		}
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
		Log.d(TAG, "stopPreview");
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
		final BBSCustomerDialog blurDlg = BBSCustomerDialog.newInstance(mContext, R.style.blurdialog);
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
	public void onEventMainThread(final EventBusData data) {
		if (data.getAction() == EventAction.FinishActivity) {
			finish();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		String picturePath = null;
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaColumns.DATA };
			Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();
		} else if (requestCode == RESULT_LOAD_GALLERY && resultCode == RESULT_OK && data != null) {
		}
	}
	@Override
	public void registerVVBroadCastReceivers() {
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, acquire the camera and tell it where
		// to draw.
		openCamera(mCameraPosition);
		preview_camera.setWillNotDraw(false); // see http://stackoverflow.com/questions/2687015/extended-surfaceviews-ondraw-method-never-called
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return, so stop the preview.
		// Because the CameraDevice object is not a shared resource, it's very
		// important to release it when the activity is paused.
		stopPreview();
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
	}

	private enum PreviewMode {
		PREVIEW_CAMERA, PREVIEW_PICTURE
	}
}
