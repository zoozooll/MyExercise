package com.oregonscientific.meep.communicator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.oregonscientific.meep.ServiceHandler;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.communicator.compat.Group;
import com.oregonscientific.meep.communicator.compat.GroupMessagePropertyTypeAdapterFactory;
import com.oregonscientific.meep.communicator.view.friend.FriendAdapter;
import com.oregonscientific.meep.database.ModelAttributes;
import com.oregonscientific.meep.database.Schema;
import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.msm.MessageFilter;
import com.oregonscientific.meep.msm.MessageReceiver;
import com.oregonscientific.meep.msm.MessageService;
import com.oregonscientific.meep.http.Status;
import com.oregonscientific.meep.notification.Notification;
import com.oregonscientific.meep.notification.NotificationManager;
import com.oregonscientific.meep.permission.Component;
import com.oregonscientific.meep.permission.Permission;
import com.oregonscientific.meep.permission.PermissionManager;
import com.oregonscientific.meep.permission.Permission.AccessLevels;
import com.oregonscientific.meep.recommendation.RecommendationManager;
import com.oregonscientific.meep.serialization.MessagePropertyTypeAdapterFactories;
import com.oregonscientific.meep.util.DateUtils;

public class CommunicatorServiceHandler extends ServiceHandler {

	private final String TAG = getClass().getSimpleName();
	private final OrmLiteSqliteOpenHelper mHelper;
	private ICommunicatorServiceCallback mCallback;

	private final String KEY_FRIEND_GROUP = "friends";
	private final String KEY_MEEP_TAG = "meeptag";
	private final String KEY_FRIEND_ID = "friendid";
	private final String KEY_RECIPIENT = "recipient";
	private final String KEY_MESSAGE = "message";
	private final String KEY_SENDER = "sender";
	private final String KEY_NICKNAME = "nickname";
	private final String KEY_COMMAND = "command";
	private final String PACKAGE_NAME = "com.oregonscientific.meep.communicator";
	private final String NOTIFICATION_TITLE = "Communicator";

	private final String KEY_RESULT = "result";
	private final String KEY_NAME = "name";

	private enum NotificationType {
		FRIEND_REQUEST, CHAT_MESSAGE, FRIEND_REQUEST_STATUS
	}

	public enum FriendRequestStatus {

		UNKNOWN("unknown"), SELF_PARENT_REJECT("self-parent-rejected"), OPPOSITE_PARENT_REJECT(
				"opposite-parent-rejected"), FRIEND_REJECT("friend-rejected"), ALL_ACCEPT(
				"friend-accepted");

		private String name = null;

		FriendRequestStatus(String requestStatus) {
			this.name = requestStatus;
		}

		public String toString() {
			return name;
		}

		public static FriendRequestStatus fromString(String text) {
			if (text != null) {
				for (FriendRequestStatus b : FriendRequestStatus.values()) {
					if (text.equalsIgnoreCase(b.name)) {
						return b;
					}
				}
			}
			return UNKNOWN;
		}
	}

	public CommunicatorServiceHandler(Context context,
			OrmLiteSqliteOpenHelper helper,
			ICommunicatorServiceCallback callback) {
		super(context);
		mHelper = helper;
		mCallback = callback;
		init();
	}

	public CommunicatorServiceHandler(Context context,
			OrmLiteSqliteOpenHelper helper) {
		this(context, helper, null);
	}

	/**
	 * Initailize the service handler
	 * 
	 */
	private void init() {

		setupTypeAdapters();
		registerIncomingMessageReceiver();
		registerIncomingFriendRequestReceiver();
		registerGetFriendListCommandReceiver();
		registerIncomingFriendRequestStatusReceiver();
	}

	/**
	 * Register type adapter for old response for friend-list,delete friend and
	 * accept friend
	 * 
	 */
	private void setupTypeAdapters() {

		MessageFilter filter = new MessageFilter(Message.PROCESS_INSTANT_MESSAGING);
		filter.addOperation(Message.OPERATION_CODE_GET_FRIEND_LIST);
		filter.addOperation(Message.OPERATION_CODE_DELETE_FRIEND);
		filter.addOperation(Message.OPERATION_CODE_ACCEPT_FRIEND);
		MessagePropertyTypeAdapterFactories.getInstance().registerTypeAdapterFactory(filter, new GroupMessagePropertyTypeAdapterFactory());

	}

	/**
	 * Register a receiver that receive incoming text message
	 * 
	 */
	private void registerIncomingMessageReceiver() {

		MessageFilter filter = new MessageFilter(Message.PROCESS_INSTANT_MESSAGING);
		filter.addOperation(Message.OPERATION_CODE_TEXT_MSG);
		// A message receiver that receive incoming message
		MessageReceiver receiver = new MessageReceiver(filter) {

			@Override
			public void onReceive(Message message) {
				// Quick return if there is nothing to process
				if (message == null) {
					return;
				}

				final String sender = (String) message.getProperty(KEY_SENDER);

				// Check if this is an incoming message
				if (sender == null)
					return;

				Friend friend = getFriend(sender);
				if (friend == null) {
					// friend not exist in friend list, sync friend list
					Boolean bool = synchronousGetFriendListTask();
					if (bool.booleanValue()) {
						friend = getFriend(sender);
						if (friend == null) {
							Log.e(TAG, "fail to retreive friend: " + sender
									+ " from friend list");
							return;
						}
					}
				}

				// why status = null from portal message

				if (message.getStatus() != 0
						&& message.getStatus() != Status.SUCCESS_OK) {
					mCallback.onChatMessageReceived(null, getLocalizedStatusFromServerMessage(message));
				} else {

					final String recievedMessage = (String) message.getProperty(KEY_MESSAGE);

					if (recievedMessage != null) {

						ExecutorService service = Executors.newSingleThreadExecutor();
						service.execute(new Runnable() {

							@Override
							public void run() {

								User user = getLoggedInUser();
								if (user != null) {
									Log.e(TAG, "Getting Permission ...");
									PermissionManager pm = (PermissionManager) ServiceManager.getService(getContext(), ServiceManager.PERMISSION_SERVICE);
									if (pm != null) {
										String replacedMessage = pm.replaceBadwordsBlocking(user.getAccountId(), recievedMessage, CommunicatorService.BAD_WORD_REPLACEMENT_STRING);
										Log.e(TAG, "Done Permission ...");
										ConversationMessage convo = addConversationMessage(sender, replacedMessage, true);
										try {
											String str = getForegroundApp();
											if (str.equals(PACKAGE_NAME)) {
												if (mCallback != null)
													mCallback.onChatMessageReceived(convo, null);
												return;
											} else {
												createNotification(getLoggedInUser(), NotificationType.CHAT_MESSAGE, convo);
											}
										} catch (NameNotFoundException e) {
											Log.e(TAG, "cannot find foreground package");
											return;
										}
									} else {
										Log.e(TAG, " Permission is null...");
									}
								}
							}
						});
					}

				}

			}

			@Override
			public boolean isPersistent() {
				return true;
			}

		};
		// Register the inline receiver to process the {@link Message}
		registerReceiver(receiver);

	}

