/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.notification;

import com.oregonscientific.meep.notification.Notification;

/**
 * Interface for interacting with the MessageService
 */
interface INotificationService {

	/**
	 * Post a notification to be shown in notification center. If the {@link Notification} posted contains
	 * an identifier, it will be replaced by the updated information
	 * 
	 * @param user the owner of the notifications. May not be null
	 * @param notification A {@link Notification} object describing what to show the user. Must not be null.
	 */
	long notify(String user, in com.oregonscientific.meep.notification.Notification notification);

	/**
	 * Cancel a previously posted notification. This only removes the notification from the underlying
	 * data store. 
	 * 
	 * @param notificationId the Id of the notification to be removed
	 */
	void cancel(long notificationId);

	/**
	 * Cancel all previously posted notification for the given {@code user}. This will remove all notificaitons
	 * posted for the user in the underlying data store
	 * 
	 * @param user The user who owns the notification
	 */
	void cancelAll(String user);
	
	/**
	 * Cancel previously posted notification of the given type. This will remove all notifications of the given
	 * type for the user in the underlying data store
	 * 
	 * @param user the owner of the notifications
	 * @param The kind of the notification. Should be one of {@link Notification#KIND_NEWS}, 
	 * {@link Notification#KIND_ALERT}, {@link Notification#KIND_MESSAGE}, or {@link Notification#KIND_STORE}.
	 */
	void cancelKind(String user, String kind);
	
	/**
	 * Indicates whether a {@link Notification} should be marked as read
	 * 
	 * @param id The identifier of the notification to flag
	 * @param read read {@code true} if the {@link Notification} is to be marked as read, {@code false} otherwise
	 */
	void markAsRead(long id, boolean read);

	/**
	 * Returns the notification identified by {@code id}
	 * 
	 * @param user the owner of the notifications
	 * @param id The identifier of the notification to return
	 */
	com.oregonscientific.meep.notification.Notification retrieve(long id);

	/**
	 * Return all previously posted notifications for the given user.
	 * 
	 * @param user the owner of the notifications
	 * @return a List of {@link Notification} that belongs of the user
	 */
	List<com.oregonscientific.meep.notification.Notification> retrieveAll(String user);
	
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
	List<com.oregonscientific.meep.notification.Notification> get(String user, String kind, boolean ascending, long offset, long limit);

	/**
	 * Returns the number of posted notifications of the given type for the user
	 * 
	 * @param user the owner of the notifications
	 * @param kind The kind of notification. Should be one of {@link Notification#KIND_NEWS},
	 * {@link Notification#KIND_ALERT}, {@link Notification#KIND_MESSAGE}, or {@link Notification#KIND_STORE}.
	 * @param flags Exclude notifications with the given {@code flags} 
	 * @return The number of notifications of the given type
	 */
	int count(String user, String kind, int flags);

}
