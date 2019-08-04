/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.notification;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;

import com.oregonscientific.meep.notification.internal.NewsService;
import com.oregonscientific.meep.notification.view.MessageAdapter;
import com.oregonscientific.meep.notification.view.NewsMessageAdapter;
import com.oregonscientific.meep.notification.view.OnListItemRemoveListener;

/**
 * The {@link android.app.Activity} that displays news
 */
public class NewsActivity extends NotificationActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Starts the service if it was not already started
		Intent intent = new Intent(this, NewsService.class);
		startService(intent);
	}
	
	@Override
	protected int getLayoutResId() {
		return R.layout.news_main;
	}
	
	@Override
	public String[] getMessageFilter() {
		return new String[] { Notification.KIND_NEWS };
	}
	
	@Override
	protected MessageAdapter getAdapter(List<Notification> notifications) {
		return new NewsMessageAdapter(this, notifications);
	}
	
	@Override
	protected OnListItemRemoveListener getOnListItemRemoveListener() {
		return new OnListItemRemoveListener() {

			@Override
			public void onRemove(long notificationId) {
				removeNotification(notificationId);
			}

			@Override
			public void onRemoveAll() {
				removeNotification(Notification.KIND_NEWS);
			}
			
		};
	}

}
