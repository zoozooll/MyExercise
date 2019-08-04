/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home.test;

import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.msm.MessageFilter;

/**
 * Base class that will respond to a {@link Message} received
 */
public abstract class MessageResponder {

	private MessageFilter filter;
	
	public MessageResponder(MessageFilter messageFilter) {
		filter = messageFilter;
	}
	
	/**
	 * Determines whether or not the {@link MessageReceiver} should receive the
	 * given {@code message} 
	 * 
	 * @param message the message to determine whether the receiver should receive
	 * @return true if the receiver should receive the given {@code message}, false
	 * otherwise
	 */
	public final boolean shouldRespond(Message message) {
		boolean result = false;
		if (filter != null) {
			result = filter.match(message) > 0;
		}
		return result;
	}
	
	/**
	 * This method is called when ServerResponse is sending a message to client. 
	 * 
	 * @param message The {@link Message} received
	 */
	public abstract String onRespond(Message message);
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != getClass()) {
			return false;
		}
		
		MessageResponder other = (MessageResponder) obj;
		boolean result = false;
		if (filter != null && other.filter != null) {
			result = filter.equals(other.filter);
		} else {
			result = filter == null && other.filter == null;
		}
		return result;
	}
	
}
