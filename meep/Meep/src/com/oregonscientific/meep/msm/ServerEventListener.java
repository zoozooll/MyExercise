/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.msm;


/**
 * A MEEP event listener receivers notifications from a MEEP server.
 * Notification indicates various state of the connection such as
 * when the connection established, close, etc.
 * 
 * @author Stanley Lam
 */
public interface ServerEventListener {
	
	/**
	 * Notified when a connection is established with a MEEP server
	 */
	public void onOpen();
	
	/**
	 * Notifies when a message is sent to the MEEP server 
	 */
	public void onSend(String data);
	
	/**
	 * Notified when a message is received from the MEEP server 
	 */
	public void onReceive(String message);
	
	/**
	 * Notified when connection with the MEEP server is closed
	 */
	public void onClose();
	
	/**
	 * Notified when there was an error
	 */
	public void onError(Throwable cause);

}
