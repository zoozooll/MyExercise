package com.beem.project.btf.ui.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.helpers.Util;

import android.R.anim;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

//import com.baidu.mobads.SplashAdListener;
import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.ui.activity.base.BeemServiceHelper;
import com.beem.project.btf.ui.activity.base.VVBaseActivity;
import com.beem.project.btf.ui.guide.WelcomeActivity;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.FileUtil;
import com.beem.project.btf.utils.LogUtils;
import com.beem.project.btf.utils.PictureUtil;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.beem.project.btf.utils.ThreadUtils;
import com.butterfly.piqs.vvcartoon.CartoonLib;
import com.butterfly.vv.vv.utils.MD5;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.teleca.jamendo.api.WSError;
import com.teleca.jamendo.api.util.Caller;

public class StartActivity extends VVBaseActivity {
	private static final String PREFERENCE_FIRSTRUN = "PREFERENCE_FIRSTRUN";
	private Handler mHandler = new Handler();
	private BeemServiceHelper xmppHelper;
	private ImageView animImage;
	private Animation animation;
	private RelativeLayout adsParent, ad_wraper;
	private String adPlaceId = "2425289"; // 重要：请填上您的广告位ID，代码位错误会导致无法请求到广告
	//	private SplashAdListener listener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		((BeemApplication) getApplication()).reloadAppProperty();
		setContentView(R.layout.start_activity);
		animImage = (ImageView) findViewById(R.id.animImage);
		// 设置多渠道发布
		setMutileChannel();
		/*if (BuildConfig.DEBUG) {
			Toast.makeText(this, "DEBUG", Toast.LENGTH_SHORT).show();
		}*/
		initCartooncameraFiles();
		new Thread(new Runnable() {
			@Override
			public void run() {
				queryAdData();
			}
		}, "queryAdData").start();
		String adFile = SharedPrefsUtil.getValue(this, "startUpAdsImage", null);
		if (adFile != null && new File(adFile).exists()) {
			Bitmap bm = BitmapFactory.decodeFile(adFile);
			animImage.setImageBitmap(bm);
			animImage.setClickable(true);
			animImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String link = SharedPrefsUtil.getValue(StartActivity.this, "startUpAdsLink", null);
					if (link != null) {
						Intent intent = new Intent();        
						intent.setAction("android.intent.action.VIEW");    
						Uri content_url = Uri.parse(link);   
						intent.setData(content_url);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
				}
			});
		} else {
			animImage.setClickable(false);
		}
		animation = AnimationUtils.loadAnimation(mContext, R.anim.alpha);
		adsParent = (RelativeLayout) this.findViewById(R.id.adsRl);
		ad_wraper = (RelativeLayout) this.findViewById(R.id.ad_wraper);
		animImage.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				/*mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
					}
				}, 0);*/
				jumpToActivity();
			}
		});
		/*listener = new SplashAdListener() {
			@Override
			public void onAdDismissed() {
				jumptoMainActivity();
			}
			@Override
			public void onAdFailed(String arg0) {
				jumptoMainActivity();
			}
			@Override
			public void onAdPresent() {
				Log.i("StartActivity", "onAdPresent");
			}
			@Override
			public void onAdClick() {
			}
		};*/
	}
	// 设置多渠道发布
	private void setMutileChannel() {
		ApplicationInfo appInfo;
		String market = "";
		try {
			appInfo = this.getPackageManager().getApplicationInfo(
					getPackageName(), PackageManager.GET_META_DATA);
			market = appInfo.metaData.getString("PublishMarket");
			if ("360".equals(market)) {
				animImage.setImageResource(R.drawable.login_bg_360);
			} else if ("pplive".equals(market)) {
				animImage.setImageResource(R.drawable.login_bg_pplive);
			} else if ("yingyongbao".equals(market)) {
				animImage.setImageResource(R.drawable.login_bg_yiyongbao);
			} else {
				animImage.setImageResource(R.drawable.login_bg);
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void queryAdData() {
		LogUtils.d("queryAdData");
		String adsurl = AppProperty.getInstance().VVAPI  + AppProperty.GET_STARTUP;
		//如果不存在，即下载；
		try {
			String response = Caller.doGet(adsurl);
			JSONObject jobj = new JSONObject(response);
			JSONObject data = jobj.getJSONObject("data");
			String url = data.getString("img");
			String link = data.getString("target");
			File folder = BBSUtils.getAppCacheDir(this, "startUpAds");
			File imageFile = new File(folder, MD5.getMD5(url.getBytes())
					+ ".jpg");
			if (!imageFile.exists()) {
				if (PictureUtil.downloadFile(url, imageFile)) {
					SharedPrefsUtil.putValue(this, "startUpAdsUrl", url);
					SharedPrefsUtil.putValue(this, "startUpAdsImage", imageFile.getPath());
					SharedPrefsUtil.putValue(this, "startUpAdsLink", link);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WSError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void jumpToActivity() {
		if (SharedPrefsUtil.getValue(mContext, PREFERENCE_FIRSTRUN, false)) {
			jumptoMainActivity();
			//			ADjump();
		} else {
			// 如果是第一次进入，即会进入welcome界面；
			SharedPrefsUtil.putValue(this, PREFERENCE_FIRSTRUN, true);
			Intent i = new Intent(this, WelcomeActivity.class);
			startActivity(i);
		}
		finish();
	}
	private void jumptoMainActivity() {
		Intent i = new Intent(this, MainpagerActivity.class);
		startActivity(i);
	}
	private void ADjump() {
		//		ad_wraper.setVisibility(View.VISIBLE);
		//		animImage.setVisibility(View.GONE);
		//		new SplashAd(mContext, adsParent, listener, adPlaceId, false);
	}
	// 拷贝漫画相关文件（从assets到data文件目录）
	private Boolean CopyAssetsFile(String filename, String des) {
		Boolean isSuccess = true;
		AssetManager assetManager = this.getAssets();
		InputStream in = null;
		OutputStream out = null;
		try {
			in = assetManager.open(filename);
			String newFileName = des + "/" + filename;
			out = new FileOutputStream(newFileName);
			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		} catch (Exception e) {
			e.printStackTrace();
			isSuccess = false;
		}
		return isSuccess;
	}
	// 拷贝漫画相关文件夹（从assets到data文件目录）
	private Boolean CopyAssetsDir(String src, String des) {
		Boolean isSuccess = true;
		String[] files;
		try {
			files = this.getResources().getAssets().list(src);
		} catch (IOException e1) {
			return false;
		}
		if (files.length == 0) {
			isSuccess = CopyAssetsFile(src, des);
			if (!isSuccess)
				return isSuccess;
		} else {
			File srcfile = new File(des + "/" + src);
			if (!srcfile.exists()) {
				if (srcfile.mkdir()) {
					for (int i = 0; i < files.length; i++) {
						isSuccess = CopyAssetsDir(src + "/" + files[i], des);
						if (!isSuccess)
							return isSuccess;
					}
				} else {
					return false;
				}
			}
		}
		return isSuccess;
	}
	private void initCartooncameraFiles() {
		ThreadUtils.executeTask(new Runnable() {
			@Override
			public void run() {
				// 拷贝漫画相关文件
				CopyAssetsDir("Cartoon", BeemApplication.getContext()
						.getFilesDir().getAbsolutePath());
				// 调用jni库,初始化
				int istimeout = CartoonLib.InitLib(
						BBSUtils.getTestCartoonDir(
								BeemApplication.getContext(), "cartoon", true)
								.getAbsolutePath(), StartActivity.this
								.getFilesDir().getAbsolutePath());
				SharedPrefsUtil.putValue(mContext, SettingKey.LibTimeOut,
						istimeout);
			}
		});
	}
	@Override
	public void registerVVBroadCastReceivers() {
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
