package com.mogoo.components.ad;

import java.util.List;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * 双行展示的VIEW
 *
 * @author Administrator
 *
 */
class DoubleRowView extends LinearLayout
{
	private static final String tag = "DoubleRowView";
	private Context mContext;
	private MogooImageView[] mogooAdPic;
	
	public DoubleRowView(Context context)
	{
		super(context);
		this.mContext = context;
		initView();
	}

	private void initView()
	{

		mogooAdPic = new MogooImageView[4];
		for (int i = 0; i < 4; i++)
		{
			mogooAdPic[i] = new MogooImageView(mContext);
			// 设置默认图片
			if (MogooInfo.DEFAULT_AD_PIC_RES_ID > 0)
			{
				mogooAdPic[i].setDefaultImage(MogooInfo.DEFAULT_AD_PIC_RES_ID);
			}
		}
		LayoutParams myparams = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		this.setLayoutParams(myparams);
		this.setOrientation(LinearLayout.VERTICAL);

		LinearLayout row1 = new LinearLayout(mContext);
		LayoutParams myparams1 = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		myparams1.weight = 1;
		row1.setLayoutParams(myparams1);

		LinearLayout row2 = new LinearLayout(mContext);
		LayoutParams myparams2 = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		myparams2.weight = 1;
		row2.setLayoutParams(myparams2);

		LayoutParams piv1 = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		piv1.weight = 1;
		piv1.rightMargin = MogooInfo.AD_PADING;
		piv1.bottomMargin = MogooInfo.AD_PADING;
		mogooAdPic[0].setLayoutParams(piv1);
		row1.addView(mogooAdPic[0]);

		LayoutParams piv2 = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		piv2.weight = 1;
		piv2.bottomMargin = MogooInfo.AD_PADING;
		mogooAdPic[1].setLayoutParams(piv2);
		row1.addView(mogooAdPic[1]);

		LayoutParams piv3 = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		piv3.weight = 1;
		piv3.rightMargin = MogooInfo.AD_PADING;
		mogooAdPic[2].setLayoutParams(piv3);
		row2.addView(mogooAdPic[2]);

		LayoutParams piv4 = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		piv4.weight = 1;
		mogooAdPic[3].setLayoutParams(piv4);
		row2.addView(mogooAdPic[3]);

		this.addView(row1);
		this.addView(row2);
	}

	void setAdOnClickListener(AdOnClickListener listener)
	{
		if (listener != null)
			for (int i = 0; i < 4; i++)
			{
				mogooAdPic[i].setAdOnClickListener(listener);
			}
	}

	void setAdData(List<AdPositionItem> adPositionItemList)
	{
		if (adPositionItemList == null)
			return;
		int size = adPositionItemList.size();
		if (size == 0)
			return;

		AdvertiseItem advertiseItem;
		AdPositionItem adPositionItem = null;
		List<AdvertiseItem> advertiseItemList;
		// MogooImageView adImage = null;
		MogooInfo.Log(tag, "setAdData().size:" + size);

		for (int i = 0; i < size; i++)
		{
			if (i < mogooAdPic.length)
			{
				adPositionItem = adPositionItemList.get(i);
				advertiseItemList = adPositionItem.getAdvertiseItemList();
				if (advertiseItemList != null)
				{
					advertiseItem = advertiseItemList.get(0);
					// adImage = mogooAdPic[i];
					// adImage.setAdvertiseItem(advertiseItem);
					mogooAdPic[i].setAdvertiseItem(advertiseItem);

				}

			}
		}

	}

	void refreshImageViews() {
		if (mogooAdPic == null)
			return;
		int size = mogooAdPic.length;

		for (int i = 0; i < size; i++) {
			mogooAdPic[i].refreshImageView();
		}
	}
}
