/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.msm;

import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.msm.MessageFilter;
import android.content.ComponentName;

 
/**
 * Interface for interacting with the MessageService
 */
interface IMessageService {
	
	/**
	 * Sends a message to MEEP server
	 */
	void sendMessage(in Message message);
	
	/**
	 * Returns whether or not the service is connected with the messaging server
	 */
	boolean isConnected();
	
	/**
	 * Registers the given component to be run when a message that matches {@code filter}
	 * is received.
	 * 
	 * @param componentName The concrete component name of the service to handle the message
	 * @param filter selects the message to receive
	 */
	void registerCallback(in ComponentName componentName, in MessageFilter filter);
	
	/**
	 * Remove a previously registered component. All filters that have been registered
	 * for this component will be removed
	 */
	void unregisterCallback(in ComponentName componentName);
	
	/**
	 * Restart the message service with given environment
	 * 
	 * @param environmentInt 0 is for Production, 1 is for Sandbox
	 */
	void reconnect(int environmentInt);

}