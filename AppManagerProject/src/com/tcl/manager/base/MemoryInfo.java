package com.tcl.manager.base;

import android.graphics.drawable.Drawable;

/**
 * @Description:内存实体
 * 
 * @author pengcheng.zhang
 * @date 2014-12-27 下午1:32:00
 * @copyright TCL-MIE
 */
public class MemoryInfo extends BaseInfo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1302321694362930978L;
	/** icon图片 */
	public Drawable icon;
	/** 应用名称 */
	public String appName;
	/** 所占内存大小，单位：MB */
	public String memorySize;
	/** 使用描述 */
	public String usageDesc;
	/** 按钮类型 */
	public ButtonType buttonType;
	
	public enum ButtonType
	{
		START,
		STOP 
	}

	public MemoryInfo()
	{
		;
	}
}
