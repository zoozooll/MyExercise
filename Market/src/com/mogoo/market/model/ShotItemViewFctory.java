package com.mogoo.market.model;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.mogoo.market.R;
import com.mogoo.market.uicomponent.ImageDownloader;

/**
 * @author csq 软件详情简介  应用图片快照
 */
public class ShotItemViewFctory 
{
	public static View create(String imageUrl,Context context)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		View row = inflater.inflate(R.layout.image_shot_item, null);
		
		ImageView iv = (ImageView) row.findViewById(R.id.app_iv);
		iv.setImageResource(R.drawable.appdetail_shot_default);
		
		ImageDownloader idl = ImageDownloader.getInstance(context);
		idl.download(imageUrl, iv,
				BitmapFactory.decodeResource(context.getResources(), R.drawable.appdetail_shot_default));
		
		return row;
	}
	
	public static ImageView getImagesView(String imageUrl,Context context)
	{
		ImageView iv = new ImageView(context);
//		iv.setImageResource(R.drawable.appdetail_shot_default);
		ImageDownloader idl = ImageDownloader.getInstance(context);
		//idl.setDefaultRes(R.drawable.appdetail_shot_default, R.drawable.appdetail_shot_default, R.drawable.appdetail_shot_default);
		idl.download(imageUrl, iv,
				BitmapFactory.decodeResource(context.getResources(), R.drawable.appdetail_shot_default));
		return iv;
	}
}
