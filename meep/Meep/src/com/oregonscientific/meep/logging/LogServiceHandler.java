/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.logging;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.oregonscientific.meep.Build;
import com.oregonscientific.meep.ServiceHandler;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.database.ModelAttributes;
import com.oregonscientific.meep.database.Schema;
import com.oregonscientific.meep.database.internal.ForeignCollectionInstanceCreator;
import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.msm.MessageFilter;
import com.oregonscientific.meep.msm.MessageReceiver;
import com.oregonscientific.meep.serialization.MessagePropertyTypeAdapterFactories;

/**
 * The handlder that implements logic for actual logging
 */
public class LogServiceHandler extends ServiceHandler {

	private final String TAG = "LogServiceHandler";
	
	/** Keys used to decode a "log" message */
	private final String KEY_LOG = "logs";
	private final long MESSAGE_TIMEOUT = 3600000;
	
	private final OrmLiteSqliteOpenHelper mHelper;

	public LogServiceHandler(Context context, OrmLiteSqliteOpenHelper helper) {
		super(context);
		
		mHelper = helper;
		init();
	}
	
	private void init() {
		// Register a type adapter to serialize or deserialize permission setting messages
		MessageFilter filter = new MessageFilter(Message.PROCESS_SYSTEM);
		filter.addOperation(Message.OPERATION_CODE_LOG);
		MessagePropertyTypeAdapterFactories.getInstance().registerTypeAdapterFactory(
				filter, 
				new LogMessagePropertyTypeAdapterFactory());
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
		LogRecord logRecord = new LogRecord(type, level, message);
		log(logRecord, sourceUser);
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
		
		LogRecord logRecord = new LogRecord(type, level, message);
		logRecord.setResourceBundleName(bundleName);
		logRecord.setResourceName(resource);
		logRecord.setParameters(param.toArray());
		log(logRecord, sourceUser);
	}
	
	/**
	 * Logs a given log record. All other log methods call this method to actually perform the 
	 * logging action.
	 * 
	 * @param logRecord the log record to be logged.
	 * @param sourceUser the name of the source user
	 */
	public void log(LogRecord logRecord, String sourceUser) {
		// Quick return if the request cannot be processed
		if (mHelper == null || logRecord == null) {
			return;
		}
		
		// Retrieves the source user. Creates the {@link $User} object
		// if the user referenced is not found. We cannot proceed if
		// the there was error
		$User user = getUser(sourceUser, true);
		if (user == null) {
			return;
		}
		
		try {
			// Creates the {@link $Log$Record}
			Dao<$Log$Record, Long> dao = mHelper.getDao($Log$Record.class);
			ForeignCollectionInstanceCreator creator = new ForeignCollectionInstanceCreator(mHelper.getConnectionSource());
			
			$Log$Record log = logRecord.to$Log$Record(creator);
			log.setUser(user);
			log.setDao(dao);
			log.create();
			
			// Creates the parameters
			ForeignCollection<$Parameter> parameters = log.getParameters();
			parameters.updateAll();
		} catch (SQLException ex) {
			Log.e(TAG, "Cannot create log record because " + ex);
		}
	}
	
	/**
	 * Sends log records of the given user {@code account} to remote server
	 * 
	 * @param account the user account whose log records are to be sent to remote server
	 */
	public void sendLogs(Account account) {
		// Retrieves the user whose log records are to be sent to remote server.
		// We cannot proceed if the user is not found
		$User user = getUser(account);
		if (user == null) {
			return;
		}
		
		// Retrieves the log records that need to be sent to remote server
		final List<$Log$Record> records = getPendingLogRecords(user);
		
		// Creates the message to send to remote server
		final Message msg = new Message(Message.PROCESS_SYSTEM, Message.OPERATION_CODE_LOG);
		msg.addProperty(KEY_LOG, records);
		
		if (Build.environment.equals(Build.Environment.SANDBOX)) {
			String json = msg.toJson();
			Log.v(TAG, json);
		}
		
		MessageFilter filter = new MessageFilter(msg.getMessageID());
		MessageReceiver receiver = new MessageReceiver(filter) {

			@Override
			public void onReceive(Message message) {
				// Quick return if there is nothing to process
				if (message == null || mHelper == null) {
					return;
				}
				
				try {
					TransactionManager.callInTransaction(mHelper.getConnectionSource(), new Callable<Void>() {

						@Override
						public Void call() throws Exception {
							// Updates status of log records
							Dao<$Log$Record, Long> dao = mHelper.getDao($Log$Record.class);
							for ($Log$Record record : records) {
								record.setSent(true);
								record.setDao(dao);
								record.update();
							}
							
							return null;
						}
						
					});
				} catch (SQLException ex) {
					Log.e(TAG, "Cannot update status of the sent log records because " + ex);
				}
			}
			
		};
		
		// Register the inline receiver to process the {@link Message}
		registerReceiver(receiver);
		// Sends the log records to remote server
		sendMessage(msg);
	}
	
