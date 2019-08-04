package com.oregonscientific.meep.message.common;

public class FriendGroup {
	private String name = null;
	private Member[] members = null;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Member[] getMembers() {
		return members;
	}
	public void setMembers(Member[] members) {
		this.members = members;
	}
}
