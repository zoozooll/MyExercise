package com.oregonscientific.meep.message.common;

//import com.google.gson.JsonObject;

public class MsmGetFriend extends MeepServerMessage{	
	
	private FriendGroup[] friends = null;
	
	public MsmGetFriend(String proc, String opcode) {
		super(proc, opcode);		
	}

	public FriendGroup[] getFriends() {
		return friends;
	}



	public void setFriends(FriendGroup friends[]) {
		this.friends = friends;
	}

}
