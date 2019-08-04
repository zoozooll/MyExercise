/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.notification;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.oregonscientific.meep.ServiceHandler;
import com.oregonscientific.meep.database.ModelAttributes;
import com.oregonscientific.meep.database.Schema;

public class NotificationServiceHandler extends ServiceHandler {
	
	private static final String TAG = "NotificationServiceHandler";
	
	private final OrmLiteSqliteOpenHelper mHelper;
	
	/**
	 * Constructor
	 * 
	 * @param context The context that this handler runs in
	 * @param helper The SQLiteOpenHelper that this handler uses to connect to the
	 * underlying data store
	 */
	public NotificationServiceHandler(Context context, OrmLiteSqliteOpenHelper helper) {
		super(context);
		
		mHelper = helper;
	}
	
	/**
	 * Post a notification to be shown in notification center. If the {@link Notification} posted contains
	 * an identifier, it will be replaced by the updated information
	 * 
	 * @param user the owner of the notifications. May not be null
	 * @param notification A {@link Notification} object describing what to show the user. Must not be null.
	 */
	public long notify(String userId, Notification notification) {
		// Quick return if there is nothing to process
		if (userId == null || notification == null || mHelper == null) {
			return 0;
		}
		
		// Cannot proceed if there is user is not found. 
		$User user = getUser(userId, true);
		if (user == null) {
			return 0;
		}
		
		long result = 0;
		$Notification n = $Notification.fromNotification(notification);
		try {
			Dao<$Notification, Long> dao = mHelper.getDao($Notification.class);
			n.setUser(user);
			
			long id = dao.extractId(n);
			if (dao.idExists(id)) {
				$Notification existing = dao.queryForId(id);
				n.setLastModifiedDate(existing.getLastModifiedDate());
				dao.update(n);
			} else {
				dao.create(n);
			}
			result = dao.extractId(n);
			broadcastNotificationPosted(result);
		} catch (Exception ex) {
			Log.e(TAG, "Cannot post " + notification + " for " + userId + " because " + ex);
		}
		return result;
	}
	
	/**
	 * Broadcast the {@link Notification} posted 
	 * 
	 * @param identifier the identifier of the {@link Notification} posted
	 */
	private void broadcastNotificationPosted(long identifier) {
		// Quick return if {@code identifier} of a {@link Notification} is invalid 
		if (identifier == 0 || getContext() == null) {
			return;
		}
		
		Intent intent = new Intent();
		intent.setAction(NotificationManager.NOTIFICATION_POSTED_ACTION);
		intent.putExtra(NotificationManager.EXTRA_IDENTIFIER, identifier);
		getContext().sendBroadcast(intent);
	}
	
	private void broadcastNotificationCancelled(CancellationInfo info) {
		// Quick return if {@code info} of a {@link Notification} is invalid 
		if (info == null || getContext() == null) {
			return;
		}
		
		Intent intent = new Intent();
		intent.setAction(NotificationManager.NOTIFICATION_CANCELED_ACTION);
		intent.putExtra(NotificationManager.EXTRA_CAUSE, info);
		getContext().sendBroadcast(intent);
	}
	
	/**
	 * Returns the notification identified by {@code id}
	 * 
	 * @param user the owner of the notifications
	 * @param id The identifier of the notification to return
	 */
	public Notification retrieve(long id) {
		Notification result = null;
		try {
			ModelAttributes attrs = Schema.getAttributes($Notification.class);
			Dao<$Notification, Long> dao = mHelper.getDao($Notification.class);
			
			// Constructs and executes the query
			QueryBuilder<$Notification, Long> qb = dao.queryBuilder();
			qb.where().eq(attrs.getColumnName($Notification.ID_FIELD_NAME), Long.valueOf(id));
			$Notification notification = qb.queryForFirst();
			
			if (notification != null) {
				result = Notification.fromJson(notification.toJson());
			}
		} catch (Exception ex) {
			Log.e(TAG, "Cannot find notification with identifier " + id + " because " + ex);
		}
		return result;
	}

