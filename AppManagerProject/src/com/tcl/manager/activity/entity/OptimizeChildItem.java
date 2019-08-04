package com.tcl.manager.activity.entity;

import android.graphics.drawable.Drawable;

/** 
 * @Description: 可优化子项
 * @author wenchao.zhang 
 * @date 2014年12月24日 上午11:06:20 
 * @copyright TCL-MIE
 */

public class OptimizeChildItem {
	//app图片
	private Drawable appIcon;
	//app名字
	private String appName = "";
	
	//app描述，经常用
	private String appDesc = "";
	
	//app 百分比数据
	private String data = "";
	//app是否已选
	private boolean isSelected;
	/**包名*/
	private String pgkName = "";
	
	private int needAddScore = 0;
	
	private int type = 0;
	
	public static final int TYPE_DATA = 1;
	public static final int TYPE_BATTERY = 2;
	
	public Drawable getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppDesc() {
		return appDesc;
	}
	public void setAppDesc(String appDesc) {
		this.appDesc = appDesc;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public String getPgkName() {
		return pgkName;
	}
	public void setPgkName(String pgkName) {
		this.pgkName = pgkName;
	}
	public int getNeedAddScore() {
		return needAddScore;
	}
	public void setNeedAddScore(int needAddScore) {
		this.needAddScore = needAddScore;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
	
	
}
