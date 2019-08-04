/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.msm;

/**
 * Base class that will receive {@link Message} sent from {@link MesssageService}
 */
public abstract class MessageReceiver {
	
	private MessageFilter filter;
	
	public MessageReceiver(MessageFilter messageFilter) {
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
	public final boolean shouldReceive(Message message) {
		boolean result = false;
		if (filter != null) {
			result = filter.match(message) > 0;
		}
		return result;
	}
	
	/**
	 * Returns whether or not the receiver is persistent 
	 * 
	 * @return true if the receiver is persistent and should not be removed after {@link #onReceive(Message)} 
	 * is called, false otherwise. The default is false, thus, the receiver will be removed after
	 * {@link #onReceive(Message)} is called.
	 */
	public boolean isPersistent() {
		return false;
	}
	
	/**
	 * This method is called when MessageReceiver is receiving a message from server. This
	 * method is always called in a new thread
	 * 
	 * @param message The {@link Message} received
	 */
	public abstract void onReceive(Message message);
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != getClass()) {
			return false;
		}
		
		MessageReceiver other = (MessageReceiver) obj;
		boolean result = false;
		if (filter != null && other.filter != null) {
			result = filter.equals(other.filter);
		} else {
			result = filter == null && other.filter == null;
		}
		return result;
	}
	
}
