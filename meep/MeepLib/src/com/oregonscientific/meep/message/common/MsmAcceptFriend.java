package com.oregonscientific.meep.message.common;

public class MsmAcceptFriend extends MeepServerMessage{

	private String meeptag = null;
	private FriendGroup[] contactList = null;
	private String affectedUserId = null;
	
	public MsmAcceptFriend(String proc, String opcode) {
		super(proc, opcode);
		// TODO Auto-generated constructor stub
	}

	public String getMeeptag() {
		return meeptag;
	}

	public void setMeeptag(String meeptag) {
		this.meeptag = meeptag;
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
