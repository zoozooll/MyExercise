/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home.test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.notification.Notification;
import com.oregonscientific.meep.notification.NotificationManager;
import com.oregonscientific.meep.notification.NotificationService;
import com.oregonscientific.meep.permission.Blacklist;
import com.oregonscientific.meep.permission.PermissionManager;

public class NotificationServiceTestCase extends BaseServiceTestCase<NotificationService> {
	
	private final String TAG = NotificationServiceTestCase.class.getName();
	
	private final String EXTRA_BLACKLIST_KEYWORD = "blacklist_keyword";
	
	public NotificationServiceTestCase(Class<NotificationService> serviceClass) {
		super(serviceClass);
	}
	
	public NotificationServiceTestCase() {
		super(NotificationService.class);
	}

	/**
	 * Creates a {@link Notification} for this test
	 * 
	 * @param withContentIntent
	 *            indicates whether or not the resulting
	 *            {@link android.content.Intent} should have a
	 *            {@link Notification#contentIntent}
	 * @return a {@link Notification}
	 */
	protected Notification getTestNotification(boolean withContentIntent) {
		Notification.Builder builder = new Notification.Builder()
				.setKind(Notification.KIND_ALERT)
				.setContentTitle("Test")
				.setImportant(true)
				.setContentText("This is a going to be a long night");
		
		if (withContentIntent) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DEFAULT);
			intent.addCategory(Intent.CATEGORY_APP_BROWSER);
			Blacklist blacklist = new Blacklist();
			blacklist.setType(PermissionManager.BLACKLIST_TYPE_KEYWORD);
			blacklist.setEntry("");
			intent.putExtra(EXTRA_BLACKLIST_KEYWORD, Test.DATA_BLACKLIST_SHORT_PASSAGE);
			builder.setContentIntent(intent, Notification.ACTION_START_ACTIVITY);
		}
		
		return builder.build();
	}
	
	/**
	 * Creates a {@link Notification} of the given kind
	 * 
	 * @param kind the kind of notification to create.
	 * @return a {@link Notification}
	 */
	protected Notification getNotification(String kind) {
		Notification.Builder builder = new Notification.Builder()
				.setKind(kind)
				.setContentTitle("Test")
				.setImportant(true)
				.setContentText("This is a going to be a long night");
		
		return builder.build();
	}
	
	/**
	 * Creates a progress {@link Notification} for this test
	 * 
	 * @param progress the amount of progress for this {@link Notification}
	 * @param progressMax the maximum amount of progress for this {@link Notification}
	 * @param progressIndeterminate if true, the {@link Notification} will display a continuous animation
	 * @return a {@link Notification}
	 */
	protected Notification getProgressNotification(long id, int progressMax, int progress, boolean progressIndeterminate) {
		Notification.Builder builder = new Notification.Builder()
				.setKind(Notification.KIND_ALERT)
				.setIdentifier(id)
				.setContentTitle("Progress")
				.setContentText("This is a progress style notification");
		
		Notification.ProgressBarStyle progressStyle = new Notification.ProgressBarStyle(builder)
				.setProgress(progressMax, progress, progressIndeterminate);
		
		return progressStyle.build();
	}
	
	public void testNotificationSerialization() {
		Notification n = getTestNotification(true);
		
		String json = n.toJson();
		Log.i(TAG, "JSON: " + json);
		
		// Must set the class loader for the {@link Intent} to correctly load the {@link Parcelable}
		Notification not = Notification.fromJson(json);
		Intent intent = not.contentIntent;
		intent.setExtrasClassLoader(getContext().getClassLoader());
		Blacklist word = intent.getParcelableExtra(EXTRA_BLACKLIST_KEYWORD);
		
		assertTrue(word != null);
	}
	
	public void testServiceBinding() {
		this.bindService(new Intent());
		this.shutdownService();
	}
	
	public void testPostNotificationWithNoContentIntent() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				NotificationManager nm = (NotificationManager) ServiceManager.getService(getContext(), ServiceManager.NOTIFICATION_SERVICE);
				long identifier = nm.notifyBlocking(Test.DATA_ACCOUNT_USER_ID, getTestNotification(false));
				
				completeTest(identifier != 0);
			}
			
		});
		
		waitUntilComplete();
		assertTrue(mTestResult);
	}
	
	public void testPostNotificationWithContentIntent() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				NotificationManager nm = (NotificationManager) ServiceManager.getService(getContext(), ServiceManager.NOTIFICATION_SERVICE);
				long identifier = nm.notifyBlocking(Test.DATA_ACCOUNT_USER_ID, getTestNotification(true));
				
				completeTest(identifier != 0);
			}
			
		});
		
		waitUntilComplete();
		assertTrue(mTestResult);
	}
	
	public void testPostNotificationWithProgress() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				NotificationManager nm = (NotificationManager) ServiceManager.getService(getContext(), ServiceManager.NOTIFICATION_SERVICE);
				
				// Post a progress notification
				long identifier = nm.notifyBlocking(Test.DATA_ACCOUNT_USER_ID, getProgressNotification(0, 100, 10, false));
				if (identifier == 0) {
					completeTest(false);
					return;
				}
				
				// Updates the progress
				long id = nm.notifyBlocking(Test.DATA_ACCOUNT_USER_ID, getProgressNotification(identifier, 100, 20, false));
				if (id == 0 || id != identifier) {
					completeTest(false);
					return;
				}
				
				// Ensure that the {@link Notification} is updated
				Notification n = nm.retrieve(id);
				if (n == null) {
					completeTest(false);
					return;
				}
				
				completeTest(n.progress == 20);
			}
			
		});
		
		waitUntilComplete();
		assertTrue(mTestResult);
	}
	
	public void testRetrieveNotificationWithIdentifier() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				NotificationManager nm = (NotificationManager) ServiceManager.getService(getContext(), ServiceManager.NOTIFICATION_SERVICE);
				long identifier = nm.notifyBlocking(Test.DATA_ACCOUNT_USER_ID, getTestNotification(true));
				
				if (identifier == 0) {
					completeTest(false);
					return;
				}
				
				Notification n = nm.retrieveBlocking(identifier);
				if (n == null) {
					completeTest(false);
					return;
				}
				
				Intent intent = n.contentIntent;
				if (intent == null) {
					completeTest(false);
					return;
				}
				
				intent.setExtrasClassLoader(getContext().getClassLoader());
				Blacklist blacklsit = intent.getParcelableExtra(EXTRA_BLACKLIST_KEYWORD);
				completeTest(blacklsit != null);
			}
			
		});
		
		waitUntilComplete();
		assertTrue(mTestResult);
	}
	
	public void testRetrieveAllNotificationsForUser() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				NotificationManager nm = (NotificationManager) ServiceManager.getService(getContext(), ServiceManager.NOTIFICATION_SERVICE);
				long identifier = nm.notifyBlocking(Test.DATA_ACCOUNT_USER_ID, getTestNotification(false));
				
				if (identifier == 0) {
					completeTest(false);
					return;
				}
				
				List<Notification> list = nm.retrieveAllBlocking(Test.DATA_ACCOUNT_USER_ID);
				completeTest(list != null);
			}
			
		});
		
		waitUntilComplete();
		assertTrue(mTestResult);
	}
	
	public void testCountOfNotificationsForUser() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				NotificationManager nm = (NotificationManager) ServiceManager.getService(getContext(), ServiceManager.NOTIFICATION_SERVICE);
				long identifier = nm.notifyBlocking(Test.DATA_ACCOUNT_USER_ID, getTestNotification(false));
				
				if (identifier == 0) {
					completeTest(false);
					return;
				}
				
				int count = nm.countBlocking(Test.DATA_ACCOUNT_USER_ID, Notification.KIND_ALERT);
				completeTest(count > 0);
			}
			
		});
		
		waitUntilComplete();
		assertTrue(mTestResult);
	}
	
	public void testMarkNotificationAsRead() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				NotificationManager nm = (NotificationManager) ServiceManager.getService(getContext(), ServiceManager.NOTIFICATION_SERVICE);
				long identifier = nm.notifyBlocking(Test.DATA_ACCOUNT_USER_ID, getTestNotification(false));
				
				if (identifier == 0) {
					completeTest(false);
					return;
				}
				
				nm.markAsRead(identifier, true);
				Notification notification = nm.retrieve(identifier);
				
				if (notification == null) {
					completeTest(false);
					return;
				}
				
				int flags = notification.flags;
				int result = flags & Notification.FLAG_READ;
				completeTest(result == Notification.FLAG_READ);
			}
			
		});
		
		waitUntilComplete();
		assertTrue(mTestResult);
	}
	
	public void testCancelNotification() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				NotificationManager nm = (NotificationManager) ServiceManager.getService(getContext(), ServiceManager.NOTIFICATION_SERVICE);
				long identifier = nm.notifyBlocking(Test.DATA_ACCOUNT_USER_ID, getTestNotification(false));
				
				if (identifier == 0) {
					completeTest(false);
					return;
				}
				
				nm.cancel(identifier);
				Notification notification = nm.retrieve(identifier);
				completeTest(notification == null);
			}
			
		});
		
		waitUntilComplete();
		assertTrue(mTestResult);
	}
	
	public void testCancelNotificationsOfKind() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				NotificationManager nm = (NotificationManager) ServiceManager.getService(getContext(), ServiceManager.NOTIFICATION_SERVICE);
				long identifier = nm.notifyBlocking(Test.DATA_ACCOUNT_USER_ID, getNotification(Notification.KIND_NEWS));
				
				if (identifier == 0) {
					completeTest(false);
					return;
				}
				
				long count = nm.count(Test.DATA_ACCOUNT_USER_ID, Notification.KIND_NEWS);
				if (count == 0) {
					completeTest(false);
					return;
				}
				
				nm.cancelKind(Test.DATA_ACCOUNT_USER_ID, Notification.KIND_NEWS);
				count = nm.count(Test.DATA_ACCOUNT_USER_ID, Notification.KIND_NEWS);
				completeTest(count == 0);
			}
			
		});
		
		waitUntilComplete();
		assertTrue(mTestResult);
	}
	
	public void testCancelAllNotifications() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				NotificationManager nm = (NotificationManager) ServiceManager.getService(getContext(), ServiceManager.NOTIFICATION_SERVICE);
				long identifier = nm.notifyBlocking(Test.DATA_ACCOUNT_USER_ID, getNotification(Notification.KIND_NEWS));
				
				if (identifier == 0) {
					completeTest(false);
					return;
				}
				
				nm.cancelAll(Test.DATA_ACCOUNT_USER_ID);
				List<Notification> notifications = nm.retrieveAllBlocking(Test.DATA_ACCOUNT_USER_ID);
				completeTest(notifications == null);
			}
			
		});
		
		waitUntilComplete();
		assertTrue(mTestResult);
	}
	
	public void testGetNotifications() {
		final long MAX_SIZE = 15;
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				NotificationManager nm = (NotificationManager) ServiceManager.getService(getContext(), ServiceManager.NOTIFICATION_SERVICE);
				nm.cancelAll(Test.DATA_ACCOUNT_USER_ID);
				
				for (int i = 0; i < MAX_SIZE; i++) {
					long identifier = nm.notifyBlocking(Test.DATA_ACCOUNT_USER_ID, getTestNotification(false));
					if (identifier == 0) {
						completeTest(false);
						return;
					}
				}
				
				List<Notification> notifications = nm.getBlocking(Test.DATA_ACCOUNT_USER_ID, Notification.KIND_ALERT, false, 6, 10);
				completeTest((notifications != null && notifications.size() == 9));
			}
			
		});
		
		
	}
	
	private final ScheduledExecutorService mScheduled = Executors.newSingleThreadScheduledExecutor();
	Future<?> mFuture;
	
	@SmallTest
	public void testPostProgressNotification() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
				final Account account = am.getLastLoggedInAccountBlocking();
				
				Notification.Builder builder = new Notification.Builder()
						.setContentTitle("Downloading...")
						.setContentText("Some text...")
						.setKind(Notification.KIND_STORE);
				Notification.Style style = new Notification.ProgressBarStyle(builder).setProgress(100, 0, false);
				
				final NotificationManager nm = (NotificationManager) ServiceManager.getService(getContext(), ServiceManager.NOTIFICATION_SERVICE);
				final Notification n = style.build();
				final long id = nm.notifyBlocking(account.getId(), n);
				
				mFuture = mScheduled.scheduleWithFixedDelay(new Runnable() {

					@Override
					public void run() {
						if (n.progress != 100) {
							n.id = id;
							n.progress = n.progress + 1;						
							nm.notify(account.getId(), n);
						} else {
							Notification notification = nm.retrieve(id);
							completeTest(notification.progress == 100);
							
							if (mFuture != null) {
								mFuture.cancel(false);
							}
						}
					}
					
				}, 1000, 1000, TimeUnit.MILLISECONDS);
			}
			
		});
		
		
		waitUntilComplete();
		assertTrue(mTestResult);
	}

}
