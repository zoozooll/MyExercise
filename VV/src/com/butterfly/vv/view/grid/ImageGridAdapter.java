package com.butterfly.vv.view.grid;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.activity.ContactInfoActivity.ContactFrgmtStatus;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ImageGridAdapter extends BaseAdapter
		implements
		com.butterfly.vv.view.grid.OptimizeGridView.OptimizeGridAdapter<VVImage> {
	private List<VVImage> mItems;
	private Context mContext;
	private DisplayImageOptions[] options = new DisplayImageOptions[] {
			new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.deafult_imgloading)
					.showImageForEmptyUri(R.drawable.default_headw_selector)
					.showImageOnFail(R.drawable.deafult_imgloading)
					.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
					.cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
					.build(),
			new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.deafult_imgloading)
					.showImageForEmptyUri(R.drawable.default_head_selector)
					.showImageOnFail(R.drawable.deafult_imgloading)
					.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
					.cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
					.build() };
	private int count;
	private int maxCount;
	private ContactFrgmtStatus mstatus = ContactFrgmtStatus.CheckContactInfo;
	private String sex = "1";

	public ImageGridAdapter(Activity context, List<VVImage> mItems,
			ContactFrgmtStatus status, int... pMaxCount) {
		mContext = context;
		this.mItems = mItems;
		this.maxCount = pMaxCount.length > 0 ? pMaxCount[0] : Integer.MAX_VALUE;
		this.mstatus = status;
		reCount();
	}
	@Override
	public int getCount() {
		return count;
	}
	@Override
	public VVImage getItem(int position) {
		return mItems.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.image = (ImageView) convertView.findViewById(R.id.icon);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.position = position;
		setupView(position, convertView, viewHolder);
		return convertView;
	}
	private void setupView(final int position, View convertView,
			final ViewHolder viewHolder) {
		viewHolder.image.setVisibility(View.VISIBLE);
		ImageLoader.getInstance().cancelDisplayTask(viewHolder.image);
		if (mstatus == ContactFrgmtStatus.EditContactInfo) {
			// 编辑界面
			if (position == (mItems.size())) {
				viewHolder.image.setImageBitmap(null);
				viewHolder.image
						.setImageResource(R.drawable.addcontacts2_selector);
			} else {
				ImageLoader.getInstance().displayImage(
						mItems.get(position).getPathThumb(), viewHolder.image,
						options[Integer.parseInt(sex)]);
			}
		} else {
			// 查看界面
			ImageLoader.getInstance().displayImage(
					mItems.get(position).getPathThumb(), viewHolder.image,
					options[Integer.parseInt(sex)]);
		}
	}

	private class ViewHolder {
		ImageView image;
		int position;
	}

	public static VVImage NULL_ITEM = new VVImage();

	@Override
	public List<VVImage> getItems() {
		return mItems;
	}
	@Override
	public void setItems(List<VVImage> items) {
		mItems = items;
		reCount();
	}
	@Override
	public VVImage getNullItem() {
		return NULL_ITEM;
	}
	@Override
	public boolean isNullItem(VVImage item) {
		return item == NULL_ITEM;
	}
	public void setStatus(ContactFrgmtStatus status) {
		this.mstatus = status;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public boolean removeItem(int position) {
		if (mItems.remove(position) != null) {
			reCount();
			return true;
		}
		return false;
	}
	public void reCount() {
		count = mstatus == ContactFrgmtStatus.EditContactInfo ? Math.min(
				mItems.size() + 1, maxCount) : Math
				.min(mItems.size(), maxCount);
	}
	public void addItem(int pos, VVImage vvImage) {
		mItems.add(pos, vvImage);
		reCount();
	}
	public void setItem(int pos, VVImage vvImage) {
		mItems.set(pos, vvImage);
		reCount();
	}
	public void addItem(VVImage vvImage) {
		mItems.add(vvImage);
		reCount();
	}
	public void clearItems() {
		mItems.clear();
		reCount();
	}
	public void addItems(List<VVImage> vvImages) {
		mItems.addAll(vvImages);
		reCount();
	}
}
