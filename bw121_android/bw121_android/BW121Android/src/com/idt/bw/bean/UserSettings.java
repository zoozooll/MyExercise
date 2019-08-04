/**
 * 
 */
package com.idt.bw.bean;

import java.io.Serializable;

/**
 * @author aaronli
 *
 */
public class UserSettings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7231960864499613159L;

	/**
	 * 
	 */
	private long id;
	
	private long userId;
	
	private float targetWeight;
	private float clothweight;
	
	private boolean isClothlOn;
	private boolean isTargetlOn;
	private boolean isGeneralOn;
	private boolean isNotifyOn;
	
	private String notifyTime;
	
	private String[] notifyLoop;
	
	private float currentHeight;
	
	private String notifyDate;
	
	private boolean isNotifyFeature;
	
	


	public float getClothweight() {
		return clothweight;
	}



	public void setClothweight(float clothweight) {
		this.clothweight = clothweight;
	}



	public boolean isClothlOn() {
		return isClothlOn;
	}



	public void setClothlOn(boolean isClothlOn) {
		this.isClothlOn = isClothlOn;
	}



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



	public boolean isTargetlOn() {
		return isTargetlOn;
	}



	public void setTargetlOn(boolean isTargetlOn) {
		this.isTargetlOn = isTargetlOn;
	}



	public boolean isGeneralOn() {
		return isGeneralOn;
	}



	public void setGeneralOn(boolean isGeneralOn) {
		this.isGeneralOn = isGeneralOn;
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
	 * @return the isNotifyOn
	 */
	public boolean isNotifyOn() {
		return isNotifyOn;
	}



	/**
	 * @param isNotifyOn the isNotifyOn to set
	 */
	public void setNotifyOn(boolean isNotifyOn) {
		this.isNotifyOn = isNotifyOn;
	}



	/**
	 * @return the notifyTime
	 */
	public String getNotifyTime() {
		return notifyTime;
	}



	/**
	 * @param notifyTime the notifyTime to set
	 */
	public void setNotifyTime(String notifyTime) {
		this.notifyTime = notifyTime;
	}



	/**
	 * @return the notifyLoop
	 */
	public String[] getNotifyLoop() {
		return notifyLoop;
	}



	/**
	 * @param notifyLoop the notifyLoop to set
	 */
	public void setNotifyLoop(String[] notifyLoop) {
		this.notifyLoop = notifyLoop;
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
	 * @return the notifyDate
	 */
	public String getNotifyDate() {
		return notifyDate;
	}



	/**
	 * @param notifyDate the notifyDate to set
	 */
	public void setNotifyDate(String notifyDate) {
		this.notifyDate = notifyDate;
	}



	/**
	 * @return the isNotifyFeature
	 */
	public boolean isNotifyFeature() {
		return isNotifyFeature;
	}



	/**
	 * @param isNotifyFeature the isNotifyFeature to set
	 */
	public void setNotifyFeature(boolean isNotifyFeature) {
		this.isNotifyFeature = isNotifyFeature;
	}



	public static enum NotifyLoopMode {
		EveryDay,EveryWeek, Never;
		
		public static NotifyLoopMode get(int ordina){
			switch (ordina) {
			case 0:
				return  EveryDay;
			case 1:
				return  EveryWeek;
			default:
				return Never;
			}
			
		}
	}
}


