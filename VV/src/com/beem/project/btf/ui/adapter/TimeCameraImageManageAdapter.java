package com.beem.project.btf.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import com.beem.project.btf.R;
import com.beem.project.btf.ui.adapter.CommonViewHolder;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.TimeCameraImageInfo;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.nostra13.universalimageloader.core.ImageLoader;
import de.greenrobot.event.EventBus;

/**
 * @ClassName: TimeCameraImageManageAdapter
 * @Description: 时光相机素材管理adapter
 * @author: yuedong bao
 * @date: 2015-11-16 下午2:47:31
 */
public class TimeCameraImageManageAdapter extends
		BaseCameraImageManageAdapter<TimeCameraImageInfo> {
	public TimeCameraImageManageAdapter(List<TimeCameraImageInfo> ImageInfos,
			Context context) {
		super(ImageInfos, context);
	}
	@Override
	public void setViewClickListener(int position, CommonViewHolder holder,
			final TimeCameraImageInfo imageinfo) {
		holder.getView(R.id.delete).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imageinfo.setField(DBKey.isDownloaded, false);
				imageinfo.saveToDatabase();
				if (Integer.parseInt(imageinfo.getLaycount()) == 2) {
					ImageLoader.getInstance().getMemoryCache()
							.remove(imageinfo.getLaypath1());
					ImageLoader.getInstance().getMemoryCache()
							.remove(imageinfo.getLaypath2());
					ImageLoader.getInstance().getDiskCache()
							.remove(imageinfo.getLaypath1());
					ImageLoader.getInstance().getDiskCache()
							.remove(imageinfo.getLaypath2());
				} else if (Integer.parseInt(imageinfo.getLaycount()) == 3) {
					ImageLoader.getInstance().getMemoryCache()
							.remove(imageinfo.getLaypath1());
					ImageLoader.getInstance().getMemoryCache()
							.remove(imageinfo.getLaypath2());
					ImageLoader.getInstance().getMemoryCache()
							.remove(imageinfo.getLaypath3());
					ImageLoader.getInstance().getDiskCache()
							.remove(imageinfo.getLaypath1());
					ImageLoader.getInstance().getDiskCache()
							.remove(imageinfo.getLaypath2());
					ImageLoader.getInstance().getDiskCache()
							.remove(imageinfo.getLaypath3());
				}
				List<TimeCameraImageInfo> ImageInfos = TimeCameraImageInfo
						.queryWebDownload(imageinfo.getGroupid());
				setItem(ImageInfos);
				// 使用eventbus发送消息(类似广播)
				EventBus.getDefault().post(
						new EventBusData(EventAction.ImageDelete, imageinfo
								.getGroupid()));
			}
		});
	}
}
