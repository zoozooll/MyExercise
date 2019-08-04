package com.oregonscientific.meep.message.common;

public class MsmSearchFriend extends MeepServerMessage {

	private String meeptag = null;
	private String nickname =  null;
	private String avatar = null;

	public MsmSearchFriend(String proc, String opcode) {
		super(proc, opcode);
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

}
