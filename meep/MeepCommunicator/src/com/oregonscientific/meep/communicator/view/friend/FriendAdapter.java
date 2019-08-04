package com.oregonscientific.meep.communicator.view.friend;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.oregonscientific.meep.communicator.Friend;
import com.oregonscientific.meep.communicator.activity.CommunicatorActivity;

/**
 * Array Adapter for showing a list of friends
 */
public class FriendAdapter extends ArrayAdapter<Friend> {
	
	private final int LAYOUT_WIDTH = 300;
	private final int LAYOUT_HEIGHT = 200;
	
	private final int PADDING_LEFT = 0;
	private final int PADDING_TOP = 20;
	private final int PADDING_RIGHT = 0;
	private final int PADDING_BOTTOM = 20;
	
	private Context mContext;
	private List<Friend> mFriendList;
	
	/**
	 * Array Adapter Constructor
	 * @param context the context to operate in
	 * @param values the values to be viewed
	 */
	public FriendAdapter(Context context, List<Friend> values) {
		super(context, 0, values);
		
		this.mContext = context;
		this.mFriendList = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		BaseFriend baseFriend = null;
		
		if (mFriendList == null || mFriendList.size() <= position)
			return null;
		
		if (convertView != null 
				&& convertView.getTag() != null 
				&& mFriendList.get(position).getName() != null 
				&& convertView.getTag().equals(mFriendList.get(position).getName())) {
			baseFriend = (BaseFriend) convertView;
		} else {
			// if it's not recycled, initialize friend
			int number = position % 4;
			switch (number) {
				case 0:
					baseFriend = new FriendStyle1(mContext, mFriendList.get(position));
					break;
				case 1:
					baseFriend = new FriendStyle2(mContext, mFriendList.get(position));
					break;
				case 2:
					baseFriend = new FriendStyle3(mContext, mFriendList.get(position));
					break;
				case 3:
					baseFriend = new FriendStyle4(mContext, mFriendList.get(position));
					break;
				default:
					baseFriend = new FriendStyle1(mContext, mFriendList.get(position));
					break;
			}
			baseFriend.setPadding(PADDING_LEFT, PADDING_TOP, PADDING_RIGHT, PADDING_BOTTOM);
			baseFriend.setLayoutParams(new GridView.LayoutParams(LAYOUT_WIDTH, LAYOUT_HEIGHT));
			baseFriend.setTag(mFriendList.get(position).getName());
			CommunicatorActivity activity = (CommunicatorActivity) mContext;
			baseFriend.setUnreadCount(activity.getUnreadMessageCount(mFriendList.get(position)));
			
		}

		return baseFriend;
	}
	
	
	public void setFriendList(List<Friend> friendList) {
		mFriendList =  friendList;
		notifyDataSetChanged();
	}
	
	
}