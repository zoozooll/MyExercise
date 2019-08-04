package com.beem.project.btf.ui.adapter;

import java.util.List;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.adapter.CommonViewHolder;
import com.beem.project.btf.ui.entity.CommonCameraInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public abstract class BaseCameraImageManageAdapter<T extends CommonCameraInfo>
		extends BaseCameraImageAdapter<T> {
	public BaseCameraImageManageAdapter(List<T> ImageInfos, Context context) {
		super(ImageInfos, context);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CommonViewHolder viewHolder = CommonViewHolder.getViewHolder(
				position, convertView, parent,
				R.layout.image_grid_fragment_manage_item, mLayoutInflater);
		T imageinfo = getItem(position);
		ImageLoader.getInstance().displayImage(imageinfo.getPathThumbLarge(),
				(ImageView) viewHolder.getView(R.id.album_image),
				defaultOptions);
		setViewClickListener(position, viewHolder, imageinfo);
		return viewHolder.getConvertView();
	}
}
