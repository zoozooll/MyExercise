package com.oregonscientific.meep.together.bean;

public class MigrateChild {
	private String userid;
	private String sn;
	
	public MigrateChild(String userid, String sn) {
		this.userid = userid;
		this.sn = sn;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
}
