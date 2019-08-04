package com.oregonscientific.meep.communicator.view.conversation;

import java.util.Vector;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.oregonscientific.meep.communicator.ConversationMessage;
import com.oregonscientific.meep.communicator.User;

/**
 * Array Adapter for showing a vector of conversation messages
 */
public class ConversationMessageAdapter extends ArrayAdapter<ConversationMessage> {
	
	private Context mContext;
	private Vector<ConversationMessage> mMessageList;
	private User currentUser;
	
	/**
	 * Array Adapter Constructor
	 * @param context the context to operate in
	 * @param values the values to be viewed
	 * @param user current user
	 */
	public ConversationMessageAdapter(
			Context context,
			Vector<ConversationMessage> values,
			User user) {
		super(context, 0, values);
		
		this.mContext = context;
		this.mMessageList = values;
		this.currentUser = user;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		BaseConversationMessage baseConversationMessage;
		
		if (convertView != null && convertView.getTag().equals(mMessageList.get(position))) {
			baseConversationMessage = (BaseConversationMessage) convertView;
		} else {
			baseConversationMessage = new BaseConversationMessage(mContext, mMessageList.get(position), currentUser);
			baseConversationMessage.setTag(mMessageList.get(position));
			if (!mMessageList.get(position).getIsIncomingMessage()) {
				baseConversationMessage.setGravity(Gravity.RIGHT);
			}
		}
		
		return baseConversationMessage;
	}
}