package com.oregonscientific.meep.message.common;

public class MsmDeleteFriend extends MeepServerMessage {

	private FriendGroup[] contactList = null;
	private String affectedUserId = null;
	private String friendid = null;

	public MsmDeleteFriend(String proc, String opcode) {
		super(proc, opcode);
	}

	public String getFriendid() {
		return friendid;
	}

	public void setFriendid(String friendid) {
		this.friendid = friendid;
	}

	public FriendGroup[] getContactList() {
		return contactList;
	}

	public void setContactList(FriendGroup[] friendGroup) {
		this.contactList = friendGroup;
	}
	
	public String getAffectedUserId() {
		return affectedUserId;
	}

	public void setAffectedUserId(String affectedUserId) {
		this.affectedUserId = affectedUserId;
	}


}
