package com.mogoo.market.model;

import com.mogoo.market.utils.ToolsUtils;

/**
 * 下载记录信息
 */
public class DownloadInfo 
{
	private String download_id = null;
	private String app_id = null;
	private String download_url = null;
	private String save_path = null;
	private String name = null;
	private String size = null;
	private String icon_url = null;
	private String rating = null;
	private String versionName = null;
	private int versionCode = 0;
	private String packageName = null;
	
	public DownloadInfo(String download_id,String app_id, String download_url,
			String save_path, String name, String size,String icon_url, String rating,
			String versionName,int versionCode,String packageName) 
	{
		super();
		this.download_id = download_id;
		this.app_id = app_id;
		this.download_url = download_url;
		this.save_path = save_path;
		this.name = name;
		this.size = size;
		this.icon_url = icon_url;
		this.rating = rating;
		this.versionName = versionName;
		this.versionCode = versionCode;
		this.packageName = packageName;
	}
	
	@Override
	public String toString() 
	{
		// TODO Auto-generated method stub
		return "DownloadInfo : download_id = "+download_id+" app_id "+app_id+" download_url "+download_url+
				" save_path "+save_path+" name "+name+" size "+size+
				" icon_url "+icon_url+" rating "+rating+" versionName "+versionName+" versionCode "+versionCode+" packageName "+packageName;
	}

	public String getDownload_id() {
		return download_id;
	}
	
	public String getApp_id() {
		return app_id;
	}

	public void setDownload_id(String download_id) {
		this.download_id = download_id;
	}

	public String getDownload_url() {
		return download_url;
	}

	public void setDownload_url(String download_url) {
		this.download_url = download_url;
	}

	public String getSave_path() {
		return save_path;
	}

	public void setSave_path(String save_path) {
		this.save_path = save_path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}
	
	public String getFormatedSize() {
		return ToolsUtils.getSizeStr(size);
	}

	public void setSize(String size) {
		this.size = size;
	}
	
	public String getIcon_url() {
		return icon_url;
	}

	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}
	
	public String getVersionName() {
		return versionName;
	}
	
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	
	public int getVersionCode() {
		return versionCode;
	}
	
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	
	public String getPackageName() {
		return packageName;
	}
}
