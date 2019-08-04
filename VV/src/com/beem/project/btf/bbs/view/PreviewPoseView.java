package com.beem.project.btf.bbs.view;

import com.beem.project.btf.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PreviewPoseView {
	private Context mContext;
	private View mView;
	private ImageView bg1, bg2, bg3;
	private View preview_close;
	private DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.deafult_imgloading)
			.showImageForEmptyUri(R.drawable.deafult_imgloading)
			.showImageOnFail(R.drawable.deafult_imgloading).cacheInMemory(true)
			.cacheOnDisk(true).build();

	public PreviewPoseView(Context mContext, String[] urls) {
		this.mContext = mContext;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.previewpose_layout, null);
		bg1 = (ImageView) mView.findViewById(R.id.layout_bg);
		bg2 = (ImageView) mView.findViewById(R.id.layout_bg2);
		bg3 = (ImageView) mView.findViewById(R.id.layout_bg3);
		preview_close = mView.findViewById(R.id.preview_close);
		preview_close.setVisibility(View.GONE);
		if (urls.length == 2) {
			bg3.setVisibility(View.GONE);
			ImageLoader.getInstance().displayImage(urls[0], bg1,
					defaultOptions, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub
							super.onLoadingComplete(imageUri, view, loadedImage);
							preview_close.setVisibility(View.VISIBLE);
						}
					});
			ImageLoader.getInstance()
					.displayImage(urls[1], bg2, defaultOptions);
		} else if (urls.length == 3) {
			ImageLoader.getInstance().displayImage(urls[0], bg1,
					defaultOptions, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub
							super.onLoadingComplete(imageUri, view, loadedImage);
							preview_close.setVisibility(View.VISIBLE);
						}
					});
			ImageLoader.getInstance()
					.displayImage(urls[1], bg2, defaultOptions);
			ImageLoader.getInstance()
					.displayImage(urls[2], bg3, defaultOptions);
		} else if (urls.length == 1) {
			bg1.setVisibility(View.VISIBLE);
			bg2.setVisibility(View.GONE);
			bg3.setVisibility(View.GONE);
			preview_close.setVisibility(View.VISIBLE);
			bg1.setImageResource(R.drawable.prepare_publish_icon);
		}
	}
	public void setCloseListener(OnClickListener lis) {
		preview_close.setOnClickListener(lis);
	}
	public View getView() {
		return mView;
	}
}
