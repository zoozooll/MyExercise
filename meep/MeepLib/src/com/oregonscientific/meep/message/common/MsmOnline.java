package com.oregonscientific.meep.message.common;

public class MsmOnline extends MeepServerMessage{
	public MsmOnline(String proc, String opcode) {
		super(proc, opcode);
		// TODO Auto-generated constructor stub
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	private String userid = null;
	
	
}
