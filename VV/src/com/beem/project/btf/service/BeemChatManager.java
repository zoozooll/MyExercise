/*
    BEEM is a videoconference application on the Android Platform.

    Copyright (C) 2009 by Frederic-Charles Barthelery,
                          Jean-Manuel Da Silva,
                          Nikita Kozlov,
                          Philippe Lago,
                          Jean Baptiste Vergely,
                          Vincent Veronis.

    This file is part of BEEM.

    BEEM is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    BEEM is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with BEEM.  If not, see <http://www.gnu.org/licenses/>.

    Please send bug reports with examples or suggestions to
    contact@beem-project.com or http://dev.beem-project.com/

    Epitech, hereby disclaims all copyright interest in the program "Beem"
    written by Frederic-Charles Barthelery,
               Jean-Manuel Da Silva,
               Nikita Kozlov,
               Philippe Lago,
               Jean Baptiste Vergely,
               Vincent Veronis.

    Nicolas Sadirac, November 26, 2009
    President of Epitech.

    Flavien Astraud, November 26, 2009
    Head of the EIP Laboratory.

 */
package com.beem.project.btf.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.BeemService;
import com.beem.project.btf.R;
import com.beem.project.btf.providers.AvatarProvider;
import com.beem.project.btf.service.aidl.IChat;
import com.beem.project.btf.service.aidl.IChatManager;
import com.beem.project.btf.service.aidl.IChatManagerListener;
import com.beem.project.btf.service.aidl.IMessageListener;
import com.beem.project.btf.service.aidl.IRoster;
import com.beem.project.btf.ui.activity.ChatActivity;
import com.beem.project.btf.utils.Status;
import net.java.otr4j.OtrException;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import com.beem.project.btf.service.aidl.onNewChatListener;
import com.butterfly.vv.vv.utils.VVXMPPUtils;

/**
 * An adapter for smack's ChatManager. This class provides functionnality to handle chats.
 * @author darisk
 */
public class BeemChatManager extends IChatManager.Stub {
	private static final String TAG = "BeemChatManager";
	private final ChatManager mAdaptee;
	private final Map<String, ChatAdapter> mChats = new HashMap<String, ChatAdapter>();
	private final ChatListener mChatListener = new ChatListener();
	private final RemoteCallbackList<IChatManagerListener> mRemoteChatCreationListeners = new RemoteCallbackList<IChatManagerListener>();
	private final BeemService mService;
	//	private final Roster mRoster;
	private final ChatRosterListener mChatRosterListener = new ChatRosterListener();
	// 将ChatManager中新产生的消息透传给上层 #2015-02-13 by baoyuedong
	private final List<onNewChatListener> newChatsListeners = new ArrayList<onNewChatListener>();
	private MessageManager messageManager;

