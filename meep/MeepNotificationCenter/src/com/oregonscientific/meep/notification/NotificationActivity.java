/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.notification.view.MessageAdapter;
import com.oregonscientific.meep.notification.view.MessageListView;
import com.oregonscientific.meep.notification.view.OnListItemRemoveListener;

public class NotificationActivity extends Activity{
	
	private final String TAG = getClass().getSimpleName();
	
	public final int MAX_NOTIFICATION_COUNT = 999;
	
	private MessageListView mListView;
	
	private final Vector<Runnable> mFutureTasks = new Vector<Runnable>(); 
	private final Object mLock = new Object();
	private MessageAdapter mAdapter;
	
	private final ExecutorService mExecutor = Executors.newCachedThreadPool();
	protected LocalReceiver mReceiver = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setLayout();
	}
	
	protected void setLayout() {
		setContentView(getLayoutResId());
		
		mListView = (MessageListView) findViewById(R.id.notifications_listview);
		if (mListView != null) {
			mListView.setOnListItemRemoveListener(getOnListItemRemoveListener());
			
			mExecutor.execute(new Runnable() {

				@Override
				public void run() {
					Account account = getAccount();
					// We cannot proceed if there is no account associated with the running context
					if (account == null) {
						return;
					}
					
					List<Notification> notifications = getNotifications(account, getMessageFilter());

					if (notifications == null) {
						notifications = new ArrayList<Notification>();
					}
					
					synchronized (mLock) {
						mAdapter = getAdapter(notifications);
					}
					
					for (Runnable task : mFutureTasks) {
						mExecutor.execute(task);
					}
					
					if (mListView != null) {
						NotificationActivity.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								mListView.setAdapter(mAdapter);
							}
							
						});
						
					}
				}
				
			});
		}
		
		View v = findViewById(R.id.notification_center_remove_all);
		if (v != null) {
			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mListView != null) {
						mListView.removeAllItems();
					}
				}
				
			});
		}
		
	}
	
	/**
	 * Returns the layout resource to be used in this Activity
	 */
	protected int getLayoutResId() {
		return R.layout.activity_main;
	}
	
	/**
	 * Returns the class to be invoked when items in the activity are removed
	 */
	protected OnListItemRemoveListener getOnListItemRemoveListener() {
		return new OnListItemRemoveListener() {

			@Override
			public void onRemove(long notificationId) {
				removeNotification(notificationId);
			}

			@Override
			public void onRemoveAll() {
				removeAllNotification();
			}
		
		};
	}

	/**
	 * Retrieves the message filter
	 * 
	 * @return an array of string the notifications should be filtered 
	 */
	protected String[] getMessageFilter() {
		return new String[] { 
				Notification.KIND_ALERT, 
				Notification.KIND_STORE,
				Notification.KIND_MESSAGE,
				Notification.KIND_WARNING };
	}
	
	/**
	 * Returns a new instance of {@link MessageAdapter} to be associated with the list view
	 * in this {@link android.app.Activity}. Subclasses should override this method to return
	 * the appropriate {@link MessageAdapter}
	 */
	protected MessageAdapter getAdapter(List<Notification> notifications) {
		return new MessageAdapter(this, notifications);
	}
	
	public void startIntent(Intent intent, String action) {
		if (intent == null || action == null) {
			return;
		}
		
		if(action.equalsIgnoreCase(Notification.ACTION_SEND_BROADCAST)) {
			sendBroadcast(intent);
		}else if(action.equalsIgnoreCase(Notification.ACTION_START_ACTIVITY)) {
			startActivity(intent);
		}else if(action.equalsIgnoreCase(Notification.ACTION_START_SERVICE)) {
			startService(intent);
		}
	}
	
	/**
	 * Remove the specify notification
	 * @param notification
	 */
	public void removeNotification(long notificationId) {
		NotificationManager notificationManager = (NotificationManager) ServiceManager.getService(NotificationActivity.this, ServiceManager.NOTIFICATION_SERVICE);
		notificationManager.cancel(notificationId);
	}
	
	/**
	 * Remove all notification without kind of {@link Notification.KIND_NEWS}
	 */
	public void removeAllNotification() {
		mExecutor.execute(new Runnable() {

			@Override
			public void run() {
				Account account = getAccount();
				// We cannot proceed if there is no account associated with the running context
				if (account == null) {
					return;
				}
				
				NotificationManager notificationManager = (NotificationManager) ServiceManager.getService(NotificationActivity.this, ServiceManager.NOTIFICATION_SERVICE);
				String[] filter = getMessageFilter();
				for (String kind : filter) {
					notificationManager.cancelKind(account.getId(), kind);
				}
			}
		});
	}
	
	/**
	 * Remove notifications of the {@code kind} specified  
	 * 
	 * @param kind the kind of notification to cancel
	 */
	public void removeNotification(final String kind) {
		// Quick return if no notification needs to be removed
		if (kind == null) {
			return;
		}
		
		mExecutor.execute(new Runnable() {

			@Override
			public void run() {
				Account account = getAccount();
				// We cannot proceed if there is no account associated with the running context
				if (account == null) {
					return;
				}
				
				NotificationManager notificationManager = (NotificationManager) ServiceManager.getService(NotificationActivity.this, ServiceManager.NOTIFICATION_SERVICE);
				notificationManager.cancelKind(account.getId(), kind);
			}
		});
		
	}

	
	/**
	 * Retrieves all the notifications from {@link com.oregonscientific.meep.notification.NotificationManager}
	 * 
	 * @return a list of {@link com.oregonscientific.meep.notification.Notification}
	 */
	public List<Notification> getNotifications (Account account, final String[] kinds) {
		List<String> filter = Arrays.asList(kinds);
		
		NotificationManager nm = (NotificationManager) ServiceManager.getService(NotificationActivity.this, ServiceManager.NOTIFICATION_SERVICE);
		List<Notification> notifications = nm.retrieveAllBlocking(account.getId()); 
		if (notifications != null) {
			Iterator<Notification> iterator = notifications.iterator();
			while (iterator.hasNext()) {
				Notification n = iterator.next();
				if (!filter.contains(n.kind)) {
					iterator.remove();
				}
			}
		}
		
		return notifications;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		// Unbinds any previously binded service
		ServiceManager.unbindServices(this);
	}
	
	/**
	 * Returns the {@link com.oregonscientific.meep.account.Account} that this context should query from
	 */
	protected Account getAccount() {
		AccountManager am = (AccountManager) ServiceManager.getService(NotificationActivity.this, ServiceManager.ACCOUNT_SERVICE);
		Account account = am.getLastLoggedInAccountBlocking();
		if (account == null) {
			account = am.getDefaultAccount();
		}
		return account;
	}
	
	/**
	 * Called when a notification is posted 
	 * 
	 * @param id the ID of the notification posted
	 */
	protected void onNotificationPosted(final long id) {
		Runnable task = new Runnable() {

			@Override
			public void run() {
				Account account = getAccount();
				// We cannot proceed if there is no account associated with the running context
				if (account == null) {
					return;
				}
				
				Log.d(TAG, "Notification: " + id + " is posted...");
				NotificationManager notificationManager = (NotificationManager) ServiceManager.getService(NotificationActivity.this, ServiceManager.NOTIFICATION_SERVICE);
				final Notification notification = notificationManager.retrieveBlocking(id);
				
				int position = -1;
				boolean containsKind = false;
				synchronized (mLock) {
					List<String> filter = Arrays.asList(getMessageFilter());
					if (filter != null && notification != null && filter.contains(notification.kind)) {
						containsKind = true;
						position = mAdapter.getPosition(notification);
					}
				}
				
				final int pos = position;
				final boolean hasKind = containsKind;
				NotificationActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (hasKind) {
							if (pos == -1) {
								mAdapter.add(notification);
							} else {
								mAdapter.remove(notification);
								mAdapter.insert(notification, pos);
							}
						}
						
						mAdapter.notifyDataSetChanged();
					}
					
				});
			}
			
		};
		
		if (mAdapter == null) {
			mFutureTasks.add(task);
		} else {
			mExecutor.execute(task);
		}
	}
	
	/**
	 * Called when notification(s) are canceled
	 * 
	 * @param info the cause of the cancellation
	 */
	protected void onNotificationCanceled(final CancellationInfo info) {
		// Quick return if the call to this method cannot be serviced
		if (info == null) {
			return;
		}
		
		Runnable task = null;
		
		switch (info.getCause()) {
		case ID:
			task = new Runnable() {

				@Override
				public void run() {
					long id = Long.valueOf(info.getData());
					final int position = mAdapter.getPosition(id);
					
					NotificationActivity.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (position != -1) {
								mAdapter.remove(mAdapter.getItem(position));
								mAdapter.notifyDataSetChanged();
							}
						}
						
					});
				}
				
			};
			break;
			
		case KIND:
		case USER:
			task = new Runnable() {

				@Override
				public void run() {
					Account account = getAccount();
					// We cannot proceed if there is no account associated with the running context
					if (account == null) {
						return;
					}
					
					List<Notification> notifications = getNotifications(account, getMessageFilter());
					final List<Notification> removeList = new ArrayList<Notification>();
					
					for (int i = 0; i < mAdapter.getCount(); i++) {
						int position = notifications.indexOf(mAdapter.getItem(i));
						if (position == -1) {
							removeList.add(mAdapter.getItem(i)); 
						}
					}
					
					NotificationActivity.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							for (Notification n : removeList) {
								mAdapter.remove(n);
							}
							mAdapter.notifyDataSetChanged();
						}
						
					});
				}
				
			};
			break;
			
		case ALL:
			task = new Runnable() {

				@Override
				public void run() {
					NotificationActivity.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							mAdapter.clear();
							mAdapter.notifyDataSetChanged();
						}
						
					});
				}
				
			};
			break;
		}
		
		if (mAdapter == null) {
			mFutureTasks.add(task);
		} else {
			mExecutor.execute(task);
		}
	}
	
	/**
	 * The local receiver that receive intents sent by sendBroadcast(). The handling
	 * of the intents are delegated to the service
	 */
	private final class LocalReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// Delegate intent processing to the service
			if (intent != null && context != null) {
				try {
					String action = intent.getAction();
					if (NotificationManager.NOTIFICATION_POSTED_ACTION.equals(action)) {
						long id = intent.getLongExtra(NotificationManager.EXTRA_IDENTIFIER, 0);
						onNotificationPosted(id);
					} else if (NotificationManager.NOTIFICATION_CANCELED_ACTION.equals(action)) {
						CancellationInfo info = intent.getParcelableExtra(NotificationManager.EXTRA_CAUSE);
						onNotificationCanceled(info);
					}
				} catch (Exception ex) {
					// Ignored
					Log.e(TAG, "Receiver cannot start intent because " + ex + " occurred");
				}
			}	
		}

	}
	
	@Override 
	protected void onPause() {
		super.onPause();
		
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (mReceiver == null) {
			mReceiver = new LocalReceiver();
			IntentFilter filter = new IntentFilter(NotificationManager.NOTIFICATION_POSTED_ACTION);
			filter.addAction(NotificationManager.NOTIFICATION_CANCELED_ACTION);
			registerReceiver(mReceiver, filter);
		}
	}
	
}
