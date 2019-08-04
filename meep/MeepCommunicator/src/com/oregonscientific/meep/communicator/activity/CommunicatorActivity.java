package com.oregonscientific.meep.communicator.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.account.IAccountServiceCallback;
import com.oregonscientific.meep.communicator.CommunicatorService;
import com.oregonscientific.meep.communicator.CommunicatorServiceConnector;
import com.oregonscientific.meep.communicator.CommunicatorServiceHandler.FriendRequestStatus;
import com.oregonscientific.meep.communicator.Conversation;
import com.oregonscientific.meep.communicator.ConversationMessage;
import com.oregonscientific.meep.communicator.Friend;
import com.oregonscientific.meep.communicator.ICommunicatorServiceCallback;
import com.oregonscientific.meep.communicator.R;
import com.oregonscientific.meep.communicator.User;
import com.oregonscientific.meep.communicator.view.IDeleteFriendPopUpInterface;
import com.oregonscientific.meep.communicator.view.IPopupInterface;
import com.oregonscientific.meep.communicator.view.PopUpFragment;
import com.oregonscientific.meep.communicator.view.conversation.Emoticon;
import com.oregonscientific.meep.communicator.view.friend.BaseFriend;
import com.oregonscientific.meep.communicator.view.friend.FriendAdapter;
import com.oregonscientific.meep.permission.PermissionManager;
import com.oregonscientific.meep.util.BitmapUtils;
import com.oregonscientific.meep.util.ImageDownloader;
import com.oregonscientific.meep.util.NetworkUtils;
import com.oregonscientific.meep.widget.StrokedTextView;


/**
 * Main activity of MEEP Communicator
 */
public class CommunicatorActivity extends FragmentActivity {
	
	private View friendInDeleteMode = null;

	private final String TAG = getClass().getSimpleName();
	
	private FriendAdapter friendAdapter = null;

	private final String CONVERSATION_FRAGMENT = "conversationFragment";
	
	private final String POPUP_DIALOG = "dialog";
	
	private String IMAGE_CACHE_DIR = "communicator";
	
	private CommunicatorServiceConnector mConnector;
	
	private ImageDownloader mImageDownloader = null;
	
	private User mUser = null;
	
