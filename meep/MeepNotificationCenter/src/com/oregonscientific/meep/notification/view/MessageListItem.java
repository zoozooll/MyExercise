package com.oregonscientific.meep.notification.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.oregonscientific.meep.notification.Notification;
import com.oregonscientific.meep.notification.R;

public class MessageListItem extends RelativeLayout{

	private ImageView mBadgeImageView;
	
	public MessageListItem(Context context, AttributeSet attributeSet, int defStyle) {
		super(context, attributeSet, defStyle);
	}
	
	public MessageListItem(Context context, Notification notification) {
		super(context);
		initLayout(notification);
	}
	
	private void initLayout(Notification notification) {
		View v = View.inflate(getContext(), R.layout.message_list_item, this);
	    mBadgeImageView = (ImageView)v.findViewById(R.id.message_list_item_batch);
	    enableBatch(false);
	}
	
	public void enableBatch(boolean b) {
		mBadgeImageView.setVisibility(b ? View.VISIBLE:View.GONE);
	}

}
