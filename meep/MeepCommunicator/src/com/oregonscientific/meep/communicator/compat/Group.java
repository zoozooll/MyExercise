package com.oregonscientific.meep.communicator.compat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.oregonscientific.meep.communicator.Friend;

public class Group {

	@SerializedName("name")
	@Expose
	private String name;
	
	@SerializedName("members")
	@Expose
	private List<FriendCompat> members;
	
	
	public List<Friend> toFriends() {
		
		List<Friend> friends = new ArrayList<Friend>();
		if (members != null && members.size() > 0) {
			Iterator<FriendCompat> iterator = members.iterator();
			while (iterator.hasNext()) {
			
				FriendCompat frdCompat = iterator.next();
				friends.add(frdCompat.toFriend());
			}
		}
		return friends;
	}
	
}
