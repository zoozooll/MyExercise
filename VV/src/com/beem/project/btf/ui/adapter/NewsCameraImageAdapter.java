package com.beem.project.btf.ui.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.adapter.CommonViewHolder;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.NewsImageInfo;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.fragment.NewsCameraImageFragement.NewsMaterialType;
import com.beem.project.btf.utils.DimenUtils;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import de.greenrobot.event.EventBus;

/**
 * @ClassName: NewsCameraImageAdapter
 * @Description: 娱乐相机素材适配器
 * @author: yuedong bao
 * @date: 2015-11-16 下午1:42:13
 */
public class NewsCameraImageAdapter extends
		BaseCameraImageAdapter<NewsImageInfo> {
	private NewsMaterialType materiaType;

	public NewsCameraImageAdapter(List<NewsImageInfo> ImageInfos,
			Context context, NewsMaterialType materiaType) {
		super(ImageInfos, context);
		this.materiaType = materiaType;
	}
	@Override
	public void setViewClickListener(int position,
			final CommonViewHolder holder, final NewsImageInfo imageInfo) {
		holder.getConvertView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (imageInfo.isDownloaded()) {
					return;
				}
				//将大图下载下来
				ImageLoader.getInstance().loadImage(imageInfo.getPath(), null,
						defaultOptions, new ImageLoadingListener() {
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
								imageInfo.setField(DBKey.isDownloaded, true);
								imageInfo.saveToDatabase();
								EventBus.getDefault().post(
										new EventBusData(EventAction.ImageAdd,
												imageInfo.getGroupid()));
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
			}
		});
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = super.getView(position, convertView, parent);
			updateConvertViewSize(convertView, parent, position);
		} else {
			convertView = super.getView(position, convertView, parent);
		}
		return convertView;
	}
	public void updateConvertViewSize(final View convertView,
			final ViewGroup parent, final int position) {
		// 精确计算GridView的item高度	
		convertView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						convertView.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						int numColumns = materiaType.getNumColumns();
						int w = (parent.getWidth() - parent.getPaddingLeft()
								- parent.getPaddingRight() - DimenUtils.dip2px(
								parent.getContext(), 10) * (numColumns - 1))
								/ numColumns;
						int h = (int) (materiaType.getConvertViewscale() * w);
						AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
								w, h);
						convertView.setLayoutParams(lp);
					}
				});
	}
}