	private final IAccountServiceCallback mAccountServiceCallback = new IAccountServiceCallback.Stub() {
		
		@Override
		public void onUpdateUser(boolean arg0, String arg1, Account arg2)
				throws RemoteException {
			
		}

		@Override
		public void onSignOut(boolean arg0, String arg1, Account arg2)
				throws RemoteException {
			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					onSignOutCallback();
				}
			});
			
		}

		@Override
		public void onSignIn(boolean arg0, String arg1, Account arg2)
				throws RemoteException {

			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					onSignInCallback();
				}
			});
		}
	
	};
	
	private final ICommunicatorServiceCallback mCommunicatorCallback = new ICommunicatorServiceCallback() {
		
		@Override
		public void onServiceDisconnected() {
			
		}
		
		@Override
		public void onServiceConnected() {
			
			final Handler handler = new Handler();
			ExecutorService service = Executors.newSingleThreadExecutor();
			service.execute(new Runnable() {

				@Override
				public void run() {

					User user = getLoggedInUser();
					boolean isOffline = true;
					if (user == null) {
						user = getLastLoggedInUser();
						isOffline = true;
					} else {
						isOffline = false;
					}
						
					final boolean statusLightState = !isOffline;
					
					if (user != null) {
						setUser(user);
						handler.post(new Runnable() {

							@Override
							public void run() {
								// update user profile
								updateUserProfile(getUser());
								refreshFriendList(getUser());
								getFriendListFromServer();
								setStatusLight(statusLightState);
								
								if (!statusLightState)
									showNetworkDialog();
							}

						});
					}
				}
			});
	
		}

		@Override
		public void onFriendRequestSent(String meepTag, String errorMessage) {
			doFriendRequestSent(meepTag, errorMessage);
		}
		
		@Override
		public void onFriendRequestReceived(
				final String meepTag, 
				final String nickname,
				final String friendMessage,
				final String errorMessage) {
			
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					doFriendRequestReceived(meepTag, nickname, friendMessage, errorMessage);
				}
			});
			
		}
		
		@Override
		public void onFriendListReceived(final String errorMessage) {
	
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					doFriendListReceived(errorMessage);
					// to perform pending actions
					performPendingAction();
				}
				
			});
			
		}
		
		@Override
		public void onFriendDeleted(final String accountId, final String nickname, final String errorMessage) {
			
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					doFriendDeleted(accountId, nickname, errorMessage);
				}
			});
			
		}
		
		@Override
		public void onFriendAccepted(final String meepTag, final String errorMessage) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					doFriendAccepted(meepTag, errorMessage);
				}
				
			});
		}
		
		@Override
		public void onChatMessageSent(final String message, final String errorMessage) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					doMessageSent(message, errorMessage);
				}
				
			});				
		}
		
		@Override
		public void onChatMessageReceived(final ConversationMessage message, final String errorMessage) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					doChatMessageReceived(message, errorMessage);
				}
			});
							
		}
		

		@Override
		public void onFriendSearched(final String meepTag,final String nickname, final String errorMessage) {
			
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					doFriendSearched(meepTag, nickname, errorMessage);
				}
			});
			
		}

		@Override
		public void onFriendRequestStatusReceived(final String meepTag, final String name,
				final FriendRequestStatus status, final String errorMessage) {

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					doFriendRequestStatusReceived(meepTag, name, status, errorMessage);
				}
			});
			
		}

		@Override
		public void onFriendRejected(final String meepTag, final String errorMessage) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					doFriendRejected(meepTag, errorMessage);
				}

			});
		}			
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		mConnector = new CommunicatorServiceConnector(this, mCommunicatorCallback);
		mConnector.connect();		
		
		AccountManager am = (AccountManager) ServiceManager.getService(CommunicatorActivity.this, ServiceManager.ACCOUNT_SERVICE);
		am.registerCallback(mAccountServiceCallback);
		
		initUI();
	}
	
	/**
	 * Handle received chat message and request from notification center
	 * 
	 */
	private void performPendingAction() {

		Intent intent = getIntent();
		String action = null;
		if (intent != null) {
			action = intent.getAction();
			if (action == null)
				// no pending action
				return;
		} else {
			// no pending intent
			return;
		}


		if (action.equals(CommunicatorService.COMMUNICATOR_ACTION_RECEIVED_FRIEND_REQUEST)) {

			Bundle extras = intent.getExtras();
			if (extras != null) {
				String meepTag = extras.getString(CommunicatorService.NOTIFICATION_KEY_MEEP_TAG);
				String nickname = extras.getString(CommunicatorService.NOTIFICATION_KEY_NICKNAME);
				String friendMessage = extras.getString(CommunicatorService.NOTIFICATION_KEY_MESSAGE);
				if (meepTag != null && nickname != null)
					doFriendRequestReceived(meepTag, nickname, friendMessage, null);
			}
		}

		else if (action.equals(CommunicatorService.COMMUNICATOR_ACTION_RECEIVED_CHAT_MESSAGE)) {
			if (mConnector != null) {
				CommunicatorService service = mConnector.getService();
		
				if (service != null) {
					Bundle extras = intent.getExtras();
					if (extras != null) {
						Long conversationMsgId = extras.getLong(CommunicatorService.NOTIFICATION_KEY_CONVERSATION_MESSAGE_ID);
						
						ConversationMessage cm =  service.getConversationMessage(conversationMsgId);
						if (cm != null)
								showConversationDialog(cm.getConversation().getFriend());
					}
				}
			}
		}
	}

	/**
	 * Set the unread message count of a friend
	 * @param friend friend whose unread count is to be updated
	 */
	private void setUnreadCount(Friend friend, int unreadCount) {
		
		final GridView gridView = (GridView) findViewById(R.id.friends);
		if (gridView.getAdapter() != null) {

			FriendAdapter frdAdapter = (FriendAdapter) gridView.getAdapter();
			int position = -1;
			for (int i=0 ; i < frdAdapter.getCount(); i++) {
				Friend adapterFrd = frdAdapter.getItem(i);
				if (adapterFrd.getAccountId() != null) {
					if (friend.getAccountId() != null) {
						if (friend.getAccountId().equals(adapterFrd.getAccountId())) {
							position = i;
							break;
						}
					}
				}
			}
			if (position != -1) {
				BaseFriend friendView = (BaseFriend) gridView.getChildAt(position);
				if (friendView != null) {
					friendView.setUnreadCount(unreadCount);
					frdAdapter.notifyDataSetChanged();
				}
			}
		}

	}
	

	/**
	 * Gets all friends of a user in database
	 * @param meepTag MEEP tag of user
	 * @return a list of friends containing all friends of the user in database
	 */
	private List<Friend> getFriends(User user) {
		
		if (mConnector != null) {
			
			CommunicatorService service = mConnector.getService();
			if (service != null) {
				return service.getFriends(user);
			}
		}
		
		return null;
	}
	
	/**
	 * 
	 * Initialize the UI
	 * 
	 */
	private void initUI() {
		
		setContentView(R.layout.friend_view);
		
		showLoading();
		
		final List<Friend> friendList = new ArrayList<Friend>();
		
		// set listener for add friends button
		final LinearLayout addFriends = (LinearLayout) findViewById(R.id.add_friends);

		addFriends.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showSearchFriendPopup();
			}
		});
		
		// set friends to adapter
		final GridView gridView = (GridView) findViewById(R.id.friends);
		
		final FriendAdapter friendAdapter = new FriendAdapter(this, friendList);
		
		gridView.setAdapter(friendAdapter);
		
		// go to conversation view when friend is clicked
		gridView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(
					AdapterView<?> parent,
					View view,
					int position,
					long id) {

				GridView gridView = (GridView) findViewById(R.id.friends);
				if (gridView != null) {

					Friend friend = null;
					FriendAdapter adapter = (FriendAdapter)gridView.getAdapter();
					if (adapter != null && adapter.getCount() > position) {
						friend = adapter.getItem(position);
					}
					showConversationDialog(friend);
				}
			}
		});
		
		// show delete icon when friend is long clicked
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(
					AdapterView<?> parent,
					View view,
					final int position,
					long id) {
				
				GridView gridView = (GridView) findViewById(R.id.friends);
				gridView.setEnabled(false);
				int rowNumber = position / gridView.getNumColumns();
				gridView.smoothScrollToPosition(rowNumber * gridView.getNumColumns());
				setAddFriendsButtonEnabled(false);
				BaseFriend friend = (BaseFriend) view;
				View home = (View) parent.getParent();
				home.getBackground().setAlpha(70);
				for (int i = 0; i < gridView.getCount(); i++) {
					View friendView = gridView.getChildAt(i);
					if (friendView != null) {
						friendView.setAlpha(0.2f);
					}
				}
				friend.setAlpha(1.0f);
				friend.findViewById(R.id.unread_count).setAlpha(0.1f);
				
				friendInDeleteMode = friend;
				ImageView cross = (ImageView) friend.findViewById(R.id.cross);
				cross.setVisibility(View.VISIBLE);
				cross.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View view) {
						GridView gridView = (GridView) findViewById(R.id.friends);
						FriendAdapter frdAdapter = (FriendAdapter) gridView.getAdapter();
						Friend friend = frdAdapter.getItem(position);
						showDeleteFriendPopup(friend);
						onBackPressed();
					}
				});
				return true;
			}
		});
		
		final ImageView transitionUpView = (ImageView) findViewById(R.id.transition_up);
		final ImageView transitionDownView = (ImageView) findViewById(R.id.transition_down);
		transitionUpView.setVisibility(View.INVISIBLE);
		transitionDownView.setVisibility(View.INVISIBLE);
		gridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// Ignore
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				if (gridView.getFirstVisiblePosition() == 0) {
					transitionUpView.setVisibility(View.INVISIBLE);
				} else {
					transitionUpView.setVisibility(View.VISIBLE);
				}

			}
		});
		
		setStatusLight(false);
		
	}
	
	
	private void showDeleteFriendPopup(Friend friend) {
		
		String accountId = friend.getAccountId();
		String nickname = friend.getName();
		IDeleteFriendPopUpInterface listener = new IDeleteFriendPopUpInterface() {

			@Override
			public void onYesButtonPressed(PopUpFragment fragment, String listenerAccId) {
				if (mConnector != null) {
					CommunicatorService service = mConnector.getService();
					if (service != null)
						service.removeFriend(listenerAccId);
						fragment.setDeleteFriendPopupListener(null);
						fragment.dismiss();
				}
			}

			@Override
			public void onNoButtonPressed(PopUpFragment fragment) {
				fragment.setDeleteFriendPopupListener(null);
				fragment.dismiss();
			}
		};
		
		PopUpFragment fragment = PopUpFragment.newInstance(PopUpFragment.DELETE_FRIEND_DIALOG_ID, accountId, nickname);
		fragment.setDeleteFriendPopupListener(listener);
		fragment.show(getSupportFragmentManager(), POPUP_DIALOG);
	}
	
	
	private void showSearchFriendPopup() {


		final IPopupInterface listener = new IPopupInterface() {

			@Override
			public void onAddButtonPressed(PopUpFragment fragment, String message) {
			}

			@Override
			public void onDeclineButtonPressed(PopUpFragment thisFragment,
					String meepTag) {}

			@Override
			public void onAcceptButtonPressed(final PopUpFragment thisFragment,
					String meepTag) {}

			@Override
			public void onSearchButtonPressed(
					final PopUpFragment fragment,
					final String message) {

				if (message == null || message.length() == 0) {
					fragment.dismiss();
					showErrorDialog(getString(R.string.please_enter_meep_id), null, PopUpFragment.ERROR_ACTION_SEARCH_FRIEND);
					return;
				}

				final User user = getUser();

				if (user == null) {
					if (fragment == null) {
						PopUpFragment popupFragment =  (PopUpFragment)getSupportFragmentManager().findFragmentByTag(POPUP_DIALOG);
						popupFragment.dismiss();
					} else {
						fragment.dismiss();
					}
					return;
				}

				if (message.equals(user.getMeepTag())) {
					showErrorDialog(getString(R.string.cannot_add_yourself), null);
					return;
				}


				Handler handler = new Handler();
				searchFriendAsynchronously(user, handler, message);
				fragment.dismiss();
				
			}

			@Override
			public void onYesButtonPressed(PopUpFragment thisFragment) {
				// Ignore
			}

			@Override
			public void onNoButtonPressed(PopUpFragment thisFragment) {
				// Ignore
			}

			@Override
			public void onOkButtonPressed(PopUpFragment thisFragment, String action) {
				// Ignore
			}
		};

		setAddFriendsButtonEnabled(false);

		PopUpFragment oldFragment = (PopUpFragment) getSupportFragmentManager().findFragmentByTag(POPUP_DIALOG);
		if (oldFragment != null) {
			if (oldFragment.getType() == PopUpFragment.SEARCH_FRIENDS_DIALOG_ID) {
				return;
			}
		}
		PopUpFragment newFragment = PopUpFragment.newInstance(PopUpFragment.SEARCH_FRIENDS_DIALOG_ID, null, null);
		newFragment.setListener(listener);
		newFragment.show(getSupportFragmentManager(), POPUP_DIALOG);

	}
	
	
	private void searchFriendAsynchronously(final User user, final Handler handler, final String message) {
		
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				ExecutorService service = Executors.newSingleThreadExecutor();
				service.execute(new Runnable() {

					@Override
					public void run() {

						if (user != null) {
							PermissionManager pm = (PermissionManager) ServiceManager.getService(CommunicatorActivity.this, ServiceManager.PERMISSION_SERVICE);
							Log.e(TAG, "Getting permission @ SearchUser");
							if (pm != null) {
								if (!pm.containsBadwordBlocking(user.getAccountId(), message)) {
									Log.e(TAG, "Finish get permission @ Search User");
									mConnector.getService().searchUser(message);

								} else {

									handler.post(new Runnable() {
										public void run() {
											showErrorDialog(getString(R.string.blocked), PopUpFragment.ERROR_ACTION_SEARCH_FRIEND);
										}
									});
								}
							}
						}
					}

				});								
			}
		};
		
		// spawn another thread to search friend
		new Thread(r).start();
		
	}
	
	
	private void showDeleteFriendSuccessPopup(String nickname) {

		PopUpFragment fragment = PopUpFragment.newInstance(PopUpFragment.DELETE_FRIEND_SUCCESS_DIALOG_ID, nickname, null);
		fragment.show(getSupportFragmentManager(), POPUP_DIALOG);
	}
	
	private void showFriendRequestSuccessDialog() {

		PopUpFragment fragment = PopUpFragment.newInstance(PopUpFragment.FRIEND_REQUEST_SUCCESS_DIALOG_ID, null, null);
		fragment.show(getSupportFragmentManager(), POPUP_DIALOG);
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.communicator, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		// override back button when in delete mode
		if (friendInDeleteMode != null) {
			
			try {
				ImageView cross = (ImageView) friendInDeleteMode.findViewById(R.id.cross);
				cross.setVisibility(View.GONE);

				((View) friendInDeleteMode.getParent().getParent()).getBackground().setAlpha(255);
				GridView gridView = (GridView) findViewById(R.id.friends);
				for (int i = 0; i < gridView.getCount(); i++) {
					View friendView = gridView.getChildAt(i);
					if (friendView != null) {
						friendView.setAlpha(1.0f);
						friendView.findViewById(R.id.unread_count).setAlpha(1.0f);
					}
				}

				friendInDeleteMode = null;

			} catch (Exception ex) {
				// refresh friend list if encountered error
				refreshFriendList(getUser());
			} finally {
				setGridViewEnabled(true);
				setAddFriendsButtonEnabled(true);
			}
		} else {
			super.onBackPressed();
		}
		
	}
	
	
	
	@Override
	public void onDestroy() {
		if (mConnector != null) {
			mConnector.disconnect();
		}
		
		AccountManager am = (AccountManager) ServiceManager.getService(CommunicatorActivity.this, ServiceManager.ACCOUNT_SERVICE);
		am.unregisterCallback(mAccountServiceCallback);
				
		ServiceManager.unbindServices(this);
		
		ConversationFragment.clearConversations();
		Emoticon.clearEmoticonMap();
		
		super.onDestroy();
	}
	
	/**
	 * Set the enabled state of grid view
	 * @param enabled true if grid view is enabled, false otherwise
	 */
	public void setGridViewEnabled(boolean enabled) {
		GridView gridView = (GridView) findViewById(R.id.friends);
		gridView.setEnabled(enabled);
	}
	
	/**
	 * Set the enabled state of add friend button
	 * @param enabled true if add friend button is enabled, false otherwise
	 */
	public void setAddFriendsButtonEnabled(boolean enabled) {
		LinearLayout addFriends = (LinearLayout) findViewById(R.id.add_friends);
		addFriends.setEnabled(enabled);
	}
	
	/**
	 * Add a friend to the friend adapter
	 * @param newFriend the new friend to be added
	 */
	public void addFriendToAdapter(Friend newFriend) {
		
		GridView gridView = (GridView) findViewById(R.id.friends);
		if (gridView != null) {
			
			((FriendAdapter) gridView.getAdapter()).add(newFriend);
		}
		((FriendAdapter) gridView.getAdapter()).notifyDataSetChanged();
		
		gridView.post(new Runnable() {
			
			@Override
			public void run() {
				
				GridView gridView = (GridView) findViewById(R.id.friends);
				ImageView transitionDownView = (ImageView) findViewById(R.id.transition_down);	
				if (gridView.getLastVisiblePosition() < gridView.getCount()) {
					transitionDownView.setVisibility(View.VISIBLE);
				} else {
					transitionDownView.setVisibility(View.INVISIBLE);
				}
				
				dismissLoading();
			}
		});
	}
	
	/**
	 * Handle response from search friend request
	 * 
	 * @param meepTag The meepTag of the searched friend
	 * @param nickname The nickname of the searched friend
	 * @param errorMessage The error message from the server response
	 */
	private void doFriendSearched(final String meepTag, final String nickname, String errorMessage) {
		
		
		if (errorMessage != null) {
			showErrorDialog(errorMessage, getString(R.string.no_results), PopUpFragment.ERROR_ACTION_SEARCH_FRIEND);
			return;
		}
		
		final IPopupInterface listener = new IPopupInterface() {

			@Override
			public void onAddButtonPressed(final PopUpFragment fragment, final String message) {

				final Handler handler = new Handler();
				addFriendAsynchronously(handler, message, nickname, meepTag);
				fragment.dismiss();
			}

			@Override
			public void onDeclineButtonPressed(PopUpFragment thisFragment,
					String meepTag) {}

			@Override
			public void onAcceptButtonPressed(PopUpFragment thisFragment,
					String meepTag) {}

			@Override
			public void onSearchButtonPressed(PopUpFragment fragment,
					String message) {				
			}

			@Override
			public void onYesButtonPressed(PopUpFragment thisFragment) {
				// Ignore
			}

			@Override
			public void onNoButtonPressed(PopUpFragment thisFragment) {
				// Ignore
			}

			@Override
			public void onOkButtonPressed(PopUpFragment thisFragment, String action) {
				// Ignore
			}
		};

		PopUpFragment newFragment = PopUpFragment.newInstance(PopUpFragment.ADD_FRIENDS_DIALOG_ID, nickname, null);
		newFragment.setListener(listener);
		newFragment.show(getSupportFragmentManager(), POPUP_DIALOG);

	}
	
	
	private void addFriendAsynchronously(final Handler handler, final String message, final String nickname, final String meepTag) {
		
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				ExecutorService service = Executors.newSingleThreadExecutor();
				service.execute(new Runnable() {

					@Override
					public void run() {

						User user = getLoggedInUser();
						String enclosedMessage = message;
						if (user != null) {
							if (enclosedMessage == null || enclosedMessage.length() == 0) {
								enclosedMessage = getString(R.string.add_friend_hint, nickname);
							}
							
							PermissionManager pm = (PermissionManager) ServiceManager.getService(CommunicatorActivity.this, ServiceManager.PERMISSION_SERVICE);
							if (!pm.isBadword(user.getAccountId(), enclosedMessage)) {
								mConnector.getService().addFriend(meepTag, enclosedMessage);
							} else {
								handler.post(new Runnable() {
									public void run() {
										showErrorDialog(getString(R.string.blocked), null);
									}
								});
							}
						}		
					}

				});				
			}
		};
	
		new Thread(r).start();
	}
	
	/**
	 * Handle response from sending a friend request
	 * 
	 * @param meepTag The meepTag of the searched friend
	 * @param errorMessage The error message from the server response
	 */
	private void doFriendRequestSent(String meepTag, String errorMessage) {
		if (errorMessage != null) {
			showErrorDialog(errorMessage, null);
			return;
		}
			
		showFriendRequestSuccessDialog();
		
	}

	/**
	 * Handle response from delete-friend request
	 * 
	 * @param accountId The unique identifier for a friend
	 * @param errorMessage The error message from the server response
	 */
	private void doFriendDeleted(String accountId, String nickname, String errorMessage) {

		if (errorMessage != null) {
			showErrorDialog(errorMessage, null);
			return;
		}

		showDeleteFriendSuccessPopup(nickname);
		refreshFriendList(getUser());
	}
	
	/**
	 * Display the conversation dialog of within user and his/her friend
	 * 
	 * @param friend The friend who is in user's conversation dialog
	 */
	private void showConversationDialog(final Friend friend) {


		if (friend != null) {
			
			setMessagesToRead(friend);

			if (mConnector != null) {
				CommunicatorService  service = mConnector.getService();
				if (service != null) {
					service.cancelAllNotificationsForFriend(getUser(), friend);
				}
			}
			
			setGridViewEnabled(false);

			ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentByTag(CONVERSATION_FRAGMENT);
			if (fragment == null) {
				final Context context = this;

				IConversationInterface conversationInterface = new  IConversationInterface() {

					@Override
					public void onSendButtonPressed(final ConversationFragment fragment,
							final String message, final ConversationMessage conversationMessage) {

						final Handler handler = new Handler();
						ExecutorService service = Executors.newSingleThreadExecutor();
						service.execute(new Runnable() {

							@Override
							public void run() {

								if (!NetworkUtils.hasConnection(CommunicatorActivity.this)) {
									handler.post(new Runnable() {

										@Override
										public void run() {
											showNetworkDialog2();
										}
									});
									return;
								}

								sendMessageAsynchronously(fragment, handler, message, conversationMessage);
								
							}
						});
						
					}

					@Override
					public void onConversationTabChanged(
							ConversationFragment fragment,
							Conversation conversation) {
					
						if (conversation != null) {
							Friend friend = conversation.getFriend();
							setMessagesToRead(friend);
						}
						
					}
				};

				fragment = ConversationFragment.newInstance();
				fragment.setListener(conversationInterface);
			}
			
			// show the fragment
			Conversation conversation = mConnector.getService().getConversation(getUser(), friend.getAccountId());
			if (conversation != null) {
				fragment.addConversation(this, friend, conversation);
				fragment.show(getSupportFragmentManager(), CONVERSATION_FRAGMENT);
			}
		}
		
	}

	
	
	private void sendMessageAsynchronously(final ConversationFragment fragment, final Handler handler, final String message, final ConversationMessage conversationMessage) {
		
		User user = getLoggedInUser();

		if (user != null) {

			PermissionManager pm = (PermissionManager) ServiceManager.getService(CommunicatorActivity.this, ServiceManager.PERMISSION_SERVICE);
			if (pm != null) {
				String replacedMessage = pm.replaceBadwordsBlocking(user.getAccountId(), message, CommunicatorService.BAD_WORD_REPLACEMENT_STRING);
				Friend friend = conversationMessage.getConversation().getFriend();
				mConnector.getService().sendChatMessage(friend.getAccountId(), replacedMessage);
				final ConversationMessage newMessage = new ConversationMessage(conversationMessage);
				newMessage.setContent(replacedMessage);
				handler.post(new Runnable() {

					@Override
					public void run() {
						fragment.addMessageToMessageList(newMessage);

					}
				});
			}
		}
	}

	
	/**
	 * Set all the messages between user and friends to read, and update the unread count
	 * 
	 * @param accountId The unique identifier for a friend
	 * @param errorMessage The error message from the server response
	 */
	private void setMessagesToRead(Friend friend) {
		
		if (mConnector != null) {
			
			CommunicatorService service = mConnector.getService();
			if (service != null) {
				service.setMessagesToRead(friend);
				refreshUnreadCount(friend);
			}
			
		}
	}
	
	/**
	 * Set one the message between user and friends to read, and update the unread count
	 * 
	 * @param friend The unique identifier for a friend
	 * @param message Conversation Message object that to be modify
	 */
	private void setMessageToRead(Friend friend, ConversationMessage message) {
		
		if (mConnector != null) {
			
			CommunicatorService service = mConnector.getService();
			if (service != null) {
				service.setMessageToRead(friend, message);
				refreshUnreadCount(friend);
			}
			
		}
		
	}
	
	/**
	 * Set one the message between user and friends to read, and update the unread count
	 * 
	 * @param message Conversation Message object that received from server
	 * @param errorMessage The error message from the server response
	 */
	private void doChatMessageReceived(ConversationMessage message, String errorMessage) {
		
		if (errorMessage != null) {
			showErrorDialog(errorMessage, null);
			return;
		}
		
		if (message == null)
			return;
		
		// update unread badge on friend list
		Friend friend  = null;
		try {
			friend = message.getConversation().getFriend();
			if (friend != null) {
				friend.refresh();
			}
		} catch ( SQLException ex) {
			Log.e(TAG, "Fail to refresh friend");
			return;
		}
		
		if (friend != null) {
			refreshUnreadCount(friend);
			sortFriendList(getUser());
			ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentByTag(CONVERSATION_FRAGMENT);
			if (fragment != null) {
				SharedPreferences settings = getSharedPreferences("MEEP Communicator", 0);
				String currentlyChattingAccountId= settings.getString("FriendCurrentChatting", null);
				if (friend.getAccountId().equals(currentlyChattingAccountId)) {
					setMessageToRead(friend, message);
					fragment.onReceiveConversationMessage(message);
				}
			}
		}
		
	}

	/**
	 * Handle the friend request received from server, 
	 * show a Pop up dialog if errorMessage is null 
	 * 
	 * @param meepTag The meepTag of the friend 
	 * @param nickname The nickname of the friend
	 * @param erroMessage The error message in the response of server, null when the request was successful
	 */
	private void doFriendRequestReceived(String meepTag, String nickname, String friendMessage, String errorMessage) {
		
		if (errorMessage != null) {
			showErrorDialog(errorMessage, null);
			return;
		}
		
		final IPopupInterface listener = new IPopupInterface() {

			@Override
			public void onAddButtonPressed(PopUpFragment fragment, String message) {			
			}

			@Override
			public void onDeclineButtonPressed(PopUpFragment thisFragment,
					String meepTag) {
				
				if (mConnector != null) {
					
					CommunicatorService service = mConnector.getService();
					if (service != null) {
						service.declineFriend(meepTag);
						thisFragment.dismiss();
					}
				}	
			}

			@Override
			public void onAcceptButtonPressed(PopUpFragment thisFragment,
					String meepTag) {
				
				if (mConnector != null) {
					
					CommunicatorService service = mConnector.getService();
					if (service != null) {
						service.acceptFriend(meepTag);
						thisFragment.dismiss();
					}
				}
				
			}

			@Override
			public void onSearchButtonPressed(PopUpFragment fragment, String message) {
				// Ignore
			}

			@Override
			public void onYesButtonPressed(PopUpFragment thisFragment) {
				// Ignore
			}

			@Override
			public void onNoButtonPressed(PopUpFragment thisFragment) {
				// Ignore
			}

			@Override
			public void onOkButtonPressed(PopUpFragment thisFragment, String action) {
				// Ignore
			}
		};
		
		friendMessage = friendMessage == null ? "" : friendMessage;
		
		PopUpFragment newFragment = PopUpFragment.newInstance(PopUpFragment.ACCEPT_FRIEND_DIALOG_ID, meepTag, nickname, friendMessage);
		newFragment.setListener(listener);
		newFragment.show(getSupportFragmentManager(), POPUP_DIALOG);
		
	}
	
	/**
	 * Handle friend list received from server, refresh the friend list if the request was successful
	 * 
	 * @param erroMessage The error message in the response of server, null when the request was successful
	 */
	private void doFriendListReceived(String errorMessage) {
		
		if (errorMessage != null) {
			showErrorDialog(errorMessage, null);
			return;
		}
	
		refreshFriendList(getUser());
	}
	
	/**
	 * Handle friend accepted responses, refresh the friend list if the request was successful
	 * 
	 * @param erroMessage The error message in the response of server, null when the request was successful
	 */
	private void doFriendAccepted(String accountId, String errorMessage) {
		if (errorMessage != null) {
			showErrorDialog(errorMessage, null);
			return;
		}
		
		refreshFriendList(getUser());
	}
	
	/**
	 * Handle friend reject responses, refresh the friend list if the request was successful
	 * 
	 * @param erroMessage The error message in the response of server, null when the request was successful
	 */
	private void doFriendRejected(String accountId, String errorMessage) {
		if (errorMessage != null) {
			showErrorDialog(errorMessage, null);
			return;
		}
	}
	
	/**
	 * Handle message sent to server, display error dialog if the request was not successful
	 * 
	 * @param erroMessage The error message in the response of server, null when the request was successful
	 */
	private void doMessageSent(String message, String errorMessage) {
		if (errorMessage != null) {
			showErrorDialog(errorMessage, null);
		}
	}
	
	private void doFriendRequestStatusReceived(String meepTag, String name, FriendRequestStatus status, String errorMessage) {
		
		if (errorMessage != null) {
			showErrorDialog(errorMessage, null);
			return;
		}

		int rString = -1;
		String title = null;
		
		switch (status) {
		case ALL_ACCEPT:
			rString = R.string.friend_request_approved;
			title = getString(R.string.nice);
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
			String message = getString(rString, name);
			
			PopUpFragment newFragment = PopUpFragment.newInstance(PopUpFragment.FRIEND_REQUEST_STATUS_DIALOG_ID, message, title, null);
			newFragment.show(getSupportFragmentManager(), POPUP_DIALOG);
		}
	}
	/**
	 * Refresh and sort the friend list  
	 */
	private synchronized void refreshFriendList(User user) {
		
		if (user == null)
			return;
		
		
		showLoading();
		
		List<Friend> friends = getFriends(user);
		
		if (friends == null || friends.size() == 0) {
			dismissLoading();
			return;
		}
		
		GridView gridView = (GridView) findViewById(R.id.friends);
		FriendAdapter frdAdapter = new FriendAdapter(this, friends);
		
		if (mConnector != null) {
			CommunicatorService service = mConnector.getService();
			if (service != null) {
				service.sortFriends(user, frdAdapter);
			}
		}
		gridView.setAdapter(frdAdapter);
		gridView.post(new Runnable() {
			
			@Override
			public void run() {
				
				GridView gridView = (GridView) findViewById(R.id.friends);
				ImageView transitionDownView = (ImageView) findViewById(R.id.transition_down);	
				if (gridView.getLastVisiblePosition() < gridView.getCount()) {
					transitionDownView.setVisibility(View.VISIBLE);
				} else {
					transitionDownView.setVisibility(View.INVISIBLE);
				}
				
				dismissLoading();
			}
		});
	
		
		dismissLoading();
	}
	
	/**
	 * Refresh the unread message count for a specific friend
	 * 
	 *  @param friend
	 */
	private void refreshUnreadCount(Friend friend) {
		
		int count = getUnreadMessageCount(friend);
		setUnreadCount(friend, count);
	}
	
	/**
	 * Retrieve unread message count from database for a specific friend
	 * 
	 *  @param friend The friend UI that to be updated
	 *  @return The unread count of the friend
	 */
	public int getUnreadMessageCount(Friend friend) {
		CommunicatorService service = mConnector.getService();
		if (service != null) {
			return service.getUnreadMessageCount(getUser(), friend.getAccountId());
		}
		return 0;
	}
	
	/**
	 * Retrieve currently logged in user from service
	 * 
	 * @return The currently logged in user object
	 */
	public User getLoggedInUser() {
		if (mConnector != null) {
			CommunicatorService service = mConnector.getService();
			if (service != null)
				return service.getLoggedInUser();
		}
		return null;
	}
	
	/**
	 * Retrieve last logged in user from service
	 * 
	 * @return The last logged in user object
	 */
	public User getLastLoggedInUser() {
		if (mConnector != null) {
			CommunicatorService service = mConnector.getService();
			if (service != null)
				return service.getLastLoggedInUser();
		}
		return null;
	}
	
	
	/**
	 * Shows a error dialog pop up
	 * 
	 * @param errorMessage The message that will be shown in the pop-up
	 */
	private void showErrorDialog(String errorMessage, String title, String action) {

		IPopupInterface listener = null;
		if (action != null) {
			
			listener = new IPopupInterface() {
				
				@Override
				public void onYesButtonPressed(PopUpFragment thisFragment) {
					// Ignore
				}
				
				@Override
				public void onSearchButtonPressed(PopUpFragment fragment, String message) {
					// Ignore
				}
				
				@Override
				public void onOkButtonPressed(PopUpFragment thisFragment, String action) {
					performErrorAction(action);
				}
				
				@Override
				public void onNoButtonPressed(PopUpFragment thisFragment) {
					// Ignore
				}
				
				@Override
				public void onDeclineButtonPressed(PopUpFragment thisFragment, String meepTag) {
					// Ignore
				}
				
				@Override
				public void onAddButtonPressed(PopUpFragment fragment, String message) {
					// Ignore
				}
				
				@Override
				public void onAcceptButtonPressed(PopUpFragment thisFragment, String meepTag) {
					// Ignore
				}
			};
			
			
		}
		
		PopUpFragment fragment = PopUpFragment.newInstance(PopUpFragment.ERROR_MESSAGE_DIALOG_ID, errorMessage, title, action);
		fragment.setListener(listener);
		fragment.show(getSupportFragmentManager(), POPUP_DIALOG);
		
	}
	
	private void showErrorDialog(String errorMessage, String action) {
		showErrorDialog(errorMessage, null, action);
	}
	
	
	private void performErrorAction (String action) {
		
		if (action.equals(PopUpFragment.ERROR_ACTION_SEARCH_FRIEND)) {
			
			showSearchFriendPopup();
			
		}
	}
	
	/**
	 * Update user profile, including logged in user name and avatar
	 * 
	 */
	private void updateUserProfile(final User user) {

		StrokedTextView name = (StrokedTextView) findViewById(R.id.kidname);
		if (name != null) {
			name.setText(user.getFirstName());
		}
		// set current user's icon
		ImageView icon = (ImageView) findViewById(R.id.kidicon);
		if (icon != null) {

			String iconAddress = user.getIconAddress();
			if (iconAddress == null || iconAddress.length() == 0) {
				Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.default_avatar, icon.getWidth(), icon.getHeight());
				icon.setImageBitmap(bitmap);
			} else {
				ImageDownloader imageDownloader = getImageDownloader();
				if (imageDownloader != null) {
					imageDownloader.download(iconAddress, R.drawable.avatar, icon.getLayoutParams().width, icon.getLayoutParams().height, icon);
				}
			}
		}
	}
	
	/**
	 * Reorder the friend list base on the sorting algorithm from service and 
	 * update the UI accordingly
	 * 
	 */
	private void sortFriendList(User user) {
		
		GridView gridView = (GridView) findViewById(R.id.friends);
		FriendAdapter adapter = (FriendAdapter) gridView.getAdapter();
		CommunicatorService service = mConnector.getService();
		if (service != null) {
			service.sortFriends(user, adapter);
		}
		adapter.notifyDataSetChanged();
	}
	
	/** 
	 * Show the loading spinner
	 */
	private void showLoading() {
		
		ProgressBar pb = (ProgressBar) findViewById(R.id.loading);
		pb.setVisibility(View.VISIBLE);
	}
	
	/** 
	 * Hide the loading spinner
	 */
	private void dismissLoading() {
		
		ProgressBar pb = (ProgressBar) findViewById(R.id.loading);
		pb.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * Get the Image downloader instance, create if an instance is not exist
	 * 
	 * @return the ImageDownloader object
	 */
	public ImageDownloader getImageDownloader() {
		
		if (mImageDownloader == null) { 
			mImageDownloader = new ImageDownloader(this, IMAGE_CACHE_DIR);
		}
		return mImageDownloader;
	}
	
	private void getFriendListFromServer() {

		if (mConnector != null) {
			CommunicatorService service = mConnector.getService();
			if (service != null) 
				service.syncFriendList();
		}
	}
	
	public synchronized User getUser() {
		return mUser;
	}
	
	private synchronized void setUser(User user) {
		mUser = user;
	}
	
	
	private void setStatusLight(final boolean online) {
		
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				ImageView statusView = (ImageView) findViewById(R.id.statusLight);
				
				if (statusView != null) {
					if (online) {
						statusView.setImageDrawable(getResources().getDrawable(R.drawable.green));
					} else {
						statusView.setImageDrawable(getResources().getDrawable(R.drawable.red));
					}
				}
			}
		}
		);
		

	}
	
	private void showNetworkDialog() {
		
		final IPopupInterface listener = new IPopupInterface() {

			@Override
			public void onAddButtonPressed(PopUpFragment fragment, String message) {			
			}

			@Override
			public void onDeclineButtonPressed(PopUpFragment thisFragment, String meepTag) {
				
				//TODO: decline a friend
				thisFragment.dismiss();
			}

			@Override
			public void onAcceptButtonPressed(PopUpFragment thisFragment, String meepTag) {
				// Ignore
			}

			@Override
			public void onSearchButtonPressed(PopUpFragment fragment, String message) {
				// Ignore
			}

			@Override
			public void onYesButtonPressed(PopUpFragment thisFragment) {
					
				Intent intent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
				startActivity(intent);
				thisFragment.dismiss();
				
			}

			@Override
			public void onNoButtonPressed(PopUpFragment thisFragment) {
				thisFragment.dismiss();
			}

			@Override
			public void onOkButtonPressed(PopUpFragment thisFragment, String action) {
				// Ignore
			}
		};
		
		
		PopUpFragment fragment = PopUpFragment.newInstance(PopUpFragment.NETWORK_DIALOG_ID, null, null);
		fragment.setListener(listener);
		fragment.show(getSupportFragmentManager(), POPUP_DIALOG);
	}
	
	private void showNetworkDialog2() {

		PopUpFragment fragment = PopUpFragment.newInstance(PopUpFragment.NETWORK_DIALOG_2_ID, null, null);
		fragment.show(getSupportFragmentManager(), POPUP_DIALOG);

	}

	private void onSignInCallback() {
		
		// Set current logged in user and get friend list
		final Handler handler = new Handler();
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {

				final User user = getLoggedInUser();
				setStatusLight(true);

				if (user != null) {

					handler.post(new Runnable() {

						@Override
						public void run() {
							setUser(user);
							// update user profile
							showLoading();
							updateUserProfile(getUser());
							getFriendListFromServer();
						}

					});
				}
			}
		});
	}
	
	private void onSignOutCallback() {
		
		final Handler handler = new Handler();
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {

				final User user = getLastLoggedInUser();
				setStatusLight(false);

				if (user != null) {

					handler.post(new Runnable() {

						@Override
						public void run() {
							setUser(user);
							updateUserProfile(getUser());
							getFriendListFromServer();
						}

					});
				} else {
					setUser(null);
				}
			}
		});
	}
	
	
	public List<ConversationMessage> getConversationMessages(Conversation conversation, int limit) {
		
		List<ConversationMessage>  messages = null;
		
		if (mConnector != null) {
			if (mConnector.getService() != null) {
			
				messages = mConnector.getService().getSortedConversationMessages(conversation, limit); 
				
			}
		}
		
		return messages;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		if (mConnector != null) {
			if (mConnector.getService() != null)
				mConnector.getService().setCallback(null);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (mConnector != null) {
			if (mConnector.getService() != null)
				mConnector.getService().setCallback(mCommunicatorCallback);
		}
	}
}