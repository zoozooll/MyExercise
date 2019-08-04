/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.notification.view;

import java.util.List;

import android.content.Context;

import com.oregonscientific.meep.notification.Notification;
import com.oregonscientific.meep.notification.R;

public class NewsMessageAdapter extends MessageAdapter {
	
	public NewsMessageAdapter(Context context) {
		this(context, null);
	}
	
	public NewsMessageAdapter(Context context, List<Notification> objects) {
		super(context, objects);
	}
	
	@Override
	public int getLayoutId(int position) {
		int i = position % 2;
		if (i == 0) {
			return R.layout.news_message_list_item_even;
		} else {
			return R.layout.news_message_list_item_odd;
		}
	}
	
	@Override
	public MessageBoxItem getMessageBoxItem() {
		return new NewsMessageBoxItem(getContext());
	}
	
	@Override
	public int getViewTypeCount() {
		// Although the view for alternate items are different, they are of the
		// same type. Therefore, we return 1
		return 1;
	}

}
