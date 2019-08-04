/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.notification;

import java.util.List;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.oregonscientific.meep.DatabaseService;
import com.oregonscientific.meep.ServiceManager;

/**
 * The service to notify user of events that happen. The service does not create
 * UI elements. Instead, individual package can access this service to retrieve
 * notification of specific type and create the appropriate UI elements and tell
 * user that something happened.
 */
public class NotificationService extends DatabaseService<NotificationDatabaseHelper> {
	
	/**
	 * For handling messages received
	 */
	private NotificationServiceHandler mHandler;
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		mHandler = new NotificationServiceHandler(this, getHelper());
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
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		// Unbinds any previously binded service
		ServiceManager.unbindServices(this);
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
		
		// {@NotificationService} only supports service binding
	}

	private INotificationService.Stub mBinder = new INotificationService.Stub() {
		
		@Override
		public List<Notification> get(String user, String kind, boolean ascending, long offset, long limit) throws RemoteException {
			List<Notification> result = null;
			if (mHandler != null) {
				result = mHandler.get(user, kind, ascending, offset, limit);
			}
			return result;
		}
		
		@Override
		public List<Notification> retrieveAll(String user) throws RemoteException {
			List<Notification> result = null;
			if (mHandler != null) {
				result = mHandler.retrieveAll(user);
			}
			return result;
		}
		
		@Override
		public Notification retrieve(long id) throws RemoteException {
			Notification result = null;
			if (mHandler != null) {
				result = mHandler.retrieve(id);
			}
			return result;
		}
		
		@Override
		public long notify(String user, Notification notification) throws RemoteException {
			long result = 0;
			if (mHandler != null) {
				result = mHandler.notify(user, notification);
			}
			return result;
		}
		
		@Override
		public void markAsRead(long id, boolean read) throws RemoteException {
			if (mHandler != null) {
				mHandler.markAsRead(id, read);
			}
		}
		
		@Override
		public int count(String user, String kind, int flags) throws RemoteException {
			int result = 0;
			if (mHandler != null) {
				result = mHandler.count(user, kind, flags);
			}
			return result;
		}
		
		@Override
		public void cancelKind(String user, String kind) throws RemoteException {
			if (mHandler != null) {
				mHandler.cancelKind(user, kind);
			}
		}
		
		@Override
		public void cancelAll(String user) throws RemoteException {
			if (mHandler != null) {
				mHandler.cancelAll(user);
			}
		}
		
		@Override
		public void cancel(long notificationId) throws RemoteException {
			if (mHandler != null) {
				mHandler.cancel(notificationId);
			}
		}
	};
	
}
