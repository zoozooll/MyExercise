package com.butterfly.vv.camera.renew;

import java.util.ArrayList;
import java.util.List;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.loadimages.ThumbImageFetcher;
import com.butterfly.vv.camera.base.ImageFolderInfo;
import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupAdapter extends BaseAdapter {
	private Context mContext;
	private List<ImageFolderInfo> list = new ArrayList<ImageFolderInfo>();
	private Point mPoint = new Point(0, 0);// 用来封装ImageView的宽和高的对象
	protected LayoutInflater mInflater;

	@Override
	public int getCount() {
		return list.size();
	}
	@Override
	public Object getItem(int position) {
		return list.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	public GroupAdapter(Context context, List<ImageFolderInfo> list) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		if (list != null)
			this.list.addAll(list);
	}
	public void setDataList(List<ImageFolderInfo> list) {
		this.list.clear();
		if (list != null)
			this.list.addAll(list);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		ImageFolderInfo mImageBean = list.get(position);
		String path = mImageBean.getmFolderFirstImgPath();
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.grid_group_item, null);
			viewHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.group_image);
			viewHolder.mTextViewTitle = (TextView) convertView
					.findViewById(R.id.group_title);
			viewHolder.mTextViewCounts = (TextView) convertView
					.findViewById(R.id.group_count);
			viewHolder.tvw_groupPath = (TextView) convertView
					.findViewById(R.id.tvw_groupPath);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.mImageView
					.setImageResource(R.drawable.deafult_imgloading);
		}
		viewHolder.mTextViewTitle.setText(mImageBean.getmFolderName());
		viewHolder.mTextViewCounts
				.setText(mContext.getString(R.string.timefly_picCountSting,
						mImageBean.getmFolderImgCount()));
		ThumbImageFetcher.getInstance(mContext).loadImage(path,
				viewHolder.mImageView);
		viewHolder.tvw_groupPath.setText(mImageBean.getmFolderPath());
		return convertView;
	}

	public static class ViewHolder {
		public ImageView mImageView;
		public TextView mTextViewTitle;
		public TextView mTextViewCounts;
		public TextView tvw_groupPath;
	}
}
