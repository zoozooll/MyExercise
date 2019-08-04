/**
 * 
 */
package com.oregonscientific.bbq.bean;

import java.io.Serializable;

/**
 * @author aaronli
 *
 */
public class BBQDataSet implements Cloneable, Serializable {
	
	private Mode mode;
	
	private CookingStatus status;
	
	private boolean normal;
	
	private boolean lowBattery;
	
	private int meatTypeInt;
	
	private DonenessLevel donenessLevel;
	
	private Timer reloadTimer;
	
	private Timer currentTimer;

	private boolean upCountTimer;
	
	private float targetTemperature;
	
	private float probeTemperature;
	
	private float percentage;
	

	public BBQDataSet copy() throws CloneNotSupportedException {
		BBQDataSet copy = (BBQDataSet) this.clone();
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
		result = prime * result
				+ ((donenessLevel == null) ? 0 : donenessLevel.hashCode());
		result = prime * result + meatTypeInt;
		result = prime * result + ((mode == null) ? 0 : mode.hashCode());
		result = prime * result
				+ ((reloadTimer == null) ? 0 : reloadTimer.hashCode());
		result = prime * result + Float.floatToIntBits(targetTemperature);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BBQDataSet [mode=" + mode + ", status=" + status + ", normal="
				+ normal + ", lowBattery=" + lowBattery + ", meatTypeInt="
				+ meatTypeInt + ", donenessLevel=" + donenessLevel
				+ ", reloadTimer=" + reloadTimer + ", currentTimer="
				+ currentTimer + ", upCountTimer=" + upCountTimer
				+ ", targetTemperature=" + targetTemperature
				+ ", probeTemperature=" + probeTemperature + ", percentage="
				+ percentage + "]";
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
		BBQDataSet other = (BBQDataSet) obj;
		if (donenessLevel != other.donenessLevel)
			return false;
		if (meatTypeInt != other.meatTypeInt)
			return false;
		if (mode != other.mode)
			return false;
		if (reloadTimer == null) {
			if (other.reloadTimer != null)
				return false;
		} else if (!reloadTimer.equals(other.reloadTimer))
			return false;
		if (Float.floatToIntBits(targetTemperature) != Float
				.floatToIntBits(other.targetTemperature))
			return false;
		return true;
	}

	/**
	 * @return the mode
	 */
	public Mode getMode() {
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(Mode mode) {
		this.mode = mode;
	}

	/**
	 * @return the status
	 */
	public CookingStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(CookingStatus status) {
		this.status = status;
	}

	/**
	 * @return the normal
	 */
	public boolean isNormal() {
		return normal;
	}

	/**
	 * @param normal the normal to set
	 */
	public void setNormal(boolean normal) {
		this.normal = normal;
	}

	/**
	 * @return the lowBattery
	 */
	public boolean isLowBattery() {
		return lowBattery;
	}

	/**
	 * @param lowBattery the lowBattery to set
	 */
	public void setLowBattery(boolean lowBattery) {
		this.lowBattery = lowBattery;
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
	 * @return the donelessLevel
	 */
	public DonenessLevel getDonelessLevel() {
		return donenessLevel;
	}

	/**
	 * @param donelessLevel the donelessLevel to set
	 */
	public void setDonelessLevel(DonenessLevel donelessLevel) {
		this.donenessLevel = donelessLevel;
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
	 * @return the probeTemperature
	 */
	public float getProbeTemperature() {
		return probeTemperature;
	}

	/**
	 * @param probeTemperature the probeTemperature to set
	 */
	public void setProbeTemperature(float probeTemperature) {
		this.probeTemperature = probeTemperature;
	}

	/**
	 * @return the percentage
	 */
	public float getPercentage() {
		return percentage;
	}

	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}

	public static enum Mode {
		UNKNOW,
		MEAN_TYPE_MODE,
		TARGET_TEMPERATURE_MODE,
		TIMER_MODE;
		
		public static Mode get(int ordinal) {
			switch (ordinal) {
			case 1:
				return MEAN_TYPE_MODE;
			case 2:
				return TARGET_TEMPERATURE_MODE;
			case 3:
				return TIMER_MODE;
			default:
				throw new IndexOutOfBoundsException();
			}
		}
	}
	
	public static enum CookingStatus {
		STOPED,
		COOKING,
		ALMOST,
		READY,
		OVERCOOK;
		
		public static CookingStatus get(int ordinal) {
			switch (ordinal) {
			case 0:
				return STOPED;
			case 1:
				return COOKING;
			case 2:
				return ALMOST;
			case 3:
				return READY;
			case 4:
				return OVERCOOK;
			default:
				throw new IndexOutOfBoundsException();
			}
		}
	}
	
	public static enum DonenessLevel {
		NA, RARE, MEDIUMRARE, MEDIUM, MEDIUMWELL, WELLDONE;
		
		public static DonenessLevel get(int ordinal) {
			switch (ordinal) {
			case 0:
				return NA;
			case 1:
			case 9:
				return RARE;
			case 2:
			case 10:
				return MEDIUMRARE;
			case 3:
			case 11:
				return MEDIUM;
			case 4:
			case 12:
				return MEDIUMWELL;
			case 5:
			case 13:
				return WELLDONE;
			default:
				return NA;
			}
		}
	}
}


