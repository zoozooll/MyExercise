/**
 * 
 */
package com.idt.bw.bean;

/**
 * @author aaronli
 *
 */
public class Alarm {

	private long user;
	
	private long time;
	
	private String message;

	/**
	 * @return the notifyTime
	 */
	public long getNotifyTime() {
		return time;
	}

	/**
	 * @param notifyTime the notifyTime to set
	 */
	public void setNotifyTime(long notifyTime) {
		this.time = notifyTime;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the userName
	 */
	public long getUser() {
		return user;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUser(long user) {
		this.user = user;
	}
	
}