	/**
	 * Return all previously posted notifications for the given user.
	 * 
	 * @param user
	 *            the owner of the notifications
	 * @return a List of {@link Notification} that belongs of the user,
	 *         {@code null} if the given user does not has any notification
	 */
	public List<Notification> retrieveAll(String username) {
		// Quick return if the request cannot be processed
		if (username == null) {
			return null;
		}
		
		// Cannot proceed if there is user is not found. 
		$User user = getUser(username);
		if (user == null) {
			return null;
		}
		
		List<Notification> result = null;
		CloseableIterator<$Notification> iterator = user.getNotifications().closeableIterator();
		try {
			while (iterator.hasNext()) {
				$Notification notification = iterator.next();
				if (result == null) {
					result = new ArrayList<Notification>();
				}
				Notification n = Notification.fromJson(notification.toJson());
				result.add(n);
			}
		} catch (Exception ex) {
			Log.e(TAG, "Cannot find notification for " + username + " because " + ex);
		} finally {
			try {
				iterator.close();
			} catch (SQLException ignored) {
				
			}
		}
		return result;
	}
	
	/**
	 * Returns a subset of previously posted notification for the given user. 
	 * 
	 * @param user the owner of the notifications
	 * @param kind The kind of notification. Should be one of {@link Notification#KIND_NEWS},
	 * {@link Notification#KIND_ALERT}, {@link Notification#KIND_MESSAGE}, or {@link Notification#KIND_STORE}.
	 * @param ascending whether or not the resulting list of {@link Notification} should be sorted in ascending order
	 * @param offset start the output at this row number
	 * @param limit limit output to this number of items
	 */
	public List<Notification> get(String username, String kind, boolean ascending, long offset, long limit) {
		// Quick return if the request cannot be processed
		if (username == null || mHelper == null) {
			return null;
		}
		
		// Cannot proceed if there is user is not found. 
		$User user = getUser(username);
		if (user == null) {
			return null;
		}
		
		List<Notification> result = null;
		try {
			// Construct the query
			ModelAttributes attrs = Schema.getAttributes($Notification.class);
			Dao<$Notification, Long> dao = mHelper.getDao($Notification.class);
			QueryBuilder<$Notification, Long> qb = dao.queryBuilder();
			qb.limit(limit)
					.offset(offset)
					.orderBy(attrs.getColumnName($Notification.LAST_MODIFIED_DATE_FIELD_NAME), ascending);
			qb.where()
					.eq(attrs.getColumnName($Notification.USER_FIELD_NAME), user.getId())
					.and()
					.eq(attrs.getColumnName($Notification.KIND_FIELD_NAME), kind);
			
			List<$Notification> notifications = qb.query();
			for ($Notification notification : notifications) {
				if (result == null) {
					result = new ArrayList<Notification>();
				}
				Notification n = Notification.fromJson(notification.toJson());
				result.add(n);
			}
		} catch (SQLException ex) {
			// Ignored
			Log.e(TAG, "Cannot find notification for " + username + " because " + ex);
			result = null;
		}
		return result;
	}
	
	/**
	 * Returns the number of posted notifications of the given type for the user
	 * 
	 * @param user the owner of the notifications
	 * @param kind The kind of notification. Should be one of {@link Notification#KIND_NEWS},
	 * {@link Notification#KIND_ALERT}, {@link Notification#KIND_MESSAGE}, or {@link Notification#KIND_STORE}.
	 * @param flags Exclude notifications with the given {@code flags} 
	 * @return The number of notifications of the given type
	 */
	public int count(String username, String kind, int flags) {
		// Quick return if the request cannot be processed
		if (username == null || kind == null || mHelper == null) {
			return 0;
		}
		
		// Cannot proceed if there is user is not found. 
		$User user = getUser(username);
		if (user == null) {
			return 0;
		}
		
		long result = 0;
		try {
			ModelAttributes attrs = Schema.getAttributes($Notification.class);
			Dao<$Notification, Long> dao = mHelper.getDao($Notification.class);
			
			// Constructs and executes the query
			String raw = String.format(Locale.US, " & %1$d = 0", flags, flags);
			QueryBuilder<$Notification, Long> qb = dao.queryBuilder();
			qb.where()
					.eq(attrs.getColumnName($Notification.USER_FIELD_NAME), user.getId())
					.and()
					.eq(attrs.getColumnName($Notification.KIND_FIELD_NAME), kind)
					.and()
					.raw(attrs.getColumnName($Notification.FLAGS_FIELD_NAME) + raw);
			result = qb.countOf();
		} catch (Exception ex) {
			Log.e(TAG, "Cannot retrieve the number of posted " + kind + " notifications for " + username + " because " + ex);
		}
		return (int) result;
	}
	
