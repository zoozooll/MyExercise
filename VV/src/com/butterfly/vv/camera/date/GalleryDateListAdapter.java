/**
 * 
 */
package com.butterfly.vv.camera.date;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.beem.project.btf.R;
import com.beem.project.btf.ui.loadimages.ThumbImageFetcher;
import com.butterfly.vv.camera.CameraActivity;
import com.butterfly.vv.camera.MyDateFormat;
import com.butterfly.vv.camera.base.DateImageHolder;
import com.butterfly.vv.camera.base.ImageInfoHolder;
import com.butterfly.vv.camera.base.PhotoChoiceChangeListener;
import com.butterfly.vv.camera.base.PhotosChooseManager;
import com.butterfly.vv.camera.renew.RenewDetailBaseActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Aaron Lee Created at 上午11:12:37 2015-9-7
 */
public class GalleryDateListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater layoutInflater; // 布局加载器
	//	private List<DateImageHolder> data; 
	private List<Object> data;
	private int itemCount;
	private int colorIndex;
	//	private Collection<String> checkedImagePaths = new HashSet<String>();
	public PhotoChoiceChangeListener mPhotoCheckedListener;
	private boolean mIsMarkMode;
	private static final String TAG = "GalleryDateListAdapter";
	private static final int[] COLORIMAGERESID = { R.drawable.date_clock1, R.drawable.date_clock2,
			R.drawable.date_clock3, R.drawable.date_clock4, R.drawable.date_clock5, R.drawable.date_clock6,
			R.drawable.date_clock7, R.drawable.date_clock8 };
	private PhotosChooseManager chooser;
	private List<Integer> datePositions;

	/**
	 * @param context
	 */
	public GalleryDateListAdapter(Context context) {
		super();
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		chooser = PhotosChooseManager.getInstance();
	}
	public void setData(List<DateImageHolder> dateImages) {
		if (data == null) {
			data = new ArrayList<Object>();
			datePositions = new ArrayList<Integer>(dateImages.size());
		} else {
			data.clear();
			datePositions.clear();
		}
		if (dateImages == null) {
			return;
		}
		for (DateImageHolder h : dateImages) {
			Date dt = null;
			try {
				dt = MyDateFormat.getDateByString(h.mDateString, "yyyy:MM:dd");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			DateTitle dateTitle = new DateTitle(dt, h.mDateImageList.size());
			datePositions.add(data.size());
			data.add(dateTitle);
			int index = 0;
			ImageViewsItem infoItem = null;
			for (ImageInfoHolder info : h.mDateImageList) {
				int j = index % 3;
				if (j == 0) {
					infoItem = new ImageViewsItem();
					infoItem.items = new ImageInfoHolder[3];
					data.add(infoItem);
				}
				if (infoItem != null)
					infoItem.items[j] = info;
				index++;
			}
		}
	}
	/*public Collection<String> getCheckListimagespath() {
		return checkedImagePaths;
	}*/
	public void setPhotoCheckedListener(PhotoChoiceChangeListener l) {
		mPhotoCheckedListener = l;
	}
	public int getMarkedImageCount() {
		return chooser.getChoosedCount();
	}
	public void resetMarkModeStatus() {
		//		checkedImagePaths.clear();
		chooser.exitChooseMode();
		checkAndSetMarkStatus();
		notifyDataSetChanged();
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		if (data != null) {
			return data.size();
		}
		return 0;
	}
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	@Override
	public int getItemViewType(int position) {
		return getItemType(position).ordinal();
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		if (data != null) {
			return data.get(position);
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolderGroup groupHolder = null;
		ViewHolderChild childHolder = null;
		final ItemType type = getItemType(position);
		if (convertView == null) {
			switch (type) {
				case TITLE: {
					groupHolder = new ViewHolderGroup();
					convertView = layoutInflater.inflate(ViewHolderGroup.VIEWID, null);
					groupHolder.clock = (ImageView) convertView.findViewById(R.id.clock);
					groupHolder.month_day = (TextView) convertView.findViewById(R.id.month_day);
					groupHolder.num = (TextView) convertView.findViewById(R.id.num);
					groupHolder.year_place = (TextView) convertView.findViewById(R.id.year_place);
					convertView.setTag(groupHolder);
				}
					break;
				case CHILD: {
					childHolder = new ViewHolderChild();
					convertView = layoutInflater.inflate(ViewHolderChild.VIEWID, null);
					childHolder.layout_ImageItems = new View[3];
					childHolder.layout_ImageItems[0] = convertView.findViewById(R.id.layout_ImageItem0);
					childHolder.layout_ImageItems[1] = convertView.findViewById(R.id.layout_ImageItem1);
					childHolder.layout_ImageItems[2] = convertView.findViewById(R.id.layout_ImageItem2);
					convertView.setTag(childHolder);
				}
					break;
				default:
					break;
			}
		} else {
			switch (type) {
				case TITLE: {
					groupHolder = (ViewHolderGroup) convertView.getTag();
				}
					break;
				case CHILD: {
					childHolder = (ViewHolderChild) convertView.getTag();
				}
					break;
				default:
					break;
			}
		}
		// set data
		switch (type) {
			case TITLE: {
				DateTitle item = (DateTitle) getItem(position);
				groupHolder.year_place
						.setText(context.getString(R.string.timefly_yearSting, item.date.getYear() + 1900));
				groupHolder.month_day.setText(context.getString(R.string.gallery_dateStringFormat,
						item.date.getMonth() + 1, item.date.getDate()));
				groupHolder.num.setText(context.getString(R.string.timefly_picCountSting, item.imagesCount));
				groupHolder.clock.setImageResource(COLORIMAGERESID[colorIndex]);
				colorIndex++;
				if (colorIndex >= COLORIMAGERESID.length) {
					colorIndex = 0;
				}
			}
				break;
			case CHILD: {
				ImageViewsItem item = (ImageViewsItem) getItem(position);
				for (int i = 0; i < 3; i++) {
					ImageView item_image = (ImageView) childHolder.layout_ImageItems[i].findViewById(R.id.item_image);
					//					CheckBox item_check = (CheckBox) childHolder.layout_ImageItems[i].findViewById(R.id.item_check);
					ImageView btn_showDetail = (ImageView) childHolder.layout_ImageItems[i]
							.findViewById(R.id.btn_showDetail);
					ImageView imv_check = (ImageView) childHolder.layout_ImageItems[i].findViewById(R.id.imv_check);
					bindChildView(childHolder.layout_ImageItems[i], item.items[i]);
				}
			}
				break;
			default:
				break;
		}
		return convertView;
	}
	private ItemType getItemType(int position) {
		Object o = getItem(position);
		if (o instanceof DateTitle) {
			return ItemType.TITLE;
		} else if (o instanceof ImageViewsItem) {
			return ItemType.CHILD;
		}
		return ItemType.OTHER;
	}
	private void bindChildView(View parent, final ImageInfoHolder item) {
		if (item != null) {
			parent.setVisibility(View.VISIBLE);
			final ImageView item_image = (ImageView) parent.findViewById(R.id.item_image);
			final ImageView btn_showDetail = (ImageView) parent.findViewById(R.id.btn_showDetail);
			final ImageView imv_check = (ImageView) parent.findViewById(R.id.imv_check);
			//			item_image.setImageResource(R.drawable.friends_sends_pictures_no);
			//			ImageLoaderLocal.getInstance().loadImage(item.mImagePath, item_image);
			/*ImageLoader.getInstance().displayImage(Scheme.FILE.wrap(item.mImagePath), item_image,
					ImageLoaderConfigers.sDefaultGalleryConfig);*/
			//			Log.i(TAG, "setChecked " + position +" " + checkedImagePaths.contains(item.mImagePath) + " " + item.mImagePath);
			ThumbImageFetcher.getInstance(context).loadImage(item.mImagePath, item_image);
			boolean choosed = chooser.isChoosed(item);
			imv_check.setVisibility(choosed ? View.VISIBLE : View.GONE);
			btn_showDetail.setVisibility(choosed ? View.GONE : View.VISIBLE);
			/*item_check.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (((CheckBox) v).isChecked()) {
						// 记录勾选图片路径
						if (!chooser.choosePhoto(item)) {
							Toast.makeText(context, R.string.showimage_uploadCountFull, Toast.LENGTH_SHORT).show();
							((CheckBox) v).setChecked(false);
						}
					} else {
						// 销毁不勾选图片路径
						chooser.unChoose(item);
					}
					// 检测是否有勾选
					checkAndSetMarkStatus();
					if (mPhotoCheckedListener != null) {
						mPhotoCheckedListener.onPhotoChoiceChange();
					}
				}
			});*/
			OnClickListener l = new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (v == item_image) {
						// 记录勾选图片路径
						if (!chooser.choosePhoto(item)) {
							Toast.makeText(context, R.string.showimage_uploadCountFull, Toast.LENGTH_SHORT).show();
						} else {
							imv_check.setVisibility(View.VISIBLE);
							btn_showDetail.setVisibility(View.GONE);
							// 检测是否有勾选
							checkAndSetMarkStatus();
							if (mPhotoCheckedListener != null) {
								mPhotoCheckedListener.onPhotoChoiceChange();
							}
						}
					} else if (v == imv_check) {
						// 销毁不勾选图片路径
						chooser.unChoose(item);
						// 检测是否有勾选
						checkAndSetMarkStatus();
						v.setVisibility(View.GONE);
						btn_showDetail.setVisibility(View.VISIBLE);
						if (mPhotoCheckedListener != null) {
							mPhotoCheckedListener.onPhotoChoiceChange();
						}
					} else if (btn_showDetail == v) {
						String titleString = context.getResources().getString(R.string.tabtitle_photo);
						RenewDetailBaseActivity.setBigImageInfoList(DateGridCellGetData.getInstance(context)
								.getmImageInfoList());
						Intent intent = new Intent(context, RenewDetailBaseActivity.class);
						intent.putExtra(CameraActivity.IMAGE_VIEW_HOLDER_EXTRA, item);
						intent.putExtra(CameraActivity.IMAGE_DETAIL_TITLE_STRING, titleString);
						intent.putExtra(CameraActivity.IMAGE_DETAIL_TYPE,
								RenewDetailBaseActivity.DATE_GRID_BIG_IMAGE_TYPE);
						//intent.putExtra(CameraActivity.IMAGE_DETAIL_COUNT, count);
						CameraActivity activity = (CameraActivity) context;
						intent.putExtra(CameraActivity.CAMERA_GALLERY_TYPE, activity.cameraType);
						activity.startActivityForResult(intent, CameraActivity.REQUESTCODE_ENTER);
					}
				}
			};
			item_image.setOnClickListener(l);
			imv_check.setOnClickListener(l);
			btn_showDetail.setOnClickListener(l);
		} else {
			parent.setVisibility(View.INVISIBLE);
		}
	}
	public void checkAndSetMarkStatus() {
		mIsMarkMode = chooser.isChooseMode();
	}
	public boolean getMarkModeStatus() {
		return mIsMarkMode;
	}
	public int getTopIndexOfDate(int newPos) {
		if (datePositions != null && datePositions.size() > newPos) {
			return datePositions.get(newPos);
		}
		return 0;
	}

	private class ViewHolderGroup {
		private static final int VIEWID = R.layout.listitem_gallery_titleview;
		private ImageView clock;
		private TextView month_day;
		private TextView num;
		private TextView year_place;
	}

	private class ViewHolderChild {
		private static final int VIEWID = R.layout.listitem_gallery_childview;
		private View[] layout_ImageItems;
	}

	private class DateTitle {
		private Date date;
		private int imagesCount;

		public DateTitle(Date date, int imagesCount) {
			super();
			this.date = date;
			this.imagesCount = imagesCount;
		}
	}

	private class ImageViewsItem {
		private ImageInfoHolder[] items;
	}

	private enum ItemType {
		TITLE, CHILD, OTHER
	}
}
