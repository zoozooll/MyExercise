package com.oregonscientific.meep.message.common;

public class MsmReportAppVersion extends MeepServerMessage {
	private String appName = null;
	private int versionCode = 0;
	private String versionName = null;
	private long firstInstallTime = 0;
	private long lastUpdateTime = 0;

	public MsmReportAppVersion(String proc, String opcode) {
		super(proc, opcode);
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public long getFirstInstallTime() {
		return firstInstallTime;
	}

	public void setFirstInstallTime(long firstInstallTime) {
		this.firstInstallTime = firstInstallTime;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
