package com.butterfly.vv.db;

import com.j256.ormlite.field.DatabaseField;

public class ImagesCount {
	@DatabaseField(id = true)
	private String id;
	@DatabaseField
	private String year;
	@DatabaseField
	private String month;
	@DatabaseField
	private String day;
	@DatabaseField
	private String picNum;
	@DatabaseField
	private String uid;

	public static class ColumnName {
		public static String uid = "uid";
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getPicNum() {
		return picNum;
	}
	public void setPicNum(String picNum) {
		this.picNum = picNum;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
}
