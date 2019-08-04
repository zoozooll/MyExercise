package com.butterfly.vv.adapter;

import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beem.project.btf.R;

public class ImageGalleryAdapter extends BaseAdapter {
	private int selectItem;
	Context mContext;
	HashMap<String, Object> yearhashMap;

	public ImageGalleryAdapter(Context mContext) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
	}
	public ImageGalleryAdapter(Context mContext,
			HashMap<String, Object> yearhashMap) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.yearhashMap = yearhashMap;
	}
	@Override
	public int getCount() {
		return this.yearhashMap.size();// 3imageUrls.length;
	}
	@Override
	public Object getItem(int position) {
		return position;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// View view = (ImageView) convertView;
		// if (view == null) {
		// view = (View)
		// LayoutInflater.from(mContext).inflate(R.layout.gridviewitem, parent,
		// false);
		// }
		// //imageLoader.displayImage(imageUrls[position], imageView, options);
		// ImageView imageView = (ImageView)
		// view.findViewById(R.id.CoverImageView);
		// imageView.setImageResource(R.drawable.ic_stub);
		// // if(selectItem==position){
		// // imageView.setLayoutParams(new Gallery.LayoutParams(105,120));
		// // }
		// // else{
		// // imageView.setLayoutParams(new Gallery.LayoutParams(75,90));
		// // }
		//
		// return view;
		ViewHolderTL viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.year_gallery_viewitem, null);
			viewHolder = new ViewHolderTL();
			// viewHolder.galleryitem = (ImageView)
			// convertView.findViewById(R.id.CoverImageView);
			viewHolder.textView = (TextView) convertView
					.findViewById(R.id.year_gallry);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolderTL) convertView.getTag();
		}
		// viewHolder.galleryitem.setImageResource(R.drawable.ic_stub);
		viewHolder.textView.setText(((String) this.yearhashMap.keySet()
				.toArray()[position]));
		return convertView;
		// ImageView imageView = (ImageView) convertView;
		// if (imageView == null) {
		// LayoutInflater inflater = (LayoutInflater)
		// mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// imageView = (ImageView) inflater.inflate(R.layout.item_gallery_image,
		// parent, false);
		// }
		// //imageLoader.displayImage(imageUrls[position], imageView, options);
		// imageView.setImageResource(R.drawable.ic_stub);
		// if(selectItem==position){
		// imageView.setLayoutParams(new Gallery.LayoutParams(105,120));
		// }
		// else{
		// imageView.setLayoutParams(new Gallery.LayoutParams(75,90));
		// }
		//
		// return imageView;
	}

	static class ViewHolderTL {
		public ImageView galleryitem;
		public TextView textView;
	}

	public void setSelectItem(int selectItem) {
		if (this.selectItem != selectItem) {
			this.selectItem = selectItem;
			notifyDataSetChanged();
		}
	}
}
