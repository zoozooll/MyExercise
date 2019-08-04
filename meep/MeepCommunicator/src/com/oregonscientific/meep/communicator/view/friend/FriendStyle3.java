package com.oregonscientific.meep.communicator.view.friend;

import android.content.Context;

import com.oregonscientific.meep.communicator.Friend;
import com.oregonscientific.meep.communicator.R;

/**
 * Friend view of style 3
 */
public class FriendStyle3 extends BaseFriend {
	
	public FriendStyle3(Context context, Friend friend) {
		super(context, friend);
	}
	
	@Override
	public FriendViewProperty getProperty() {
		return new FriendViewProperty(R.layout.friend_style3, getResources().getDrawable(R.drawable.custom_style3), null, -10f, 5f, false);
	}
	
}