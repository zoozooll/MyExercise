package com.butterfly.vv.camera.renew;

import java.text.ParseException;
import java.util.Date;

import com.butterfly.vv.camera.MyDateFormat;
import com.beem.project.btf.R;
import com.butterfly.vv.camera.base.CameraBaseListElement;
import com.butterfly.vv.camera.base.ImageInfoHolder;
import com.butterfly.vv.camera.base.LocationUtil;
import com.butterfly.vv.camera.base.PhotoChoiceChangeListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CameraCell implements CameraBaseListElement {
	private String TAG = "CameraCellTag";
	public ImageInfoHolder mImageInfo = null;
	private CameraCellViewHolder mCameraCellHolder = null;
	private PhotoChoiceChangeListener mChoiceListener;
	private Context mContext;
	private Bitmap mTempBm;

	public CameraCell(Context context, ImageInfoHolder holder) {
		mContext = context;
		mImageInfo = holder;
		mTempBm = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.xc_photo_temp_img);
		// getDatePlaceString();
	}
	@Override
	public int getLayoutId() {
		return R.layout.xc_list_item;
	}
	@Override
	public boolean isClickable() {
		return false;
	}
	public void setPhotoChoiceChangeListener(PhotoChoiceChangeListener listener) {
		mChoiceListener = listener;
	}
	@Override
	public View getViewForElement(final int position,
			LayoutInflater layoutInflater, Context context, View view) {
		Log.i(TAG, "position = " + position + ", view is null?"
				+ (view == null));
		if (view == null) {
			view = layoutInflater.inflate(getLayoutId(), null);
			mCameraCellHolder = new CameraCellViewHolder();
			setCellHolder(mCameraCellHolder, view, position);
			view.setTag(mCameraCellHolder);
		} else {
			mCameraCellHolder = (CameraCellViewHolder) view.getTag();
			setCellHolder(mCameraCellHolder, view, position);
		}
		boolean checkStatus = ListViewAdapter.getItemCheckedStatus(position);
		mCameraCellHolder.choiceTag.setChecked(checkStatus);
		mCameraCellHolder.choiceTag.setClickable(false);
		mCameraCellHolder.itemInfoLayout
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						boolean oldStatus = ListViewAdapter
								.getItemCheckedStatus(position);
						Log.d(TAG, "choiceTag onClick, position = " + position
								+ ", oldStatus = " + oldStatus);
						boolean newStatus = !oldStatus;
						mCameraCellHolder.choiceTag.setChecked(newStatus);
						ListViewAdapter.setItemCheckedStatus(position,
								newStatus);
						if (mChoiceListener != null) {
							mChoiceListener.onPhotoChoiceChange();
						}
					}
				});
		return view;
	}
	private void setCellHolder(CameraCellViewHolder cellHolder, View view,
			int position) {
		cellHolder.imageHolder = mImageInfo;
		cellHolder.listImageView = (ImageView) view
				.findViewById(R.id.list_latest_img);
		cellHolder.listImageView.setImageBitmap(getCellBm(position));
		cellHolder.itemInfoLayout = (RelativeLayout) view
				.findViewById(R.id.list_item_info_layout);
		cellHolder.watchTag = (ImageView) view.findViewById(R.id.watchtag);
		cellHolder.timeTxt = (TextView) view.findViewById(R.id.timetxt);
		if (cellHolder.imageHolder.mLatestTime == null) {
			cellHolder.timeTxt.setText(R.string.str_detail_unknown);
		} else {
			String strTime = getTimeDurationString(cellHolder.imageHolder.mLatestTime);
			if (strTime != null) {
				cellHolder.timeTxt.setText(strTime);
			} else {
				cellHolder.timeTxt.setText(R.string.str_detail_unknown);
			}
		}
		cellHolder.placeTag = (ImageView) view.findViewById(R.id.placetag);
		cellHolder.placeTxt = (TextView) view.findViewById(R.id.placetxt);
		if (cellHolder.imageHolder.mPhotoCity == null) {
			cellHolder.placeTag.setVisibility(View.GONE);
			cellHolder.placeTxt.setVisibility(View.GONE);
		} else {
			cellHolder.placeTag.setVisibility(View.VISIBLE);
			cellHolder.placeTxt.setVisibility(View.VISIBLE);
			cellHolder.placeTxt.setText(cellHolder.imageHolder.mPhotoCity);
		}
		cellHolder.choiceTag = (CheckBox) view.findViewById(R.id.choicetag);
	}
	private String getTimeDurationString(String dateTime) {
		String timeString = null;
		Date dt = null;
		try {
			dt = MyDateFormat.getDateByString(dateTime, "yyyy:MM:dd HH:mm:ss");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long passMills = 0;
		if (dt != null) {
			passMills = System.currentTimeMillis() - dt.getTime();
		}
		timeString = MyDateFormat.getIntervalTimeFromMillis(passMills);
		return timeString;
	}
	public Bitmap getCellBm(int position) {
		Bitmap bm = null;
		if (ListViewAdapter.mBmList != null
				&& ListViewAdapter.mBmList.size() > position) {
			bm = ListViewAdapter.mBmList.get(position);
		} else {
			bm = mTempBm;
		}
		return bm;
	}
	private void getDatePlaceString() {
		if (mImageInfo.mLongitude == null || mImageInfo.mLatitude == null) {
			mImageInfo.mPhotoCity = null;
		} else {
			if (mContext != null && mImageInfo.mLatitude.length() > 0
					&& mImageInfo.mLongitude.length() > 0) {
				LocationUtil locUtil = new LocationUtil(mContext);
				mImageInfo.mPhotoCity = locUtil.getLocationCityByNormalLatLon(
						mImageInfo.mLatitude, mImageInfo.mLongitude);
			}
		}
	}
}
