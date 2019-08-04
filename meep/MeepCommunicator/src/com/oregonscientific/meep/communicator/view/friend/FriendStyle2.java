package com.oregonscientific.meep.communicator.view.friend;

import android.content.Context;

import com.oregonscientific.meep.communicator.Friend;
import com.oregonscientific.meep.communicator.R;

/**
 * Friend view of style 2
 */
public class FriendStyle2 extends BaseFriend {
	
	public FriendStyle2(Context context, Friend friend) {
		super(context, friend);
	}
	
	@Override
	public FriendViewProperty getProperty() {
		return new FriendViewProperty(R.layout.friend_style2, getResources().getDrawable(R.drawable.custom_style2), null, 15f, 0f, true);
	}
	
}