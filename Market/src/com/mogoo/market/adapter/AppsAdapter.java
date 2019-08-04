package com.mogoo.market.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.mogoo.market.R;
import com.mogoo.market.model.Apps;
import com.mogoo.market.ui.CateActivity;
import com.mogoo.market.ui.ChildCateActivity;
import com.mogoo.market.ui.MarketGroupActivity;
import com.mogoo.market.uicomponent.ImageDownloader;

public class AppsAdapter<T> extends ResourceCursorAdapter {
	static final int TYPE_CATE_INFO = 1;
	
	private Context mContext;
	private LayoutInflater mInflater;
	private ImageDownloader mImageLoader;
	
	public AppsAdapter(Context context, Cursor cursor, ImageDownloader imageLoader) {
		super(context, R.layout.cate_list_item, cursor);
		mContext = context;
		mImageLoader = imageLoader;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public void bindView(View convertView, Context context, Cursor cursor) {
		ViewHolder viewHolder;
		final Apps apps = Apps.getApps(cursor);
		viewHolder = (ViewHolder) convertView.getTag();
		
		if (viewHolder == null) {
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		
		mImageLoader.download(apps.getImgUrl(), viewHolder.picView,
				BitmapFactory.decodeResource(mContext.getResources(), R.drawable.defautl_list_itme_pic_loading));
		viewHolder.nameView.setText(apps.getName());
		if(!TextUtils.isEmpty(apps.getDescription())) {
			viewHolder.descView.setText(mContext.getString(R.string.cate_item_new, apps.getDescription()));
		}
		if(!TextUtils.isEmpty(apps.getCount())) {
			viewHolder.countView.setText(apps.getCount());
		}
		
		viewHolder.itemView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
				Intent childintent = new Intent();
			    Bundle bundle = new Bundle();
			    bundle.putInt(ChildCateActivity.EXTRA_REQUEST_ID, Integer.valueOf(apps.getId()));
			    bundle.putInt(ChildCateActivity.EXTRA_REQUEST_TYPE, TYPE_CATE_INFO);
			    bundle.putString(ChildCateActivity.EXTRA_REQUEST_TITLE, apps.getName());
			    childintent.putExtras(bundle);
				childintent.setClass(mContext, ChildCateActivity.class);
				MarketGroupActivity mga = MarketGroupActivity.getInstance();
				childintent.putExtra(MarketGroupActivity.EXTRA_BACK_INTENT, mga.getNowTabIntent());
				mga.setupTabChildView(childintent);
				mga.setupTabIntent(MarketGroupActivity.getCurrentTab(),childintent);
			}
		});
	}

	static class ViewHolder {
		TextView nameView;
		TextView descView;
		TextView countView;
		ImageView picView;
		View itemView;

		public ViewHolder(View view) {
			this.itemView = (View) view.findViewById(R.id.rl_cate_item);
			this.nameView = (TextView) view.findViewById(R.id.tv_cate_name);
			this.descView = (TextView) view.findViewById(R.id.tv_cate_desc);
			this.picView = (ImageView) view.findViewById(R.id.iv_cate_pic);
			this.countView = (TextView) view.findViewById(R.id.tv_cate_count);
		}
	}
}
