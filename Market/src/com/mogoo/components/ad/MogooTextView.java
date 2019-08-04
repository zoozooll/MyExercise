package com.mogoo.components.ad;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

class MogooTextView extends TextView
{

	private static final String tag = "MogooTextView";
	private AdOnClickListener mListener;
	private AdvertiseItem mItem;

	public MogooTextView(Context context)
	{
		super(context);
		setTextSize(MogooInfo.txtSize);
		setTextColor(MogooInfo.textColor);
		setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
	}

	void setAdvertiseItem(AdvertiseItem item)
	{
		this.mItem = item;
		// this.setText(item.getImgurl());
		this.setText(item.getTitle());
	}

	void setAdOnClickListener(AdOnClickListener listener)
	{
		this.mListener = listener;

		// 设置点击响应处理
		this.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (mListener != null)
				{
					MogooInfo.Log(tag, "mListener != null.....");
					mListener.OnClick(mItem);
				}

			}
		});
	}

}
