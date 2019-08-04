package com.oregonscientific.meep.communicator.activity;

import com.oregonscientific.meep.communicator.Conversation;
import com.oregonscientific.meep.communicator.ConversationMessage;

public interface IConversationInterface {

	public void onSendButtonPressed(ConversationFragment fragment, String message, ConversationMessage conversationMsg); 
	public void onConversationTabChanged(ConversationFragment fragment, Conversation conversation); 

}
