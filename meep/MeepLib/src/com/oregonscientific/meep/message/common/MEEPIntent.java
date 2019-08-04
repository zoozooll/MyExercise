/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.message.common;

import android.content.Intent;

/**
 * A special subclass of Intent used in MEEP.
 * 
 * These are the supported actions that MEEP intent defines for receiving broadcast
 * 
 * <li>{@link #ACTION_COMMAND_EXECUTED}</li>
 * <li>{@link #ACTION_LOG}</li>
 * <li>{@link #ACTION_MESSAGE_RECEIVED}</li>
 * <li>{@link #ACTION_MESSAGE_SENT}</li>
 * 
 * These are the supported categories that can be used to further clarify an intent 
 * used in the MEEP
 * 
 * <li>{@link #CATEGORY_APP_COMMUNICATOR}</li>
 * <li>{@link #CATEGORY_APP_GAMES}</li>
 * <li>{@link #CATEGORY_APP_PHOTO}</li>
 * <li>{@link #CATEGORY_APP_STORE}</li>
 * 
 * These are the supported extra fields that can be used as extra data of an intent
 * used in the MEEP
 * 
 * <li>{@link #EXTRA_MESSAGE}</li>
 * <li>{@link #EXTRA_RESULT}</li>
 * 
 * @author Stanley Lam
 */
public class MEEPIntent extends Intent {
	
	/**
	 * This is broadcasted when a command is executed
	 */
	public static final String ACTION_COMMAND_EXECUTED = "meep.intent.action.COMMAND_EXECUTED";
	
	/**
	 * This is braodcasted when a message is received
	 */
	public static final String ACTION_MESSAGE_RECEIVED = "meep.intent.action.MESSAGE_RECEIVED";
	
	/**
	 * Broadcast action: the message was sent
	 */
	public static final String ACTION_MESSAGE_SENT = "meep.intent.action.MESSAGE_SENT";
	
	/**
	 * Broadcast action: sends the given text to log output 
	 */
	public static final String ACTION_LOG = "meep.intent.action.LOG";
	
	/**
	 * Used with ACTION_SEND to send data to communicator
	 */
	public static final String CATEGORY_APP_COMMUNICATOR = "meep.intent.action.APP_COMMUNICATOR";
	
	/**
	 * Used with ACTION_SEND to send data to game launcher
	 */
	public static final String CATEGORY_APP_GAMES = "meep.intent.category.APP_GAMES";
	
	/**
	 * Used with ACTION_SEND to send data to photo launcher
	 */
	public static final String CATEGORY_APP_PHOTO = "meep.intent.category.APP_PHOTO";
	
	/**
	 * Used with ACTION_SEND to send data to MEEP store
	 */
	public static final String CATEGORY_APP_STORE = "meep.intent.category.APP_STORE";
	
	/**
	 * Used as a String extra field for returning results from fulfilling an intent
	 */
	public static final String EXTRA_RESULT = "meep.intent.extra.RESULT";
	
	/**
	 * A String holding the message of the intent
	 */
	public static final String EXTRA_MESSAGE = "meep.intent.extra.MESSAGE";

}
