package com.oregonscientific.meep.communicator.compat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.oregonscientific.meep.communicator.Friend;

public class FriendCompat {

	@SerializedName("userid")
	@Expose
	private String userId;
	
	@SerializedName("name")
	@Expose
	private String name;
	
	@SerializedName("avatar")
	@Expose
	private String avatar;
	
	@SerializedName("online")
	@Expose
	private boolean online;
	
	public Friend toFriend() {
		Friend frd = new Friend(name, avatar, userId, Boolean.valueOf(online));
		return frd;
	}
	
}
