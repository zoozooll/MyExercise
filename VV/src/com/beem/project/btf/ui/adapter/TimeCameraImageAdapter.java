package com.beem.project.btf.ui.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.adapter.CommonViewHolder;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.TimeCameraImageInfo;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import de.greenrobot.event.EventBus;

/**
 * @ClassName: TimeCameraImageAdapter
 * @Description: 时光相机素材展示adapter
 * @author: yuedong bao
 * @date: 2015-11-16 下午2:48:11
 */
public class TimeCameraImageAdapter extends
		BaseCameraImageAdapter<TimeCameraImageInfo> {
	public TimeCameraImageAdapter(List<TimeCameraImageInfo> ImageInfos,
			Context context) {
		super(ImageInfos, context);
	}
	@Override
	public void setViewClickListener(int position,
			final CommonViewHolder holder, final TimeCameraImageInfo imageinfo) {
		holder.getConvertView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (imageinfo.isDownloaded()) {
					return;
				}
				ImageLoader.getInstance().loadImage(imageinfo.getLaypath1(),
						null, defaultOptions, new ImageLoadingListener() {
							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
								holder.setVisibility(R.id.progressbar,
										View.VISIBLE);
							}
							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
							}
							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								imageinfo.setField(DBKey.isDownloaded, true);
								imageinfo.saveToDatabase();
								EventBus.getDefault().post(
										new EventBusData(EventAction.ImageAdd,
												imageinfo.getGroupid()));
								holder.setVisibility(R.id.progressbar,
										View.GONE);
								holder.setVisibility(R.id.load_status,
										View.VISIBLE);
							}
							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {
							}
						}, new ImageLoadingProgressListener() {
							@Override
							public void onProgressUpdate(String imageUri,
									View view, int current, int total) {
								((ProgressBar) holder.getView(R.id.progressbar))
										.setProgress(current * 100 / total);
							}
						});
				ImageLoader.getInstance().loadImage(imageinfo.getLaypath2(),
						defaultOptions, null);
				if (Integer.parseInt(imageinfo.getLaycount()) == 3) {
					ImageLoader.getInstance().loadImage(
							imageinfo.getLaypath3(), defaultOptions, null);
				}
				ImageLoader.getInstance().loadImage(imageinfo.getPose(),
						defaultOptions, null);
			}
		});
	}
}
