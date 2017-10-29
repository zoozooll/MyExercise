package com.iskinfor.servicedata.pojo;

import java.util.ArrayList;

/**
 * 教师用户
 * @author Administrator
 *
 */
public class Teacher extends User{
	/**
	 * 教师的学生们；
	 */
	private ArrayList<Child> childs;
	/**
	 * 是否为名师
	 */
	private boolean isFamles;
	/**
	 * 任教的科目
	 * @return
	 */
	private String couserName;
	
	public String getCouserName() {
		return couserName;
	}
	public void setCouserName(String couserName) {
		this.couserName = couserName;
	}
	public ArrayList<Child> getChilds() {
		return childs;
	}
	public void setChilds(ArrayList<Child> childs) {
		this.childs = childs;
	}
	public boolean isFamles() {
		return isFamles;
	}
	public void setFamles(boolean isFamles) {
		this.isFamles = isFamles;
	}

	
}
