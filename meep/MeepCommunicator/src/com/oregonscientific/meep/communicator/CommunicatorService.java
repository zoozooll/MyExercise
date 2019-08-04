package com.oregonscientific.meep.communicator;

import java.util.List;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.j256.ormlite.android.apptools.OrmLiteBaseService;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.communicator.view.friend.FriendAdapter;
import com.oregonscientific.meep.database.CommunicatorDatabaseHelper;
import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.msm.MessageManager;

public class CommunicatorService extends OrmLiteBaseService<CommunicatorDatabaseHelper> {
	
	private final String TAG = getClass().getSimpleName();
	private CommunicatorServiceHandler mHandler;
	public static String BAD_WORD_REPLACEMENT_STRING = "****";
	
	public static final String COMMUNICATOR_ACTION_RECEIVED_FRIEND_REQUEST = "com.oregonscientific.meep.communicator.action.friendrequest";
	public static final String COMMUNICATOR_ACTION_RECEIVED_CHAT_MESSAGE = "com.oregonscientific.meep.communicator.action.chatmessage";
	
	public static final String  NOTIFICATION_KEY_MEEP_TAG = "meeptag";
	public static final String  NOTIFICATION_KEY_NICKNAME = "nickname";
	public static final String  NOTIFICATION_KEY_MESSAGE = "message";
	public static final String  NOTIFICATION_KEY_CONVERSATION_MESSAGE_ID = "conversation_msg_id";
	public static final String  NOTIFICATION_KEY_NAME = "name";
	public static final String  NOTIFICATION_KEY_RESULT = "result";
	
	public static final int MAX_DISPLAY_MESSAGES = 10;
	
