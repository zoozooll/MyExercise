/**
 * 
 */
package com.mogoo.ping.vo;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * @author Aaron Lee
 * TODO
 * @Date ����10:55:07  2012-9-18
 */
public class UsedActivityItem{
	private CharSequence name;
	private Drawable icon;
	private String packageName;
	private String className;
	private Intent launchIntent;
	
	public CharSequence getName() {
		return name;
	}
	public void setName(CharSequence name) {
		this.name = name;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Intent getLaunchIntent() {
		return launchIntent;
	}
	public void setLaunchIntent(Intent launchIntent) {
		this.launchIntent = launchIntent;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result
				+ ((packageName == null) ? 0 : packageName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsedActivityItem other = (UsedActivityItem) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (packageName == null) {
			if (other.packageName != null)
				return false;
		} else if (!packageName.equals(other.packageName))
			return false;
		return true;
	}
	
}