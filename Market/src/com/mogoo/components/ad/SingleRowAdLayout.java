package com.mogoo.components.ad;

import java.util.List;

import com.mogoo.components.ad.utils.AdapterDisplay;
import com.mogoo.components.ad.utils.MogooAnimation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 单行，轮播，不可滑动
 * 
 * @author Administrator
 * 
 */
class SingleRowAdLayout extends MogooLayoutParent {
	private static final String tag = "SingleRowAdLayout";
	// 显示文本
	private static final int showTextView = 0;
	// 显示图片
	private static final int showImageView = 1;
	private MogooTextView mTv;
	private BitmapDrawable localDrawable;
	private static final String defaultValues = "Loading...";
	private List<AdvertiseItem> mList;
	private AdvertiseItem adItem;
	private int refresh = 0;
	private Bitmap bmDrawable;
	private LayoutParams mLayoutParams = new LayoutParams(
			LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

	private void Log(String str) {
		MogooInfo.Log(tag, str);
	}

	public SingleRowAdLayout(Context context, LayoutParams localLayoutParams) {
		super(context, localLayoutParams);
		loadDefault();
	}

	private void setBg() {
  
		
		
		try {
			if (bmDrawable != null)
				bmDrawable.recycle();
			localDrawable = null;
		} catch (Exception  e) {
			Log(e.getMessage());
		}

		bmDrawable = ADPanel.panelDrawable(AdapterDisplay.getWidth(mContext),
				AdapterDisplay.getSingleRowAdHeight(mContext),
				MogooInfo.backgroundColor, MogooInfo.backgroundTransparent);
		if (bmDrawable != null) {
			localDrawable = new BitmapDrawable(bmDrawable);
			this.setBackgroundDrawable(localDrawable);

			/**
			 * @author gaolong 进行释放相关资源，是此处造成内存溢出
			 */
			// bmDrawable.recycle();
			// localDrawable = null;

		}
	}

	// 显示默认值
	private void loadDefault() {
		// 如果有设置默认图片的话，显示默认图片，否则显示文本
		if (MogooInfo.DEFAULT_AD_PIC_RES_ID > 0) {

			ImageView mImageView = new ImageView(mContext);
			mImageView.setBackgroundResource(MogooInfo.DEFAULT_AD_PIC_RES_ID);
			addView(mImageView, mLayoutParams);
		} else {
			setBg();

			TextView mTextView = new TextView(mContext);
			mTextView.setText(defaultValues);
			// 这里字体大小改为MogooInfo.txtSize,字体大小请休息MogooInfo.txtSize
			// mTextView.setTextSize(18);
			mTextView.setTextSize(MogooInfo.txtSize);
			mTextView.setTextColor(Color.WHITE);
			mTextView.setGravity(Gravity.CENTER_HORIZONTAL
					| Gravity.CENTER_VERTICAL);
			addView(mTextView, mLayoutParams);
		}
	}

	private void loadTextAdView(AdvertiseItem item) {

		setBg();
		mTv = null;
		mTv = new MogooTextView(mContext);
		mTv.setAdOnClickListener(mListener);
		mTv.setAdvertiseItem(item);
		addView(mTv, mLayoutParams);
	}

	private void loadImageAdView(AdvertiseItem item) {

		MogooImageView img = new MogooImageView(mContext);
		if (MogooInfo.DEFAULT_AD_PIC_RES_ID > 0)
		{
			img.setDefaultImage(MogooInfo.DEFAULT_AD_PIC_RES_ID);
		}
		img.setAdOnClickListener(mListener);
		img.setAdvertiseItem(item);
		addView(img, mLayoutParams);
	}

	public void updateUi() {
		Log("刷新!!!!!!!!!+++");

		int size = AdDataCache.adPositionItemList.size();

		if (size > 0) {
			AdPositionItem item = AdDataCache.adPositionItemList.get(0);

			if (item.getChangeType() > 0) {
				if (MogooInfo.animation == null) {
					MogooInfo.animation = MogooAnimation.getAnimation(mContext,
							item.getChangeType());
				}

				if (MogooInfo.refreshTime == -1) {
					MogooInfo.refreshTime = item.getChangeTime();
					startTimerUpdateUi();
				}

			}

			mList = item.getAdvertiseItemList();
			Log("item.getChangeType():" + item.getChangeType());
			Log("InitInfo.refreshTime:" + MogooInfo.refreshTime);
			Log("AdDataCache.adPositionItemList.size():" + size);
		}

		if (mList == null)
			return;

		Log("list.size():" + mList.size());

		/**
		 * @author gaolong 移除对象时候，并没有释放imagevie对象引用的图片资源，所以造成内存溢出
		 */
		removeAllViews();

		if (MogooInfo.animation != null) {
			startAnimation(MogooInfo.animation);
		} else {
			startAnimation(MogooAnimation.Left2Right(mContext));
		}

		if (mList.size() > 0 && refresh < mList.size()) {
			adItem = null;
			adItem = mList.get(refresh);
			int showType = adItem.getShowType();
			switch (showType) {
			case showTextView:
				loadTextAdView(adItem);
				break;
			case showImageView:
				loadImageAdView(adItem);
				break;

			default:
				Log("未知的显示类型showType:" + showType);
				break;
			}

		}

		if (refresh < mList.size() - 1) {

			refresh++;
		} else {
			refresh = 0;
		}

		invalidate();
	}

}
