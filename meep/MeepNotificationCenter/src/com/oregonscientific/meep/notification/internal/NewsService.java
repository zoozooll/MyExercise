/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.notification.internal;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.oregonscientific.meep.DatabaseService;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.database.ModelAttributes;
import com.oregonscientific.meep.database.Schema;
import com.oregonscientific.meep.http.RestClient;
import com.oregonscientific.meep.http.Status;
import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.notification.Notification;
import com.oregonscientific.meep.notification.NotificationManager;

public class NewsService extends DatabaseService<NewsDatabaseHelper> {
	
	private final String TAG = getClass().getSimpleName();
	
	private final long MAX_INTERVAL = 10000;
	private final long NEWS_POLL_INTERVAL = 72000000;
	
	private final String EXTRA_NEWS = "news";
	
	private final String NEWS_DEFAULT_ID = "000000000000000000000000";
	private final String NEWS_URL = "news/fetch/%1$s";
	
	private NewsChecker mNewsChecker;
	private final ScheduledExecutorService mScheduler = Executors.newSingleThreadScheduledExecutor();

	@Override
	public IBinder onBind(Intent intent) {
		// This service does not support binding
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// Periodically polls server for News
		if (mNewsChecker == null) {
			mNewsChecker = new NewsChecker();
			mNewsChecker.schedule(NEWS_POLL_INTERVAL, true);
		}
		Log.v(TAG, "News service is started...");
	}
	
	@Override
	public int onStartCommand (Intent intent, int flags, int startId) {
		// Server messages will be inside the intent's extras
		handleCommand(intent);
		
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky. This is because some context may have
		// binded to this service, as such, we do not want this service
		// to stop
		return START_STICKY;
	}
	
	/**
	 * Handles the command as specified in the {@code} intent 
	 * 
	 * @param intent The intent supplied to {@link #onStartCommand(Intent, int, int)}
	 */
	private void handleCommand(Intent intent) {
		// Quick return if there is nothing to process
		if (intent == null) {
			return;
		}
		
		// {@link NewsService} does not support any action
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		// Unbinds any previously binded service
		ServiceManager.unbindServices(this);
		// Shuts down the scheduler
		mScheduler.shutdownNow();
		
		if (mNewsChecker != null) {
			mNewsChecker.cancel();
			mNewsChecker = null;
		}
	}
	
	/**
	 * Retrieves the User object identified by the {@code account}
	 *  
	 * @param account The Account to retrieve
	 * @param createIfNotExists true to create the User object if it was not found
	 * @return The User object or <code>null</code> if the user was not found
	 */
	private $User getUser(Account account, boolean createIfNotExists) {
		String username = account == null ? "" : account.getId();
		return getUser(username, createIfNotExists);
	}
	
	/**
	 * Retrieve the User object with the given {@code username}. If the user cannot be
	 * found, creates the User object if {@code createIfNotExist} is set to true
	 * 
	 * @param userId the unique name identifying the user
	 * @param createIfNotExists true to create the User object if it was not found
	 * @return the User object or <code>null</code> if the user was not found
	 */
	private $User getUser(String userId, boolean createIfNotExists) {
		$User result = null;
		try {
			Dao<$User, Long> dao = getHelper().getDao($User.class);
			QueryBuilder<$User, Long> qb = dao.queryBuilder();
			qb.where().eq($User.IDENTIFIER_FIELD_NAME, userId);
			result = dao.queryForFirst(qb.prepare());
			
			if (result == null && createIfNotExists) {
				result = new $User(userId);
				dao.createIfNotExists(result);
			}
		} catch (Exception ex) {
			// The given user does not exist
			Log.e(TAG, "Cannot retrieve user identified by: " + userId + " because " + ex);
			result = null;
		}
		return result;
	}
	
	/**
	 * Retrieve the latest news item for {@code user}
	 * 
	 * @param user the user to retrieve the latest news item
	 * @return the latest news item for the {@code user}
	 */
	private $News getLatestNews($User user) {
		$News result = null;
		try {
			ModelAttributes attrs = Schema.getAttributes($News.class);
			Dao<$News, Long> dao = getHelper().getDao($News.class);
			QueryBuilder<$News, Long> qb = dao.queryBuilder();
			qb.where().eq(attrs.getColumnName($News.USER_FIELD_NAME), user.getId());
			qb.orderBy($News.POST_DATE_FIELD_NAME, false);
			
			result = qb.queryForFirst();
		} catch (Exception ex) {
			Log.e(TAG, "Failed to retrieve the latest news item for " + user + " because " + ex);
		}
		return result;
	}
	
	/**
	 * Determines whether or not the given {@code user} previously retrieved the news item 
	 * 
	 * @param user the user to retrieving the news item
	 * @param news the identifier of the news item
	 * @return {@code true} if the news item already exists for the given user, {@code false} otherwise
	 */
	private $News getNews($User user, String identifier) {
		$News result = null;
		try {
			ModelAttributes attrs = Schema.getAttributes($News.class);
			Dao<$News, Long> dao = getHelper().getDao($News.class);
			QueryBuilder<$News, Long> qb = dao.queryBuilder();
			qb.where()
					.eq(attrs.getColumnName($News.USER_FIELD_NAME), user.getId())
					.and()
					.eq($News.IDENTIFIER_FIELD_NAME, identifier);
			
			result = qb.queryForFirst();
		} catch (Exception ex) {
			Log.e(TAG, "Failed to determine whether " + user + " already retrieved " + identifier + " because " + ex);
		}
		return result;
	}
	
