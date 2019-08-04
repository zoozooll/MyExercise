package com.tcl.manager.base;

import android.graphics.drawable.Drawable;

/**
 * @Description:sdcard存储实体
 * 
 * @author pengcheng.zhang
 * @date 2014-12-27 下午1:32:00
 * @copyright TCL-MIE
 */
public class StorageInfo extends BaseInfo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8171121342659328217L;
	/** icon图片 */
	public Drawable icon;
	/** 应用名称 */
	public String appName;
	/** 所占内存大小，单位：MB */
	public String storageSize;
	/** 使用描述 */
	public String usageDesc;
	/** 按钮类型 */
	public ButtonType buttonType;
	
	public enum ButtonType
	{
		INSTALL,
		UNINSTALL 
	}

	public StorageInfo()
	{
		;
	}
}
