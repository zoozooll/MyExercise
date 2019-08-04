package com.tcl.manager.base;

import android.graphics.drawable.Drawable;

/**
 * @Description:电量实体
 * 
 * @author pengcheng.zhang
 * @date 2014-12-27 下午2:30:38
 * @copyright TCL-MIE
 */
public class DataInfo extends BaseInfo
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5150498409947949695L;
	/** icon图片 */
	public Drawable				icon;
	/** 应用名称 */
	public String				appName;
	/** 所用流量 */
	public String				dataSize;
	/** 平均流量 */
	public String				avarageSize;
	/** 使用描述 */
	public String				usageDesc;
	/** 按钮类型 */
	public ButtonType			buttonType;

	public enum ButtonType
	{
		ON, OFF
	}

	public DataInfo()
	{
		;
	}
}
