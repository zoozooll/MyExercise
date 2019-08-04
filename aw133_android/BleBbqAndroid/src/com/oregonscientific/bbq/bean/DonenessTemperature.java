/**
 * 
 */
package com.oregonscientific.bbq.bean;

import java.io.Serializable;

/**
 * @author aaronli
 *
 */
public class DonenessTemperature implements Serializable, Cloneable {

	private int meatTypeIndex;
	private float rareTemperature;
	private float mediumrareTemperature;
	private float mediumTemperature;
	private float mediumwellTemperature;
	private float welldoneTemperature;
	private boolean custom;
	
	private int firstIndex = -1;
	/**
	 * 
	 */
	public DonenessTemperature() {
		super();
	}
	
	/**
	 * @param meatTypeIndex
	 * @param rareTemperature
	 * @param mediumrareTemperature
	 * @param mediumTemperature
	 * @param mediumwellTemperature
	 * @param welldoneTemperature
	 */
	public DonenessTemperature(int meatTypeIndex, float rareTemperature,
			float mediumrareTemperature, float mediumTemperature,
			float mediumwellTemperature, float welldoneTemperature) {
		super();
		this.meatTypeIndex = meatTypeIndex;
		this.rareTemperature = rareTemperature;
		this.mediumrareTemperature = mediumrareTemperature;
		this.mediumTemperature = mediumTemperature;
		this.mediumwellTemperature = mediumwellTemperature;
		this.welldoneTemperature = welldoneTemperature;
	}

	/**
	 * @param meatTypeIndex
	 * @param rareTemperature
	 * @param mediumrareTemperature
	 * @param mediumTemperature
	 * @param mediumwellTemperature
	 * @param welldoneTemperature
	 * @param custom
	 */
	public DonenessTemperature(int meatTypeIndex, float rareTemperature,
			float mediumrareTemperature, float mediumTemperature,
			float mediumwellTemperature, float welldoneTemperature,
			boolean custom) {
		super();
		this.meatTypeIndex = meatTypeIndex;
		this.rareTemperature = rareTemperature;
		this.mediumrareTemperature = mediumrareTemperature;
		this.mediumTemperature = mediumTemperature;
		this.mediumwellTemperature = mediumwellTemperature;
		this.welldoneTemperature = welldoneTemperature;
		this.custom = custom;
	}
	
	public boolean temperatureAdd(float num) {
		float flag = 0;
		if (num > 0) {
			if (welldoneTemperature > 0) {
				flag = welldoneTemperature + num;
			} else if (mediumwellTemperature > 0) {
				flag = mediumwellTemperature + num;
			} else if (mediumTemperature > 0) {
				flag = mediumTemperature + num;
			} else if (mediumrareTemperature > 0) {
				flag = mediumrareTemperature + num;
			} else if (rareTemperature > 0) {
				flag = rareTemperature + num;
			}
			if (flag > 572) {
				return false;
			} 
		} else if (num < 0) {
			if (rareTemperature > 0) {
				flag = rareTemperature + num;
			} else if (mediumrareTemperature > 0) {
				flag = mediumrareTemperature + num;
			} else if (mediumTemperature > 0) {
				flag = mediumTemperature + num;
			} else if (mediumwellTemperature > 0) {
				flag = mediumwellTemperature + num;
			} else if (welldoneTemperature > 0) {
				flag = welldoneTemperature + num;
			}
			if (flag < 100) {
				return false;
			}
		}
		if (welldoneTemperature > 0 ) welldoneTemperature += num;
		if (mediumwellTemperature > 0 ) mediumwellTemperature += num;
		if (mediumTemperature > 0 ) mediumTemperature += num;
		if (mediumrareTemperature > 0 ) mediumrareTemperature += num;
		if (rareTemperature > 0 ) rareTemperature += num;
		return true;
	}
	
