package com.mogoo.components.download;

 /**
 * 下载任务信息 
 */
public class MogooDownloadInfo
{
	private String downloadUrl;
	private String downloadPosition;
	private int downloadStatus;
	private String downloadNotificationPackage;
	private long downloadTotolBytes;
	private long downloadCurrentBytes;
	private String downloadTitle;
	private int control;
	
	public MogooDownloadInfo(String downloadUrl, String downloadPosition,
			int downloadStatus, String downloadNotificationPackage,
			long downloadTotolBytes, long downloadCurrentBytes,
			String downloadTitle,int control) 
	{
		super();
		this.downloadUrl = downloadUrl;
		this.downloadPosition = downloadPosition;
		this.downloadStatus = downloadStatus;
		this.downloadNotificationPackage = downloadNotificationPackage;
		this.downloadTotolBytes = downloadTotolBytes;
		this.downloadCurrentBytes = downloadCurrentBytes;
		this.downloadTitle = downloadTitle;
		this.control = control;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public String getDownloadPosition() {
		return downloadPosition;
	}

	public int getDownloadStatus() {
		return downloadStatus;
	}

	public String getDownloadNotificationPackage() {
		return downloadNotificationPackage;
	}

	public long getDownloadTotolBytes() {
		return downloadTotolBytes;
	}

	public long getDownloadCurrentBytes() {
		return downloadCurrentBytes;
	}

	public String getDownloadTitle() {
		return downloadTitle;
	}
	
	public int getControl() {
		return control;
	}
	
}