	public static final String COMMUNICATOR_ACTION_ACCEPT_FRIEND_REQUEST = "com.oregonscientific.meep.communicator.action.friendrequest.accept";
	public static final String COMMUNICATOR_ACTION_REJECT_FRIEND_REQUEST = "com.oregonscientific.meep.communicator.action.friendrequest.reject";
	/**
	 * This is the object that receives interactions from clients.
	 */
	private final IBinder mBinder = new LocalBinder();
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mHandler = new CommunicatorServiceHandler(this, getHelper());
	}
	
	@Override
	public void onDestroy() {
		mHandler = null;
		super.onDestroy();
	}
	
	
	@Override
	public int onStartCommand (Intent intent, int flags, int startId) {
		// Server messages will be inside the intent's extras
		handleCommand(intent);
		
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_NOT_STICKY;
	}
	
	/**
	 * Handles the command as specified in the {@code} intent 
	 * 
	 * @param intent The intent supplied to {@link #onStartCommand(Intent, int, int)}
	 */
	private void handleCommand(Intent intent) {
		// Quick return if there is nothing to process
		if (intent == null) {
			return;
		}
		
		// Only handles messages received from MessageService
		if (MessageManager.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
			// retrieve extended data (added with putExtra()) from the intent
			Message message = intent.getParcelableExtra(MessageManager.EXTRA_MESSAGE);
			mHandler.handleMessage(message);
		}
		//TODO: put action here
		else if (COMMUNICATOR_ACTION_ACCEPT_FRIEND_REQUEST.equals(intent.getAction())) {
			
			String meepTag = intent.getStringExtra(NOTIFICATION_KEY_MEEP_TAG);
			acceptFriend(meepTag);
		}
		
		else if (COMMUNICATOR_ACTION_REJECT_FRIEND_REQUEST.equals(intent.getAction())) {
			
			String meepTag = intent.getStringExtra(NOTIFICATION_KEY_MEEP_TAG);
			declineFriend(meepTag);
		}
		
	}

	/**
	 * Get Friends for the current logged in account
	 * 
	 * @return a list of Friends which 
	 */
	public List<Friend> getFriends(User user) {
		return mHandler.getFriends(user);
	}
	
	/**
	 * Search a the user by his/her meeptag, to see if the user exists
	 * 
	 * @param meepTag The meeptag of the user that to be searched
	 */
	public void searchUser(String meepTag) {
		mHandler.searchUser(meepTag);
	}
	
	/**
	 * Add a the user by his/her meeptag
	 * 
	 * @param meepTag The meeptag of the user that to be added
	 */
	public void addFriend(String meepTag, String message) {
		mHandler.addFriend(meepTag, message);
	}
	
	/**
	 * Remove a the user by his/her meeptag
	 * 
	 * @param accountId The accountId of the user that to be removed
	 */
	public void removeFriend(String accountId) {
		mHandler.removeFriend(accountId);
	}
	
	/**
	 * Send a chat message to a friend, and record it in the database
	 * 
	 * @param accountId The accountId of the user that suppose to receive the chat message
	 * @param message The message that to be sent to the recipient
	 */
	public void sendChatMessage(String accountId, String message) {
		mHandler.sendChatMessage(accountId, message);
	}
	
	/**
	 * Retrieve the login status of a friend from the database
	 * 
	 * @param accountId The accountId of the user that suppose to receive the chat message
	 * @return boolean that indicate the user is online in the communicator or not
	 */
	public boolean getUserOnlineStatus(String accountId) {
		return mHandler.getUserOnlineStatus(accountId);
	}
	
	/**
	 * Retrieve the chat history for user and the friend with the given meeptag
	 * 
	 * @param accountId The accountId of the user that suppose to receive the chat message
	 * @return a list of conversation messages that is sorted by time 
	 */
	public List<ConversationMessage> getChatHistory(User user, String accountId) {
		return mHandler.getChatHistory(user, accountId);
	}

	
	/**
	 * Retrieve the count of unread messages that are from a specific friend. 
	 * 
	 * @return the number of unread messages from database
	 */
	public int getUnreadMessageCount(User user, String accountId) {
		return mHandler.getUnreadMessageCount(user, accountId);
	}
	
	
	/**
	 * Send a Accept friend request to the server 
	 * 
	 * @param meepTag The meep tag of the friend, which come from the received friend request
	 * @param callback An interface which tell the caller friend has been accepted in the server,
	 * caller should implement onFriendAccepted(String meepTag, String errorMessage), 
	 * null if the caller does not want to receive the callback
	 * 
	 */
	public void acceptFriend(String meepTag) {
		mHandler.acceptFriend(meepTag);
	}
	
	
	/**
	 * Send a Decline friend request to the server 
	 * 
	 * @param meepTag The meep tag of the friend, which come from the received friend request
	 * @param callback An interface which tell the caller friend has been accepted in the server,
	 * caller should implement onFriendAccepted(String meepTag, String errorMessage), 
	 * null if the caller does not want to receive the callback
	 * 
	 */
	public void declineFriend(String meepTag) {
		mHandler.declineFriend(meepTag);
	}
	
	/**
	 * Synchronize local friend list in database with server's friend list for currently logged in user
	 * 
	 **/
	public void syncFriendList()  {
		mHandler.syncFriendList();
	}
	
	/**
	 * Retrieves the User object that is currently logged in. This method calls the blocking
	 * method in {@link AccountManager} to retrieve the current logged in user. User must not
	 * call this method in the main thread.
	 * 
	 * <p>This method will create the User object if the logged in user is not found in the
	 * permission database</p>
	 * 
	 * @return The User object or <code>null</code> if no user is currently logged in
	 */
	public User getLoggedInUser() {
		return mHandler.getLoggedInUser();
	}
	
	
	/**
	 * Retrieves the User object that is last logged in. This method calls the blocking
	 * method in {@link AccountManager} to retrieve the current logged in user. User must not
	 * call this method in the main thread.
	 * 
	 * <p>This method will create the User object if the logged in user is not found in the
	 * permission database</p>
	 * 
	 * @return The User object or <code>null</code> if no user ever logged in
	 */
	public User getLastLoggedInUser() {
		return mHandler.getLastLoggedInUser();
	}
	
	/**
	 * Retrieve the conversation that belongs currently logged in user and a specific friend of the user.
	 * 
	 * @param accountId The accountId that the friend owns
	 * @return A Conversation object which contains conversation messages of the two parties.
	 */
	public Conversation getConversation(User user, String accountId) {
		return mHandler.getConversation(user, accountId);
	}
	
	/**
	 * Mark all the messages for a friend as read
	 * 
	 * @param friend A friend of currently logged in user
	 */
	public void setMessagesToRead(Friend friend) {
		 mHandler.setMessagesToRead(friend);
	}
	
	/**
	 * Mark a specific message as read 
	 * 
	 * @param friend A friend of currently logged in user
	 * @param message The conversation message
	 */
	public void setMessageToRead(Friend friend, ConversationMessage message) {
		 mHandler.setMessageToRead(friend, message);
	}
	
	/**
	 * Sort friends according three basis:
	 * 1. Unread messages has higher priority than Read messages   
	 * 2. Unread messages are sorted by create date of unread messages with descending order
	 * 3. Read messages are sorted by create date of sent/read messages with descending order
	 * 
	 * @param adapter The friend adapter which contains a list of friends
	 */
	public void sortFriends(User user, FriendAdapter adapter) {
		 mHandler.sortFriends(user, adapter);
	}
	
	/**
	 * Retrieve a single conversation message
	 * 
	 * @param cmId The unique Id of Conversation Message 
	 * @return The Conversation Message object with id = cmId
	 */
	public ConversationMessage getConversationMessage(Long cmId) {
		return mHandler.getConversationMessage(cmId);
	}
	
	
	public void cancelAllNotificationsForFriend(User user, Friend friend) {
		mHandler.cancelAllNotificationsForFriend(user, friend);
	}
	
	public List<ConversationMessage> getSortedConversationMessages(Conversation conversation, int limit) {
		return mHandler.getSortedConversationMessages(conversation, limit);
	}
	/**
	 * Register callback for delivering callback messages
	 * 
	 * @param callback An interface which defined the callback actions for communicator 
	 */
	public void setCallback(ICommunicatorServiceCallback callback) {
		mHandler.setCallback(callback);
	}
	
	/**
   * Class for clients to access.  Because we know this service always
   * runs in the same process as its clients, we don't need to deal with
   * IPC.
   */
	public class LocalBinder extends Binder {
		CommunicatorService getService() {
			return CommunicatorService.this;
		}
	}

	
}