	/**
	 * Retrieve log records that are not yet sent to remote server for the given {@code user} 
	 * 
	 * @param user the use whose log records are to be retrieved
	 * @return a list of {@link LogRecord} that are not yet sent to remote server
	 */
	private List<$Log$Record> getPendingLogRecords($User user) {
		// Quick return if the request cannot be processed
		if (mHelper == null || user == null) {
			return null;
		}
		
		CloseableIterator<$Log$Record> iterator = null;
		try {
			Dao<$Log$Record, Long> dao = mHelper.getDao($Log$Record.class);
			QueryBuilder<$Log$Record, Long> qb = dao.queryBuilder();
			ModelAttributes attrs = Schema.getAttributes($Log$Record.class);
			
			// Construct the query to retrieve all log records that are not sent to
			// remote server
			qb.where()
				.eq(attrs.getColumnName($Log$Record.USER_FIELD_NAME), Long.valueOf(user.getId()))
				.and()
				.eq(attrs.getColumnName($Log$Record.SENT_FIELD_NAME), false);
			
			final List<$Log$Record> result = new ArrayList<$Log$Record>();
			iterator = qb.iterator();
			while (iterator.hasNext()) {
				$Log$Record logRecord = iterator.next();
				
				if (logRecord.isSending()) {
					// If the last attempted date of the log record is greater
					// than the timeout period, the log record should be
					// considered as pending Otherwise, it should be removed
					Date lastAttemptDate = logRecord.getLastAttemptDate();
					Date currentDate = new Date();
					
					if ((currentDate.getTime() - lastAttemptDate.getTime()) >= MESSAGE_TIMEOUT) {
						result.add(logRecord);
					}
				} else {
					result.add(logRecord);
				}
			}
			
			// Updates status of the log records to indicate that they are in
			// the process of sending to remote server
			TransactionManager.callInTransaction(mHelper.getConnectionSource(), new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					for ($Log$Record logRecord : result) {
						logRecord.setSending(true);
						logRecord.setLastAttemptDate(new Date());
						logRecord.update();
					}
					
					return null;
				}
				
			});
			
			
			return result;
		} catch (SQLException ex) {
			Log.e(TAG, "Cannot retrieve log records to send to remote server because " + ex);
			return null;
		} finally {
			try {
				iterator.close();
			} catch (Exception ex) {
				// Ignore
			}
		}
	}
	
	/**
	 * Retrieves the User object identified by the {@code account}
	 *  
	 * @param account The Account to retrieve
	 * @return The User object or <code>null</code> if the user was not found
	 */
	private $User getUser(Account account) {
		return getUser(account, false);
	}
	
	/**
	 * Retrieves the User object identified by the {@code account}
	 *  
	 * @param account The Account to retrieve
	 * @param createIfNotExists true to create the User object if it was not found
	 * @return The User object or <code>null</code> if the user was not found
	 */
	private $User getUser(Account account, boolean createIfNotExists) {
		String username = account == null ? "" : account.getMeepTag();
		return getUser(username, createIfNotExists);
	}
	
	/**
	 * Retrieve the User object with the given {@code username}. If the user cannot be
	 * found, creates the User object if {@code createIfNotExist} is set to true
	 * 
	 * @param username the unique name identifying the user
	 * @param createIfNotExists true to create the User object if it was not found
	 * @return the User object or <code>null</code> if the user was not found
	 */
	private $User getUser(String username, boolean createIfNotExists) {
		if (mHelper == null) {
			return null;
		}
		
		$User result = null;
		try {
			Dao<$User, Long> dao = mHelper.getDao($User.class);
			QueryBuilder<$User, Long> qb = dao.queryBuilder();
			qb.where().eq($User.IDENTIFIER_FIELD_NAME, username);
			result = dao.queryForFirst(qb.prepare());
			
			if (result == null && createIfNotExists) {
				result = new $User(username);
				dao.createIfNotExists(result);
			}
		} catch (Exception ex) {
			// The given user does not exist
			Log.e(TAG, "Cannot retrieve " + username + " because " + ex);
			result = null;
		}
		return result;
	}
	
}