	/**
	 * Retrieve the identifier of the last news item 
	 * 
	 * @param user the user to retrieve the last news item
	 * @return the identifier of the last news item
	 */
	private String getLastNewsIdentifier($User user) {
		$News newsItem = getLatestNews(user);
		return newsItem == null ? NEWS_DEFAULT_ID : newsItem.getIdentifier();
	}
	
	private void rescheduleChecker() {
		// Only reschedule to run checker if the service is not shut down
		if (!mScheduler.isShutdown() && mNewsChecker != null) {
			mScheduler.schedule(new Runnable() {

				@Override
				public void run() {
					mNewsChecker.schedule(NEWS_POLL_INTERVAL, true);
				}
				
			}, MAX_INTERVAL, TimeUnit.MILLISECONDS);
		}
	}
	
	/**
	 * The task that periodically polls server for News
	 */
	private final class NewsChecker {
		
		/** Scheduler */
		private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		private ScheduledFuture<?> handle;
		
		/** 
		 * Check current running processes with permission, and update the permission table if needed.
		 */
		private final Runnable checker = new Runnable() {

			@Override
			public void run() {
				AccountManager am = (AccountManager) ServiceManager.getService(NewsService.this, ServiceManager.ACCOUNT_SERVICE);
				Account account = am.getLastLoggedInAccountBlocking();
				
				// We can only retrieve news with an registered account
				if (account == null) {
					rescheduleChecker();
					return;
				}
				
				// Retrieve the internal user
				final $User user = getUser(account, true);
				String identifier = getLastNewsIdentifier(user);
				String url = String.format(NEWS_URL, identifier);
				
				Log.v(TAG, "URL: " + url);
				
				// Retrieve news from remote server
				RestClient client = new RestClient();
				client.setCredentials(account.getToken());
				client.get(url, new AsyncHttpResponseHandler() {
					
					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(String content) {
						Log.d(TAG, "Response from polling for news: " + content);
						Message message = Message.fromJson(content, new NewsMessagePropertyTypeAdapterFactory());
						if (message == null) {
							return;
						}
						
						if (message.getStatus() == Status.SUCCESS_OK) {
							try {
								final List<$News> list = (List<$News>) message.getProperty(EXTRA_NEWS);
								if (list != null) {
									NotificationManager nm = (NotificationManager) ServiceManager.getService(getBaseContext(), ServiceManager.NOTIFICATION_SERVICE);
									
									for ($News news : list) {
										$News item = getNews(user, news.getIdentifier());
										long notificationId = item == null ? 0 : item.getNotificationId();
										
										// Post notifications
										Notification.Builder builder = new Notification.Builder()
												.setKind(Notification.KIND_NEWS)
												.setContentTitle(news.getTitle())
												.setContentText(news.getText())
												.setIcon(news.getIcon())
												.setPicture(news.getPicture());
										
										String url = news.getUrl();
										if (url != null) {
											Intent intent = new Intent(Intent.ACTION_VIEW);
											intent.setData(Uri.parse(url));
											builder.setContentIntent(intent, Notification.ACTION_START_ACTIVITY);
										}	
										
										Notification n = builder.build();
										n.id = notificationId;
										notificationId = nm.notify(user.getIdentifier(), n);
										 
										// Caches the news item
										if (item != null) {
											news.setId(item.getId());
										}
										news.setId(item == null ? 0 : item.getId());
										news.setUser(user);
										news.setNotificationId(notificationId);
										
										Dao<$News, String> dao = getHelper().getDao($News.class);
										dao.createOrUpdate(news);
									}
								}
							} catch (Exception e) {
								Log.e(TAG, user + " cannot retrieve news because " + e);
							}
						}
					}
					
					@Override
					public void onFailure(Throwable error, String content) {
						Log.e(TAG, "Error retrieving news: " + content + " because " + error);
					}
					
				});
			}
		};
		
		/**
		 * Schedule periodic volume checker 
		 * 
		 * @param period The time from now to delay execution
		 * @param autoReschedule true to re-schedule the task if scheduling failed
		 */
		public void schedule(final long period, boolean autoReschedule) {
			if (handle != null) {
				handle.cancel(true);
				handle = null;
			}
			
			try {
				handle = scheduler.scheduleWithFixedDelay(
						checker, 
						0, 
						period,
						TimeUnit.MILLISECONDS);
			} catch (Exception ex) {
				// The task cannot be scheduled
				Log.e(TAG, "News checker cannot be scheduled to run because " + ex.getMessage());
				
				if (autoReschedule) {
					// Try to reschedule
					if (!mScheduler.isShutdown()) {
						mScheduler.schedule(new Runnable() {

							@Override
							public void run() {
								schedule(period, true);
							}
							
						}, MAX_INTERVAL, TimeUnit.MILLISECONDS);
					}
				}
			}
		}
		
		/**
		 * Cancel execution of the task. If the task was already running, it will be interrupted
		 */
		public void cancel() {
			if (handle != null) {
				handle.cancel(true);
				handle = null;
			}
		}
		
	}

}
