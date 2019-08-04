package com.oregonscientific.meep.message.common;

public class MsmSetNickName extends MeepServerMessage {

	private String nickname = null;
	private String avatar = null;
	private String meeptag = null;
	
	public MsmSetNickName(String proc, String opcode) {
		super(proc, opcode);
		
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getMeeptag() {
		return meeptag;
	}

	public void setMeeptag(String meeptag) {
		this.meeptag = meeptag;
	}

}
