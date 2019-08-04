package com.mogoo.market.adapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mogoo.market.R;
import com.mogoo.market.model.RecommentAppInfo;
import com.mogoo.market.ui.AppDetailActivity;
import com.mogoo.market.uicomponent.ImageDownloader;
import com.mogoo.market.utils.LogUtils;

/**
 *  csq 下载此软件的用户还下载了  列表适配器
 *  暂时弃用
 */
public class GalleryLikeAdapter extends BaseAdapter 
{
	public static final String APKID ="apkid";

	private int mLayoutID = R.layout.image_like_item;
	
	private Context context;
	private  ArrayList<RecommentAppInfo> appInfoList;
	
	private ImageDownloader imageDownloader;

	public GalleryLikeAdapter(Context context) 
	{
		this.context=context;
		imageDownloader = ImageDownloader.getInstance(context);
	}

	public GalleryLikeAdapter(Context context, ArrayList<RecommentAppInfo> appInfolist) 
	{
		this.appInfoList = appInfolist;
		this.context=context;
		imageDownloader = ImageDownloader.getInstance(context);
	}
	
	public void setData( ArrayList<RecommentAppInfo> appInfolist)
	{
	    this.appInfoList=appInfolist;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View row = convertView;
		ViewHolder holder = null;
	    
		final RecommentAppInfo app=appInfoList.get(position);
		
		if (row == null) 
		{
			LayoutInflater inflater = LayoutInflater.from(context);
			row = inflater.inflate(mLayoutID, null);

			holder = new ViewHolder();
			holder.image_lay = (RelativeLayout) row.findViewById(R.id.image_lay);
			holder.app_iv = (ImageView) row.findViewById(R.id.app_iv);
			holder.app_iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
			holder.app_name = (TextView) row.findViewById(R.id.app_name);
			
			row.setTag(holder); 
		}
		else 
		{
			holder = (ViewHolder) row.getTag();
			holder.app_iv.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
				    Intent intent = new Intent();
	                intent.setClass(context, AppDetailActivity.class);
	                Bundle bundle=new Bundle();
	                bundle.putString("apkid", app.getId());
	                intent.putExtras(bundle);
	                
	                context.startActivity(intent);
				}				
			});
		}
		
		String iconUrl = app.getIconUrl();
		if(iconUrl!=null && imageCache.containsKey(iconUrl) && imageCache.get(iconUrl)!=null)
		{
			holder.app_iv.setImageBitmap(imageCache.get(iconUrl));
		}
		else
		{
			holder.app_iv.setImageResource(R.drawable.defautl_list_itme_pic_loading);
			imageDownloader.download(iconUrl, holder.app_iv,
					BitmapFactory.decodeResource(context.getResources(), R.drawable.defautl_list_itme_pic_loading));
		}
		holder.app_name.setText(app.getName());

		return row;
	}

	class ViewHolder
	{
		public RelativeLayout image_lay;
		public ImageView app_iv;
		public TextView app_name;
	}

	@Override
	public int getCount() 
	{
		return (appInfoList!=null)?appInfoList.size():0;
	}

	@Override
	public Object getItem(int position) 
	{
		return appInfoList.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}
	
	
	/**
	 * 图片缓存
	 */
	HashMap<String, Bitmap> imageCache = new HashMap<String, Bitmap>();
	/**
	 * 单个图片加载器
	 */
	class AsyncSingleImageLoader implements Runnable 
	{
		/** 图片的下载地址 */
		private String mImageUrl;
		/** 图片下载完成后回调的ImageView */
		private ImageView iv;

		public AsyncSingleImageLoader(String imageUrl,ImageView iv) 
		{
			this.mImageUrl = imageUrl;
			this.iv = iv;
		}

		public void run() 
		{
			Bitmap bitmap = null;
			InputStream inputStream = null;
			try 
			{
				URL url = new URL(mImageUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true);
				conn.connect();
				conn.setConnectTimeout(5 * 1000);

				inputStream = conn.getInputStream();
				BitmapFactory.Options options = new BitmapFactory.Options();
				bitmap = BitmapFactory.decodeStream(inputStream, null, options);
				
				if (bitmap != null) 
				{
					iv.setImageBitmap(bitmap);
					imageCache.put(mImageUrl, bitmap);
				}
				else 
				{
					iv.setImageResource(R.drawable.icon);
				}

				conn.disconnect();

			} catch (FileNotFoundException e) {
				LogUtils.error(AsyncSingleImageLoader.class,
						"java.io.FileNotFoundException: the image is not found");
			} catch (ConnectException e) {
				LogUtils.error(AsyncSingleImageLoader.class,
						" java.net.ConnectException: Connection refused");
			} catch (Exception e) {
				e.printStackTrace();
			} 
			finally 
			{
				try {
					if(inputStream!=null)
						inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}


}