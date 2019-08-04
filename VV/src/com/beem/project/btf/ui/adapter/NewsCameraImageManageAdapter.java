package com.beem.project.btf.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.adapter.CommonViewHolder;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.NewsImageInfo;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.fragment.NewsCameraImageFragement.NewsMaterialType;
import com.beem.project.btf.utils.DimenUtils;
import com.beem.project.btf.utils.NewsCameraMaterialUtil;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import de.greenrobot.event.EventBus;

/**
 * @ClassName: TimeCameraImageManageAdapter
 * @Description: 时光相机素材管理adapter
 * @author: yuedong bao
 * @date: 2015-11-16 下午2:47:31
 */
public class NewsCameraImageManageAdapter extends
		BaseCameraImageManageAdapter<NewsImageInfo> {
	private NewsMaterialType materiaType;

	public NewsCameraImageManageAdapter(List<NewsImageInfo> ImageInfos,
			Context context, NewsMaterialType materiaType) {
		super(ImageInfos, context);
		this.materiaType = materiaType;
	}
	@Override
	public void setViewClickListener(int position, CommonViewHolder holder,
			final NewsImageInfo imageinfo) {
		holder.getView(R.id.delete).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imageinfo.setField(DBKey.isDownloaded, false);
				imageinfo.saveToDatabase();
				List<NewsImageInfo> ImageInfos = NewsCameraMaterialUtil
						.queryWebDownloadMaterialTemplate(imageinfo
								.getGroupid());
				setItem(ImageInfos);
				// 使用eventbus发送消息(类似广播)
				EventBus.getDefault().post(
						new EventBusData(EventAction.ImageDelete, imageinfo
								.getGroupid()));
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
