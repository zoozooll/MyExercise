package com.mogoo.ibetask.sales;
/**
 * 销统模块配置信息类 
 * @author 张永辉
 * @date 2011-12-16
 */
 	class SalesConfig {
	/**
	 * 销量模块的APP_ID
	 */
	static final int APPID = 10000000;
	/**
	 * 开机后自动产生销量统计信息的时间（单位：秒）
	 * modify by fdl 原来是60，现在是直接上传
	 */
	static final long AUTO_CREATE_SALES_TIME = 60 ;
	/**
	 * 上传销量信息的接口
	 */
	static final String URL_UPLOAD_SALES_INFO = "/updateUserInfo.do" ;
	
	/**
	 * 销量信息在本地保存的文件名
	 */
	static final String SALESINFO_NAME = "SalesInfoLocal";
	/**
	 * 销量信息在偏好文件中的KEY
	 */
	static final String KEY_SALES_INFO = "salesInfo" ;
	/**
	 * 销量信息在SD卡上保存的文件名
	 */
	static final String SAVE_FILE_NAME = "SalesInfoLocal.cfg";
	
}
