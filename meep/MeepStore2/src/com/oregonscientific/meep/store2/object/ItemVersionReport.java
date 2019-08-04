package com.oregonscientific.meep.store2.object;

public class ItemVersionReport {
	public static final String INSTALL_STATUS_INSTALLED = "installed";
	public static final String INSTALL_STATUS_UNINSTALLED = "uninstalled";
	public static final String INSTALL_STATUS_WAITING = "waiting";
	public static final String INSTALL_STATUS_DOWNLOADING = "installed";
	
	private int version;
	private String version_name;
	private String package_name;
	private String install_status;
	
	public ItemVersionReport(String packageName, int version, String versionName,  String installStatus){
		setPackageName(packageName);
		setVersion(version);
		setInstallStatus(installStatus);
		setVersionName(versionName);
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getPackageName() {
		return package_name;
	}

	public void setPackageName(String packageName) {
		this.package_name = packageName;
	}

	public String getInstallStatus() {
		return install_status;
	}

	public void setInstallStatus(String installStatus) {
		this.install_status = installStatus;
	}

	public String getVersionName() {
		return version_name;
	}

	public void setVersionName(String versionName) {
		this.version_name = versionName;
	}
}
