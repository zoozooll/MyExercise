/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.logging;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import android.os.RemoteException;
import android.util.Log;

import com.oregonscientific.meep.ServiceAgent;
import com.oregonscientific.meep.ServiceConnector;

/**
 * LogManager is used to log records of different types and periodically send them to MEEP
 * server for recording. Client application can obtain an instance of this class by calling 
 * {@link com.oregonscientific.meep.ServiceManager#getService(android.content.Context, String)}
 */
public class LogManager extends ServiceAgent<ILogService> {
	
	private static final String TAG = "LogManager";
	
	/**
	 * A system event log
	 */
	public static final String LOG_SYSTEM = "1";
	
	/**
	 * A permission event log
	 */
	public static final String LOG_PARENTAL = "2";
	
	/**
	 * The minimum interval that the logger should run
	 */
	public static final long MIN_LOG_INTERVAL = 60000;
	
	public LogManager(ServiceConnector connector) {
		super(connector);
	}
	
	@Override
	protected ILogService getInterface() {
		return ILogService.Stub.asInterface(getConnector().getBinder());
	}
	
	/**
	 *  Logs a message of the given level and message. The message logged using this method
	 *  will not be localized.
	 *  
	 *  @param level the level of the given message
	 *  @param type The type of the log entry
	 *  @param sourceUser the name of the source user
	 *  @param message the message to be logged
	 */
	public void logp(Level level, String type, String sourceUser, String message) {
		if (isReady()) {
			try {
				getService().logp(level, type, sourceUser, message);
			} catch (RemoteException ex) {
				Log.e(TAG, "Failed to log " + message + " because " + ex);
			}
		} else {
			logpDelayed(level, type, sourceUser, message);
		}
	}
	
	private void logpDelayed(final Level level, final String type, final String sourceUser, final String message) {
		if (isReady()) {
			logp(level, type, sourceUser, message);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					logp(level, type, sourceUser, message);
					return null;
				}
				
			});
			addConnectedTasks(future);
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
	public void logrb(
			Level level, 
			String type, 
			String sourceUser, 
			String message, 
			String bundleName, 
			String resource, 
			List<String> param) {
		
		if (isReady()) {
			try {
				getService().logrb(level, type, sourceUser, message, bundleName, resource, param);
			} catch (RemoteException ex) {
				Log.e(TAG, "Failed to log " + message + " with " + bundleName + " because " + ex);
			}
		} else {
			logrbDelayed(level, type, sourceUser, message, bundleName, resource, param);
		}
	}
	
	private void logrbDelayed(
			final Level level, 
			final String type,
			final String sourceUser, 
			final String message,
			final String bundleName, 
			final String resource, 
			final List<String> param) {
		
		if (isReady()) {
			logrb(level, type, sourceUser, message, bundleName, resource, param);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					logrb(level, type, sourceUser, message, bundleName, resource, param);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}
	
	/**
	 * Logs a given log record. All other log methods call this method to actually perform the 
	 * logging action.
	 * 
	 * @param log the log record to be logged.
	 * @param sourceUser the name of the source user
	 */
	public void log(LogRecord logRecord, String sourceUser) {
		if (isReady()) {
			try {
				getService().log(logRecord, sourceUser);
			} catch (RemoteException ex) {
				Log.e(TAG, "Failed to log for " + sourceUser + " because " + ex);
			}
		} else {
			logDelayed(logRecord, sourceUser);
		}
	}
	
	private void logDelayed(final LogRecord logRecord, final String sourceUser) {
		if (isReady()) {
			log(logRecord, sourceUser);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					log(logRecord, sourceUser);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}
	
	/**
	 * Starts the logger
	 * 
	 * @param interval the interval at which the logger should run
	 */
	public void startLogger(long interval) {
		if (isReady()) {
			try {
				getService().startLogger(interval);
			} catch (RemoteException ex) {
				Log.e(TAG, "Cannot start logger because " + ex);
			}
		} else {
			startLoggerDelayed(interval);
		}
	}
	
	private void startLoggerDelayed(final long interval) {
		if (isReady()) {
			startLogger(interval);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					startLogger(interval);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}

}
