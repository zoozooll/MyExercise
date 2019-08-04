package com.oregonscientific.meep.message.common;

public class MsmAssignFriendGroup extends MeepServerMessage{
	
	private FriendGroup[] contactList = null;
	private String groupName = null;


	public MsmAssignFriendGroup(String proc, String opcode) {
		super(proc, opcode);
	}

	private String friendid = null;
	private String group = null;

	public String getFriendid() {
		return friendid;
	}

	public void setFriendid(String friendid) {
		this.friendid = friendid;
	}


	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public FriendGroup[] getContactList() {
		return contactList;
	}

	public void setContactList(FriendGroup[] friendGroup) {
		this.contactList = friendGroup;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}
