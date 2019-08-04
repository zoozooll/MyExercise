package com.oregonscientific.meep.message.common;

public class MsmAddFriend extends MeepServerMessage {

	public MsmAddFriend(String proc, String opcode) {
		super(proc, opcode);
	}

	private String meeptag = null;
	private String message = null;
	private String group = null;

	public String getMeeptag() {
		return meeptag;
	}

	public void setMeeptag(String meeptag) {
		this.meeptag = meeptag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
}
