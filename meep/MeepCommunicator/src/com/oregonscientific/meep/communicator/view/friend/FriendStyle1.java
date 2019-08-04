package com.oregonscientific.meep.communicator.view.friend;

import android.content.Context;

import com.oregonscientific.meep.communicator.Friend;
import com.oregonscientific.meep.communicator.R;

/**
 * Friend view of style 1
 */
public class FriendStyle1 extends BaseFriend {
	
	public FriendStyle1(Context context, Friend friend) {
		super(context, friend);
	}
	
	@Override
	public FriendViewProperty getProperty() {
		return new FriendViewProperty(R.layout.friend_style1, getResources().getDrawable(R.drawable.custom_style1), null, 15f, 0f, true);
	}
	
}