/**
 * 
 */
package com.tcl.manager.base;

import android.graphics.drawable.Drawable;

/**
 * @author zuokang.li
 *
 */
public class OptimizedAppInfo extends BaseInfo {
	
	/** The icon of this application,*/
	public Drawable icon;
	
	/** The application's name */
	public String appName;
	
	/** The application's state , it will be ON or OFF */
	public boolean onOff;
	
	/** The category of optimized, it will be {@link CATEGORY_BATTERY} or {@link CATEGORY_DATA} */
	public  int category;
	
	public static final int CATEGORY_BATTERY = 0;
	
	public static final int CATEGORY_DATA = 1;
}
