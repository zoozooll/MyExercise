/**
 * 
 */
package com.oregonscientific.bbq.bean;

import java.io.Serializable;

import com.oregonscientific.bbq.bean.BBQDataSet.DonenessLevel;

/**
 * @author aaronli
 *
 */
public class BbqSettings implements Cloneable, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8844165721406646381L;

	private int channel;
	
	private int meatTypeInt;
	
	private DonenessLevel donenessLevel;
	
	private float targetTemperature;
	
	private Timer reloadTimer;
	
	private Timer currentTimer;
	
	private boolean upCountTimer;
	
	
	public BbqSettings copy() throws CloneNotSupportedException {
		BbqSettings copy = (BbqSettings) this.clone();
		copy.setCurrentTimer(currentTimer.copy());
		copy.setReloadTimer(reloadTimer.copy());
		return copy;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + channel;
		result = prime * result
				+ ((donenessLevel == null) ? 0 : donenessLevel.hashCode());
		result = prime * result + meatTypeInt;
		result = prime * result
				+ ((reloadTimer == null) ? 0 : reloadTimer.hashCode());
		result = prime * result + Float.floatToIntBits(targetTemperature);
		result = prime * result + (upCountTimer ? 1231 : 1237);
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
		BbqSettings other = (BbqSettings) obj;
		if (channel != other.channel)
			return false;
		if (donenessLevel != other.donenessLevel)
			return false;
		if (meatTypeInt != other.meatTypeInt)
			return false;
		if (reloadTimer == null) {
			if (other.reloadTimer != null)
				return false;
		} else if (!reloadTimer.equals(other.reloadTimer))
			return false;
		if (Float.floatToIntBits(targetTemperature) != Float
				.floatToIntBits(other.targetTemperature))
			return false;
		if (upCountTimer != other.upCountTimer)
			return false;
		return true;
	}

	/**
	 * @return the channel
	 */
	public int getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(int channel) {
		this.channel = channel;
	}

	/**
	 * @return the meatTypeInt
	 */
	public int getMeatTypeInt() {
		return meatTypeInt;
	}

	/**
	 * @param meatTypeInt the meatTypeInt to set
	 */
	public void setMeatTypeInt(int meatTypeInt) {
		this.meatTypeInt = meatTypeInt;
	}

	/**
	 * @return the donenessLevel
	 */
	public DonenessLevel getDonenessLevel() {
		return donenessLevel;
	}

	/**
	 * @param donenessLevel the donenessLevel to set
	 */
	public void setDonenessLevel(DonenessLevel donenessLevel) {
		this.donenessLevel = donenessLevel;
	}

	/**
	 * @return the targetTemperature
	 */
	public float getTargetTemperature() {
		return targetTemperature;
	}

	/**
	 * @param targetTemperature the targetTemperature to set
	 */
	public void setTargetTemperature(float targetTemperature) {
		this.targetTemperature = targetTemperature;
	}

	/**
	 * @return the reloadTimer
	 */
	public Timer getReloadTimer() {
		return reloadTimer;
	}

	/**
	 * @param reloadTimer the reloadTimer to set
	 */
	public void setReloadTimer(Timer reloadTimer) {
		this.reloadTimer = reloadTimer;
	}

	/**
	 * @return the currentTimer
	 */
	public Timer getCurrentTimer() {
		return currentTimer;
	}

	/**
	 * @param currentTimer the currentTimer to set
	 */
	public void setCurrentTimer(Timer currentTimer) {
		this.currentTimer = currentTimer;
	}

	/**
	 * @return the upCountTimer
	 */
	public boolean isUpCountTimer() {
		return upCountTimer;
	}

	/**
	 * @param upCountTimer the upCountTimer to set
	 */
	public void setUpCountTimer(boolean upCountTimer) {
		this.upCountTimer = upCountTimer;
	}
}
