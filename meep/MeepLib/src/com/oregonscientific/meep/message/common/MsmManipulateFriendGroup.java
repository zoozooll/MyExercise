package com.oregonscientific.meep.message.common;

public class MsmManipulateFriendGroup extends MeepServerMessage {

	public MsmManipulateFriendGroup(String proc, String opcode) {
		super(proc, opcode);
	}

	private String friendid = null;
	private String iconAddr = null;
	private FriendGroup[] contactList = null;
	private String groupName = null;
	private int groupId = -1;
			
	public int getGroupId() {
		return groupId;
	}
	
	public void setGroupId(int id) {
		groupId = id;
	}
	
	public String getFriendid() {
		return friendid;
	}

	public void setFriendid(String friendid) {
		this.friendid = friendid;
	}

	private String group = null;

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getIconAddr() {
		return iconAddr;
	}

	public void setIconAddr(String iconAddr) {
		this.iconAddr = iconAddr;
	}

	public FriendGroup[] getContactList() {
		return contactList;
	}

	public void setContactList(FriendGroup[] contactList) {
		this.contactList = contactList;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	

}
