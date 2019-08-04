package com.idt.bw.bean;

import java.io.Serializable;

import android.os.Parcelable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6158697341356409107L;
	public static final String TABLENAME = "user";
	private String id;
	private String userPhoto;
	private String userName;
	private String userBirth;
	private String userGender;
	private String userPregnancyWeeks;
	private String userPregnancyDays;
	private String userHeight;
	private String userCategory;
	private String userWeightUnit;
	private String userHeightUnit;
	
	
	public String getUserWeightUnit() {
		return userWeightUnit;
	}
	public void setUserWeightUnit(String userWeightUnit) {
		this.userWeightUnit = userWeightUnit;
	}
	public String getUserHeightUnit() {
		return userHeightUnit;
	}
	public void setUserHeightUnit(String userHeightUnit) {
		this.userHeightUnit = userHeightUnit;
	}
	public String getUserPregnancyWeeks() {
		return userPregnancyWeeks;
	}
	public void setUserPregnancyWeeks(String userPregnancyWeeks) {
		this.userPregnancyWeeks = userPregnancyWeeks;
	}
	public String getUserPregnancyDays() {
		return userPregnancyDays;
	}
	public void setUserPregnancyDays(String userPregnancyDays) {
		this.userPregnancyDays = userPregnancyDays;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserBirth() {
		return userBirth;
	}
	public void setUserBirth(String userBirth) {
		this.userBirth = userBirth;
	}
	public String getUserGender() {
		return userGender;
	}
	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}
	public String getUserHeight() {
		return userHeight;
	}
	public void setUserHeight(String userHeight) {
		this.userHeight = userHeight;
	}
	public String getUserCategory() {
		return userCategory;
	}
	public void setUserCategory(String userCategory) {
		this.userCategory = userCategory;
	}
	public static String getTablename() {
		return TABLENAME;
	}
}

