package com.butterfly.vv.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.beem.project.btf.R;
import com.butterfly.vv.camera.displayimage.PhotoFolderImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

public class ImageUploadGridViewAdapter extends BaseAdapter {
	private static final String TAG = "ImageUploadGridViewAdapter";
	private ArrayList<String> listimagePath;
	protected final LayoutInflater mInflater;
	private boolean showAddBtn;

	public ImageUploadGridViewAdapter(Context context,
			ArrayList<String> listimagePath, boolean showAddBtn) {
		this.listimagePath = listimagePath;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.showAddBtn = showAddBtn;
	}
	@Override
	public int getCount() {
		if (listimagePath == null) {
			return 0;
		}
		if (showAddBtn) {
			return listimagePath.size() + 1;
		} else {
			return listimagePath.size();
		}
	}
	@Override
	public Object getItem(int position) {
		if (position >= 0 && position < listimagePath.size()) {
			return listimagePath.get(position);
		}
		return null;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final GridHolderView HolderView;
		if (convertView == null) {
			// Log.d(TAG, "getView new   view " + position);
			HolderView = new GridHolderView();
			convertView = mInflater.inflate(R.layout.imageuploadgridview,
					parent, false);
			HolderView.photoFolderImageView = (PhotoFolderImageView) convertView
					.findViewById(R.id.child_image_image_gridview);
			convertView.setTag(HolderView);
		} else {
			// Log.d(TAG, "getView convertview " + position);
			HolderView = (GridHolderView) convertView.getTag();
		}
		if (showAddBtn && position == (getCount() - 1)) {
			ImageLoader.getInstance().cancelDisplayTask(
					HolderView.photoFolderImageView);
			HolderView.photoFolderImageView
					.setImageResource(R.drawable.addcontacts_selector);
		} else {
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.deafult_imgloading)
					.showImageForEmptyUri(R.drawable.deafult_imgloading)
					.showImageOnFail(R.drawable.deafult_imgloading)
					.cacheInMemory(true).cacheOnDisk(true).build();
			ImageLoader.getInstance().displayImage(
					Scheme.FILE.wrap(listimagePath.get(position)),
					HolderView.photoFolderImageView, options);
		}
		return convertView;
	}

	private class GridHolderView {
		PhotoFolderImageView photoFolderImageView;
	}
}
