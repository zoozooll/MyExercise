package com.oregonscientific.meep.communicator;

import com.oregonscientific.meep.communicator.CommunicatorServiceHandler.FriendRequestStatus;

public interface ICommunicatorServiceCallback {

	
	/** 
	 * Callback when service is binded 
	 *  
	 **/
	public void onServiceConnected();
	
	/** 
	 * Callback when service is unbinded 
	 *  
	 **/	
	public void onServiceDisconnected();

	/**
	 * Callback when server responds to get friend list request
	 * 
	 * @param erroMessage The error message in the response of server, null when the request was successful
	 */
	public void onFriendListReceived(String erroMessage);
	
	/**
	 * Callback when server responds to search friend request
	 * 
	 * @param meepTag The meep tag of the friend who is being searched
	 * @param nickname The nickname of the friend who is being searched
	 * @param erroMessage The error message in the response of server, null when the request was successful
	 */
	public void onFriendSearched(String meepTag, String nickname, String errorMessage); 
	
	/**
	 * Callback when server responds to friend-request request
	 * 
	 * @param meepTag The meep tag of the friend
	 * @param erroMessage The error message in the response of server, null when the request was successful
	 */
	public void onFriendRequestSent(String meepTag, String errorMessage); 
	
	/**
	 * Callback when server responds to delete friend request
	 * 
	 * @param meepTag The meep tag of the friend
	 * @param erroMessage The error message in the response of server, null when the request was successful
	 */
	public void onFriendDeleted(String meepTag, String nickname, String errorMessage);
	
	/**
	 * Callback when server responds to send text message request
	 * 
	 * @param message The chat message content
	 * @param erroMessage The error message in the response of server, null when the request was successful
	 */
	public void onChatMessageSent(String message, String errorMessage);
	
	/**
	 * Callback when server responds to accept friend request
	 * 
	 * @param meepTag The meep tag of the friend
	 * @param erroMessage The error message in the response of server, null when the request was successful
	 */
	public void onFriendAccepted(String meepTag, String errorMessage);
	
	/**
	 * Callback when service receives a chat message
	 * 
	 * @param convo The conversation message object which contains the friend and message detail 
	 * @param erroMessage The error message in the response of server, null when the request was successful
	 */
	public void onChatMessageReceived(ConversationMessage convo, String errorMessage);
	
	/**
	 * Callback when service receives a friend request
	 * 
	 * @param meepTag The meepTag of the friend 
	 * @param nickname The nickname of the friend
	 * @param erroMessage The error message in the response of server, null when the request was successful
	 */
	public void onFriendRequestReceived(String meepTag, String nickname, String friendMessage, String errorMessage); 

	
	/**
	 * Callback when service receives a friend request status
	 * 
	 * @param meepTag The meepTag of the friend 
	 * @param name The nickname of the friend
	 * @param status The friend-request status
	 * @param erroMessage The error message in the response of server, null when the request was successful
	 */
	public void onFriendRequestStatusReceived(String meepTag, String name, FriendRequestStatus status, String errorMessage); 
	
	
	public void onFriendRejected(String meepTag, String errorMessage);
	
	
}
