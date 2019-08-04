package com.mogoo.components.download;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.RejectedExecutionException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.mogoo.components.ad.utils.FileUtil;

public class MogooDownImageView extends ImageView {

	private static final String tag = "MogooAdImageView";
	/** 保存图片到SD卡里的时候的文件名 */
	private String save_name = "save_name";

	private Bitmap mBitmap;

	private String mUrl;

	private Integer mDefaultImage;

	// 是否已经 启动 task，保证只启动一次
	private boolean isLoad = false;
	// 是否已经加载成功，成功就不再启动
	private boolean isOK = false;
	private String pictureName = null;

	private Context mContext;

	// 缓存到SD卡上的广告图片目录名称
	static final String pitureFolder = "mogoodown";
	private static FileUtil sdpath = new FileUtil();
	// 缓存到SD卡上的广告图片的具体路径
	static String picturepath = sdpath.getSDPath() + pitureFolder + "/";

	public MogooDownImageView(Context context) {
		super(context);
		this.mContext = context;
		// TODO Auto-generated constructor stub
		this.setScaleType(ScaleType.FIT_CENTER);
	}

	public void setImageUrl(String url) {
		mUrl = url;
		Log.i(tag, "需要加载的图片:" + mUrl);
		// url =
		// "http://192.168.0.177:8088/AD/upload/appad/20111231/20111231114700050.gif";
		if (url == null)
			return;

		if (pictureName == null)
			pictureName = url.substring(url.lastIndexOf("/") + 1);

		Log.i(tag, "pictureName:" + pictureName);

		// 检查SD卡是否已存在该图片
		Drawable drawable = Drawable.createFromPath(picturepath + pictureName);
		if (drawable != null) {
			Log.i(tag, "需要的图片 " + save_name + " 已在SD卡中，所以直接从SD卡读取");
			setBackgroundDrawable(drawable);
			drawable = null;
		}
		// SDCard不存在该图片
		else {
			try {
				// 如果线程正在运行or加载图片OK，则返回
				if (isLoad || isOK) {
					return;
				}

				// 启动线程下载图片
				new DownloadTask().execute(mUrl);
				isLoad = true;
				Log.i(tag, "--->start image task : id = " + hashCode());
			} catch (RejectedExecutionException e) {
			}
		}

	}

	void setDefaultImage(Integer resid) {
		mDefaultImage = resid;
		loadDefaultImage();
	}

	/**
	 * Loads default image
	 */
	private void loadDefaultImage() {
		if (mDefaultImage != null)
			// setImageResource(mDefaultImage);
			this.setBackgroundResource(mDefaultImage);

		Log.i(tag, "加载默认图片:" + mDefaultImage);
	}

	class DownloadTask extends AsyncTask<String, Void, String> {

		private String mTaskUrl;

		@Override
		public void onPreExecute() {
			loadDefaultImage();
			super.onPreExecute();
		}

		@Override
		public String doInBackground(String... params) {

			mTaskUrl = params[0];
			InputStream stream = null;
			URL imageUrl;
			Bitmap bmp = null;

			try {
				imageUrl = new URL(mTaskUrl);
				try {
					stream = imageUrl.openStream();
					bmp = BitmapFactory.decodeStream(stream);
					try {
						if (bmp != null) {
							mBitmap = bmp;
							// 加载图片成功
							isOK = true;
							Log.i(tag, "Image cached " + mTaskUrl);
						} else {
							Log.i(tag, "Failed to cache " + mTaskUrl);
						}
					} catch (NullPointerException e) {
						Log.i(tag, "Failed to cache " + mTaskUrl);
					}
				} catch (Exception e) {
					Log.i(tag, "Couldn't load bitmap from url: " + mTaskUrl);
				} finally {
					bmp = null;
					try {

						if (stream != null) {
							stream.close();
						}
					} catch (IOException e) {
					}
				}

			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return mTaskUrl;
		}

		@Override
		public void onPostExecute(String url) {
			super.onPostExecute(url);

			if (mBitmap == null) {
				// loadDefaultImage();
				// MogooImageView.this.setBackgroundResource(mDefaultImage);
				// invalidate();

				// Drawable d = null;
				// if (mDefaultImage != 0)
				// {
				// d = mContext.getResources().getDrawable(mDefaultImage);
				// }
				// setBackgroundDrawable(d);
			} else {

				MogooDownImageView.this
						.setBackgroundDrawable(new BitmapDrawable(mBitmap));
				saveBitmapToSdcard(mBitmap);
				mBitmap = null;
			}
			// 运行完成
			isLoad = false;
		}
	};

	// 保存图片到SD卡
	private void saveBitmapToSdcard(Bitmap bitmap) {
		try {
			if (bitmap != null) {
				FileUtil sdpath = new FileUtil();
				sdpath.saveFile(bitmap, pitureFolder, pictureName);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(tag, "+++++save picture fail");
			e.printStackTrace();
		}
	}

}
