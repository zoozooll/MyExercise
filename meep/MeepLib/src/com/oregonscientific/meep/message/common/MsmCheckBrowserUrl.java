package com.oregonscientific.meep.message.common;

public class MsmCheckBrowserUrl extends MeepServerMessage {

	public MsmCheckBrowserUrl(String proc, String opcode) {
		super(proc, opcode);
	}

	private String url = null;
	private String access = null;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

}