	/**
	 * Constructor.
	 * @param chatManager the smack ChatManager to adapt
	 * @param service the service which runs the chat manager
	 * @param roster roster used to get presences changes
	 */
	public BeemChatManager(final ChatManager chatManager,
			final BeemService service) {
		this.mService = service;
		this.mAdaptee = chatManager;
		//this.mRoster = roster;
		//this.mRoster.addRosterListener(mChatRosterListener);
		this.mAdaptee.addChatListener(mChatListener);
		//		//LogUtils.i("create BeemChatManager");
	}
	@Override
	public void addChatCreationListener(IChatManagerListener listener)
			throws RemoteException {
		if (listener != null)
			mRemoteChatCreationListeners.register(listener);
	}
	/**
	 * Create a chat session.
	 * @param jid the jid of the contact you want to chat with
	 * @param listener listener to use for chat events on this chat session
	 * @return the chat session
	 */
	@Override
	public IChat createChat(String jid, IMessageListener listener) {
		String key = VVXMPPUtils.makeJidParsed(jid);
		ChatAdapter result;
		//		//LogUtils.i("creatChat#jid:" + jid + " isExist:" + (mChats.containsKey(key)));
		if (mChats.containsKey(key)) {
			result = mChats.get(key);
			result.addMessageListener(listener);
			return result;
		}
		Chat c = mAdaptee.createChat(key, null);
		// maybe a little problem of thread synchronization
		// if so use an HashTable instead of a HashMap for mChats
		result = getChat(c);
		result.addMessageListener(listener);
		return result;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroyChat(IChat chat) throws RemoteException {
		// Can't remove it. otherwise we will lose all futur message in this
		// chat
		// chat.removeMessageListener(mChatListener);
		if (chat == null)
			return;
		deleteChatNotification(chat);
		mChats.remove(chat.getParticipant().getJid());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteChatNotification(IChat chat) {
		try {
			mService.deleteNotification(chat.getParticipant().getJid()
					.hashCode());
		} catch (RemoteException e) {
			Log.v(TAG, "Remote exception ", e);
		}
	}
	/**
	 * Get an existing ChatAdapter or create it if necessary.
	 * @param chat The real instance of smack chat
	 * @return a chat adapter register in the manager
	 */
	private ChatAdapter getChat(Chat chat) {
		String key = chat.getParticipant();
		if (mChats.containsKey(key)) {
			return mChats.get(key);
		}
		ChatAdapter res = new ChatAdapter(chat, messageManager);
		boolean history = PreferenceManager.getDefaultSharedPreferences(
				mService.getApplicationContext()).getBoolean(
				"settings_key_history", false);
		String accountUser = PreferenceManager.getDefaultSharedPreferences(
				mService.getApplicationContext()).getString(
				BeemApplication.ACCOUNT_USERNAME_KEY, "");
		String historyPath = PreferenceManager.getDefaultSharedPreferences(
				mService.getApplicationContext()).getString(
				BeemApplication.CHAT_HISTORY_KEY, "");
		if ("".equals(historyPath))
			historyPath = mService.getApplicationContext().getCacheDir()
					.getPath()
					+ File.separatorChar + "chat";
		res.setHistory(history);
		res.setAccountUser(accountUser);
		res.listenOtrSession();
		res.setHistoryPath(new File(Environment.getExternalStorageDirectory(),
				historyPath));
		Log.d(TAG, "getChat put " + key);
		mChats.put(key, res);
		return res;
	}
	/**
	 * This methods permits to retrieve the list of contacts who have an opened chat session with
	 * us.
	 * @return An List containing Contact instances.
	 * @throws RemoteException If a Binder remote-invocation error occurred.
	 */
	@Override
	public List<Contact> getOpenedChatList() throws RemoteException {
		List<Contact> openedChats = new ArrayList<Contact>();
		IRoster r = mService.getBind().getRoster();
		for (ChatAdapter chat : mChats.values()) {
			if (chat.getMessages().size() > 0) {
				Contact t = r.getContact(chat.getParticipant().getJid());
				if (t == null)
					t = new Contact(chat.getParticipant().getJid());
				openedChats.add(t);
			}
		}
		return openedChats;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeChatCreationListener(IChatManagerListener listener)
			throws RemoteException {
		if (listener != null)
			mRemoteChatCreationListeners.unregister(listener);
	}

	/**
	 * A listener for all the chat creation event that happens on the connection.
	 * @author darisk
	 */
	private class ChatListener extends IMessageListener.Stub implements
			ChatManagerListener {
		/**
		 * Constructor.
		 */
		public ChatListener() {
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void chatCreated(Chat chat, boolean locally) {
			IChat newchat = getChat(chat);
			Log.d(TAG,
					"Chat create threadId:" + chat.getThreadID()
							+ " created locally " + locally + " with "
							+ chat.getParticipant());
			try {
				newchat.addMessageListener(this);
				final int n = mRemoteChatCreationListeners.beginBroadcast();
				for (int i = 0; i < n; i++) {
					IChatManagerListener listener = mRemoteChatCreationListeners
							.getBroadcastItem(i);
					listener.chatCreated(newchat, locally);
				}
				mRemoteChatCreationListeners.finishBroadcast();
			} catch (RemoteException e) {
				// The RemoteCallbackList will take care of removing the
				// dead listeners.
				Log.w(TAG,
						" Error while triggering remote connection listeners in chat creation",
						e);
			}
		}
		/**
		 * Create the PendingIntent to launch our activity if the user select this chat
		 * notification.
		 * @param chat A ChatAdapter instance
		 * @return A Chat activity PendingIntent
		 */
		private PendingIntent makeChatIntent(IChat chat) {
			Intent chatIntent = new Intent(mService, ChatActivity.class);
			chatIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
					| Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			try {
				chatIntent.setData(chat.getParticipant().toUri());
			} catch (RemoteException e) {
				Log.e(TAG, e.getMessage());
			}
			PendingIntent contentIntent = PendingIntent.getActivity(mService,
					0, chatIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			return contentIntent;
		}
		/**
		 * Set a notification of a new chat.
		 * @param chat The chat to access by the notification
		 * @param msgBody the body of the new message
		 */
		private void notifyNewChat(IChat chat, String msgBody) {
			//			//LogUtils.i("notifyNewChat msgBody:" + msgBody);
			NotificationCompat.Builder notif = new NotificationCompat.Builder(
					mService);
			try {
				String contactJid = chat.getParticipant().getJid();
				Contact contact = mService.getBind().getRoster()
						.getContact(contactJid);
				String contactName = contactJid;
				if (contact != null) {
					contactName = contact.getNickName();
					// Bitmap avatar = getAvatar(contact);
					// notif.setLargeIcon(avatar);
				}
				notif.setTicker(contactName).setContentTitle(contactName);
				notif.setContentText(msgBody);
				notif.setSmallIcon(R.drawable.appicon);
				notif.setNumber(chat.getUnreadMessageCount());
				notif.setContentIntent(makeChatIntent(chat));
				notif.setAutoCancel(true).setWhen(System.currentTimeMillis());
				mService.sendNotification(chat.getParticipant().getJid()
						.hashCode(), notif.getNotification());
			} catch (RemoteException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		/**
		 * Get the avatar of a contact.
		 * @param c the contact
		 * @return the avatar of c or null if avatar is not defined
		 */
		private Bitmap getAvatar(Contact c) {
			String id = c.getAvatarId();
			if (id == null)
				id = "";
			Uri uri = AvatarProvider.CONTENT_URI.buildUpon().appendPath(id)
					.build();
			try {
				InputStream in = mService.getContentResolver().openInputStream(
						uri);
				return BitmapFactory.decodeStream(in);
			} catch (FileNotFoundException e) {
				Log.d(TAG, "Error loading avatar id: " + id, e);
				return null;
			}
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void processMessage(final IChat chat, Message message) {
			try {
				String body = message.getBody();
				//				//LogUtils.i("Chat#processMessage_body:" + body + " jid:" + chat.getParticipant().getJid() + " isOpen:"
				//						+ chat.isOpen());
				if (!chat.isOpen() && body != null) {
					if (chat instanceof ChatAdapter) {
						mChats.put(chat.getParticipant().getJid(),
								(ChatAdapter) chat);
					}
					for (onNewChatListener lis : newChatsListeners) {
						try {
							lis.notifyNewChat(chat, message);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
					// notifyNewChat(chat, body);
				}
			} catch (RemoteException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		@Override
		public void stateChanged(final IChat chat) {
		}
		@Override
		public void otrStateChanged(String otrState) throws RemoteException {
		}
		@Override
		public void interceptMessage(IChat chat, Message msg)
				throws RemoteException {
		}
	}

	/**
	 * implement a roster listener, is used to detect and close otr chats.
	 * @author nikita
	 */
	private class ChatRosterListener implements RosterListener {
		@Override
		public void entriesAdded(Collection<String> arg0) {
		}
		@Override
		public void entriesDeleted(Collection<String> arg0) {
		}
		@Override
		public void entriesUpdated(Collection<String> arg0) {
		}
		@Override
		public void presenceChanged(Presence presence) {
			String key = StringUtils.parseBareAddress(presence.getFrom());
			if (!mChats.containsKey(key)) {
				return;
			}
			if (Status.getStatusFromPresence(presence) >= Status.CONTACT_STATUS_DISCONNECT) {
				try {
					mChats.get(key).localEndOtrSession();
				} catch (OtrException e) {
					e.printStackTrace();
				}
			}
		}
		@Override
		public void onRostersUpdate(List<Contact> rosters) {
			// TODO Auto-generated method stub
		}
		@Override
		public void onNewFriendsUpdate(List<Contact> newfriends) {
			// TODO Auto-generated method stub
		}
		@Override
		public void onBlacklistUpdate(List<Contact> blacklist) {
			// TODO Auto-generated method stub
		}
		@Override
		public void onUserUpdate(String user, Contact contact) {
			// TODO Auto-generated method stub
		}
		@Override
		public void onBlacklistedUpdate(List<String> blacklisted) {
			// TODO Auto-generated method stub
		}
	}

	@Override
	public IChat getChat(String jid) throws RemoteException {
		IChat chat = mChats.get(VVXMPPUtils.makeJidParsed(jid));
		return chat;
	}
	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}
}
