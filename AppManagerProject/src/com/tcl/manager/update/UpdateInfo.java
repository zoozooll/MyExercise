package com.tcl.manager.update;

/**
 * @author jiaquan.huang
 */
public class UpdateInfo {
	/** 客户端最新版本代码 */
	public int versionCode;
	/** 客户端最新版本下载地址 */
	public String downloadUrl;
	/** 是否强制更新，0：不强制，1：强制更新 0为默认值 */
	public int forceUpdate;
	/** 客户端最新版本号 */
	public String versionName;
	/** 客户端名称 */
	public String apkName;
	/** 软件包大小 */
	public String size;
	/** 升级说明 */
	public String description;
    /** 文件存储路径 **/
    public String filePath;
}
