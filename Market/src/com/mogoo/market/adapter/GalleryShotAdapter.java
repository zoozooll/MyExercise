package com.mogoo.market.adapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mogoo.market.R;
import com.mogoo.market.uicomponent.ImageDownloader;
import com.mogoo.market.utils.LogUtils;

/**
 * @see 软件快照  列表适配器
 *  暂时弃用
 */
public class GalleryShotAdapter extends BaseAdapter 
{
	private int mLayoutID = R.layout.image_shot_item;
	private Context context;
	private List<String> imageUrl;
	
	private ImageDownloader imageDownloader;

	public GalleryShotAdapter(Context context,List<String> list) 
	{
		this.imageUrl=list;
		this.context=context;
		imageDownloader = ImageDownloader.getInstance(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		View row = convertView;
		ViewHolder holder = null;
	
		if (row == null) 
		{
			LayoutInflater inflater = LayoutInflater.from(context);
			row = inflater.inflate(mLayoutID, null);
			
			holder = new ViewHolder();
			holder.app_iv = (ImageView) row.findViewById(R.id.app_iv);
			holder.image_lay = (RelativeLayout) row.findViewById(R.id.image_lay);
			
			row.setTag(holder); 
		} 
		else 
		{
			holder = (ViewHolder) row.getTag();
		}
		
		String iconUrl = imageUrl.get(position);
		if(iconUrl!=null && imageCache.containsKey(iconUrl) && imageCache.get(iconUrl)!=null)
		{
			holder.app_iv.setImageBitmap(imageCache.get(iconUrl));
		}
		else
		{
			holder.app_iv.setImageResource(R.drawable.appdetail_shot_default);
			imageDownloader.download(iconUrl, holder.app_iv,
					BitmapFactory.decodeResource(context.getResources(), R.drawable.appdetail_shot_default));
		}
			
		return row;
	}

	class ViewHolder 
	{
		public RelativeLayout image_lay;
		public ImageView app_iv;
	}

	@Override
	public int getCount() 
	{
		return (imageUrl!=null)?imageUrl.size():0;
	}

	@Override
	public Object getItem(int position) 
	{
		return imageUrl.get(position);
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
