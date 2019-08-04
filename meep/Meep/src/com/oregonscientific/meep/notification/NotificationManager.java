/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.notification;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import android.os.RemoteException;
import android.util.Log;

import com.oregonscientific.meep.ServiceAgent;
import com.oregonscientific.meep.ServiceConnector;

/**
 * The class to create notifications to notify users of events happened. 
 * Client application can obtain an instance of this class by calling 
 * {@link com.oregonscientific.meep.ServiceManager#getService(android.content.Context, String)}
 */
public class NotificationManager extends ServiceAgent<INotificationService> {
	
	private static final String TAG = "NotificationManager";

	/**
	 * Broadcast intent action indicating that a notification is posted. One
	 * extra provides identifier of the notification posted.
	 * 
	 * @see #EXTRA_IDENTIFIER
	 */
	public static final String NOTIFICATION_POSTED_ACTION = 
		"com.oregonscientific.meep.NOTIFICATION_POSTED";
	
	/**
	 * Broadcast intent action indicating that a notification is canceled. One
	 * extra provides information of the notification(s) canceled
	 * 
	 * @see #EXTRA_IDENTIFIER
	 */
	public static final String NOTIFICATION_CANCELED_ACTION = "com.oregonscientific.meep.NOTIFICATION_CANCELED";
	
	/**
	 * The lookup key for the name of the identifier of the {@link Notification} posted.
	 * Retrieve it with {@link android.content.Intent#getLongExtra(String, long)}
	 * 
	 * @see #NOTIFICATION_POSTED_ACTION
	 */
	public static final String EXTRA_IDENTIFIER = "id";

	/**
	 * The lookup key for the cause of the cancellation of notification(s).
	 * Retrieve with {@link android.content.Intent#getParcelableExtra(String)}
	 */
	public static final String EXTRA_CAUSE = "cause";

	public NotificationManager(ServiceConnector connector) {
		super(connector);
	}
	
	@Override
	protected INotificationService getInterface() {
		return INotificationService.Stub.asInterface(getConnector().getBinder());
	}
	
	/**
	 * Post a notification to be shown in notification center. If the {@link Notification} posted contains
	 * an identifier, it will be replaced by the updated information
	 * 
	 * @param user the owner of the notifications. May not be null
	 * @param notification A {@link Notification} object describing what to show the user. Must not be null.
	 */
	public long notify(String user, Notification notification) {
		long result = 0;
		if (isReady()) {
			try {
				result = getService().notify(user, notification);
			} catch (RemoteException ex) {
				Log.e(TAG, "Cannot post " + notification + " because " + ex);
			}
		}
		return result;
	}

