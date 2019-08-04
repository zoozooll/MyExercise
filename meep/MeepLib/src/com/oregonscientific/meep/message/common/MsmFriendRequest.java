package com.oregonscientific.meep.message.common;

public class MsmFriendRequest extends MeepServerMessage {

	private String meeptag = null;
	private String nickname = null;
	
	public MsmFriendRequest(String proc, String opcode) {
		super(proc, opcode);
		// TODO Auto-generated constructor stub
	}

	public String getMeeptag() {
		return meeptag;
	}

	public void setMeeptag(String meeptag) {
		this.meeptag = meeptag;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
