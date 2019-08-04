/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.logging;

import com.oregonscientific.meep.logging.Level;
import com.oregonscientific.meep.logging.LogRecord;

/**
 * Interface for interacting with the LogService
 */
interface ILogService {

	/**
	 *  Logs a message of the given level and message. The message logged using this method
	 *  will not be localized.
	 *  
	 *  @param level the level of the given message
	 *  @param type The type of the log entry
	 *  @param sourceUser the name of the source user
	 *  @param message the message to be logged
	 */
	void logp(in Level level, String type, String sourceUser, String message);
	
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
	void logrb(in Level level, String type, String sourceUser, String message, String bundleName, String resource, in List<String> param);

	/**
	 * Logs a given log record. All other log methods call this method to actually perform the 
	 * logging action.
	 * 
	 * @param logRecord the log record to be logged.
	 * @param sourceUser the name of the source user
	 */
	void log(in LogRecord logRecord, String sourceUser);
	
	/**
	 * Starts the logger
	 * 
	 * @param interval the interval at which the logger should run
	 */
	void startLogger(long interval);
}
 
 