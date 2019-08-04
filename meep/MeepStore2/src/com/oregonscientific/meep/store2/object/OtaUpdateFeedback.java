package com.oregonscientific.meep.store2.object;


public class OtaUpdateFeedback {
	
	private int code;
	private String status;
	private String versionName;
	private String changelog;
	private String url;
	
	public OtaUpdateFeedback(int code, String status)
	{
		this.code = code;
		this.status = status;
	}
	
	public OtaUpdateFeedback(int code, String status,String versionName,String changelog,String url)
	{
		this.code = code;
		this.status = status;
		this.versionName = versionName;
		this.changelog = changelog;
		this.url = url;
	}
	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getChangelog() {
		return changelog;
	}

	public void setChangelog(String changelog) {
		this.changelog = changelog;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
