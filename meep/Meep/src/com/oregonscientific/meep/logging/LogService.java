/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.logging;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.oregonscientific.meep.Build;
import com.oregonscientific.meep.DatabaseService;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.msm.MessageManager;
import com.oregonscientific.meep.util.NetworkUtils;

/**
 * Logging service is used to log records as indicated by the caller. Log
 * records are periodically sent to server.
 * 
 * Once a log record is created in the logging framework, it is owned by the
 * framework. Invoking another call to log the record will result in creating
 * anther entry with the same records.
 */
public class LogService extends DatabaseService<LogDatabaseHelper>{

	private final String TAG = "LogService";
	
	/** The underlying task enforces permission settings */
	private Logger mLogger;
	
	/** The actual logic implementation */
	private LogServiceHandler mHandler;
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		mHandler = new LogServiceHandler(this, getHelper());
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		// Unbinds any previously binded service
		ServiceManager.unbindServices(this);
		
		if (mLogger != null) {
			mLogger.cancel();
		}
	}
	
	@Override
	public int onStartCommand (Intent intent, int flags, int startId) {
		// Server messages will be inside the intent's extras
		handleCommand(intent);
		
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
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
		
		// Only handles messages received from MessageService
		if (MessageManager.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
			// retrieve extended data (added with putExtra()) from the intent
			Message message = intent.getParcelableExtra(MessageManager.EXTRA_MESSAGE);
			mHandler.handleMessage(message);
		}
	}
	
	/**
	 * This is the object that receives interactions from clients.
	 */
	private ILogService.Stub mBinder = new ILogService.Stub() {
		
		/**
		 *  Logs a message of the given level and message. The message logged using this method
		 *  will not be localized.
		 *  
		 *  @param level the level of the given message
		 *  @param type The type of the log entry
		 *  @param sourceUser the name of the source user
		 *  @param message the message to be logged
		 */
		@Override
	 	public void logp(Level level, String type, String sourceUser, String message) throws RemoteException {
			if (mHandler != null) {
				mHandler.logp(level, type, sourceUser, message);
			}
	 	}

		/**
		 *  Logs a message with the given level, type and message, using the given resource bundle to 
		 *  localize the message. If bundleName is null, the empty string or not valid then the message 
		 *  is not localized. If {@code param} is null or invalid, the localized message will not be
		 *  parameterized
		 *  
		 *  @param level the level of the given message
		 *  @param type The type of the log entry
		 *  @param sourceUser the name of the source user
		 *  @param message the message to be logged
		 *  @param bundleName the name of the resource bundle used to localize the message
		 *  @param resource the name of the resource
		 *  @param param the parameter associated with the event that is logged.
		 */
		@Override
		public void logrb(
				Level level, 
				String type, 
				String sourceUser, 
				String message, 
				String bundleName, 
				String resource, 
				List<String> param) throws RemoteException {
			
			if (mHandler != null) {
				mHandler.logrb(level, type, sourceUser, message, bundleName, resource, param);
			}
		}

		/**
		 * Logs a given log record. All other log methods call this method to actually perform the 
		 * logging action.
		 * 
		 * @param logRecord the log record to be logged.
		 * @param sourceUser the name of the source user
		 */
		@Override
		public void log(LogRecord logRecord, String sourceUser) throws RemoteException {
			if (mHandler != null) {
				mHandler.log(logRecord, sourceUser);
			}
		}
		
		/**
		 * Starts the logger
		 * 
		 * @param interval the interval at which the logger should run
		 */
		public void startLogger(long interval) throws RemoteException {
			if (mLogger == null) {
				mLogger = new Logger();
			}
			mLogger.schedule(interval);
		}
	 	
	};
	
	/**
	 * Periodic log task implementation
	 */
	private final class Logger {
		private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		private ScheduledFuture<?> handle;
		/** 
		 * Send log to server periodically
		 */
		private final Runnable logTask = new Runnable() {

			@Override
			public void run() {
				// Only send log records to server if there is Internet connection
				if (mHandler != null && NetworkUtils.hasInternetConnection(LogService.this)) {
					Log.w(TAG, "Sending log records to server...");
					AccountManager am = (AccountManager) ServiceManager.getService(LogService.this, ServiceManager.ACCOUNT_SERVICE);
					boolean isProduction = Build.environment.equals(Build.Environment.PRODUCTION);
					Account account = isProduction ? am.getLoggedInAccountBlocking() : am.getLastLoggedInAccountBlocking();
					mHandler.sendLogs(account);
				}
			}
		};
		
		
		/**
		 * Schedule periodic log operations
		 * 
		 * @param period The time from now to delay execution
		 */
		public synchronized void schedule(long period) {
			// Ensure that the scheduled period is a valid period
			if (period < LogManager.MIN_LOG_INTERVAL) {
				return;
			}
			
			if (handle != null) {
				handle.cancel(true);
				handle = null;
			}
			
			try {
				boolean isProduction = Build.environment.equals(Build.Environment.PRODUCTION);
				if (isProduction) {
					handle = scheduler.scheduleWithFixedDelay(
							logTask, 
							period, 
							period,
							TimeUnit.MILLISECONDS);
				} else {
					handle = scheduler.schedule(logTask, period, TimeUnit.MILLISECONDS);
				}
			} catch (Exception ex) {
				// The task cannot be scheduled
				android.util.Log.e(TAG, "Cannot schedule sending logs to server because " + ex.getMessage());
			}
		}
		
		/**
		 * Cancel execution of the task. If the task was already running, it will be interrupted
		 */
		public synchronized void cancel() {
			if (handle != null) {
				handle.cancel(true);
				handle = null;
			}
		}
	}
}