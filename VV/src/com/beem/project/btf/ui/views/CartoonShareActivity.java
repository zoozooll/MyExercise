package com.beem.project.btf.ui.views;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.beem.project.btf.R;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.manager.ImageFolderItemManager;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.network.BDLocator;
import com.beem.project.btf.ui.CartoonCameraActivity;
import com.beem.project.btf.ui.CartoonCameraActivity.CameraType;
import com.beem.project.btf.ui.activity.InnerGuideHelper;
import com.beem.project.btf.ui.activity.NewsCameraEditorActivity;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.views.ShareChangeAlbumAuthorityView.AlbumAuthority;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.PictureUtil;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.beem.project.btf.utils.UIHelper;
import com.beem.project.btf.wxapi.InfoMessage;
import com.butterfly.vv.GalleryNavigation;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.service.TimeflyService.Valid;
import com.butterfly.vv.vv.utils.CToast;
import com.mob.tools.utils.UIHandler;
import com.teleca.jamendo.api.WSError;

import de.greenrobot.event.EventBus;

public class CartoonShareActivity extends Activity implements OnClickListener,
		PlatformActionListener, Callback {
	private static String TAG = "CartoonShareActivity";
	private ImageView mCartoonImage;
	private ScaleImageView mPengyouquanLayout, mQQZoneLayout, mWeiboLayout,
			mShareMoreLayout;
	private Bitmap mCartoonBm, mCartoonWatermarkBm;
	private String mCartoonPath, mCartoonWatermarkPath;
	private FrameLayout share_img_wraper;
	private static final String BITMAPPATH = "cartoon_bitmap_path_extra";
	private static final String ACTIVITYTYPE = "restart_activitytype";
	private String currentRestartActivity;
	private ImageFolderItem folderItem;
	private EventAction uploadPhotoAction;
	private Context mContext;
	private Platform platform;
	/** 分享的标题部分 */
	private String share_title;
	/** 分享的文字内容部分 */
	private String share_text;
	/** 分享的图片部分 */
	private String share_image;
	/** 分享的网址部分 */
	private String share_url;
	/** 分享数据的集合体 */
	private HashMap<String, Object> map;
	private LinearLayout share_friend_wrap, share_qq_zone_wrap,
			share_weibo_wrap, share_more_wrap;

	public enum RestartActivityType {
		TimeCamera, CartoonCamera, NewsCamera
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cartoon_camera_share);
		mContext = this;
		// 数据初始化
		ShareSDK.initSDK(mContext);
		if (LoginManager.getInstance().isLogined()) InnerGuideHelper.showSharedGuide(this);
		share_img_wraper = (FrameLayout) findViewById(R.id.share_img_wraper);
		GalleryNavigation mMyNavigationView = (GalleryNavigation) findViewById(R.id.share_navigation_view);
		mMyNavigationView.setBtnLeftListener(mNaviCloseClickListener);
		mMyNavigationView.setBtnUploadListener(mNaviRestartClickListener);
		mMyNavigationView
				.setBtnLeftIcon(R.drawable.share_activity_close_selector);
		mMyNavigationView.setChoiseMode("再来一张");
		Intent intent1 = getIntent();
		mCartoonPath = intent1.getStringExtra(BITMAPPATH);
		currentRestartActivity = intent1.getStringExtra(ACTIVITYTYPE);
		mCartoonImage = (ImageView) findViewById(R.id.cartoon_image);
		mPengyouquanLayout = (ScaleImageView) findViewById(R.id.share_friend);
		mQQZoneLayout = (ScaleImageView) findViewById(R.id.share_qq_zone);
		mWeiboLayout = (ScaleImageView) findViewById(R.id.share_weibo);
		mShareMoreLayout = (ScaleImageView) findViewById(R.id.share_more);
		share_friend_wrap = (LinearLayout) findViewById(R.id.share_friend_wrap);
		share_qq_zone_wrap = (LinearLayout) findViewById(R.id.share_qq_zone_wrap);
		share_weibo_wrap = (LinearLayout) findViewById(R.id.share_weibo_wrap);
		share_more_wrap = (LinearLayout) findViewById(R.id.share_more_wrap);
		share_friend_wrap.setOnClickListener(this);
		share_qq_zone_wrap.setOnClickListener(this);
		share_weibo_wrap.setOnClickListener(this);
		share_more_wrap.setOnClickListener(this);
		mPengyouquanLayout.setOnClickListener(this);
		mQQZoneLayout.setOnClickListener(this);
		mWeiboLayout.setOnClickListener(this);
		mShareMoreLayout.setOnClickListener(this);
		CheckBox autosharecheck = (CheckBox) findViewById(R.id.autosharecheck);
		autosharecheck.setVisibility(LoginManager.getInstance().isLogined() ? View.VISIBLE : View.GONE);
		if (SharedPrefsUtil.getValue(CartoonShareActivity.this,
				SettingKey.autosharecheck, false)) {
			autosharecheck.setChecked(true);
		} else {
			autosharecheck.setChecked(false);
		}
		autosharecheck
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						SharedPrefsUtil.putValue(CartoonShareActivity.this,
								SettingKey.autosharecheck, isChecked);
						if (isChecked) {
							Toast.makeText(CartoonShareActivity.this,
									"自动上传功能开启", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(CartoonShareActivity.this,
									"自动上传功能关闭", Toast.LENGTH_SHORT).show();
						}
					}
				});
		new AsyncTask<Void, Integer, String>() {
			@Override
			protected void onPreExecute() {
				UIHelper.showDialogForLoading(CartoonShareActivity.this,
						"读取图片中....", false);
			};
			@Override
			protected String doInBackground(Void... params) {
				Log.i(TAG, "~mCartoonPath~" + mCartoonPath);
				mCartoonBm = PictureUtil.getBitmapFormFile(mCartoonPath, 1);
				mCartoonWatermarkBm = setWatermark(mCartoonBm);
				try {
					if (mCartoonWatermarkBm != null) {
						mCartoonWatermarkPath = PictureUtil.saveToSDCard(
								CartoonShareActivity.this, mCartoonWatermarkBm);
						//Log.i(TAG, "~mCartoonWatermarkPath~"+mCartoonWatermarkPath);
					}
					PictureUtil.galleryAddPic(mContext, mCartoonWatermarkPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return mCartoonWatermarkPath;
			}
			@Override
			protected void onPostExecute(String result) {
				if (mCartoonWatermarkBm != null
						&& !StringUtils.isEmpty(mCartoonWatermarkPath)) {
					mCartoonImage.setVisibility(View.VISIBLE);
					mCartoonImage
							.setImageBitmap(zoomBitmap(mCartoonWatermarkBm));
					//启动上传线程
					if (SharedPrefsUtil.getValue(CartoonShareActivity.this,
							SettingKey.autosharecheck, false)) {
						new loadingImagesTask().execute();
					}
				}
				UIHelper.hideDialogForLoading();
			};
		}.execute();
	}
	public static void launch(Context ctx, String mBmPath, String restartType) {
		Intent intent = new Intent(ctx, CartoonShareActivity.class);
		intent.putExtra(BITMAPPATH, mBmPath);
		intent.putExtra(ACTIVITYTYPE, restartType);
		ctx.startActivity(intent);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.share_friend:
			case R.id.share_friend_wrap:
//				Log.i(TAG, "share_friend");
				share_CircleFriend();
				break;
			case R.id.share_qq_zone:
			case R.id.share_qq_zone_wrap:
//				Log.i(TAG, "share_qq_zone");
				share_Qzone();
				break;
			case R.id.share_weibo:
			case R.id.share_weibo_wrap:
				share_sinaWeibo();
				break;
			case R.id.share_more:
			case R.id.share_more_wrap: {
				Intent localIntent = new Intent("android.intent.action.SEND");
				localIntent.setType("image/*");
				File imageFile = new File(mCartoonWatermarkPath);
				Uri imageUri = Uri.fromFile(imageFile);
				localIntent.putExtra("android.intent.extra.STREAM", imageUri);
				startActivity(Intent.createChooser(localIntent, "分享至"));
				break;
			}
		}
	}
	private void startShareActivity(String shareAction, int type) {
		HashMap<String, ResolveInfo> hashMapResolveInfo = new HashMap<String, ResolveInfo>();
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		PackageManager pm = getPackageManager();
		final List<ResolveInfo> infos = pm.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		CharSequence[] names = new CharSequence[infos.size()];
		for (int i = 0; i < infos.size(); i++) {
			names[i] = infos.get(i).loadLabel(pm);
			String uipn = infos.get(i).activityInfo.name;
			if (uipn.equals(shareAction)) {
				hashMapResolveInfo.put(uipn, infos.get(i));
				break;
			}
		}
		ResolveInfo info = hashMapResolveInfo.get(shareAction);
		if (info == null) {
			switch (type) {
				case 5:
					Toast.makeText(CartoonShareActivity.this, "请安装微博客户端",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
			return;
		}
		File imageFile = new File(mCartoonWatermarkPath);
		Uri imageUri = Uri.fromFile(imageFile);
		Intent intentt = new Intent(Intent.ACTION_SEND);
		intentt.setType("image/*");
		intentt.putExtra(Intent.EXTRA_STREAM, imageUri);
		intentt.setClassName(info.activityInfo.packageName,
				info.activityInfo.name);
		startActivity(intentt);
	}
	public static void getShareComponet(Context mContext) {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		PackageManager pm = mContext.getPackageManager();
		final List<ResolveInfo> infos = pm.queryIntentActivities(intent,
				PackageManager.GET_RESOLVED_FILTER);
		for (int i = 0; i < infos.size(); i++) {
			Log.d(TAG, "packageName = " + infos.get(i).activityInfo.packageName);
			Log.d(TAG, "activity name = " + infos.get(i).activityInfo.name);
		}
	}

	OnClickListener mNaviRestartClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (RestartActivityType.TimeCamera.toString().equals(
					currentRestartActivity)) {
				CartoonCameraActivity.launch(CartoonShareActivity.this,
						CameraType.TIME.ordinal());
			} else if (RestartActivityType.CartoonCamera.toString().equals(
					currentRestartActivity)) {
				CartoonCameraActivity.launch(CartoonShareActivity.this,
						CameraType.CARTOON.ordinal());
			} else if (RestartActivityType.NewsCamera.toString().equals(
					currentRestartActivity)) {
				NewsCameraEditorActivity.launch(CartoonShareActivity.this);
			}
			finish();
		}
	};
	OnClickListener mNaviCloseClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			//发个消息关掉前面的页面
			finish();
		}
	};

	/** 添加水印 */
	private Bitmap setWatermark(Bitmap bitmap) {
		//Mat mat = new Mat();
		//Utils.bitmapToMat(bitmap, mat, true);
		//int state = CartoonLib.ProcessWatermark(mat.getNativeObjAddr());
		//Utils.matToBitmap(mat, bitmap);
		if (bitmap == null) {
			return null;
		}
		Bitmap newsBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), bitmap.getConfig());
		Canvas canvas = new Canvas(newsBitmap);
		Bitmap watermark_bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.watermark_bitmap);
		//缩放水印
		float scale = 1.0f;
		int postwidth = bitmap.getWidth() * 14 / 100;
		scale = (float) postwidth / (float) watermark_bitmap.getWidth();
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		watermark_bitmap = Bitmap.createBitmap(watermark_bitmap, 0, 0,
				watermark_bitmap.getWidth(), watermark_bitmap.getHeight(),
				matrix, true);
		//水印合成
		canvas.drawBitmap(bitmap, 0, 0, null);
		canvas.drawBitmap(watermark_bitmap, bitmap.getWidth()
				- watermark_bitmap.getWidth() - 10, bitmap.getHeight()
				- watermark_bitmap.getHeight() - 10, null);
		return newsBitmap;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (!StringUtils.isEmpty(mCartoonWatermarkPath)) {
			//清除保存的水印图片
			//PictureUtil.deleteTempFile(mCartoonWatermarkPath);
			//清除未打水印的图片
			PictureUtil.deleteTempFile(mCartoonPath);
		}
		if (mCartoonBm != null && !mCartoonBm.isRecycled()) {
			mCartoonBm.recycle();
			mCartoonBm = null;
		}
		if (mCartoonWatermarkBm != null && !mCartoonWatermarkBm.isRecycled()) {
			mCartoonWatermarkBm.recycle();
			mCartoonWatermarkBm = null;
		}
		// 取消分享菜单的统计
		ShareSDK.logDemoEvent(2, null);
		// 释放资源空间
		ShareSDK.stopSDK(this);
	}
	//缩放图片以适应屏幕
	private Bitmap zoomBitmap(Bitmap bm) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scale = 1.0f;
		int screenWidth = share_img_wraper.getWidth();
		int screenHeight = share_img_wraper.getHeight();
		// 计算缩放比例
		if (width >= height) {
			scale = ((float) screenWidth) / width;
		} else {
			scale = ((float) screenHeight) / height;
		}
		//float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}

	/** ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~图片上传~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
	/**
	 * @ClassName: loadingImagesTask
	 * @Description: 异步线程
	 * @author: pjunjun
	 * @date: 2015-9-19
	 */
	private class loadingImagesTask extends AsyncTask<String, Integer, Boolean> {
		loadingImagesTask() {
			super();
		}
		@Override
		protected Boolean doInBackground(String... arg0) {
			//判断当前是否连接服务器
			if (!LoginManager.getInstance().isLogined()) {
				return false;
			}
			boolean isUploadSuccess = true;
			uploadPhotoAction = EventAction.UploadTimeflyPhotoAdd;
			//LogUtils.i("WorkThread enter");
			try {
				folderItem = ImageFolderItemManager.getInstance()
						.getImageFolderItemNow(
								LoginManager.getInstance().getJidParsed());
			} catch (WSError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// //LogUtils.i("WorkThread folderItem==null?" + (folderItem == null));
			if (folderItem == null) {
				// 今天还没有上传过图片
				uploadPhotoAction = EventAction.UploadTimeflyPhotoCreate;
				folderItem = new ImageFolderItem();
				ImageFolder folder = new ImageFolder();
				folder.setField(DBKey.signature, "");
				folder.setField(DBKey.authority, SharedPrefsUtil.getValue(
						mContext, SettingKey.album_auhtority,
						AlbumAuthority.all));
				folder.setField(DBKey.jid, LoginManager.getInstance()
						.getJidParsed());
				folder.setField(DBKey.notify_time, LoginManager.getInstance()
						.getSystemTime());
				folder.setField(DBKey.notify_valid, Valid.close.val);
				folder.setField(DBKey.lat, BDLocator.getInstance().getLat());
				folder.setField(DBKey.lon, BDLocator.getInstance().getLon());
				folder.setField(DBKey.photoCount, String.valueOf(1));
				folderItem
						.setContact(LoginManager.getInstance().getMyContact());
				folderItem.setImageFolder(folder);
				folderItem.setVVImages(new ArrayList<VVImage>());
				//LogUtils.i("WorkThread exit");
			}
			return isUploadSuccess;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			// //LogUtils.i("onPostExecute enter");
			if (result) {
				LoadupCartoonImage();
			} else {
				//LogUtils.i("Connect the server failed, please connect it and try again!");
				CToast.showToast(mContext, R.string.connect_server_failed,
						Toast.LENGTH_SHORT);
			}
		}
	}

	private void LoadupCartoonImage() {
		String createTime = folderItem.getImageFolder().getCreateTime();
		//LogUtils.i("createTime=" + createTime);
		if (createTime == null || createTime.length() <= 0) {
			Date nowDate = LoginManager.getInstance().getSysytemTimeDate();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			String dateLike = sf.format(nowDate);
			folderItem.getImageFolder().setField(DBKey.createTime, dateLike);
		}
		ArrayList<String> listPath = new ArrayList<String>();
		listPath.add(mCartoonPath);
		int UploadedNum = folderItem.getImageFolder().getPhotoCount();
		if (listPath.size() > (Constants.uploadpicMaxNum - UploadedNum)) {
			//LogUtils.e("The uploaded pictures is too much, please try again!");
			CToast.showToast(mContext, "超过上传图片上限！", Toast.LENGTH_SHORT);
			return;
		}
		EventBusData data = new EventBusData(uploadPhotoAction, folderItem,
				listPath);
		EventBus.getDefault().post(data);
	}
	@Override
	public boolean handleMessage(Message msg) {
		String text = actionToString(msg.arg2);
		switch (msg.arg1) {
			case 1:
				// 成功
				text = "分享成功";
				InfoMessage.showMessage(this, text);
				CartoonShareActivity.this.finish();
				break;
			case 2:
				// 失败
				text = "分享失败";
				break;
			case 3:
				// 取消
				text = "分享已取消";
				break;
		}
		InfoMessage.showMessage(this, text);
		return false;
	}
	@Override
	public void onCancel(Platform palt, int action) {
		Message msg = new Message();
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = palt;
		UIHandler.sendMessage(msg, this);
	}
	@Override
	public void onComplete(Platform plat, int action,
			HashMap<String, Object> arg2) {
		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = plat;
		UIHandler.sendMessage(msg, this);
	}
	@Override
	public void onError(Platform arg0, int action, Throwable t) {
		t.printStackTrace();
		Message msg = new Message();
		msg.arg1 = 2;
		msg.arg2 = action;
		UIHandler.sendMessage(msg, this);
	}
	/** 将action转换为String */
	public static String actionToString(int action) {
		switch (action) {
			case Platform.ACTION_AUTHORIZING:
				return "ACTION_AUTHORIZING";
			case Platform.ACTION_GETTING_FRIEND_LIST:
				return "ACTION_GETTING_FRIEND_LIST";
			case Platform.ACTION_FOLLOWING_USER:
				return "ACTION_FOLLOWING_USER";
			case Platform.ACTION_SENDING_DIRECT_MESSAGE:
				return "ACTION_SENDING_DIRECT_MESSAGE";
			case Platform.ACTION_TIMELINE:
				return "ACTION_TIMELINE";
			case Platform.ACTION_USER_INFOR:
				return "ACTION_USER_INFOR";
			case Platform.ACTION_SHARE:
				return "ACTION_SHARE";
			default: {
				return "UNKNOWN";
			}
		}
	}
	/**
	 * 分享到朋友圈
	 */
	private void share_CircleFriend() {
		ShareParams wechatMoments = new ShareParams();
		String suitableName;
		try {
			suitableName = LoginManager.getInstance().getMyContact()
					.getSuitableName();
		} catch (IllegalArgumentException e) {
			suitableName = getString(R.string.app_name);
		}
		wechatMoments.setTitle(suitableName + "的时光相册");
		wechatMoments.setImagePath(mCartoonWatermarkPath);
		wechatMoments.setShareType(Platform.SHARE_IMAGE);
		Platform weixin = ShareSDK.getPlatform(mContext, WechatMoments.NAME);
		weixin.setPlatformActionListener(this);
		weixin.share(wechatMoments);
	}
	/**
	 * 分享到QQ空间
	 */
	private void share_Qzone() {
		ShareParams qq = new ShareParams();
		String suitableName;
		try {
			suitableName = LoginManager.getInstance().getMyContact()
					.getSuitableName();
		} catch (IllegalArgumentException e) {
			suitableName = getString(R.string.app_name);
		}
		qq.setTitle(suitableName + "的时光相册");
		qq.setTitleUrl(AppProperty.getInstance().INTRODUCTION_URL);
		qq.setText("我刚刚上传了一组自己的时光相册,大家快来围观吧!");
		qq.setImagePath(mCartoonWatermarkPath);
		qq.setSite("时光团队");
		qq.setSiteUrl(AppProperty.getInstance().INTRODUCTION_URL);
		Platform qqq = ShareSDK.getPlatform(mContext, QZone.NAME);
		qqq.setPlatformActionListener(this);
		qqq.share(qq);
	}
	/**
	 * 分享到新浪微博
	 */
	private void share_sinaWeibo() {
		ShareParams sp = new ShareParams();
		sp.setText("我刚刚上传了一组自己的时光相册,大家快来围观吧!");
		sp.setImagePath(mCartoonWatermarkPath);
		Platform weibo = ShareSDK.getPlatform(mContext, SinaWeibo.NAME);
		weibo.setPlatformActionListener(this); // 设置分享事件回调
		weibo.SSOSetting(true);
		// 执行图文分享
		weibo.share(sp);
	}
}
