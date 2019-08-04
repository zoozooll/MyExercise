/**
 * 
 */
package com.idt.bw.ble;

import java.io.Serializable;

/**
 * @author aaronli
 *
 */
public class WeightData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// The field is that the scale weight's return showing
	private int flag;
	
	// The number of weight
	private float weight;
	
	// The weight unit. 0 means KG, 1 means LB;
	private int unit;

	/**
	 * 
	 */
	public WeightData() {
		super();
	}

	/**
	 * @param flag
	 * @param weight
	 * @param unit
	 */
	public WeightData(int flag, float weight, byte unit) {
		super();
		this.flag = flag;
		this.weight = weight;
		this.unit = unit;
	}

	/**
	 * @return the flag
	 */
	public int getFlag() {
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}

	/**
	 * @return the weight
	 */
	public float getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * @return the unit
	 */
	public int getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(int unit) {
		this.unit = unit;
	}
	
	
	
}
