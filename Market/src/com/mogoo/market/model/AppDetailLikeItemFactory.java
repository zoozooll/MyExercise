package com.mogoo.market.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mogoo.market.R;
import com.mogoo.market.ui.AppDetailActivity;
import com.mogoo.market.uicomponent.ImageDownloader;

/**
 * @author csq 软件详情  下载了此软件的用户还下载了 每项布局
 */
public class AppDetailLikeItemFactory 
{
	public static View create(final Context context,String imageUrl,final String appName,final String appId,
			final String packageName,final int downloadId )
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		View row = inflater.inflate(R.layout.image_like_item, null);
		
		ImageView iv = (ImageView) row.findViewById(R.id.app_iv);
		TextView txName = (TextView) row.findViewById(R.id.app_name);
		txName.setText(appName);
		
		iv.setImageResource(R.drawable.defautl_list_itme_pic_loading);
		ImageDownloader.getInstance(context).download(imageUrl, iv,
				BitmapFactory.decodeResource(context.getResources(), R.drawable.defautl_list_itme_pic_loading));
		
		row.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent();
                intent.setClass(context, AppDetailActivity.class);
                Bundle b = new Bundle();
                b.putString(AppDetailActivity.EXTRA_APP_ID, appId);
                b.putString(AppDetailActivity.EXTRA_APP_PACKAGENAME,packageName);
                b.putInt(AppDetailActivity.EXTRA_APP_DOWNLOADID_INT,downloadId);
                intent.putExtras(b);
                context.startActivity(intent);
			}
		});
		
		return row;
	}
}
