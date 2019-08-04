package com.tcl.manager.base;

import android.graphics.drawable.Drawable;

/**
 * @Description:电量实体
 * 
 * @author pengcheng.zhang
 * @date 2014-12-27 下午2:18:15
 * @copyright TCL-MIE
 */
public class BatteryInfo extends BaseInfo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -388029995485739948L;
	/** icon图片 */
	public Drawable icon;
	/** 应用名称 */
	public String appName; 
	/** 所占电量 */
	public int percent;
	/** 使用描述 */
	public String usageDesc;
	/** 按钮类型 */
	public ButtonType buttonType; 

	public enum ButtonType
	{
		ON, OFF
	}
	
	public BatteryInfo()
	{
		;
	}
}
