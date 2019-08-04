package com.beem.project.btf.ui.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.ui.activity.base.VVBaseActivity;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.EventBusData.IEventBusAction;
import com.beem.project.btf.ui.views.ClipView;
import com.beem.project.btf.ui.views.ClipView.OnDrawListenerComplete;
import com.beem.project.btf.update.UploadUtil;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.PictureUtil;
import com.beem.project.btf.utils.ThreadUtils;
import com.beem.project.btf.utils.UIHelper;
import com.butterfly.vv.vv.utils.CToast;

import de.greenrobot.event.EventBus;

public class ClipPictureActivity extends VVBaseActivity implements OnTouchListener, OnClickListener, IEventBusAction {
	private static final String KEY_PHOTO_PATH = "photo_path";
	private static final String KEY_CLIP_TYPE = "CLIP_TYPE";
	private static final String KEY_CLIP_WIDTH = "KEY_CLIP_WIDTH";
	private static final String KEY_CLIP_HEIGHT = "KEY_CLIP_HEIGHT";
	private static final String KEY_CLIP_ROTATE = "KEY_CLIP_ROTATE";
	private ImageView srcPic;
	private ImageView ok_image_camera, cell_image_camera;
	private ClipView clipview;
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	/** 动作标志：无 */
	private static final int NONE = 0;
	/** 动作标志：拖动 */
	private static final int DRAG = 1;
	/** 动作标志：缩放 */
	private static final int ZOOM = 2;
	/** 初始化动作标志 */
	private int mode = NONE;
	/** 记录起始坐标 */
	private PointF start = new PointF();
	/** 记录缩放时两指中间点坐标 */
	private PointF mid = new PointF();
	private float oldDist = 1f;
	private Bitmap bitmap;
	private RelativeLayout relative_layout;
	private String photo_path;
	private Handler mHandler = new Handler();
	//	private boolean isregister;
	private static final String TAG = ClipPictureActivity.class.getSimpleName();
	private String savePhoto;
	private UpLoadFileRunnable uploadTask;
	private int clipWidth;
	private int clipHeight;
	private float clipRatio;
	private ClipType clipType;
	private int rotate;

