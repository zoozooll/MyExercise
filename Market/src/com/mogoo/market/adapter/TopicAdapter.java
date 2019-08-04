package com.mogoo.market.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mogoo.market.R;
import com.mogoo.market.model.Topic;
import com.mogoo.market.ui.CateActivity;
import com.mogoo.market.ui.ChildCateActivity;
import com.mogoo.market.ui.MarketGroupActivity;
import com.mogoo.market.uicomponent.ImageDownloader;

public class TopicAdapter<T> extends ResourceCursorAdapter {
	static final int TYPE_TOPIC_INFO = 0;
	
	private Context mContext;
	private LayoutInflater mInflater;
	private ImageDownloader mImageLoader;
	
	public TopicAdapter(Context context, Cursor cursor, ImageDownloader imageLoader) {
		super(context, R.layout.topic_list_item, cursor);
		
		mContext = context;
		mImageLoader = imageLoader;
		mInflater = LayoutInflater.from(mContext);
	}

	
	@Override
	public void bindView(View convertView, Context context, Cursor cursor) {
		ViewHolder viewHolder;
		final Topic topic = Topic.getTopic(cursor);
		viewHolder = (ViewHolder) convertView.getTag();
		
		if (viewHolder == null) {
			viewHolder = new ViewHolder(convertView);
		}
		
		viewHolder.nameView.setText(topic.getName());
		viewHolder.descView.setText(topic.getDescription());
		mImageLoader.download(topic.getImgUrl(), viewHolder.picView,
				BitmapFactory.decodeResource(mContext.getResources(), R.drawable.topic_list_item_pic_loading));
		int typeResId = viewHolder.getTypeViewResId(topic.getType());
		if(typeResId != -1) {
			viewHolder.typeView.setImageResource(typeResId);
			viewHolder.typeView.setVisibility(View.VISIBLE);
		}else {
			viewHolder.typeView.setVisibility(View.GONE);
		}
		
		viewHolder.itemView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
				Intent childintent = new Intent();
			    Bundle bundle = new Bundle();
			    bundle.putInt(ChildCateActivity.EXTRA_REQUEST_ID, Integer.valueOf(topic.getId()));
			    bundle.putInt(ChildCateActivity.EXTRA_REQUEST_TYPE, TYPE_TOPIC_INFO);
			    bundle.putString(ChildCateActivity.EXTRA_REQUEST_TITLE, topic.getName());
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
		static final String HOT ="H";
		static final String NEW ="N";
		static final String NONE ="Y";
		
		TextView nameView;
		TextView descView;
		ImageView picView;
		ImageView typeView;
		View itemView;
		
		public ViewHolder(View view) {
			this.itemView = (View) view.findViewById(R.id.rl_topic_item);
			this.nameView = (TextView) view.findViewById(R.id.tv_topic_name);
			this.descView = (TextView) view.findViewById(R.id.tv_topic_desc);
			this.picView = (ImageView) view.findViewById(R.id.iv_topic_pic);
			this.typeView = (ImageView) view.findViewById(R.id.iv_topic_type);
		}
		
		int getTypeViewResId(String type) {
			if(type.equals(HOT)) {
				return R.drawable.top_list_item_hot;
			}else if(type.equals(NEW)) {
				return R.drawable.top_list_item_new;
			}
			return -1;
		}
	}
}