	public boolean setFirstTemperature(float firstTemF) {
		if(firstIndex == -1)
		if (rareTemperature > 0) {
			firstIndex = 0;
		} else if (mediumrareTemperature > 0) {
			firstIndex = 1;
		} else if (mediumTemperature > 0) {
			firstIndex = 2;
		} else if (mediumwellTemperature > 0) {
			firstIndex = 3;
		} else if (welldoneTemperature > 0) {
			firstIndex = 4;
		}
		
		float delta = 0f;
		switch (firstIndex) {
		case 0:
			delta = firstTemF - rareTemperature;
			break;
		case 1:
			delta = firstTemF - mediumrareTemperature;
			break;
		case 2:
			delta = firstTemF - mediumTemperature;
			break;
		case 3:
			delta = firstTemF - mediumwellTemperature;
			break;
		case 4:
			delta = firstTemF - welldoneTemperature;
			break;

		default:
			break;
		}
		return temperatureAdd(delta);
	}
	
	public DonenessTemperature copy() throws CloneNotSupportedException {
		return (DonenessTemperature) this.clone();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (custom ? 1231 : 1237);
		result = prime * result + meatTypeIndex;
		result = prime * result + Float.floatToIntBits(mediumTemperature);
		result = prime * result + Float.floatToIntBits(mediumrareTemperature);
		result = prime * result + Float.floatToIntBits(mediumwellTemperature);
		result = prime * result + Float.floatToIntBits(rareTemperature);
		result = prime * result + Float.floatToIntBits(welldoneTemperature);
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
		DonenessTemperature other = (DonenessTemperature) obj;
		if (custom != other.custom)
			return false;
		if (meatTypeIndex != other.meatTypeIndex)
			return false;
		if (Float.floatToIntBits(mediumTemperature) != Float
				.floatToIntBits(other.mediumTemperature))
			return false;
		if (Float.floatToIntBits(mediumrareTemperature) != Float
				.floatToIntBits(other.mediumrareTemperature))
			return false;
		if (Float.floatToIntBits(mediumwellTemperature) != Float
				.floatToIntBits(other.mediumwellTemperature))
			return false;
		if (Float.floatToIntBits(rareTemperature) != Float
				.floatToIntBits(other.rareTemperature))
			return false;
		if (Float.floatToIntBits(welldoneTemperature) != Float
				.floatToIntBits(other.welldoneTemperature))
			return false;
		return true;
	}

	/**
	 * @return the meatTypeIndex
	 */
	public int getMeatTypeIndex() {
		return meatTypeIndex;
	}

	/**
	 * @param meatTypeIndex the meatTypeIndex to set
	 */
	public void setMeatTypeIndex(int meatTypeIndex) {
		this.meatTypeIndex = meatTypeIndex;
	}

	/**
	 * @return the rareTemperature
	 */
	public float getRareTemperature() {
		return rareTemperature;
	}

	/**
	 * @param rareTemperature the rareTemperature to set
	 */
	public void setRareTemperature(float rareTemperature) {
		this.rareTemperature = rareTemperature;
	}

	/**
	 * @return the mediumrareTemperature
	 */
	public float getMediumrareTemperature() {
		return mediumrareTemperature;
	}

	/**
	 * @param mediumrareTemperature the mediumrareTemperature to set
	 */
	public void setMediumrareTemperature(float mediumrareTemperature) {
		this.mediumrareTemperature = mediumrareTemperature;
	}

	/**
	 * @return the mediumTemperature
	 */
	public float getMediumTemperature() {
		return mediumTemperature;
	}

	/**
	 * @param mediumTemperature the mediumTemperature to set
	 */
	public void setMediumTemperature(float mediumTemperature) {
		this.mediumTemperature = mediumTemperature;
	}

	/**
	 * @return the mediumwellTemperature
	 */
	public float getMediumwellTemperature() {
		return mediumwellTemperature;
	}

	/**
	 * @param mediumwellTemperature the mediumwellTemperature to set
	 */
	public void setMediumwellTemperature(float mediumwellTemperature) {
		this.mediumwellTemperature = mediumwellTemperature;
	}

	/**
	 * @return the welldoneTemperature
	 */
	public float getWelldoneTemperature() {
		return welldoneTemperature;
	}

	/**
	 * @param welldoneTemperature the welldoneTemperature to set
	 */
	public void setWelldoneTemperature(float welldoneTemperature) {
		this.welldoneTemperature = welldoneTemperature;
	}

	/**
	 * @return the custom
	 */
	public boolean isCustom() {
		return custom;
	}

	/**
	 * @param custom the custom to set
	 */
	public void setCustom(boolean custom) {
		this.custom = custom;
	}

}
