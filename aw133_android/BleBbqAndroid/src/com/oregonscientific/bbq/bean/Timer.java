/**
 * 
 */
package com.oregonscientific.bbq.bean;

import java.io.Serializable;

/**
 * @author aaronli
 *
 */
public class Timer implements Cloneable, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int hours;
	private int minute;
	private int second;
	
	
	public int totalSeconds() {
		return hours * 3600 + minute * 60 + second;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hours;
		result = prime * result + minute;
		result = prime * result + second;
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Timer other = (Timer) obj;
		if (hours != other.hours)
			return false;
		if (minute != other.minute)
			return false;
		if (second != other.second)
			return false;
		return true;
	}
	
	public Timer copy() throws CloneNotSupportedException {
		return (Timer) this.clone();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Timer [" + hours + ":" + minute + ":"
				+ second + "]";
	}
	/**
	 * 
	 */
	public Timer() {
		super();
	}
	/**
	 * @param hours
	 * @param minute
	 * @param second
	 */
	public Timer(int hours, int minute, int second) {
		super();
		this.hours = hours;
		this.minute = minute;
		this.second = second;
	}
	/**
	 * @return the hours
	 */
	public int getHours() {
		return hours;
	}
	/**
	 * @param hours the hours to set
	 */
	public void setHours(int hours) {
		this.hours = hours;
	}
	/**
	 * @return the minute
	 */
	public int getMinute() {
		return minute;
	}
	/**
	 * @param minute the minute to set
	 */
	public void setMinute(int minute) {
		this.minute = minute;
	}
	/**
	 * @return the second
	 */
	public int getSecond() {
		return second;
	}
	/**
	 * @param second the second to set
	 */
	public void setSecond(int second) {
		this.second = second;
	}
	
}