	public static void launch(Fragment fragment, String photo_path) {
		Intent lastIntent = new Intent(fragment.getActivity(), ClipPictureActivity.class);
		lastIntent.putExtra(KEY_PHOTO_PATH, photo_path);
		fragment.startActivity(lastIntent);
	}
	public static void launch(Activity activity, String photo_path, int requestCode) {
		launch(activity, photo_path, requestCode, 1.0f);
	}
	public static void launch(Activity activity, String photo_path, int requestCode, int width, int height) {
		Intent intent = new Intent(activity, ClipPictureActivity.class);
		intent.putExtra(KEY_PHOTO_PATH, photo_path);
		intent.putExtra(KEY_CLIP_WIDTH, width);
		intent.putExtra(KEY_CLIP_HEIGHT, height);
		activity.startActivityForResult(intent, requestCode);
	}
	public static void launch(Activity activity, String photo_path, int requestCode, int width, int height,
			ClipType clipType) {
		Intent intent = new Intent(activity, ClipPictureActivity.class);
		intent.putExtra(KEY_PHOTO_PATH, photo_path);
		intent.putExtra(KEY_CLIP_WIDTH, width);
		intent.putExtra(KEY_CLIP_HEIGHT, height);
		intent.putExtra(KEY_CLIP_TYPE, clipType);
		activity.startActivityForResult(intent, requestCode);
	}
	public static void launch(Activity activity, String photo_path, int requestCode, float clipRotate) {
		Intent intent = new Intent(activity, ClipPictureActivity.class);
		intent.putExtra(KEY_PHOTO_PATH, photo_path);
		intent.putExtra(KEY_CLIP_ROTATE, clipRotate);
		activity.startActivityForResult(intent, requestCode);
	}
	public static void launch(Activity activity, String photo_path, int requestCode, float clipRotate, ClipType clipType) {
		Intent intent = new Intent(activity, ClipPictureActivity.class);
		intent.putExtra(KEY_PHOTO_PATH, photo_path);
		intent.putExtra(KEY_CLIP_ROTATE, clipRotate);
		intent.putExtra(KEY_CLIP_TYPE, clipType);
		activity.startActivityForResult(intent, requestCode);
	}
	public static void launch(Fragment fragment, String photo_path, int requestCode, boolean isRegister) {
		Intent intent = new Intent(fragment.getActivity(), ClipPictureActivity.class);
		intent.putExtra(KEY_PHOTO_PATH, photo_path);
		intent.putExtra(KEY_CLIP_TYPE, ClipType.REGISTER);
		fragment.startActivityForResult(intent, requestCode);
	}
	public static void launch(Fragment fragment, String photo_path, int requestCode, ClipType clipType) {
		Intent intent = new Intent(fragment.getActivity(), ClipPictureActivity.class);
		intent.putExtra(KEY_PHOTO_PATH, photo_path);
		intent.putExtra(KEY_CLIP_TYPE, clipType);
		fragment.startActivityForResult(intent, requestCode);
	}
	public static void launch(Fragment fragment, String photo_path, int requestCode, int width, int height) {
		Intent intent = new Intent(fragment.getActivity(), ClipPictureActivity.class);
		intent.putExtra(KEY_PHOTO_PATH, photo_path);
		intent.putExtra(KEY_CLIP_WIDTH, width);
		intent.putExtra(KEY_CLIP_HEIGHT, height);
		fragment.startActivityForResult(intent, requestCode);
	}
	public static void launch(Fragment fragment, String photo_path, int requestCode, float clipRotate) {
		Intent intent = new Intent(fragment.getActivity(), ClipPictureActivity.class);
		intent.putExtra(KEY_PHOTO_PATH, photo_path);
		intent.putExtra(KEY_CLIP_ROTATE, clipRotate);
		fragment.startActivityForResult(intent, requestCode);
	}
	public static void launch(Activity activity, String photo_path, int requestCode, boolean isRegister) {
		Intent intent = new Intent(activity, ClipPictureActivity.class);
		intent.putExtra(KEY_PHOTO_PATH, photo_path);
		intent.putExtra(KEY_CLIP_TYPE, ClipType.REGISTER);
		activity.startActivityForResult(intent, requestCode);
	}
	public static void launch(Activity activity, String photo_path, int requestCode, ClipType clipType) {
		Intent intent = new Intent(activity, ClipPictureActivity.class);
		intent.putExtra(KEY_PHOTO_PATH, photo_path);
		intent.putExtra(KEY_CLIP_TYPE, clipType);
		activity.startActivityForResult(intent, requestCode);
	}
	public static void launch(Fragment fragment) {
		Intent intent = new Intent(fragment.getActivity(), ClipPictureActivity.class);
		fragment.startActivity(intent);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_photo_cat_head);
		srcPic = (ImageView) this.findViewById(R.id.src_pic);
		srcPic.setOnTouchListener(this);
		rotate = 0;
		//		srcPic.setScaleType(ScaleType.CENTER_INSIDE);
		relative_layout = (RelativeLayout) findViewById(R.id.relative_layout);
		photo_path = getIntent().getStringExtra(KEY_PHOTO_PATH);
		clipWidth = getIntent().getIntExtra(KEY_CLIP_WIDTH, -1);
		clipHeight = getIntent().getIntExtra(KEY_CLIP_HEIGHT, -1);
		clipRatio = getIntent().getFloatExtra(KEY_CLIP_ROTATE, 1.0f);
		clipType = (ClipType) getIntent().getSerializableExtra(KEY_CLIP_TYPE);
		ViewTreeObserver observer = srcPic.getViewTreeObserver();
		observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				srcPic.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				initClipView(relative_layout.getHeight());
			}
		});
		ok_image_camera = (ImageView) this.findViewById(R.id.ok_image_camera);
		ok_image_camera.setOnClickListener(this);
		cell_image_camera = (ImageView) this.findViewById(R.id.cell_image_camera);
		cell_image_camera.setOnClickListener(this);
		View imv_rotate = findViewById(R.id.imv_rotate);
		imv_rotate.setOnClickListener(this);
		//		Log.i(tag, "onCreate");
		EventBus.getDefault().register(this);
	}
	/**
	 * 初始化截图区域，并将源图按裁剪框比例缩放
	 * @param top
	 */
	private void initClipView(final int top) {
		ThreadUtils.executeTask(new Runnable() {
			@Override
			public void run() {
				bitmap = PictureUtil.getBitMapFromPath(mContext, photo_path);
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						clipview = new ClipView(ClipPictureActivity.this);
						clipview.setCustomTopBarHeight(top);
						clipview.setClipWidth(clipWidth);
						clipview.setClipHeight(clipHeight);
						clipview.setClipRatio(clipRatio);
						clipview.addOnDrawCompleteListener(new OnDrawListenerComplete() {
							@Override
							public void onDrawCompelete() {
								clipview.removeOnDrawCompleteListener();
								int clipHeight = clipview.getClipHeight();
								int clipWidth = clipview.getClipWidth();
								int midX = clipview.getClipLeftMargin() + (clipWidth / 2);
								int midY = clipview.getClipTopMargin() + (clipHeight / 2);
								if (bitmap != null) {
									int imageWidth = bitmap.getWidth();
									int imageHeight = bitmap.getHeight();
									Log.i(TAG, "~imageWidth~" + imageWidth + "~imageHeight~" + imageHeight);
									// 按裁剪框求缩放比例
									float scale = (clipWidth * 1.0f) / imageWidth;
									if (imageWidth > imageHeight) {
										scale = (clipHeight * 1.0f) / imageHeight;
									}
									// 起始中心点
									float imageMidX = imageWidth * scale / 2;
									float imageMidY = +imageHeight * scale / 2;
									srcPic.setScaleType(ScaleType.MATRIX);
									// 缩放
									matrix.postScale(scale, scale);
									// 平移
									matrix.postTranslate(midX - imageMidX, midY - imageMidY);
								}
								// matrix.postTranslate(0,40);
								srcPic.setImageMatrix(matrix);
								srcPic.setImageBitmap(bitmap);
							}
						});
						addContentView(clipview, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
					}
				});
			}
		});
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView view = (ImageView) v;
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				savedMatrix.set(matrix);
				// 设置开始点位置
				start.set(event.getX(), event.getY());
				mode = DRAG;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				oldDist = spacing(event);
				if (oldDist > 10f) {
					savedMatrix.set(matrix);
					midPoint(mid, event);
					mode = ZOOM;
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				break;
			case MotionEvent.ACTION_MOVE:
				if (mode == DRAG) {
					matrix.set(savedMatrix);
					matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
				} else if (mode == ZOOM) {
					float newDist = spacing(event);
					if (newDist > 10f) {
						matrix.set(savedMatrix);
						float scale = newDist / oldDist;
						matrix.postScale(scale, scale, mid.x, mid.y);
					}
				}
				break;
		}
		view.setImageMatrix(matrix);
		return true;
	}
	/**
	 * 多点触控时，计算最先放下的两指距离
	 * @param event
	 * @return
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}
	/**
	 * 多点触控时，计算最先放下的两指中心坐标
	 * @param point
	 * @param event
	 */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.cell_image_camera:// 取消
				ClipPictureActivity.this.finish();
				break;
			case R.id.ok_image_camera: {
				// 保存确认 
				if (clipview == null || !clipview.isDrawComplete()) {
					CToast.showToast(BeemApplication.getContext(), "图片正在加载，请稍后...", Toast.LENGTH_SHORT);
					return;
				}
				if (clipType == ClipType.OTHERS || clipType == null) {
					if (uploadTask == null) {
						String audioAddress = AppProperty.getInstance().VVAPI
								+ AppProperty.getInstance().UPLOAD_PORTRAIT;
						uploadTask = new UpLoadFileRunnable(savePhoto(), audioAddress);
						ThreadUtils.executeTask(uploadTask);
					}
				} else if (clipType == ClipType.REGISTER) {
					savePhoto = savePhoto();
					EventBus.getDefault().post(new EventBusData(EventAction.RegisterCacheImage, savePhoto));
					finish();
				} else if (clipType == ClipType.CLIP_NEWSTV) {
					Bitmap bmp = getBitmap();
					String file = PictureUtil.getClipTempImage().getPath();
					PictureUtil.saveBitmapFile(bmp, file);
					bmp.recycle();
					setResult(RESULT_OK);
					finish();
				}
				break;
			}
			case R.id.imv_rotate: {
				/*Bitmap bmTemp = bitmap;
				Bitmap bmDest = PictureUtil.rotaingImageView(90, bmTemp);
				matrix.reset();
				savedMatrix.reset();
				srcPic.setImageBitmap(bmDest);
				srcPic.setImageMatrix(matrix);
				srcPic.setScaleType(ScaleType.CENTER_INSIDE);
				bitmap = bmDest;*/
				rotate += 90;
				srcPic.setRotation(rotate);
			}
				break;
			default:
				break;
		}
	}
	/**
	 * 获取裁剪框内截图
	 * @return
	 */
	private Bitmap getBitmap() {
		Bitmap matricBitmap = drawable2Bitmap(srcPic.getDrawable(), srcPic.getWidth(), srcPic.getHeight());
		PictureUtil.saveBitmapFile(matricBitmap, "/sdcard/matric.jpg");
		// 获取截屏
		View view = this.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		// 获取状态栏高度
		Rect frame = new Rect();
		this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		int x = clipview.getClipLeftMargin();
		if (x < 0)
			x = 0;
		int y = clipview.getClipTopMargin() + statusBarHeight;
		if (y < 0)
			y = 0;
		Bitmap cacheBmp = view.getDrawingCache();
		/*Bitmap finalBitmap = Bitmap.createBitmap(cacheBmp, x,
				y, clipview.getClipWidth(), clipview.getClipHeight());*/
		int outputWidth = clipview.getClipWidth();
		if (outputWidth < 0) {
			outputWidth = clipview.getWidth();
		}
		int outputHeight = clipview.getClipHeight();
		if (outputHeight < 0) {
			outputHeight = (int) (outputWidth * clipRatio);
		}
		Bitmap finalBitmap = Bitmap.createBitmap(outputWidth, outputHeight, Bitmap.Config.ARGB_8888);
		if (cacheBmp != null) {
			Canvas c = new Canvas(finalBitmap);
			c.drawBitmap(cacheBmp, new Rect(x, y, x + outputWidth, y + outputHeight), new Rect(0, 0, outputWidth,
					outputHeight), new Paint());
		}
		// 释放资源
		view.destroyDrawingCache();
		return finalBitmap;
	}
	// 保存图片
	private String savePhoto() {
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			return null;
		Bitmap savedbmp = getBitmap();
		String filepath = BBSUtils.getTakePhotoPath(this, System.currentTimeMillis() + ".jpg");
		File savedfile = new File(filepath);
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(savedfile));
			savedbmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (savedbmp != null && !savedbmp.isRecycled()) {
				savedbmp.recycle();
				savedbmp = null;
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return filepath;
	}

	@Override
	public void registerVVBroadCastReceivers() {
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
	}
	@Override
	public void onEventMainThread(EventBusData data) {
	}
	/**
	 * 从Drawable中获取Bitmap对象
	 * @param drawable
	 * @return
	 */
	private Bitmap drawable2Bitmap(Drawable drawable, int defWidth, int defHeight) {
		try {
			if (drawable == null) {
				return null;
			}
			if (drawable instanceof BitmapDrawable) {
				return ((BitmapDrawable) drawable).getBitmap();
			}
			int intrinsicWidth = drawable.getIntrinsicWidth();
			int intrinsicHeight = drawable.getIntrinsicHeight();
			Bitmap bitmap = Bitmap.createBitmap(intrinsicWidth <= 0 ? defWidth : intrinsicWidth,
					intrinsicHeight <= 0 ? defHeight : intrinsicHeight, Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
			return bitmap;
		} catch (OutOfMemoryError e) {
			return null;
		}
	}

	public static enum ClipType {
		REGISTER, CLIP_NEWSTV, OTHERS;
		public static ClipType get(int index) {
			switch (index) {
				case 0:
					return REGISTER;
				case 1:
					return CLIP_NEWSTV;
				default:
					return OTHERS;
			}
		}
	}
	
	
	
	private class UpLoadFileRunnable implements Runnable {
		private File getFile;
		private String url;

		private UpLoadFileRunnable(String path, String url) {
			super();
			this.getFile = new File(path);
			this.url = url;
			showWaitingDialog();
		}
		private void showWaitingDialog() {
			UIHelper.showDialogForLoading(mContext, "请稍候", true);
		}
		@Override
		public void run() {
			HashMap<String, String> params = new HashMap<String, String>();
			String jidParsed = LoginManager.getInstance().getJidParsed();
			params.put("tm_id", jidParsed);
			params.put("session_id", LoginManager.getInstance().getSessionId());
			String result = UploadUtil.uploadImage(new String[] { getFile.getPath() }, url, params, "portrait_file",
					true);
			String[] uploadUrl = null;
			try {
				JSONObject jsonObject = new JSONObject(result);
				JSONArray jsonArray = jsonObject.getJSONArray("url");
				uploadUrl = new String[] { jsonArray.getString(0), jsonArray.getString(1) };
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				onPostExecute(uploadUrl);
			}
			return;
		}
		protected void onPostExecute(final String[] uploadUrl) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					UIHelper.hideDialogForLoading();
					if (uploadUrl != null) {
						// 删除拍照图片
						getFile.delete();
						Intent intent = new Intent();
						intent.putExtra("savePhoto", uploadUrl);
						setResult(Activity.RESULT_OK, intent);
						EventBus.getDefault().post(new EventBusData(EventAction.SendResultFAlbum, uploadUrl));
						finish();
						CToast.showToast(mContext, "上传成功", Toast.LENGTH_SHORT);
					} else {
						Log.i(TAG, "上传失败");
						CToast.showToast(mContext, "上传失败,请重新上传", Toast.LENGTH_SHORT);
					}
				}
			});
		}
	}
}
