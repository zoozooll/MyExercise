package com.mogoo.components.ad;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.RejectedExecutionException;

import com.mogoo.components.ad.utils.FileUtil;
import com.mogoo.market.uicomponent.ImageDownloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

class MogooImageView extends ImageView
{

	private AdOnClickListener mListener;
	private AdvertiseItem mItem = null;
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

	public MogooImageView(Context context)
	{
		super(context);
		this.mContext = context;
		// TODO Auto-generated constructor stub
		this.setScaleType(ScaleType.FIT_CENTER);
	}

	void setAdvertiseItem(AdvertiseItem item)
	{
		if (item == null)
			return;
		this.mItem = item;
		// String mUrl = MogooInfo.base_url + mItem.getImgurl();
		// setImageUrl(mItem.getImgurl());
		ImageDownloader.getInstance(mContext).download(mItem.getImgurl(), this,
				BitmapFactory.decodeResource(getResources(), mDefaultImage));
	}

	void setAdOnClickListener(AdOnClickListener listener)
	{
		this.mListener = listener;

		// 设置点击响应处理
		this.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				MogooInfo.Log(tag, "MogooAdImageView.onClick().....");
				if (mListener != null && mItem != null)
				{
					MogooInfo.Log(tag, "mListener != null.....");
					mListener.OnClick(mItem);
				}

			}
		});
	}

	void refreshImageView() {
		if (mItem == null)
			return;
		ImageDownloader.getInstance(mContext).download(mItem.getImgurl(), this,
				BitmapFactory.decodeResource(getResources(), mDefaultImage));
	}
	
	private void setImageUrl(String url)
	{
		mUrl = url;
		MogooInfo.Log(tag, "需要加载的图片:" + mUrl);
		// url =
		// "http://192.168.0.177:8088/AD/upload/appad/20111231/20111231114700050.gif";
		if (url == null)
			return;

		if (pictureName == null)
			pictureName = url.substring(url.lastIndexOf("/") + 1);

		MogooInfo.Log(tag, "pictureName:" + pictureName);

		// 检查SD卡是否已存在该图片
		Drawable drawable = Drawable.createFromPath(MogooInfo.picturepath
				+ pictureName);
		if (drawable != null)
		{
			MogooInfo.Log(tag, "需要的图片 " + save_name + " 已在SD卡中，所以直接从SD卡读取");
			setBackgroundDrawable(drawable);
			drawable = null;
		}
		// SDCard不存在该图片
		else
		{
			try
			{
				// 如果线程正在运行or加载图片OK，则返回
				if (isLoad || isOK)
				{
					return;
				}

				// 启动线程下载图片
				new DownloadTask().execute(mUrl);
				isLoad = true;
				MogooInfo.Log(tag, "--->start image task : id = " + hashCode());
			}
			catch (RejectedExecutionException e)
			{
			}
		}

	}

	void setDefaultImage(Integer resid)
	{
		mDefaultImage = resid;
		loadDefaultImage();
	}

	/**
	 * Loads default image
	 */
	private void loadDefaultImage()
	{
		if (mDefaultImage != null)
			setImageResource(mDefaultImage);
			//this.setBackgroundResource(mDefaultImage);

		MogooInfo.Log(tag, "加载默认图片:" + mDefaultImage);
	}

	class DownloadTask extends AsyncTask<String, Void, String>
	{

		private String mTaskUrl;

		@Override
		public void onPreExecute()
		{
			loadDefaultImage();
			super.onPreExecute();
		}

		@Override
		public String doInBackground(String... params)
		{

			mTaskUrl = params[0];
			InputStream stream = null;
			URL imageUrl;
			Bitmap bmp = null;

			try
			{
				imageUrl = new URL(mTaskUrl);
				try
				{
					stream = imageUrl.openStream();
					bmp = BitmapFactory.decodeStream(stream);
					try
					{
						if (bmp != null)
						{
							mBitmap = bmp;
							// 加载图片成功
							isOK = true;
							MogooInfo.Log(tag, "Image cached " + mTaskUrl);
						}
						else
						{
							MogooInfo.Log(tag, "Failed to cache " + mTaskUrl);
						}
					}
					catch (NullPointerException e)
					{
						MogooInfo.Log(tag, "Failed to cache " + mTaskUrl);
					}
				}
				catch (Exception e)
				{
					MogooInfo.Log(tag, "Couldn't load bitmap from url: "
							+ mTaskUrl);
				}
				finally
				{
					bmp = null;
					try
					{

						if (stream != null)
						{
							stream.close();
						}
					}
					catch (IOException e)
					{
					}
				}

			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
			return mTaskUrl;
		}

		@Override
		public void onPostExecute(String url)
		{
			super.onPostExecute(url);

			if (mBitmap == null)
			{
				// loadDefaultImage();
				// MogooImageView.this.setBackgroundResource(mDefaultImage);
				// invalidate();

				// Drawable d = null;
				// if (mDefaultImage != 0)
				// {
				// d = mContext.getResources().getDrawable(mDefaultImage);
				// }
				// setBackgroundDrawable(d);
			}
			else
			{

				MogooImageView.this.setBackgroundDrawable(new BitmapDrawable(
						mBitmap));
				saveBitmapToSdcard(mBitmap);
				mBitmap = null;
			}
			// 运行完成
			isLoad = false;
		}
	};

	// 保存图片到SD卡
	private void saveBitmapToSdcard(Bitmap bitmap)
	{
		try
		{
			if (bitmap != null)
			{
				FileUtil sdpath = new FileUtil();
				sdpath.saveFile(bitmap, MogooInfo.pitureFolder, pictureName);
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			MogooInfo.Log(tag, "+++++save picture fail");
			e.printStackTrace();
		}
	}
}
