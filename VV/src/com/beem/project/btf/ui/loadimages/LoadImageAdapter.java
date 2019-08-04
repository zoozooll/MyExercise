package com.beem.project.btf.ui.loadimages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.beem.project.btf.R;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.manager.ImageFolderItemManager;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.butterfly.vv.camera.base.ImageFolderInfo;
import com.butterfly.vv.camera.base.ImageInfoHolder;
import com.butterfly.vv.camera.displayimage.PhotoFolderImageView;
import com.butterfly.vv.camera.displayimage.PhotoFolderImageView.OnMeasureListener;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

import de.greenrobot.event.EventBus;

public class LoadImageAdapter extends BaseAdapter {
	private Point mPoint = new Point(0, 0);// 用来封装ImageView的宽和高的对象
	/**
	 * 用来存储图片的选中情况
	 */
	private Context context;
	private HashMap<Integer, Boolean> mSelectMap = new HashMap<Integer, Boolean>();
	// private Gallery mGalleryView;
	private List<String> list;
	protected LayoutInflater mInflater;
	private int loadingSize;
	/**
	 * 获取gridview图片的路径
	 */
//	List<String> listImagePath = new ArrayList<String>();
	// 保存图片详细信息
	public List<ImageInfoHolder> mImageInfoList = new ArrayList<ImageInfoHolder>();
	ImageFolderInfo mFolderInfo;

	public LoadImageAdapter(Context context, List<String> list/*, Gallery mGalleryView*/) {
		this.list = list;
		//this.mGalleryView = mGalleryView;
		mInflater = LayoutInflater.from(context);
	}
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
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		String path = list.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.load_image_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (PhotoFolderImageView) convertView
					.findViewById(R.id.load_image);
			viewHolder.mCheckBox = (ImageView) convertView
					.findViewById(R.id.image_checkbox);
			// 用来监听ImageView的宽和高
			viewHolder.mImageView.setOnMeasureListener(new OnMeasureListener() {
				@Override
				public void onMeasureSize(int width, int height) {
					// TODO Auto-generated method stub
					mPoint.set(width, height);
				}
			});
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.mImageView
					.setImageResource(R.drawable.deafult_imgloading);
		}
		viewHolder.mImageView.setTag(path);
		/*viewHolder.mCheckBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 如果是未选中的CheckBox,则添加动画
				if (!mSelectMap.containsKey(position)
						|| !mSelectMap.get(position)) {
					addAnimation(viewHolder.mCheckBox);
				}
				boolean isChecked = ((CheckBox) v).isChecked();
				mSelectMap.put(position, isChecked);
				// 添加或删除图片地址
				String listpath = list.get(position);
				if (isChecked && !listImagePath.contains(listpath)) {
					listImagePath.add(listpath);
				} else if (!isChecked && listImagePath.contains(listpath)) {
					listImagePath.remove(listpath);
				}
				int size = listImagePath.size();
				if (isChecked) {
					// 记录勾选图片路径
					int allCount = Constants.uploadpicMaxNum;
					ImageFolder todayFolder = ImageFolderItemManager
							.getInstance().getImageFolderNow(
									LoginManager.getInstance().getJidParsed());
					if (todayFolder != null) {
						allCount -= todayFolder.getPhotoCount();
					}
					if (size > allCount) {
						// Toast.makeText(context, R.string.showimage_uploadCountFull,
						// Toast.LENGTH_SHORT).show();
						listImagePath.remove(listpath);
						((CheckBox) v).setChecked(false);
						mSelectMap.put(position, false);
						EventBus.getDefault().post(
								new EventBusData(
										EventAction.LoadImageNumberChanged,
										Integer.valueOf(-1)));
						return;
					}
				}
				// 发送选中数量更改消息
				if (size != loadingSize) {
					loadingSize = size;
					EventBus.getDefault().post(
							new EventBusData(
									EventAction.LoadImageNumberChanged, Integer
											.valueOf(size)));
				}
			}
		});*/
		boolean selected = mSelectMap.containsKey(position) ? mSelectMap
				.get(position) : false;
		setImageViewSelected(viewHolder.mCheckBox, selected);
//		viewHolder.mCheckBox.setSelected(selected);
		ImageLoader.getInstance().displayImage(Scheme.FILE.wrap(path),
				viewHolder.mImageView);
		return convertView;
	}
	/**
	 * 给CheckBox加点击动画，利用开源库nineoldandroids设置动画
	 * @param view
	 */
	private void addAnimation(View view) {
		float[] vaules = new float[] { 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f,
				1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f };
		AnimatorSet set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
				ObjectAnimator.ofFloat(view, "scaleY", vaules));
		set.setDuration(150);
		set.start();
	}
	/**
	 * 获取选中的Item的position
	 * @return
	 */
	public ArrayList<String> getSelectItems() {
		ArrayList<String> list = new ArrayList<String>();
		for (Iterator<Map.Entry<Integer, Boolean>> it = mSelectMap.entrySet()
				.iterator(); it.hasNext();) {
			Map.Entry<Integer, Boolean> entry = it.next();
			if (entry.getValue()) {
				list.add(this.list.get(entry.getKey()));
			}
		}
		return list;
	}
	
	public void setSelectItem(int position) {
		if (Boolean.TRUE.equals(mSelectMap.get(position))) {
			mSelectMap.remove(position);
		} else {
			mSelectMap.put(position, true);
		}
	}
	
	public void unSelectItem(int position) {
		mSelectMap.remove(position);
	}
	
	public void setImageViewSelected(ImageView selectionView, boolean selected) {
		if (selected) {
			selectionView.setImageResource(R.drawable.checkbox_bg_checked);
		} else {
			selectionView.setImageResource(R.drawable.checkbox_bg_normal);
		}
	}

	private static class ViewHolder {
		private PhotoFolderImageView mImageView;
		private ImageView mCheckBox;
	}

	// public ArrayList<ImageViewHolder> getViewHolderList() {
	// return viewHolderList;
	// }
	// public void setListImagePath(List<String> listImagePath) {
	// this.listImagePath = listImagePath;
	// }
	// public HashMap<Integer, Boolean> getmSelectMap() {
	// return mSelectMap;
	// }
	// public List<ImageInfoHolder> getmImageInfoList() {
	// return mImageInfoList;
	// }
}