	private boolean synchronousGetFriendListTask() {
		final User user = getLoggedInUser();
		// Quick return if the request cannot be processed
		if (user == null) {
			return false;
		}

		Boolean doneUpdateFriendList = Boolean.valueOf(false);

		FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {

				Boolean result = false;

				final FutureTask<Boolean> waitTask = new FutureTask<Boolean>(new Callable<Boolean>() {

					@Override
					public Boolean call() throws Exception {
						// TODO Auto-generated method stub
						return Boolean.valueOf(true);
					}

				});

				Message message = new Message(Message.PROCESS_INSTANT_MESSAGING, Message.OPERATION_CODE_GET_FRIEND_LIST);
				MessageFilter filter = new MessageFilter(message.getMessageID());
				MessageReceiver receiver = new MessageReceiver(filter) {

					@Override
					public void onReceive(Message message) {
						// Quick return if one of the pass in parameter is null
						if (message == null) {
							return;
						}

						// The request was unsuccessful
						if (message.getStatus() != Status.SUCCESS_OK) {
							Log.e(TAG, message.getMessage());
							if (mCallback != null)
								mCallback.onFriendListReceived(getLocalizedStatusFromServerMessage(message));
						} else {
							User user = getLoggedInUser();
							if (user != null) {
								updateLocalFriendList(user, message);
								if (mCallback != null)
									mCallback.onFriendListReceived(null);
							}
						}

						waitTask.run();
					}

				};

				// Register the inline receiver to process the {@link Message}
				registerReceiver(receiver);
				sendMessage(message);
				result = waitTask.get();

				return result;
			}

		});

		try {
			future.run();
			doneUpdateFriendList = future.get(10000, TimeUnit.MILLISECONDS);
		} catch (Exception ex) {
			Log.e(TAG, "Cannot perform synchronous get friend list task" + ex);
		}

		return doneUpdateFriendList.booleanValue();
	}

	private void registerGetFriendListCommandReceiver() {

		MessageFilter filter = new MessageFilter(Message.PROCESS_SYSTEM);
		filter.addOperation(Message.OPERATION_CODE_RUN_COMMAND);
		// A message receiver that receive incoming message
		MessageReceiver receiver = new MessageReceiver(filter) {

			@Override
			public void onReceive(Message message) {
				// Quick return if there is nothing to process
				if (message == null) {
					return;
				}

				final String command = (String) message.getProperty(KEY_COMMAND);

				// Check if this is an null command
				if (command == null)
					return;

				// Return other command, only process get friend list
				if (!command.equals(Message.COMMAND_GET_FRIEND_LIST))
					return;

				syncFriendList();
			}

			@Override
			public boolean isPersistent() {
				return true;
			}

		};
		// Register the inline receiver to process the {@link Message}
		registerReceiver(receiver);

	}

	/**
	 * Register a receiver that receive incoming friend request
	 * 
	 */
	private void registerIncomingFriendRequestReceiver() {

		MessageFilter filter = new MessageFilter(Message.PROCESS_INSTANT_MESSAGING);
		filter.addOperation(Message.OPERATION_CODE_FRIEND_REQUEST);
		// A message receiver that receive incoming message
		MessageReceiver receiver = new MessageReceiver(filter) {

			@Override
			public void onReceive(Message message) {
				// Quick return if there is nothing to process
				if (message == null) {
					return;
				}

				String meepTag = (String) message.getProperty(KEY_MEEP_TAG);
				String nickname = (String) message.getProperty(KEY_NICKNAME);
				String friendMessage = (String) message.getProperty(KEY_MESSAGE);

				if (message.getStatus() == Status.SUCCESS_OK
						|| message.getStatus() == 0) {

					try {
						String str = getForegroundApp();
						if (str.equals(PACKAGE_NAME)) {
							if (mCallback != null)
								mCallback.onFriendRequestReceived(meepTag, nickname, friendMessage, null);
							return;
						} else {
							Map<String, String> map = new HashMap<String, String>();
							map.put(CommunicatorService.NOTIFICATION_KEY_MEEP_TAG, meepTag);
							map.put(CommunicatorService.NOTIFICATION_KEY_NICKNAME, nickname);
							map.put(CommunicatorService.NOTIFICATION_KEY_MESSAGE, friendMessage);
							createNotification(getLoggedInUser(), NotificationType.FRIEND_REQUEST, map);
						}
					} catch (NameNotFoundException e) {
						Log.e(TAG, "Fail to get foreground app");
					}
				} else {

					if (mCallback != null)
						mCallback.onFriendRequestReceived(meepTag, nickname, null, getLocalizedStatusFromServerMessage(message));
				}

			}

			@Override
			public boolean isPersistent() {
				return true;
			}

		};
		// Register the inline receiver to process the {@link Message}
		registerReceiver(receiver);

	}

	private void registerIncomingFriendRequestStatusReceiver() {

		MessageFilter filter = new MessageFilter(Message.PROCESS_INSTANT_MESSAGING);
		filter.addOperation(Message.OPERATION_CODE_FRIEND_REQUEST_STATUS);
		// A message receiver that receive incoming message
		MessageReceiver receiver = new MessageReceiver(filter) {

			@Override
			public void onReceive(Message message) {
				// Quick return if there is nothing to process
				if (message == null) {
					return;
				}

				String meepTag = (String) message.getProperty(KEY_MEEP_TAG);
				String name = (String) message.getProperty(KEY_NAME);
				String result = (String) message.getProperty(KEY_RESULT);

				if (message.getStatus() == Status.SUCCESS_OK
						|| message.getStatus() == 0) {

					try {
						String str = getForegroundApp();
						if (str.equals(PACKAGE_NAME)) {
							if (mCallback != null)
								mCallback.onFriendRequestStatusReceived(meepTag, name, FriendRequestStatus.fromString(result), null);
							return;
						} else {

							Map<String, String> map = new HashMap<String, String>();
							map.put(CommunicatorService.NOTIFICATION_KEY_MEEP_TAG, meepTag);
							map.put(CommunicatorService.NOTIFICATION_KEY_NAME, name);
							map.put(CommunicatorService.NOTIFICATION_KEY_RESULT, result);
							createNotification(getLoggedInUser(), NotificationType.FRIEND_REQUEST_STATUS, map);
						}
					} catch (NameNotFoundException e) {
						Log.e(TAG, "Fail to get foreground app");
					}

				} else {

					if (mCallback != null)
						mCallback.onFriendRequestStatusReceived(meepTag, name, FriendRequestStatus.fromString(result), getLocalizedStatusFromServerMessage(message));
				}

			}

			@Override
			public boolean isPersistent() {
				return true;
			}

		};
		// Register the inline receiver to process the {@link Message}
		registerReceiver(receiver);

	}

	/**
	 * Get Friends for the current logged in account
	 * 
	 * @return a list of Friends
	 */
	public synchronized List<Friend> getFriends(User user) {
		List<Friend> friends = new ArrayList<Friend>();
		if (user != null) {
			try {
				Dao<UserFriend, Long> dao = mHelper.getDao(UserFriend.class);
				ModelAttributes attrs = Schema.getAttributes(UserFriend.class);
				QueryBuilder<UserFriend, Long> builder = dao.queryBuilder();
				builder.where().eq(attrs.getColumnName(UserFriend.USER_FIELD_NAME), Long.valueOf(user.getId()));
				List<UserFriend> userFriends = builder.query();
				for (UserFriend userFriend : userFriends) {
					Friend friend = userFriend.getFriend();
					friend.refresh();
					friends.add(friend);
				}

			} catch (SQLException e) {
				Log.e(TAG, "Fail to retrieve friends");
			}
		}

		// return null if no friends
		if (friends.size() == 0)
			friends = null;

		return friends;
	}

	/**
	 * Synchronize local friend list in database with server's friend list for
	 * currently logged in user
	 * 
	 **/
	public synchronized void syncFriendList() {

		// Make a server message object
		Message message = new Message(Message.PROCESS_INSTANT_MESSAGING, Message.OPERATION_CODE_GET_FRIEND_LIST);
		MessageFilter filter = new MessageFilter(message.getMessageID());
		MessageReceiver receiver = new MessageReceiver(filter) {

			@Override
			public void onReceive(Message message) {
				// Quick return if one of the pass in parameter is null
				if (message == null) {
					return;
				}

				// The request was unsuccessful
				if (message.getStatus() != Status.SUCCESS_OK) {
					Log.e(TAG, message.getMessage());
					if (mCallback != null)
						mCallback.onFriendListReceived(getLocalizedStatusFromServerMessage(message));
				} else {
					User user = getLoggedInUser();
					if (user != null) {
						updateLocalFriendList(user, message);
						if (mCallback != null)
							mCallback.onFriendListReceived(null);
					}
				}
			}

		};

		// Register the inline receiver to process the {@link Message}
		registerReceiver(receiver);

		sendMessage(message);
	}

	/**
	 * Synchronize local friend list in database with server's friend list for
	 * the given user
	 * 
	 * @param user
	 *            The currently logged in user
	 * @param message
	 *            The returned message from server
	 */
	private synchronized void updateLocalFriendList(final User user,
			final Message message) {

		if (message == null || user == null) {
			return;
		}

		try {
			TransactionManager.callInTransaction(mHelper.getConnectionSource(), new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					List<Group> groups = (List<Group>) message.getProperty(KEY_FRIEND_GROUP);
					if (groups != null && groups.size() > 0) {

						List<Friend> remoteFriends = new ArrayList<Friend>();
						// combine all friends in different groups
						for (Group g : groups) {
							remoteFriends.addAll(g.toFriends());
						}

						List<Friend> dbFriends = getFriends(user);
						if (dbFriends != null && dbFriends.size() > 0) {
							Iterator<Friend> dbFriendIterator = dbFriends.iterator();

							// remove friends that are not in the new remote
							// friends
							while (dbFriendIterator.hasNext()) {
								Friend dbFriend = dbFriendIterator.next();
								boolean friendFound = false;
								for (Friend remoteFriend : remoteFriends) {
									if (dbFriend.getAccountId().equals(remoteFriend.getAccountId())) {
										friendFound = true;
										continue;

									}
								}
								if (!friendFound) {
									removeFriendFromDatabase(user, dbFriend);
									dbFriendIterator.remove();
								}
							}
						}
						if (remoteFriends != null && remoteFriends.size() > 0) {
							// add friends or update friends that is in the
							// remote friend list

							Iterator<Friend> remoteFriendIterator = remoteFriends.iterator();
							while (remoteFriendIterator.hasNext()) {
								boolean friendFound = false;
								Friend remoteFriend = remoteFriendIterator.next();
								if (dbFriends != null && dbFriends.size() > 0) {
									Iterator<Friend> dbFriendIterator = dbFriends.iterator();
									while (dbFriendIterator.hasNext()) {
										Friend dbFriend = dbFriendIterator.next();
										if (dbFriend.getAccountId().equals(remoteFriend.getAccountId())) {

											ModelAttributes attr = Schema.getAttributes(Friend.class);
											QueryBuilder<?, ?> updateQueryBuilder = mHelper.getDao(Friend.class).queryBuilder();
											updateQueryBuilder.where().eq(attr.getColumnName(Friend.ID_FIELD_NAME), dbFriend.getId());
											Friend friend = (Friend) updateQueryBuilder.queryForFirst();
											if (friend != null) {
												friend.setName(remoteFriend.getName());
												friend.setIconAddress(remoteFriend.getIconAddress());
												friend.update();
											}

											friendFound = true;
										}
									}
								}
								if (!friendFound)
									addOrUpdateFriendToDatabase(user, remoteFriend);
							}
						}
					}
					return null;
				}
			});

		} catch (Exception ex) {
			Log.e(TAG, "Fail in updating friend list in transactoin with error"
					+ ex);
		}
	}

	private synchronized void addOrUpdateFriendToDatabase(User user,
			Friend friend) throws Exception {

		Dao<Friend, Long> friendDao = mHelper.getDao(Friend.class);

		Friend dbFriend = getFriend(friend.getAccountId());
		if (dbFriend != null) {
			dbFriend.setIconAddress(friend.getIconAddress());
			dbFriend.setLastModifiedDate(friend.getLastModifiedDate());
			dbFriend.setName(friend.getName());
			dbFriend.setOnline(friend.getOnline());
			dbFriend.update();
		} else {
			friendDao.create(friend);
		}

		Dao<UserFriend, Long> userFriendDao = mHelper.getDao(UserFriend.class);

		ModelAttributes userFriendAttr = Schema.getAttributes(UserFriend.class);
		ModelAttributes friendAttr = Schema.getAttributes(Friend.class);

		QueryBuilder<UserFriend, Long> userFriendQB = userFriendDao.queryBuilder();
		QueryBuilder<Friend, Long> friendQB = friendDao.queryBuilder();

		friendQB.where().eq(friendAttr.getColumnName(Friend.ACCOUNT_ID_FIELD_NAME), friend.getAccountId());
		userFriendQB.where().eq(userFriendAttr.getColumnName(UserFriend.USER_FIELD_NAME), Long.valueOf(user.getId()));
		userFriendQB.join(friendQB);

		List<UserFriend> userFriends = userFriendQB.query();
		if (userFriends != null && userFriends.size() > 0)
			// friend already exist in db, do not create
			return;

		if (dbFriend != null)
			friend = dbFriend;

		UserFriend userFriend = new UserFriend(friend, user);
		userFriendDao.createOrUpdate(userFriend);
		Conversation conversation = getConversation(user, friend.getAccountId());
		if (conversation == null) {
			Conversation newConvo = new Conversation(user.getId(), friend);
			Dao<Conversation, Long> conversationDao = mHelper.getDao(Conversation.class);
			conversationDao.create(newConvo);
		}
	}

	private synchronized void removeFriendFromDatabase(User user, Friend friend)
			throws Exception {

		Dao<UserFriend, Long> userFriendDao = mHelper.getDao(UserFriend.class);
		ModelAttributes attrs = Schema.getAttributes(UserFriend.class);
		QueryBuilder<UserFriend, Long> builder = userFriendDao.queryBuilder();
		builder.where().eq(attrs.getColumnName(UserFriend.USER_FIELD_NAME), Long.valueOf(user.getId())).and().eq(attrs.getColumnName(UserFriend.FRIEND_FIELD_NAME), Long.valueOf(friend.getId()));

		UserFriend userFriend = (UserFriend) builder.queryForFirst();
		if (userFriend != null) {

			Dao<Conversation, Long> conversationDao = mHelper.getDao(Conversation.class);
			ModelAttributes conversationAttrs = Schema.getAttributes(Conversation.class);
			QueryBuilder<Conversation, Long> conversationBuilder = conversationDao.queryBuilder();
			conversationBuilder.where().eq(conversationAttrs.getColumnName(Conversation.USER_FIELD_NAME), Long.valueOf(user.getId())).and().eq(conversationAttrs.getColumnName(Conversation.FRIEND_FIELD_NAME), Long.valueOf(friend.getId()));

			Conversation conversation = conversationBuilder.queryForFirst();
			if (conversation != null) {

				Dao<ConversationMessage, Long> conversationMessageDao = mHelper.getDao(ConversationMessage.class);
				ModelAttributes conversationMessageAttrs = Schema.getAttributes(ConversationMessage.class);
				QueryBuilder<ConversationMessage, Long> conversationMessageBuilder = conversationMessageDao.queryBuilder();
				conversationMessageBuilder.where().eq(conversationMessageAttrs.getColumnName(ConversationMessage.CONVERSATION_FIELD_NAME), Long.valueOf(conversation.getId()));

				List<ConversationMessage> conversationMessages = conversationMessageBuilder.query();

				if (conversationMessages != null
						&& conversationMessages.size() > 0) {

					for (ConversationMessage cm : conversationMessages) {
						cm.delete();
					}

				}

				conversation.delete();

			}

			userFriend.delete();

		}

	}

	/**
	 * Search a the user by his/her meeptag, to see if the user exists
	 * 
	 * @param meepTag
	 *            The meeptag of the user that to be searched
	 */
	public void searchUser(String meepTag) {

		if (meepTag == null)
			return;

		// Check if the meeptag is bad word, return if bad word

		MessageFilter filter = new MessageFilter(Message.PROCESS_INSTANT_MESSAGING);
		filter.addOperation(Message.OPERATION_CODE_SEARCH_FRIEND);
		MessageReceiver receiver = new MessageReceiver(filter) {

			@Override
			public void onReceive(Message message) {
				// Quick return if one of the pass in parameter is null
				if (message == null) {
					return;
				}

				String meepTag = (String) message.getProperty(KEY_MEEP_TAG);
				String nickname = (String) message.getProperty(KEY_NICKNAME);
				// The request was unsuccessful
				if (message.getStatus() != Status.SUCCESS_OK) {
					Log.e(TAG, message.getMessage());
					if (mCallback != null) {
						mCallback.onFriendSearched(meepTag, nickname, getLocalizedStatusFromServerMessage(message));
					}
				} else {
					if (mCallback != null) {
						mCallback.onFriendSearched(meepTag, nickname, null);
					}
				}

			}

		};

		// Register the inline receiver to process the {@link Message}
		registerReceiver(receiver);

		Message message = new Message(Message.PROCESS_INSTANT_MESSAGING, Message.OPERATION_CODE_SEARCH_FRIEND);
		message.addProperty(KEY_MEEP_TAG, meepTag);
		sendMessage(message);
	}

	/**
	 * Add a user by his/her meeptag
	 * 
	 * @param meepTag
	 *            The meeptag of the user that to be added
	 */
	public void addFriend(String meepTag, String msg) {

		if (meepTag == null)
			return;

		Message message = new Message(Message.PROCESS_INSTANT_MESSAGING, Message.OPERATION_CODE_ADD_FRIEND);
		message.addProperty(KEY_MEEP_TAG, meepTag);
		message.addProperty(KEY_MESSAGE, msg);

		MessageFilter filter = new MessageFilter(message.getMessageID());
		MessageReceiver receiver = new MessageReceiver(filter) {

			@Override
			public void onReceive(Message message) {
				// Quick return if one of the pass in parameter is null
				if (message == null) {
					return;
				}

				// Check the request was successful or not
				if (message.getStatus() != Status.SUCCESS_OK) {
					Log.e(TAG, message.getMessage());
					if (mCallback != null)
						mCallback.onFriendRequestSent(null, getLocalizedStatusFromServerMessage(message));
				} else {
					String meepTag = (String) message.getProperty(KEY_MEEP_TAG);

					if (mCallback != null)
						mCallback.onFriendRequestSent(meepTag, null);
				}
			}

		};

		// Register the inline receiver to process the {@link Message}
		registerReceiver(receiver);

		sendMessage(message);
	}

	/**
	 * Remove a the user by his/her accountId
	 * 
	 * @param accountId
	 *            The account id of the user that to be removed
	 */
	public void removeFriend(String accountId) {

		if (accountId == null)
			return;

		Friend pendingDeleteFriend = getFriend(accountId);
		final String nickname = pendingDeleteFriend.getName();

		Message message = new Message(Message.PROCESS_INSTANT_MESSAGING, Message.OPERATION_CODE_DELETE_FRIEND);
		message.addProperty(KEY_FRIEND_ID, accountId);

		MessageFilter filter = new MessageFilter(message.getMessageID());
		MessageReceiver receiver = new MessageReceiver(filter) {

			@Override
			public void onReceive(Message message) {
				// Quick return if one of the pass in parameter is null
				if (message == null) {
					return;
				}

				String friendId = (String) message.getProperty(KEY_FRIEND_ID);

				// The request was unsuccessful
				if (message.getStatus() != Status.SUCCESS_OK) {
					Log.e(TAG, message.getMessage());
					if (mCallback != null)
						mCallback.onFriendDeleted(friendId, nickname, getLocalizedStatusFromServerMessage(message));
				} else {

					try {
						User user = getLoggedInUser();
						Friend friend = getFriend(friendId);
						removeFriendFromDatabase(user, friend);
					} catch (Exception e) {

						Log.e(TAG, "cannot remove friend with error: " + e);

					}
					if (mCallback != null)
						mCallback.onFriendDeleted(friendId, nickname, null);
				}

			}

		};

		// Register the inline receiver to process the {@link Message}
		registerReceiver(receiver);

		sendMessage(message);
	}

	/**
	 * Send a chat message to a friend, and record it in the database
	 * 
	 * @param accountId
	 *            The accountId of the user that suppose to receive the chat
	 *            message
	 * @param message
	 *            The message content
	 */
	public void sendChatMessage(final String accountId, String message) {

		if (accountId == null || message == null)
			return;

		Message serverMessage = new Message(Message.PROCESS_INSTANT_MESSAGING, Message.OPERATION_CODE_TEXT_MSG);
		serverMessage.addProperty(KEY_RECIPIENT, accountId);
		serverMessage.addProperty(KEY_MESSAGE, message);

		MessageFilter filter = new MessageFilter(serverMessage.getMessageID());
		MessageReceiver receiver = new MessageReceiver(filter) {

			@Override
			public void onReceive(Message message) {
				// Quick return if one of the pass in parameter is null
				if (message == null) {
					return;
				}
				String m = (String) message.getProperty(KEY_MESSAGE);

				// The request was unsuccessful
				if (message.getStatus() != Status.SUCCESS_OK) {
					Log.e(TAG, message.getMessage());
					if (mCallback != null)
						mCallback.onChatMessageSent(m, getLocalizedStatusFromServerMessage(message));
				} else {

					if (mCallback != null)
						mCallback.onChatMessageSent(m, null);
				}
			}

		};

		addConversationMessage(accountId, message, false);
		// Register the inline receiver to process the {@link Message}
		registerReceiver(receiver);
		sendMessage(serverMessage);
	}

	/**
	 * Retrieve the login status of a friend from the database
	 * 
	 * @param accountId
	 *            The friend id of the user that suppose to receive the chat
	 *            message
	 * @return boolean that indicate the user is online in the communicator or
	 *         not
	 */
	public boolean getUserOnlineStatus(String accountId) {

		if (accountId == null)
			return false;

		Friend friend = null;
		try {
			ModelAttributes attrs = Schema.getAttributes(Friend.class);
			Dao<Friend, Long> dao = mHelper.getDao(Friend.class);
			QueryBuilder<Friend, Long> builder = dao.queryBuilder();
			builder.where().eq(attrs.getColumnName(Friend.ACCOUNT_ID_FIELD_NAME), accountId);
			friend = (Friend) builder.queryForFirst();

		} catch (SQLException e) {

			Log.e(TAG, "Fail to retrieve the online status for account Id "
					+ accountId);
		}

		if (friend != null) {
			return friend.getOnline();
		}

		return false;
	}

	/**
	 * Retrieve the chat history for user and the friend with the given
	 * accountId
	 * 
	 * @param user
	 *            The user that suppose to receive the chat message
	 * @param user
	 *            The account id of the owner of the chat history
	 * 
	 * @return a list of conversation messages that is sorted by time
	 */
	public List<ConversationMessage> getChatHistory(User user, String accountId) {

		if (user == null || accountId == null)
			return null;

		List<ConversationMessage> messages = new ArrayList<ConversationMessage>();
		Conversation conversation = getConversation(user, accountId);
		try {
			ModelAttributes attrs = Schema.getAttributes(ConversationMessage.class);
			Dao<ConversationMessage, ?> dao = mHelper.getDao(ConversationMessage.class);
			QueryBuilder<ConversationMessage, ?> builder = dao.queryBuilder();
			builder.where().eq(attrs.getColumnName(ConversationMessage.CONVERSATION_FIELD_NAME), Long.valueOf(conversation.getId()));
			builder.orderBy(attrs.getColumnName(Conversation.CREATED_DATE_FIELD_NAME), false);
			messages = builder.query();

		} catch (Exception e) {
			Log.e(TAG, "Fail to retrieve conversatoin messages when iterating");
			messages = null;
		}

		return messages;
	}

	/**
	 * Retrieve the conversation that belongs to a specific user and a specific
	 * friend of the user.
	 * 
	 * @param user
	 *            The user object that owns the conversation object
	 * @param accountId
	 *            The accountId that the friend owns
	 * @return A Conversation object which contains conversation messages of the
	 *         two parties.
	 */
	public Conversation getConversation(User user, String accountId) {

		if (user == null || accountId == null) {
			return null;
		}

		Conversation conversation = null;

		try {

			Friend friend = getFriend(accountId);
			ModelAttributes attrs = Schema.getAttributes(Conversation.class);
			Dao<Conversation, Long> dao = mHelper.getDao(Conversation.class);
			QueryBuilder<Conversation, Long> builder = dao.queryBuilder();
			builder.where().eq(attrs.getColumnName(Conversation.USER_FIELD_NAME), Long.valueOf(user.getId())).and().eq(attrs.getColumnName(Conversation.FRIEND_FIELD_NAME), Long.valueOf(friend.getId()));

			conversation = (Conversation) builder.queryForFirst();
			if (conversation != null) {
				// refresh friend's detail
				conversation.getFriend().refresh();
			}

		} catch (Exception e) {
			Log.e(TAG, "Fail to retrieve conversation with error for user "
					+ user.getAccountId() + " and friendId " + accountId
					+ " with error: " + e);
		}

		return conversation;
	}

	/**
	 * Retrieve a friend from a account id
	 * 
	 * @param accountId
	 *            The account id of the friend holds
	 * @return The friend object with the account id
	 */

	private Friend getFriend(String accountId) {

		if (accountId == null)
			return null;

		Friend friend = null;
		try {

			ModelAttributes attrs = Schema.getAttributes(Friend.class);
			Dao<Friend, Long> dao = mHelper.getDao(Friend.class);
			QueryBuilder<Friend, Long> builder = dao.queryBuilder();
			builder.where().eq(attrs.getColumnName(Friend.ACCOUNT_ID_FIELD_NAME), accountId);

			friend = (Friend) builder.queryForFirst();

		} catch (Exception e) {
			Log.e(TAG, "Fail to link accountId to a friend with Id "
					+ accountId + " and error " + e);
		}

		return friend;
	}

	/**
	 * Retrieve the count of unread messages that are from a specific friend.
	 * 
	 * @param accountId
	 *            The account id of the user who sent those unread messages
	 * @return the number of unread messages from database
	 */
	public int getUnreadMessageCount(User user, String accountId) {

		if (accountId == null || user == null)
			return 0;

		int count = 0;

		Friend friend = getFriend(accountId);

		if (user == null || friend == null)
			return 0;

		try {
			ModelAttributes conversationAttrs = Schema.getAttributes(Conversation.class);
			ModelAttributes conversationMessageAttrs = Schema.getAttributes(ConversationMessage.class);
			Dao<Conversation, Long> conversationDao = mHelper.getDao(Conversation.class);
			Dao<ConversationMessage, Long> conversationMessageDao = mHelper.getDao(ConversationMessage.class);
			QueryBuilder<Conversation, Long> conversationQB = conversationDao.queryBuilder();
			QueryBuilder<ConversationMessage, Long> conversationMessageQB = conversationMessageDao.queryBuilder();

			conversationMessageQB.where().eq(conversationMessageAttrs.getColumnName(ConversationMessage.READ_FIELD_NAME), Boolean.valueOf(false));

			conversationQB.where().eq(conversationAttrs.getColumnName(Conversation.USER_FIELD_NAME), Long.valueOf(user.getId())).and().eq(conversationAttrs.getColumnName(Conversation.FRIEND_FIELD_NAME), Long.valueOf(friend.getId()));

			conversationMessageQB.join(conversationQB);
			// For debug, check the list by -> List<ConversationMessages>
			// messages = conversationMessageQB.query();
			count = (int) conversationMessageQB.countOf();

		} catch (SQLException e) {
			Log.e(TAG, "Fail to retrieve unread messages count for user "
					+ accountId + " with error " + e);
		}

		return count;
	}

	/**
	 * Send a Accept friend request to the server
	 * 
	 * @param meepTag
	 *            The meep tag of the friend, which come from the received
	 *            friend request
	 * 
	 */
	public void acceptFriend(String meepTag) {

		if (meepTag == null)
			return;

		Message message = new Message(Message.PROCESS_INSTANT_MESSAGING, Message.OPERATION_CODE_ACCEPT_FRIEND);
		message.addProperty(KEY_MEEP_TAG, meepTag);
		MessageFilter filter = new MessageFilter(message.getMessageID());
		MessageReceiver receiver = new MessageReceiver(filter) {

			@Override
			public void onReceive(Message message) {
				// Quick return if one of the pass in parameter is null
				if (message == null) {
					return;
				}

				String accountId = (String) message.getProperty(KEY_FRIEND_ID);

				// The request was unsuccessful
				if (message.getStatus() != Status.SUCCESS_OK) {
					Log.e(TAG, message.getMessage());
					if (mCallback != null)
						mCallback.onFriendAccepted(accountId, getLocalizedStatusFromServerMessage(message));
				} else {

					// update local friend list, response should return the new
					// friend list
					User user = getLoggedInUser();
					Friend friend = getFriend(accountId);
					try {
						addOrUpdateFriendToDatabase(user, friend);
					} catch (Exception e) {
						Log.e(TAG, "Failed to insert friend to database with error"
								+ e);
					}

					if (mCallback != null)
						mCallback.onFriendAccepted(accountId, null);
				}

			}

		};

		// Register the inline receiver to process the {@link Message}
		registerReceiver(receiver);

		sendMessage(message);
	}

	/**
	 * Send a Reject friend request to the server
	 * 
	 * @param meepTag
	 *            The meep tag of the friend, which come from the received
	 *            friend request
	 * 
	 */
	public void declineFriend(String meepTag) {

		if (meepTag == null)
			return;

		Message message = new Message(Message.PROCESS_INSTANT_MESSAGING, Message.OPERATION_CODE_REJECT_FRIEND);
		message.addProperty(KEY_MEEP_TAG, meepTag);
		MessageFilter filter = new MessageFilter(message.getMessageID());
		MessageReceiver receiver = new MessageReceiver(filter) {

			@Override
			public void onReceive(Message message) {
				// Quick return if one of the pass in parameter is null
				if (message == null) {
					return;
				}

				String accountId = (String) message.getProperty(KEY_FRIEND_ID);

				// The request was unsuccessful
				if (message.getStatus() != Status.SUCCESS_OK) {
					Log.e(TAG, message.getMessage());
					if (mCallback != null)
						mCallback.onFriendRejected(accountId, getLocalizedStatusFromServerMessage(message));
				} else {

					// update local friend list, response should return the new
					// friend list
					User user = getLoggedInUser();
					Friend friend = getFriend(accountId);
					try {
						addOrUpdateFriendToDatabase(user, friend);
					} catch (Exception e) {
						Log.e(TAG, "Failed to insert friend to database with error"
								+ e);
					}

					if (mCallback != null)
						mCallback.onFriendRejected(accountId, null);
				}

			}

		};

		// Register the inline receiver to process the {@link Message}
		registerReceiver(receiver);

		sendMessage(message);
	}

	/**
	 * Retrieves the User object that is currently logged in. This method calls
	 * the blocking method in {@link AccountManager} to retrieve the current
	 * logged in user. User must not call this method in the main thread.
	 * 
	 * <p>
	 * This method will create the User object if the logged in user is not
	 * found in the communicator database
	 * </p>
	 * 
	 * @return The User object or <code>null</code> if no user is currently
	 *         logged in
	 */
	public User getLoggedInUser() {
		AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		Account account = am.getLoggedInAccountBlocking();
		return account == null ? null : getUser(account, true);
	}

	/**
	 * Retrieves the User object that is last logged in. This method calls the
	 * blocking method in {@link AccountManager} to retrieve the current logged
	 * in user. User must not call this method in the main thread.
	 * 
	 * <p>
	 * This method will create the User object if the logged in user is not
	 * found in the communicator database
	 * </p>
	 * 
	 * @return The User object or <code>null</code> if no user ever logged in
	 */
	public User getLastLoggedInUser() {
		AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		if (am != null) {
			Account account = am.getLastLoggedInAccountBlocking();
			return account == null ? null : getUser(account, true);
		}
		return null;
	}

	/**
	 * Retrieves the User object identified by the {@code account}
	 * 
	 * @param account
	 *            The Account to retrieve
	 * @return The User object or <code>null</code> if the user was not found
	 */
	User getUser(Account account) {
		return getUser(account, true);
	}

	/**
	 * Retrieves the User object identified by the {@code account}
	 * 
	 * @param account
	 *            The Account to retrieve
	 * @param createIfNotExists
	 *            true to create the User object if it was not found
	 * @return The User object or <code>null</code> if the user was not found
	 */
	User getUser(Account account, boolean createIfNotExists) {
		String username = account == null ? "" : account.getId();
		String iconAddress = account == null ? "" : account.getIconAddress();
		String firstName = account == null ? "" : account.getFirstName();
		String meepTag = account == null ? "" : account.getMeepTag();
		return getUser(username, iconAddress, firstName, meepTag, createIfNotExists);
	}

	/**
	 * Retrieve the User object with the given {@code username}. If the user
	 * cannot be found, creates the User object if {@code createIfNotExist} is
	 * set to true
	 * 
	 * @param username
	 *            the unique name identifying the user
	 * @param iconAddress
	 *            the icon address that belongs to the logged in user
	 * @param firstName
	 *            the first name that belongs to the logged in user
	 * @param createIfNotExists
	 *            true to create the User object if it was not found
	 * @return the User object or <code>null</code> if the user was not found
	 */
	private User getUser(String username, String iconAddress, String firstName,
			String meepTag, boolean createIfNotExists) {

		if (username == null)
			return null;

		User result = null;
		try {
			Dao<User, Long> dao = mHelper.getDao(User.class);
			QueryBuilder<User, Long> qb = dao.queryBuilder();
			qb.where().eq(User.ACCOUNT_ID_FIELD_NAME, username);
			result = dao.queryForFirst(qb.prepare());

			if (result == null && createIfNotExists) {
				result = new User(username, iconAddress, firstName, meepTag);
				dao.createIfNotExists(result);
			} else {
				// update user profile everytime
				result.setFirstName(firstName);
				result.setIconAddress(iconAddress);
				result.update();
			}

		} catch (Exception ex) {
			// The given user does not exist
			Log.e(TAG, "Cannot retrieve " + username + " because " + ex);
		}
		return result;
	}

	/**
	 * Add the conversation message to db, create conversation object if it was
	 * first message need
	 * 
	 * @param target
	 *            The accountId for the recipient, null if the recipient is
	 *            current logged in user
	 * @param sentMessage
	 *            The message has been delivered / received
	 */
	private ConversationMessage addConversationMessage(String target,
			String sentMessage, boolean isReceive) {

		if (target == null || sentMessage == null)
			return null;

		User user = getLoggedInUser();

		if (user == null)
			return null;

		Conversation conversation = getConversation(user, target);
		if (conversation == null) {
			Friend friend = getFriend(target);
			// No previous conversation, create a conversation between the user
			// and friend with accountId: target
			conversation = new Conversation(user.getId(), friend);
			try {
				Dao<Conversation, Long> dao = mHelper.getDao(Conversation.class);
				dao.create(conversation);

			} catch (SQLException e) {
				Log.e(TAG, "Fail to create conversation, conversation message cannot be add; Error: ", e);
				return null;
			}
		}

		ConversationMessage conversationMessage = new ConversationMessage(conversation, null, sentMessage, null, Boolean.valueOf(isReceive), Boolean.valueOf(!isReceive), ConversationMessage.NOTIFICATION_EMPTY_ID);

		try {
			Dao<ConversationMessage, Long> dao = mHelper.getDao(ConversationMessage.class);
			dao.create(conversationMessage);

		} catch (SQLException e) {
			Log.e(TAG, "Fail to create conversationMessage, conversation message cannot be add; Error: ", e);
			return null;
		}

		return conversationMessage;
	}

	/**
	 * Service must register callback in order to receive the message.
	 * 
	 * @param callback
	 *            An interface which implement the callback actions
	 */
	public void setCallback(ICommunicatorServiceCallback callback) {
		mCallback = callback;
	}

	/**
	 * Mark all the messages for a friend as read
	 * 
	 * @param friend
	 *            A friend of currently logged in user
	 */
	public void setMessagesToRead(Friend friend) {

		if (friend == null) {
			return;
		}

		try {
			User user = getLoggedInUser();
			Conversation conversation = getConversation(user, friend.getAccountId());

			if (conversation == null) {
				return;
			}

			ForeignCollection<ConversationMessage> conversationMessages = conversation.getConversationMessages();

			for (ConversationMessage cm : conversationMessages) {
				cm.refresh();
				if (!cm.getRead().booleanValue()) {
					cm.setRead(Boolean.valueOf(true));
					cm.update();
				}
			}

		} catch (SQLException ex) {
			Log.e(TAG, "Fail to set all message to read for user :"
					+ friend.getAccountId());
		}
	}

	/**
	 * Mark a specific message as read
	 * 
	 * @param friend
	 *            A friend of currently logged in user
	 * @param message
	 *            The conversation message
	 */
	public void setMessageToRead(Friend friend, ConversationMessage message) {

		if (friend == null)
			return;

		try {
			message.setRead(Boolean.valueOf(true));
			message.update();

		} catch (SQLException ex) {
			Log.e(TAG, "Fail to set message to read for user :"
					+ friend.getAccountId());
		}

	}

	/**
	 * Map server response status code into localized message
	 * 
	 * @param status
	 *            The default status in English returned from server
	 * @param code
	 *            The status code for the response
	 * 
	 * @return The localized status message
	 */
	private String getLocalizedStatusFromServerMessage(Message message) {

		int code = message.getStatus();
		String status = message.getMessage();
		String operation = message.getOperation();

		if (operation.equalsIgnoreCase(Message.OPERATION_CODE_TEXT_MSG)) {
			if (code != Status.SUCCESS_OK) {
				if (code == Status.CLIENT_ERROR_NOT_FOUND) {
					return getContext().getString(R.string.user_not_online);
				} else {
					return getContext().getString(R.string.try_again_later);
				}
			}
		}

		switch (code) {

		case Status.CLIENT_ERROR_FORBIDDEN: { // 403
			if (operation.equalsIgnoreCase(Message.OPERATION_CODE_DELETE_FRIEND)) {
				String str = getContext().getString(R.string.error_message_parents_cannot_delete);
				if (str != null)
					status = str;
				break;
			} else {
				String str = getContext().getString(R.string.guest_not_allowed);
				if (str != null)
					status = str;
				break;
			}
		}
		case Status.CLIENT_ERROR_NOT_FOUND: { // 404
			if (operation.equalsIgnoreCase(Message.OPERATION_CODE_SEARCH_FRIEND)) {
				String str = getContext().getString(R.string.user_not_found);
				if (str != null)
					status = str;
				break;
			}
		}

		case Status.CLIENT_ERROR_EXPECTATION_FAILED: { // 417
			if (operation.equalsIgnoreCase(Message.OPERATION_CODE_ADD_FRIEND)) {
				String str = getContext().getString(R.string.user_already_your_friend);
				if (str != null)
					status = str;
				break;
			}
		}

		case Status.CLIENT_ERROR_CONFLICT: { // 409
			if (operation.equalsIgnoreCase(Message.OPERATION_CODE_ADD_FRIEND)) {
				String str = getContext().getString(R.string.friend_request_pending);
				if (str != null)
					status = str;
				break;
			}
		}
		case Status.SERVER_ERROR_GATEWAY_TIMEOUT: { // 504
			String str = getContext().getString(R.string.try_again_later);
			if (str != null)
				status = str;
			break;
		}
		}
		return status;
	}

	/**
	 * Sort friends according three basis: 1. Unread messages has higher
	 * priority than Read messages 2. Unread messages are sorted by create date
	 * of unread messages with descending order 3. Read messages are sorted by
	 * create date of sent/read messages with descending order
	 * 
	 * @param adapter
	 *            The friend adapter which contains a list of friends
	 */
	public void sortFriends(User user, FriendAdapter adapter) {

		adapter.sort(new FriendComparator(user));

	}

	/**
	 * A private comparator class for sorting the friend list
	 * 
	 * @author andy
	 */
	private class FriendComparator implements Comparator<Friend> {

		private User mUser;

		public FriendComparator(User user) {
			mUser = user;
		}

		@Override
		public int compare(Friend lhs, Friend rhs) {

			ConversationMessage lhsLatestUnread = getLatestMessage(lhs, false);
			ConversationMessage rhsLatestUnread = getLatestMessage(rhs, false);

			if (lhsLatestUnread != null && rhsLatestUnread != null) {
				// both have unread, compare create date
				// multiply by -1 to become descending
				return (-1 * lhsLatestUnread.getCreatedDate().compareTo(rhsLatestUnread.getCreatedDate()));
			} else if (lhsLatestUnread != null && rhsLatestUnread == null) {
				return -1;
			} else if (lhsLatestUnread == null && rhsLatestUnread != null) {
				return 1;
			} else if (lhsLatestUnread == null && rhsLatestUnread == null) {

				ConversationMessage lhsLatestRead = getLatestMessage(lhs, true);
				ConversationMessage rhsLatestRead = getLatestMessage(rhs, true);

				if (lhsLatestRead != null && rhsLatestRead != null) {
					// multiply by -1 to become descending
					return (-1 * DateUtils.compare(lhs.getCreatedDate(), rhs.getCreatedDate()));
				} else if (lhsLatestRead != null && rhsLatestRead == null) {
					return -1;
				} else if (lhsLatestRead == null && rhsLatestRead != null) {
					return 1;
				} else if (lhsLatestRead == null && rhsLatestRead == null) {
					return 0;
				}
			}

			return 0;
		}

		/**
		 * Retrieve lastest message according to the read/unread, if read is
		 * true, retrieve the lastest sent / read message, if unread is false
		 * retrieve the lastest unread message
		 * 
		 * @param friend
		 *            The friend of the logged in user
		 * @param read
		 *            , A flag to tell the database helper to retrieve which
		 *            type of message
		 * 
		 * @return a ConversationMessage which has the latest create date
		 */
		private ConversationMessage getLatestMessage(Friend friend, boolean read) {

			if (friend == null) {
				return null;
			}

			ConversationMessage retMessage = null;

			List<ConversationMessage> messages = getChatHistory(mUser, friend.getAccountId());
			if (messages != null && messages.size() > 0) {
				Iterator<ConversationMessage> messageIterator = messages.iterator();
				while (messageIterator.hasNext()) {
					ConversationMessage message = messageIterator.next();

					if (read == true) {
						// get latest message sent or received if find read
						// message
						retMessage = message;
						break;
					} else {
						if (message.getIsIncomingMessage().booleanValue()
								&& message.getRead().booleanValue() == read) {
							// get latest received message only if find unread
							retMessage = message;
							break;
						}
					}
				}
			}

			return retMessage;
		}
	}

	/**
	 * Create a notfication for a user and save to the database
	 * 
	 * @param user
	 *            The user of the notification
	 * @param type
	 *            The type of notification, either chat message / friend request
	 * @param data
	 *            The specific data for each type of notification 1.
	 *            ChatMessage: ConversationMessage Object 2. FriendRequest: A
	 *            map contains meeptag, nickname
	 */
	private void createNotification(User user, NotificationType type,
			Object data) {

		if (user == null || type == null || data == null)
			return;

		Log.e(TAG, "creating notificaions...");

		Notification.Builder builder = new Notification.Builder();

		Intent intent = new Intent();
		intent.setPackage(PACKAGE_NAME);

		switch (type) {
		case FRIEND_REQUEST:
			// create friend request notification
			Map<String, String> map = (Map<String, String>) data;
			intent.setAction(CommunicatorService.COMMUNICATOR_ACTION_RECEIVED_FRIEND_REQUEST);
			intent.putExtra(CommunicatorService.NOTIFICATION_KEY_MEEP_TAG, map.get(CommunicatorService.NOTIFICATION_KEY_MEEP_TAG));
			intent.putExtra(CommunicatorService.NOTIFICATION_KEY_NICKNAME, map.get(CommunicatorService.NOTIFICATION_KEY_NICKNAME));
			intent.putExtra(CommunicatorService.NOTIFICATION_KEY_MESSAGE, map.get(CommunicatorService.NOTIFICATION_KEY_MESSAGE));

			builder.setContentText(getContext().getString(R.string.accept_friend_format_string, map.get(CommunicatorService.NOTIFICATION_KEY_NICKNAME)
					+ " ("
					+ map.get(CommunicatorService.NOTIFICATION_KEY_MEEP_TAG)
					+ ")"));

			builder.setKind(Notification.KIND_ALERT);

			Class<?> clazz = CommunicatorService.class;
			Intent positiveIntent = new Intent();
			positiveIntent.setComponent(new ComponentName(PACKAGE_NAME, clazz.getName()));
			positiveIntent.setAction(CommunicatorService.COMMUNICATOR_ACTION_ACCEPT_FRIEND_REQUEST);
			positiveIntent.putExtra(CommunicatorService.NOTIFICATION_KEY_MEEP_TAG, map.get(CommunicatorService.NOTIFICATION_KEY_MEEP_TAG));
			builder.setPositiveIntent(positiveIntent, Notification.ACTION_START_SERVICE);

			Intent negativeIntent = new Intent();
			negativeIntent.setComponent(new ComponentName(PACKAGE_NAME, clazz.getName()));
			negativeIntent.setAction(CommunicatorService.COMMUNICATOR_ACTION_REJECT_FRIEND_REQUEST);
			negativeIntent.putExtra(CommunicatorService.NOTIFICATION_KEY_MEEP_TAG, map.get(CommunicatorService.NOTIFICATION_KEY_MEEP_TAG));
			builder.setNegativeIntent(negativeIntent, Notification.ACTION_START_SERVICE);

			break;
		case CHAT_MESSAGE:
			// create chat message notification
			ConversationMessage conversationMessage = (ConversationMessage) data;
			Friend friend = conversationMessage.getConversation().getFriend();
			if (friend == null)
				return;

			try {
				friend.refresh();
			} catch (SQLException e) {
				Log.e(TAG, "Fail to retrieve friend's name when creating notification with error"
						+ e);
				return;
			}

			String message = conversationMessage.getContent();
			intent.setAction(CommunicatorService.COMMUNICATOR_ACTION_RECEIVED_CHAT_MESSAGE);
			intent.putExtra(CommunicatorService.NOTIFICATION_KEY_CONVERSATION_MESSAGE_ID, Long.valueOf(conversationMessage.getId()));
			builder.setContentText(String.format("%s: %s", friend.getName(), message));
			builder.setKind(Notification.KIND_MESSAGE);
			builder.setContentIntent(intent, Notification.ACTION_START_ACTIVITY);

			break;
		case FRIEND_REQUEST_STATUS:
			Map<String, String> maps = (Map<String, String>) data;
			FriendRequestStatus status = FriendRequestStatus.fromString((String) maps.get(CommunicatorService.NOTIFICATION_KEY_RESULT));

			int rString = -1;
			switch (status) {

			case ALL_ACCEPT:
				rString = R.string.friend_request_approved;
				break;
			case OPPOSITE_PARENT_REJECT:
				rString = R.string.friend_request_oppo_parent_reject;
				break;
			case SELF_PARENT_REJECT:
				rString = R.string.friend_request_self_parent_reject;
				break;
			case FRIEND_REJECT:
				rString = R.string.friend_request_friend_reject;
				break;
			default:
				// Unexpected friend request status
				return;
			}

			if (rString != -1) {
				builder.setContentText(getContext().getString(rString, maps.get(CommunicatorService.NOTIFICATION_KEY_NAME)));

				builder.setContentIntent(intent, Notification.ACTION_START_ACTIVITY);
				builder.setKind(Notification.KIND_WARNING);
			}

		}

		builder.setContentTitle(NOTIFICATION_TITLE);
		builder.setImportant(true);
		builder.setNumber(getUnreadConversationCount(user));

		// add the notification to the notification service
		Notification notification = builder.build();
		NotificationManager nm = (NotificationManager) ServiceManager.getService(getContext(), ServiceManager.NOTIFICATION_SERVICE);
		long notificationId = nm.notifyBlocking(user.getAccountId(), notification);

		Log.e(TAG, "done create notificaions...");

		if (type == NotificationType.CHAT_MESSAGE) {
			try {
				ConversationMessage conversationMessage = (ConversationMessage) data;
				conversationMessage.setNotificationId(notificationId);
				conversationMessage.update();
			} catch (SQLException ex) {
				Log.e(TAG, "Fail to add notification Id to database");
			}
		}

	}

	/**
	 * Retrieve unread conversation count
	 * 
	 * @return unread conversation count
	 */
	private int getUnreadConversationCount(User user) {

		int count = 0;

		if (user == null)
			return 0;

		try {
			ModelAttributes conversationAttrs = Schema.getAttributes(Conversation.class);
			ModelAttributes conversationMessageAttrs = Schema.getAttributes(ConversationMessage.class);
			Dao<Conversation, Long> conversationDao = mHelper.getDao(Conversation.class);
			Dao<ConversationMessage, Long> conversationMessageDao = mHelper.getDao(ConversationMessage.class);
			QueryBuilder<Conversation, Long> conversationQB = conversationDao.queryBuilder();
			QueryBuilder<ConversationMessage, Long> conversationMessageQB = conversationMessageDao.queryBuilder();

			conversationMessageQB.selectColumns(conversationMessageAttrs.getColumnName(ConversationMessage.CONVERSATION_FIELD_NAME));
			conversationMessageQB.where().eq(conversationMessageAttrs.getColumnName(ConversationMessage.READ_FIELD_NAME), Boolean.valueOf(false));

			conversationQB.where().eq(conversationAttrs.getColumnName(Conversation.USER_FIELD_NAME), Long.valueOf(user.getId())).and().in(conversationAttrs.getColumnName(Conversation.ID_FIELD_NAME), conversationMessageQB);

			count = (int) conversationQB.distinct().countOf();

		} catch (SQLException e) {
			Log.e(TAG, "Fail to retrieve unread conversation count with error:"
					+ e);
		}

		return count;

	}

	/**
	 * Retrieve the application which launching in foreground
	 * 
	 * @return The package name of the foreground application
	 * @throws NameNotFoundException
	 */
	public String getForegroundApp() throws NameNotFoundException {

		RunningTaskInfo info = null;
		ActivityManager am = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> l = am.getRunningTasks(1000);
		Iterator<RunningTaskInfo> i = l.iterator();

		String packName = new String();

		if (i != null && i.hasNext()) {
			info = i.next();
			packName = info.topActivity.getPackageName();
		}
		return packName;
	}

	/**
	 * Retrieve a single conversation message
	 * 
	 * @param cmId
	 *            The unique Id of Conversation Message
	 * @return The Conversation Message object with id = cmId
	 */
	public ConversationMessage getConversationMessage(Long cmId) {

		ConversationMessage message = null;

		try {
			ModelAttributes attrs = Schema.getAttributes(ConversationMessage.class);
			Dao<ConversationMessage, Long> dao = mHelper.getDao(ConversationMessage.class);

			QueryBuilder<ConversationMessage, Long> builder = dao.queryBuilder();
			builder.where().eq(attrs.getColumnName(ConversationMessage.ID_FIELD_NAME), cmId);
			message = builder.queryForFirst();
			message.getConversation().refresh();
			message.getConversation().getFriend().refresh();
		} catch (SQLException e) {
			Log.e(TAG, "Fail to retreive conversation message by Id");
		}

		return message;
	}

	public List<ConversationMessage> getSortedConversationMessages(
			Conversation conversation, int limit) {

		if (conversation == null)
			return null;

		List<ConversationMessage> messages = null;

		try {
			ModelAttributes attrs = Schema.getAttributes(ConversationMessage.class);
			Dao<ConversationMessage, Long> dao = mHelper.getDao(ConversationMessage.class);

			QueryBuilder<ConversationMessage, Long> builder = dao.queryBuilder();
			builder.where().eq(attrs.getColumnName(ConversationMessage.CONVERSATION_FIELD_NAME), Long.valueOf(conversation.getId()));

			builder.orderBy(attrs.getColumnName(ConversationMessage.CREATED_DATE_FIELD_NAME), false);
			builder.limit(limit);

			messages = builder.query();

		} catch (SQLException e) {
			Log.e(TAG, "Fail to retreive conversation message by Id");
		}

		return messages;
	}

	public void cancelAllNotificationsForFriend(User user, Friend friend) {

		Conversation conversation = getConversation(user, friend.getAccountId());

		if (conversation != null) {
			try {
				ModelAttributes conversationMessageAttr = Schema.getAttributes(ConversationMessage.class);
				Dao<ConversationMessage, Long> conversationMessageDao = mHelper.getDao(ConversationMessage.class);

				QueryBuilder<ConversationMessage, Long> conversationMessageQB = conversationMessageDao.queryBuilder();
				conversationMessageQB.where().eq(conversationMessageAttr.getColumnName(ConversationMessage.CONVERSATION_FIELD_NAME), Long.valueOf(conversation.getId())).and().gt(conversationMessageAttr.getColumnName(ConversationMessage.NOTIFICATION_ID_FIELD_NAME), Long.valueOf(ConversationMessage.NOTIFICATION_EMPTY_ID));

				// conversationMessageQB.selectColumns(conversationMessageAttr.getColumnName(ConversationMessage.NOTIFICATION_ID_FIELD_NAME)).distinct();

				List<ConversationMessage> conversationMessages = conversationMessageQB.query();

				if (conversationMessages != null) {
					Iterator<ConversationMessage> cmIterator = conversationMessages.iterator();

					NotificationManager nm = (NotificationManager) ServiceManager.getService(getContext(), ServiceManager.NOTIFICATION_SERVICE);

					List<Notification> notifications = nm.get(user.getAccountId(), Notification.KIND_MESSAGE, false, 0, 1);

					if (notifications != null && notifications.size() > 0) {
						Notification n = notifications.get(0);
						n.number = getUnreadConversationCount(user);
						nm.notify(user.getAccountId(), n);
					}

					while (cmIterator.hasNext()) {
						ConversationMessage conversationMessage = cmIterator.next();
						long longValue = conversationMessage.getNotificationId();
						nm.cancel(longValue);
					}
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
