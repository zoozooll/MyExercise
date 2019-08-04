/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.msm;

/**
 * Exception that is thrown if there the given message could not be sent 
 * to the sever
 * 
 * @author Stanley Lam
 */
public class ServerMessageException extends ServerConnectionException {

	private static final long serialVersionUID = 737879151513010215L;

	/**
   * Constructs a new server message exception with the specified detail message.
   *
   * @param message the message
   */
	public ServerMessageException(String message) {
		super(message);
	}
	
	/**
   * Constructs a new server message exception with the specified detail message and cause.
   *
   * @param message the message
   * @param cause the cause
   */
	public ServerMessageException(String message, Throwable t) {
		super(message, t);
	}
	
}
