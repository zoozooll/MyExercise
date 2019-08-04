/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.msm;

import java.io.IOException;

/**
 * IO Exception that is thrown if there was an error communicating with
 * the server.
 * 
 * @author Stanley Lam
 */
public class ServerConnectionException extends IOException {

	private static final long serialVersionUID = -524705717651468639L;


	/**
   * Constructs a new server connection exception with the specified detail message.
   *
   * @param message the message
   */
	public ServerConnectionException(String message) {
		super(message);
	}
	
	/**
   * Constructs a new server connection exception with the specified detail message and cause.
   *
   * @param message the message
   * @param cause the cause
   */
	public ServerConnectionException(String message, Throwable t) {
		super(message, t);
	}
	
	 /**
   * Constructs a new server connection exception with the specified cause.
   *
   * @param cause the cause
   */
  public ServerConnectionException(Throwable cause) {
    super(cause);
  }
	
}