	/**
	 * Post a notification to be shown in notification center. If the
	 * {@link Notification} posted contains an identifier, it will be replaced
	 * by the updated information. This method will block until the underlying
	 * service becomes ready. Calling this method in main thread may cause dead
	 * lock. User should not call this method in the main thread
	 * 
	 * @param user
	 *            the owner of the notifications. May not be null
	 * @param notification
	 *            A {@link Notification} object describing what to show the
	 *            user. Must not be null.
	 */
	public long notifyBlocking(final String user, final Notification notification) {
		long result = 0;
		if (isReady()) {
			result = notify(user, notification);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Long> future = new FutureTask<Long>(new Callable<Long>() {

				@Override
				public Long call() throws Exception {
					return NotificationManager.this.notify(user, notification);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot post " + notification + " because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Cancel a previously posted notification. This only removes the notification from the underlying
	 * data store. 
	 * 
	 * @param id the Id of the notification to be removed
	 */
	public void cancel(long id) {
		if (isReady()) {
			try {
				getService().cancel(id);
			} catch (RemoteException ex) {
				Log.e(TAG, "Cannot cancel notification " + id + " because " + ex);
			}
		} else {
			cancelDelayed(id);
		}
	}
	
	private void cancelDelayed(final long id) {
		if (isReady()) {
			cancel(id);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					cancel(id);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}
	
	/**
	 * Cancel all previously posted notification for the given {@code user}. This will remove all notificaitons
	 * posted for the user in the underlying data store
	 * 
	 * @param user The user who owns the notification
	 */
	public void cancelAll(String user) {
		if (isReady()) {
			try {
				getService().cancelAll(user);
			} catch (RemoteException ex) {
				Log.e(TAG, "Cannot cancel notifications for " + user + " because " + ex);
			}
		} else {
			cancelAllDelayed(user);
		}
	}
	
	private void cancelAllDelayed(final String user) {
		if (isReady()) {
			cancelAll(user);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					cancelAll(user);
					return null;
				}
				
			});
			addConnectedTasks(future);
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
	public void cancelKind(String user, String kind) {
		if (isReady()) {
			try {
				getService().cancelKind(user, kind);
			} catch (RemoteException ex) {
				Log.e(TAG, "Cannot cancel " + kind + " notifications for " + user + " because " + ex);
			}
		} else {
			cancelKindDelayed(user, kind);
		}
	}
	
	private void cancelKindDelayed(final String user, final String kind) {
		if (isReady()) {
			cancelKind(user, kind);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					cancelKind(user, kind);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}
	
	/**
	 * Indicates whether a {@link Notification} should be marked as read
	 * 
	 * @param id The identifier of the notification to flag
	 * @param read {@code true} if the {@link Notification} is to be marked as read, {@code false} otherwise
	 */
	public void markAsRead(long id, boolean read) {
		if (isReady()) {
			try {
				getService().markAsRead(id, read);
			} catch (RemoteException ex) {
				Log.e(TAG, "Cannot flag notification " + id + " because " + ex);
			}
		} else {
			markAsReadDelayed(id, read);
		}
	}
	
	private void markAsReadDelayed(final long id, final boolean read) {
		if (isReady()) {
			markAsRead(id, read);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					markAsRead(id, read);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}
	
	/**
	 * Returns the notification identified by {@code id}
	 * 
	 * @param user the owner of the notifications
	 * @param id The identifier of the notification to return
	 */
	public Notification retrieve(long id) {
		Notification result = null;
		if (isReady()) {
			try {
				result = getService().retrieve(id);
			} catch (RemoteException ex) {
				Log.e(TAG, "Cannot retrieve notification " + id + " because " + ex);
			}
		} 
		return result;
	}
	
	public Notification retrieveBlocking(final long id) {
		Notification result = null;
		if (isReady()) {
			result = retrieve(id);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Notification> future = new FutureTask<Notification>(new Callable<Notification>() {

				@Override
				public Notification call() throws Exception {
					return retrieve(id);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot retrieve notification " + id + " because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Return all previously posted notifications for the given user.
	 * 
	 * @param user the owner of the notifications
	 * @return a List of {@link Notification} that belongs of the user, {@code null} if the given user does not has any notification
	 */
	public List<Notification> retrieveAll(String user) {
		List<Notification> result = null;
		if (isReady()) {
			try {
				result = getService().retrieveAll(user);
			} catch (RemoteException ex) {
				Log.e(TAG, "Cannot retrieve notifications for " + user + " because " + ex);
			}
		} 
		return result;
	}
	
	/**
	 * Return all previously posted notifications for the given user. This method
	 * will block until the underlying service becomes ready. Calling this method in main 
	 * thread may cause dead lock. User should not call this method in the main thread
	 * 
	 * @param user the owner of the notifications
	 * @return a List of {@link Notification} that belongs of the user, {@code null} if the given user does not has any notification
	 */
	public List<Notification> retrieveAllBlocking(final String user) {
		List<Notification> result = null;
		if (isReady()) {
			result = retrieveAll(user);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<List<Notification>> future = new FutureTask<List<Notification>>(new Callable<List<Notification>>() {

				@Override
				public List<Notification> call() throws Exception {
					return retrieveAll(user);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot retrieve notifications for " + user + " because " + ex);
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
	 * @return a list of {@link Notification} that belongs to the user, {@code null} if the given user does not has any notification
	 */
	public List<Notification> get(String user, String kind, boolean ascending, long offset, long limit) {
		List<Notification> result = null;
		if (isReady()) {
			try {
				result = getService().get(user, kind, ascending, offset, limit);
			} catch (RemoteException ex) {
				Log.e(TAG, "Cannot retrieve notifications for " + user + " because " + ex);
			}
		} 
		return result;
	}
	
	/**
	 * Returns a subset of previously posted notification for the given user. This method
	 * will block until the underlying service becomes ready. Calling this method in main 
	 * thread may cause dead lock. User should not call this method in the main thread
	 * 
	 * @param user the owner of the notifications
	 * @param kind The kind of notification. Should be one of {@link Notification#KIND_NEWS},
	 * {@link Notification#KIND_ALERT}, {@link Notification#KIND_MESSAGE}, or {@link Notification#KIND_STORE}.
	 * @param ascending whether or not the resulting list of {@link Notification} should be sorted in ascending order
	 * @param offset start the output at this row number
	 * @param limit limit output to this number of items
	 * @return a list of {@link Notification} that belongs to the user, {@code null} if the given user does not has any notification
	 */
	public List<Notification> getBlocking(final String user, final String kind, final boolean ascending, final long offset, final long limit) {
		List<Notification> result = null;
		if (isReady()) {
			result = get(user, kind, ascending, offset, limit);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<List<Notification>> future = new FutureTask<List<Notification>>(new Callable<List<Notification>>() {

				@Override
				public List<Notification> call() throws Exception {
					return get(user, kind, ascending, offset, limit);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot retrieve notifications for " + user + " because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Returns the number of posted notifications of the given type for the user
	 * 
	 * @param user the owner of the notifications
	 * @param kind The kind of notification. Should be one of {@link Notification#KIND_NEWS},
	 * {@link Notification#KIND_ALERT}, {@link Notification#KIND_MESSAGE}, or {@link Notification#KIND_STORE}.
	 * @return The number of notifications of the given type
	 */
	public int count(String user, String kind) {
		return count(user, kind, 0);
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
	public int count(String user, String kind, int flags) {
		int result = 0;
		if (isReady()) {
			try {
				result = getService().count(user, kind, flags);
			} catch (RemoteException ex) {
				Log.e(TAG, "Cannot retrieve number of " + kind + " notifications for " + user + " because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Returns the number of posted notifications of the given type for the user. This method
	 * will block until the underlying service becomes ready. Calling this method in main 
	 * thread may cause dead lock. User should not call this method in the main thread
	 * 
	 * @param user the owner of the notifications
	 * @param kind The kind of notification. Should be one of {@link Notification#KIND_NEWS},
	 * {@link Notification#KIND_ALERT}, {@link Notification#KIND_MESSAGE}, or {@link Notification#KIND_STORE}.
	 * @return The number of notifications of the given type
	 */
	public int countBlocking(final String user, final String kind) {
		return countBlocking(user, kind, 0);
	}
	
	/**
	 * Returns the number of posted notifications of the given type for the user. This method
	 * will block until the underlying service becomes ready. Calling this method in main 
	 * thread may cause dead lock. User should not call this method in the main thread
	 * 
	 * @param user the owner of the notifications
	 * @param kind The kind of notification. Should be one of {@link Notification#KIND_NEWS},
	 * {@link Notification#KIND_ALERT}, {@link Notification#KIND_MESSAGE}, or {@link Notification#KIND_STORE}.
	 * @param flags Exclude notifications with the given {@code flags} 
	 * @return The number of notifications of the given type
	 */
	public int countBlocking(final String user, final String kind, final int flags) {
		int result = 0;
		if (isReady()) {
			result = count(user, kind, flags);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Integer> future = new FutureTask<Integer>(new Callable<Integer>() {

				@Override
				public Integer call() throws Exception {
					return count(user, kind, flags);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot retrieve number of " + kind + " notifications for " + user + " because " + ex);
			}
		}
		return result;
	}

}
