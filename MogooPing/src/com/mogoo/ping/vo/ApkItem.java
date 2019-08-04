package com.mogoo.ping.vo;

public class ApkItem {
	
	private String apkId;
	
	private String apkName;
	
	private int versionCode;
	
	private String versionStr;
	
	private String iconRemote;
	
	private String iconLocal;
	
	private String apkAddressRemote;
	
	private String apkAddressLocal;
	
	private String packageName;
	
	private int type;
	
	public ApkItem() {
		super();
	}

	public ApkItem(String apkId, String apkName, int versionCode,
			String versionStr, String iconRemote, String iconLocal,
			String apkAddressRemote, String apkAddressLocal) {
		super();
		this.apkId = apkId;
		this.apkName = apkName;
		this.versionCode = versionCode;
		this.versionStr = versionStr;
		this.iconRemote = iconRemote;
		this.iconLocal = iconLocal;
		this.apkAddressRemote = apkAddressRemote;
		this.apkAddressLocal = apkAddressLocal;
	}

	public String getApkId() {
		return apkId;
	}

	public void setApkId(String apkId) {
		this.apkId = apkId;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionStr() {
		return versionStr;
	}

	public void setVersionStr(String versionStr) {
		this.versionStr = versionStr;
	}

	public String getIconRemote() {
		return iconRemote;
	}

	public void setIconRemote(String iconRemote) {
		this.iconRemote = iconRemote;
	}

	public String getIconLocal() {
		return iconLocal;
	}

	public void setIconLocal(String iconLocal) {
		this.iconLocal = iconLocal;
	}

	public String getApkAddressRemote() {
		return apkAddressRemote;
	}

	public void setApkAddressRemote(String apkAddressRemote) {
		this.apkAddressRemote = apkAddressRemote;
	}

	public String getApkAddressLocal() {
		return apkAddressLocal;
	}

	public void setApkAddressLocal(String apkAddressLocal) {
		this.apkAddressLocal = apkAddressLocal;
	}
	
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof ApkItem)) {
			return false;
		}
		if (super.equals(o) || packageName.equals(((ApkItem)o).packageName)) {
			return true;
		}
		return false;
	}

}
