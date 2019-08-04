package com.tcl.manager.statics;

public class OAEvent {
	/**
	 * 成功上报启动事件
	 */
	public final static String ACTIVE_BOOT = "ACB";
	/**
	 * 未成功上报启动事件
	 */
	public final static String ALL_BOOT = "ALB";
	
	/**
	 * 页面浏览事件 
	 */
	public final static String PAGE_NAVIGATE = "SRC";
	/**
	 * 页面浏览事件 
	 */
	public final static String N_PAGE_NAVIGATE = "NSRC";
	/**
	 * 页面浏览事件 
	 */
	public final static String BANNER_CLICK = "CRC";
	
	/**
	 * 应用 下载事件 
	 */
	public final static String APP_DOWNLOAD = "DRC";
	/**
	 * N应用 下载事件 
	 */
	public final static String N_APP_DOWNLOAD = "NDRC";
	
	/**
	 * 应用安装事件
	 */
	public final static String APP_INSTALL  = "IRC";
	/**
	 * N应用安装事件
	 */
	public final static String N_APP_INSTALL  = "NIRC";
	
	/**
	 * PUSH事件
	 */
	public final static String APP_PUSH  = "PSSRC";
	/**
	 * N PUSH事件
	 */
	public final static String N_APP_PUSH  = "NPSSRC";
}
