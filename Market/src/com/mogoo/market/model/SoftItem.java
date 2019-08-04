package com.mogoo.market.model;

public class SoftItem {
	public static final int FLAG_INSTALL = 0;
	public static final int FLAG_SYSTEM = 1;
	/** 软件包名 唯一标志 */
	public String package_name;
	/** 名称 */
	public String app_name;
	/** 0代表三方应用，非0代表系统应用 */
	public int softFlag;
	/** 软件版本号 */
	public int versionCode;
	/** 是否有更新 */
	public boolean updateable;
}
