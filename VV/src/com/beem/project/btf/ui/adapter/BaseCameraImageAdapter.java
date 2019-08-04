package com.beem.project.btf.ui.adapter;

import java.util.List;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.adapter.CommonViewHolder;
import com.beem.project.btf.ui.entity.CommonCameraInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @ClassName: BaseTimeCameraImageAdapter
 * @Description:
 * @author: yuedong bao
 * @date: 2015-11-16 上午11:21:06
 * @param <T>
 */
public abstract class BaseCameraImageAdapter<T extends CommonCameraInfo>
		extends BaseAdapter {
	private List<T> ImageInfos;
	protected LayoutInflater mLayoutInflater;
	protected Context mContext;
	protected DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.deafult_imgloading)
			.showImageForEmptyUri(R.drawable.deafult_imgloading)
			.showImageOnFail(R.drawable.deafult_imgloading).cacheInMemory(true)
			.cacheOnDisk(true).build();

	public BaseCameraImageAdapter(List<T> ImageInfos, Context context) {
		this.mContext = context;
		this.ImageInfos = ImageInfos;
		this.mLayoutInflater = LayoutInflater.from(context);
		//LogUtils.v("create BaseTimeCameraImageAdapter");
	}
	@Override
	public int getCount() {
		int count = ImageInfos == null ? 0 : ImageInfos.size();
		return count;
	}
	@Override
	public T getItem(int position) {
		return ImageInfos.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		CommonViewHolder viewHolder = CommonViewHolder.getViewHolder(position,
				convertView, parent, R.layout.image_grid_fragment_item,
				mLayoutInflater);
		viewHolder.setVisibility(R.id.progressbar, View.GONE);
		T imageinfo = getItem(position);
		viewHolder
				.displayImage(R.id.album_image, imageinfo.getPathThumbLarge());
		if (imageinfo.isDownloaded()) {
			viewHolder.setVisibility(R.id.load_status, View.VISIBLE);
		} else {
			viewHolder.setVisibility(R.id.load_status, View.GONE);
		}
		setViewClickListener(position, viewHolder, imageinfo);
		return viewHolder.getConvertView();
	}
	public void setItem(List<T> ImageInfos) {
		this.ImageInfos = ImageInfos;
		notifyDataSetChanged();
	}
	public abstract void setViewClickListener(int position,
			CommonViewHolder holder, T t);
}