	/**
	 * Indicates whether a {@link Notification} should be marked as read
	 * 
	 * @param id The identifier of the notification to flag
	 * @param read {@code true} if the {@link Notification} is to be marked as read, {@code false} otherwise
	 */
	public void markAsRead(long id, boolean read) {
		// Quick return if the request cannot be processed
		if (mHelper == null) {
			return;
		}
		
		try {
			Dao<$Notification, Long> dao = mHelper.getDao($Notification.class);
			$Notification notification = dao.queryForId(id);
			
			// Cannot proceed if the {@link Notification} is not found
			if (notification == null) {
				return;
			}
			
			int flag = notification.getFlags();
			if (read) {
				flag |= Notification.FLAG_READ;
			} else {
				flag &= ~Notification.FLAG_READ;
			}
			notification.setFlags(flag);
			
			// Updates the {@link Notification}
			dao.update(notification);
			broadcastNotificationPosted(notification.getId());
		} catch (Exception ex) {
			Log.e(TAG, "Cannot mark notification " + id + " because " + ex);
		}
	}
	
	/**
	 * Cancel a previously posted notification. This only removes the notification from the underlying
	 * data store. 
	 * 
	 * @param id the Id of the notification to be removed
	 */
	public void cancel(long id) {
		try {
			Dao<$Notification, Long> dao = mHelper.getDao($Notification.class);
			dao.deleteById(id);
			
			broadcastNotificationCancelled(new CancellationInfo(CancellationInfo.Cause.ID, String.valueOf(id)));
		} catch (Exception ex) {
			Log.e(TAG, "Cannot cancel notification " + id  + " because " + ex);
		}
	}
	
	/**
	 * Cancel all previously posted notification for the given {@code user}. This will remove all notifications
	 * posted for the user in the underlying data store
	 * 
	 * @param username The user who owns the notification
	 */
	public void cancelAll(String username) {
		// Quick return if the request cannot be processed
		$User user = getUser(username);
		if (user == null) {
			return;
		}
		
		try {
			ModelAttributes attrs = Schema.getAttributes($Notification.class);
			Dao<$Notification, Long> dao = mHelper.getDao($Notification.class);
			DeleteBuilder<$Notification, Long> db = dao.deleteBuilder();
			db.where().eq(attrs.getColumnName($Notification.USER_FIELD_NAME), user.getId());
			db.delete();
			
			broadcastNotificationCancelled(new CancellationInfo(CancellationInfo.Cause.USER, username));
		} catch (Exception ex) {
			Log.e(TAG, "Cannot cancel notifications for " + username  + " because " + ex);
		}
	}
	
	/**
	 * Cancel previously posted notification of the given type. This will remove all notifications of the given
	 * type for the user in the underlying data store
	 * 
	 * @param user the owner of the notifications
	 * @param The kind of the notification. Should be one of {@link Notification#KIND_NEWS}, 
	 * {@link Notification#KIND_ALERT}, {@link Notification#KIND_MESSAGE}, or {@link Notification#KIND_STORE}.
	 */
	public void cancelKind(String username, String kind) {
		// Quick return if the request cannot be processed
		$User user = getUser(username);
		if (user == null) {
			return;
		}
		
		try {
			ModelAttributes attrs = Schema.getAttributes($Notification.class);
			Dao<$Notification, Long> dao = mHelper.getDao($Notification.class);
			DeleteBuilder<$Notification, Long> db = dao.deleteBuilder();
			db.where()
					.eq(attrs.getColumnName($Notification.USER_FIELD_NAME), user.getId())
					.and()
					.eq(attrs.getColumnName($Notification.KIND_FIELD_NAME), kind);
			db.delete();
			
			broadcastNotificationCancelled(new CancellationInfo(CancellationInfo.Cause.KIND, kind));
		} catch (Exception ex) {
			Log.e(TAG, "Cannot cancel " + kind + " notifications for " + username  + " because " + ex);
		}
	}
	
	/**
	 * Retrieve the User object with the given {@code username}
	 * 
	 * @param username the unique name identifying the user
	 * @return the User object or <code>null</code> if the user was not found
	 */
	private $User getUser(String username) {
		return getUser(username, false);
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
		if (mHelper == null) {
			return null;
		}
		
		$User result = null;
		try {
			Dao<$User, Long> dao = mHelper.getDao($User.class);
			QueryBuilder<$User, Long> qb = dao.queryBuilder();
			qb.where().eq($User.IDENTIFIER_FIELD_NAME, userId);
			result = dao.queryForFirst(qb.prepare());
			
			if (result == null && createIfNotExists) {
				result = new $User(userId);
				dao.createIfNotExists(result);
			}
		} catch (Exception ex) {
			// The given user does not exist
			Log.e(TAG, "Cannot retrieve " + userId + " because " + ex);
			result = null;
		}
		return result;
	}

}
