package com.oregonscientific.meep.message.common;

public class MsmReportDownload extends MeepServerMessage{

	public MsmReportDownload(String proc, String opcode) {
		super(proc, opcode);
		// TODO Auto-generated constructor stub
	}
	private String downloadstatus = null;
	private int percentage = 0;
	private String filename = null;
	
	public String getDownloadstatus() {
		return downloadstatus;
	}
	public void setDownloadstatus(String downloadstatus) {
		this.downloadstatus = downloadstatus;
	}
	public int getPercentage() {
		return percentage;
	}
	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}

}
