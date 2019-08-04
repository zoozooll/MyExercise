/**
 * 
 */
package com.idt.bw.bean;

import java.io.Serializable;

/**
 * @author aaronli
 *
 */
public class Record implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2471478482751237099L;

	
	private long id;
	
	private float weight;
	
	private int unit;
	
	private long userId;
	
	private float currentHeight;
	
	private String datetime;
	
	private float targetWeight;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
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

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the currentHeight
	 */
	public float getCurrentHeight() {
		return currentHeight;
	}

	/**
	 * @param currentHeight the currentHeight to set
	 */
	public void setCurrentHeight(float currentHeight) {
		this.currentHeight = currentHeight;
	}

	/**
	 * @return the datetime
	 */
	public String getDatetime() {
		return datetime;
	}

	/**
	 * @param datetime the datetime to set
	 */
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	/**
	 * @return the targetWeight
	 */
	public float getTargetWeight() {
		return targetWeight;
	}

	/**
	 * @param targetWeight the targetWeight to set
	 */
	public void setTargetWeight(float targetWeight) {
		this.targetWeight = targetWeight;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
