package com.beem.project.btf.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.beem.project.btf.R;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ThumbnailAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<VVImage> mDatas;
	private DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.deafult_imgloading)
			.showImageOnFail(R.drawable.deafult_imgloading).cacheInMemory(true)
			.cacheOnDisk(true).build();
	private int currentSelectedPostion = 0;

	public ThumbnailAdapter(Context context, List<VVImage> mDatas) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = mDatas;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public void setSelectedPosition(int postion) {
		currentSelectedPostion = postion;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.share_ranking_thumbnail_item, parent, false);
			viewHolder.galleryitem = (ImageView) convertView
					.findViewById(R.id.share_ranking_thumbnail_item_image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (currentSelectedPostion == position) {
			convertView.setSelected(true);
		} else {
			convertView.setSelected(false);
		}
		ImageLoader.getInstance().displayImage(
				mDatas.get(position).getPathThumb(), viewHolder.galleryitem,
				defaultOptions);
		return convertView;
	}
	public void setItems(List<VVImage> list) {
		this.mDatas = list;
		notifyDataSetChanged();
	}

	class ViewHolder {
		public ImageView galleryitem;
	}
}
